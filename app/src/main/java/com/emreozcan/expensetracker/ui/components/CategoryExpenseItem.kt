package com.emreozcan.expensetracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emreozcan.expensetracker.model.ExpenseCategory

@Composable
fun CategoryExpenseItem(
    category: ExpenseCategory,
    amount: Double,
    currencySymbol: String = "$",
    entries: Int,
    totalAmount: Double = 0.0,
    modifier: Modifier = Modifier
) {
    val percentOfTotal = if (totalAmount > 0) {
        (amount / totalAmount) * 100
    } else {
        0.0
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Kategori ikonu
        CategoryIcon(category = category)
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Kategori bilgileri
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = getCategoryDisplayName(category),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            
            Text(
                text = "$entries ${if (entries == 1) "entry" else "entries"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        
        // Miktar bilgileri
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$currencySymbol${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            
            if (totalAmount > 0) {
                val localCurrency = when (currencySymbol) {
                    "$" -> "USD"
                    "€" -> "EUR"
                    "¥" -> "JPY"
                    "£" -> "GBP"
                    "₺" -> "TRY"
                    else -> currencySymbol
                }
                
                Text(
                    text = String.format("%,.2f", amount) + " " + localCurrency,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

// Kategorilerin görünen isimlerini döndüren yardımcı fonksiyon
private fun getCategoryDisplayName(category: ExpenseCategory): String {
    return when (category) {
        ExpenseCategory.FOOD -> "Food"
        ExpenseCategory.TRANSPORTATION -> "Transportation"
        ExpenseCategory.ENTERTAINMENT -> "Entertainment"
        ExpenseCategory.SHOPPING -> "Shopping"
        ExpenseCategory.UTILITIES -> "Utilities"
        ExpenseCategory.RENT -> "Rent"
        ExpenseCategory.HEALTH -> "Health"
        ExpenseCategory.EDUCATION -> "Education"
        ExpenseCategory.TRAVEL -> "Travel"
        ExpenseCategory.PETS -> "Pets"
        ExpenseCategory.COFFEE -> "Coffee"
        ExpenseCategory.SNACKS -> "Snacks"
        ExpenseCategory.SALARY -> "Salary"
        ExpenseCategory.OTHER -> "Other"
    }
} 