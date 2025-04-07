package com.emreozcan.expensetracker.data.repository

import com.emreozcan.expensetracker.data.local.dao.ExpenseDao
import com.emreozcan.expensetracker.data.mapper.toEntity
import com.emreozcan.expensetracker.data.mapper.toModel
import com.emreozcan.expensetracker.model.Expense
import com.emreozcan.expensetracker.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    
    override suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense.toEntity())
    }
    
    override suspend fun insertExpenses(expenses: List<Expense>) {
        expenseDao.insertExpenses(expenses.map { it.toEntity() })
    }
    
    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }
    
    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }
    
    override suspend fun deleteExpenseById(id: String) {
        expenseDao.deleteExpenseById(id)
    }
    
    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override suspend fun getExpenseById(id: String): Expense? {
        return expenseDao.getExpenseById(id)?.toModel()
    }
    
    override fun getExpensesInTimeRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Expense>> {
        return expenseDao.getExpensesInTimeRange(startDate, endDate).map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override fun getExpensesAfterDate(startDate: LocalDateTime): Flow<List<Expense>> {
        return expenseDao.getExpensesAfterDate(startDate).map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category).map { entities ->
            entities.map { it.toModel() }
        }
    }
    
    override fun getTotalAmountInTimeRange(startDate: LocalDateTime, endDate: LocalDateTime, excludeCategory: ExpenseCategory): Flow<Double> {
        return expenseDao.getTotalAmountInTimeRange(startDate, endDate, excludeCategory).map { 
            it ?: 0.0
        }
    }
} 