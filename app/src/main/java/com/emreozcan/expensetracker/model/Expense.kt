package com.emreozcan.expensetracker.model

import java.time.LocalDateTime
import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val description: String,
    val category: ExpenseCategory,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val currency: String = "USD"
)

enum class ExpenseCategory {
    FOOD,
    TRANSPORTATION,
    ENTERTAINMENT,
    SHOPPING,
    UTILITIES,
    RENT,
    HEALTH,
    EDUCATION,
    TRAVEL,
    PETS,
    COFFEE,
    SNACKS,
    SALARY,
    OTHER
} 