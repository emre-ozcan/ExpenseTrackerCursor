package com.emreozcan.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emreozcan.expensetracker.data.repository.ExpenseRepository
import com.emreozcan.expensetracker.model.Expense
import com.emreozcan.expensetracker.model.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
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

    init {
        loadExpensesFromDatabase()
        if (shouldLoadSampleData()) {
            loadSampleData()
        }
        
        observeTodayExpenses()
        observeYesterdayExpenses()
        observeWeeklyTotal()
    }

    private fun shouldLoadSampleData(): Boolean {
        // İlk kez çalışıyorsa örnek veri yükle
        // Gerçek uygulamada bir SharedPreferences kontrolü kullanabilirsiniz
        return true
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
            val weekStart = now.minusDays(now.dayOfWeek.value.toLong() - 1).truncatedTo(ChronoUnit.DAYS)
            val weekEnd = weekStart.plusDays(7)
            
            expenseRepository.getTotalAmountInTimeRange(weekStart, weekEnd, ExpenseCategory.SALARY)
                .collectLatest { total ->
                    _totalSpentThisWeek.value = total
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
} 