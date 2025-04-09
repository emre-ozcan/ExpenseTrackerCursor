package com.emreozcan.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emreozcan.expensetracker.data.repository.ExpenseRepository
import com.emreozcan.expensetracker.model.Expense
import com.emreozcan.expensetracker.model.ExpenseCategory
import com.emreozcan.expensetracker.ui.components.DailyExpense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _totalSpentThisWeek = MutableStateFlow(0.0)
    val totalSpentThisWeek: StateFlow<Double> = _totalSpentThisWeek

    private val _todayExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val todayExpenses: StateFlow<List<Expense>> = _todayExpenses

    private val _yesterdayExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val yesterdayExpenses: StateFlow<List<Expense>> = _yesterdayExpenses
    
    private val _weeklyExpenses = MutableStateFlow<List<DailyExpense>>(emptyList())
    val weeklyExpenses: StateFlow<List<DailyExpense>> = _weeklyExpenses
    
    private val _weeklyExpensesByCategory = MutableStateFlow<Map<ExpenseCategory, Double>>(emptyMap())
    val weeklyExpensesByCategory: StateFlow<Map<ExpenseCategory, Double>> = _weeklyExpensesByCategory

    init {
        loadExpensesFromDatabase()
        if (shouldLoadSampleData()) {
            loadSampleData()
        }
        
        observeTodayExpenses()
        observeYesterdayExpenses()
        observeWeeklyTotal()
        observeWeeklyExpensesByDay()
        observeWeeklyExpensesByCategory()
    }

    private fun shouldLoadSampleData(): Boolean {
        // İlk kez çalışıyorsa örnek veri yükle
        // Gerçek uygulamada bir SharedPreferences kontrolü kullanabilirsiniz
        return false
    }

    private fun loadExpensesFromDatabase() {
        viewModelScope.launch {
            expenseRepository.getAllExpenses().collectLatest { expenseList ->
                _expenses.value = expenseList
            }
        }
    }

    private fun observeTodayExpenses() {
        viewModelScope.launch {
            val today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
            val tomorrow = today.plusDays(1)
            
            expenseRepository.getExpensesInTimeRange(today, tomorrow)
                .collectLatest { expenseList ->
                    _todayExpenses.value = expenseList
                }
        }
    }

    private fun observeYesterdayExpenses() {
        viewModelScope.launch {
            val today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
            val yesterday = today.minusDays(1)
            
            expenseRepository.getExpensesInTimeRange(yesterday, today)
                .collectLatest { expenseList ->
                    _yesterdayExpenses.value = expenseList
                }
        }
    }

    private fun observeWeeklyTotal() {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            
            // Hafta şu anki günden bir hafta önceki güne kadar (bugün dahil)
            val weekEnd = now.truncatedTo(ChronoUnit.DAYS).plusDays(1) // Bugünün sonuna kadar
            val weekStart = weekEnd.minusDays(7) // Bir hafta öncesi
            
            // Debug amaçlı tarih aralığını kontrol et
            println("Hafta başlangıcı: $weekStart")
            println("Hafta bitişi: $weekEnd")
            
            expenseRepository.getExpensesInTimeRange(weekStart, weekEnd)
                .collectLatest { expenseList ->
                    val weeklyTotal = expenseList
                        .filter { it.category != ExpenseCategory.SALARY }
                        .sumOf { it.amount }
                    
                    println("Haftalık toplam harcama: $weeklyTotal")
                    println("Harcama listesi: ${expenseList.map { "${it.description}: ${it.amount}" }}")
                    
                    _totalSpentThisWeek.value = weeklyTotal
                }
        }
    }
    
    private fun observeWeeklyExpensesByDay() {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            
            // Hafta şu anki günden bir hafta önceki güne kadar (bugün dahil)
            val weekEnd = now.truncatedTo(ChronoUnit.DAYS).plusDays(1) // Bugünün sonuna kadar
            val weekStart = weekEnd.minusDays(7) // Bir hafta öncesi
            
            expenseRepository.getExpensesInTimeRange(weekStart, weekEnd)
                .collectLatest { expenseList ->
                    // Günlük harcamaları gruplandır
                    val dailyExpenses = mutableListOf<DailyExpense>()
                    
                    // Haftanın her günü için
                    for (dayOffset in 6 downTo 0) {
                        val day = now.minusDays(dayOffset.toLong()).truncatedTo(ChronoUnit.DAYS)
                        val nextDay = day.plusDays(1)
                        
                        // Bu gün için harcamalar
                        val thisDayExpenses = expenseList
                            .filter { 
                                it.timestamp.isAfter(day) && 
                                it.timestamp.isBefore(nextDay) && 
                                it.category != ExpenseCategory.SALARY
                            }
                        
                        // Günün adını al (Mon, Tue vs.)
                        val dayName = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                        
                        // Günlük toplam harcama
                        val totalForDay = thisDayExpenses.sumOf { it.amount }
                        
                        dailyExpenses.add(DailyExpense(dayName, totalForDay))
                    }
                    
                    _weeklyExpenses.value = dailyExpenses
                }
        }
    }
    
    private fun observeWeeklyExpensesByCategory() {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            
            // Hafta şu anki günden bir hafta önceki güne kadar (bugün dahil)
            val weekEnd = now.truncatedTo(ChronoUnit.DAYS).plusDays(1) // Bugünün sonuna kadar
            val weekStart = weekEnd.minusDays(7) // Bir hafta öncesi
            
            expenseRepository.getExpensesInTimeRange(weekStart, weekEnd)
                .collectLatest { expenseList ->
                    // Kategori bazında harcamaları gruplandır
                    val expensesByCategory = expenseList
                        .filter { it.category != ExpenseCategory.SALARY }
                        .groupBy { it.category }
                        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
                    
                    _weeklyExpensesByCategory.value = expensesByCategory
                }
        }
    }

    private fun loadSampleData() {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            val sampleExpenses = listOf(
                // Bugün
                Expense(
                    amount = 3.35,
                    description = "Treats",
                    category = ExpenseCategory.PETS,
                    timestamp = now.withHour(8).withMinute(54).withSecond(0)
                ),
                Expense(
                    amount = 1.70,
                    description = "Snacks",
                    category = ExpenseCategory.SNACKS,
                    timestamp = now.withHour(8).withMinute(54).withSecond(0)
                ),
                Expense(
                    amount = 2.19,
                    description = "Coffee",
                    category = ExpenseCategory.COFFEE,
                    timestamp = now.withHour(8).withMinute(37).withSecond(0)
                ),
                Expense(
                    amount = 2300.00,
                    description = "Salary",
                    category = ExpenseCategory.SALARY,
                    timestamp = now.withHour(7).withMinute(44).withSecond(0)
                ),
                
                // Dün
                Expense(
                    amount = 12.99,
                    description = "Lunch",
                    category = ExpenseCategory.FOOD,
                    timestamp = now.minusDays(1).withHour(13).withMinute(30)
                ),
                Expense(
                    amount = 40.95,
                    description = "Gas",
                    category = ExpenseCategory.TRANSPORTATION,
                    timestamp = now.minusDays(1).withHour(18).withMinute(15)
                ),
                
                // 2 gün önce
                Expense(
                    amount = 8.50,
                    description = "Book",
                    category = ExpenseCategory.EDUCATION,
                    timestamp = now.minusDays(2).withHour(14).withMinute(20)
                ),
                Expense(
                    amount = 15.75,
                    description = "Movie tickets",
                    category = ExpenseCategory.ENTERTAINMENT,
                    timestamp = now.minusDays(2).withHour(19).withMinute(45)
                ),
                
                // 3 gün önce
                Expense(
                    amount = 35.99,
                    description = "Dinner",
                    category = ExpenseCategory.FOOD,
                    timestamp = now.minusDays(3).withHour(20).withMinute(0)
                ),
                Expense(
                    amount = 9.99,
                    description = "Netflix",
                    category = ExpenseCategory.ENTERTAINMENT,
                    timestamp = now.minusDays(3).withHour(22).withMinute(0)
                )
            )
            
            expenseRepository.insertExpenses(sampleExpenses)
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.insertExpense(expense)
        }
    }

    fun getTodayExpenses(): List<Expense> {
        return _todayExpenses.value
    }

    fun getYesterdayExpenses(): List<Expense> {
        return _yesterdayExpenses.value
    }

    fun getTodayTotal(): Double {
        return _todayExpenses.value
            .filter { it.category != ExpenseCategory.SALARY }
            .sumOf { it.amount }
    }

    fun getYesterdayTotal(): Double {
        return _yesterdayExpenses.value
            .filter { it.category != ExpenseCategory.SALARY }
            .sumOf { it.amount }
    }
    
    fun getWeeklyExpensePercent(): Int {
        // Bu hafta ve önceki hafta arasındaki farkı yüzde olarak hesapla
        // Gerçek uygulamada önceki haftanın verileri olmalı
        // Şimdilik sabit bir değer döndürelim
        return -11 // % 11 azalma
    }
} 