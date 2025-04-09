package com.emreozcan.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emreozcan.expensetracker.model.ExpenseCategory
import com.emreozcan.expensetracker.ui.components.DailyExpense
import com.emreozcan.expensetracker.ui.components.WeeklyExpenseChart
import com.emreozcan.expensetracker.ui.components.CategoryExpenseItem
import com.emreozcan.expensetracker.ui.components.EmojiCategoryIcon

@Composable
fun ExpensesScreen(
    weeklyExpenses: List<DailyExpense>,
    totalSpent: Double,
    percentageChange: Int,
    expensesByCategory: Map<ExpenseCategory, Double>,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(ExpensesTab.WEEK) }
    
    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Sekme butonları
            TabButtons(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // İçerik
            when (currentTab) {
                ExpensesTab.WEEK -> {
                    WeekTab(
                        weeklyExpenses = weeklyExpenses,
                        totalSpent = totalSpent,
                        percentageChange = percentageChange
                    )
                }
                ExpensesTab.MONTH -> {
                    // Aylık içerik (ilerleyen aşamalarda eklenecek)
                    Text(text = "Monthly expense summary will be available soon.")
                }
                ExpensesTab.YEAR -> {
                    // Yıllık içerik (ilerleyen aşamalarda eklenecek)
                    Text(text = "Yearly expense summary will be available soon.")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Kategori bazında harcamalar bölümü
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            
            CategoryExpensesSection(
                expensesByCategory = expensesByCategory,
                totalSpent = totalSpent
            )
            
            // Ekranın altında boşluk bırak
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TabButtons(
    currentTab: ExpensesTab,
    onTabSelected: (ExpensesTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF5F5F5)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExpensesTab.values().forEach { tab ->
            Button(
                onClick = { onTabSelected(tab) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentTab == tab) Color(0xFF1B1D36) else Color.Transparent,
                    contentColor = if (currentTab == tab) Color.White else Color.Black
                ),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                Text(text = tab.displayName)
            }
        }
    }
}

@Composable
private fun WeekTab(
    weeklyExpenses: List<DailyExpense>,
    totalSpent: Double,
    percentageChange: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Haftalık harcama grafiği
        WeeklyExpenseChart(
            weeklyExpenses = weeklyExpenses,
            totalSpent = totalSpent,
            percentageChange = percentageChange,
            maxHeight = 130f
        )
    }
}

@Composable
private fun CategoryExpensesSection(
    expensesByCategory: Map<ExpenseCategory, Double>,
    totalSpent: Double
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Başlık
        Text(
            text = "Expenses by Category",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Kategori listesi
        LazyColumn {
            items(
                items = expensesByCategory.entries.sortedByDescending { it.value }.toList(),
                key = { (category, _) -> category.name }
            ) { (category, amount) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji
                    EmojiCategoryIcon(category = category)
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = getCategoryDisplayName(category),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        
                        // Yüzde hesaplama
                        val percentage = if (totalSpent > 0) (amount / totalSpent * 100) else 0.0
                        Text(
                            text = String.format("%.1f%%", percentage),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Text(
                        text = "₺${String.format("%.2f", amount)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                if (category != expensesByCategory.keys.last()) {
                    Divider()
                }
            }
        }
    }
}

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

enum class ExpensesTab(val displayName: String) {
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year")
} 