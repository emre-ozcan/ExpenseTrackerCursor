package com.emreozcan.expensetracker.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emreozcan.expensetracker.model.Expense
import com.emreozcan.expensetracker.model.ExpenseCategory
import com.emreozcan.expensetracker.ui.components.ExpenseItem
import com.emreozcan.expensetracker.ui.components.ExpenseTimeHeader
import com.emreozcan.expensetracker.viewmodel.ExpenseViewModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(
    onAddExpense: () -> Unit = {},
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val expenses by viewModel.expenses.collectAsState()
    val totalSpentThisWeek by viewModel.totalSpentThisWeek.collectAsState()
    val todayExpenses by viewModel.todayExpenses.collectAsState()
    val yesterdayExpenses by viewModel.yesterdayExpenses.collectAsState()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpense,
                containerColor = Color(0xFF1B1D36)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Expense",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                SpentThisWeekCard(
                    amount = totalSpentThisWeek,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            
            // Bugün
            item {
                ExpenseTimeHeader(
                    time = "Today",
                    amount = viewModel.getTodayTotal()
                )
            }
            
            items(todayExpenses) { expense ->
                ExpenseItem(expense = expense)
            }
            
            item {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
            }
            
            // Dün
            item {
                ExpenseTimeHeader(
                    time = "Yesterday",
                    amount = viewModel.getYesterdayTotal()
                )
            }
            
            items(yesterdayExpenses) { expense ->
                ExpenseItem(expense = expense)
            }
            
            // 2 gün önce (örnek olarak ekliyoruz)
            item {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                
                val twoDaysAgo = LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.DAYS)
                val twoDaysAgoExpenses = expenses.filter {
                    it.timestamp.isAfter(twoDaysAgo) && 
                    it.timestamp.isBefore(twoDaysAgo.plusDays(1))
                }
                
                ExpenseTimeHeader(
                    time = "Thursday",  // Sabit bir gün ismi kullandım, gerçek uygulamada dinamik olmalı
                    amount = twoDaysAgoExpenses.filter { it.category != ExpenseCategory.SALARY }.sumOf { it.amount }
                )
                
                twoDaysAgoExpenses.forEach { expense ->
                    ExpenseItem(expense = expense)
                }
            }
            
            // 3 gün önce (örnek olarak ekliyoruz)
            item {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                
                val threeDaysAgo = LocalDateTime.now().minusDays(3).truncatedTo(ChronoUnit.DAYS)
                val threeDaysAgoExpenses = expenses.filter {
                    it.timestamp.isAfter(threeDaysAgo) && 
                    it.timestamp.isBefore(threeDaysAgo.plusDays(1))
                }
                
                ExpenseTimeHeader(
                    time = "Wednesday",  // Sabit bir gün ismi kullandım, gerçek uygulamada dinamik olmalı
                    amount = threeDaysAgoExpenses.filter { it.category != ExpenseCategory.SALARY }.sumOf { it.amount }
                )
                
                threeDaysAgoExpenses.forEach { expense ->
                    ExpenseItem(expense = expense)
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SpentThisWeekCard(
    amount: Double,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "Alpha Animation"
    )
    
    LaunchedEffect(key1 = Unit) {
        isVisible = true
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp)
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Spent this week",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        )
        
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "$",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    fontSize = 36.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 64.sp
                )
            )
        }
    }
} 