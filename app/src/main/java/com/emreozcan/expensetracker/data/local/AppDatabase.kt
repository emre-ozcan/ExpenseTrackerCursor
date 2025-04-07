package com.emreozcan.expensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emreozcan.expensetracker.data.local.converter.CategoryConverter
import com.emreozcan.expensetracker.data.local.converter.DateTimeConverter
import com.emreozcan.expensetracker.data.local.dao.ExpenseDao
import com.emreozcan.expensetracker.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateTimeConverter::class, CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
} 