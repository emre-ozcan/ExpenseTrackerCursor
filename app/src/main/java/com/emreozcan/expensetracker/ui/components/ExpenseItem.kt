package com.emreozcan.expensetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emreozcan.expensetracker.model.Expense
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseItem(expense: Expense, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EmojiCategoryIcon(category = expense.category)
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = expense.category.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            if (expense.description.isNotEmpty()) {
                Text(
                    text = expense.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Saat bilgisini ekliyoruz
            if (expense.timestamp.hour > 0 || expense.timestamp.minute > 0) {
                Text(
                    text = "${expense.timestamp.hour}:${expense.timestamp.minute.toString().padStart(2, '0')} AM",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            val sign = if (expense.category == com.emreozcan.expensetracker.model.ExpenseCategory.SALARY) "+" else ""
            Text(
                text = "$sign$${String.format("%.2f", expense.amount)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (expense.category == com.emreozcan.expensetracker.model.ExpenseCategory.SALARY) 
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun EmojiCategoryIcon(category: com.emreozcan.expensetracker.model.ExpenseCategory, modifier: Modifier = Modifier) {
    val emoji = when (category) {
        com.emreozcan.expensetracker.model.ExpenseCategory.FOOD -> "🍔"
        com.emreozcan.expensetracker.model.ExpenseCategory.TRANSPORTATION -> "🚗"
        com.emreozcan.expensetracker.model.ExpenseCategory.ENTERTAINMENT -> "🎭"
        com.emreozcan.expensetracker.model.ExpenseCategory.SHOPPING -> "🛍️"
        com.emreozcan.expensetracker.model.ExpenseCategory.UTILITIES -> "🔌"
        com.emreozcan.expensetracker.model.ExpenseCategory.RENT -> "🏠"
        com.emreozcan.expensetracker.model.ExpenseCategory.HEALTH -> "💊"
        com.emreozcan.expensetracker.model.ExpenseCategory.EDUCATION -> "📚"
        com.emreozcan.expensetracker.model.ExpenseCategory.TRAVEL -> "✈️"
        com.emreozcan.expensetracker.model.ExpenseCategory.PETS -> "🐶"
        com.emreozcan.expensetracker.model.ExpenseCategory.COFFEE -> "☕"
        com.emreozcan.expensetracker.model.ExpenseCategory.SNACKS -> "🍪"
        com.emreozcan.expensetracker.model.ExpenseCategory.SALARY -> "💰"
        com.emreozcan.expensetracker.model.ExpenseCategory.OTHER -> "📝"
    }
    
    Text(
        text = emoji,
        fontSize = 24.sp,
        modifier = modifier
    )
}

@Composable
fun ExpenseTimeHeader(
    time: String,
    amount: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = "$${String.format("%.2f", amount)}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
} 