package com.emreozcan.expensetracker.data.mapper

import com.emreozcan.expensetracker.data.local.entity.ExpenseEntity
import com.emreozcan.expensetracker.model.Expense

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        amount = amount,
        description = description,
        category = category,
        timestamp = timestamp,
        currency = currency
    )
}

fun ExpenseEntity.toModel(): Expense {
    return Expense(
        id = id,
        amount = amount,
        description = description,
        category = category,
        timestamp = timestamp,
        currency = currency
    )
} 