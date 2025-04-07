package com.emreozcan.expensetracker.data.repository

import com.emreozcan.expensetracker.model.Expense
import com.emreozcan.expensetracker.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ExpenseRepository {
    suspend fun insertExpense(expense: Expense)
    
    suspend fun insertExpenses(expenses: List<Expense>)
    
    suspend fun updateExpense(expense: Expense)
    
    suspend fun deleteExpense(expense: Expense)
    
    suspend fun deleteExpenseById(id: String)
    
    fun getAllExpenses(): Flow<List<Expense>>
    
    suspend fun getExpenseById(id: String): Expense?
    
    fun getExpensesInTimeRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Expense>>
    
    fun getExpensesAfterDate(startDate: LocalDateTime): Flow<List<Expense>>
    
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>>
    
    fun getTotalAmountInTimeRange(startDate: LocalDateTime, endDate: LocalDateTime, excludeCategory: ExpenseCategory = ExpenseCategory.SALARY): Flow<Double>
} 