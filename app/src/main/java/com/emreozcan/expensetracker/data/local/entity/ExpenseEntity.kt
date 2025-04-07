package com.emreozcan.expensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.emreozcan.expensetracker.data.local.converter.DateTimeConverter
import com.emreozcan.expensetracker.model.ExpenseCategory
import java.time.LocalDateTime

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val amount: Double,
    val description: String,
    val category: ExpenseCategory,
    val timestamp: LocalDateTime,
    val currency: String
) 