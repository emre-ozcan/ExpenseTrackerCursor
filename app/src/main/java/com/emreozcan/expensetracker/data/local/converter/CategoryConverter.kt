package com.emreozcan.expensetracker.data.local.converter

import androidx.room.TypeConverter
import com.emreozcan.expensetracker.model.ExpenseCategory

class CategoryConverter {
    @TypeConverter
    fun fromCategory(category: ExpenseCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): ExpenseCategory {
        return ExpenseCategory.valueOf(value)
    }
} 