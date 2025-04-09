package com.emreozcan.expensetracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp

data class DailyExpense(
    val day: String,
    val amount: Double
)

@Composable
fun WeeklyExpenseChart(
    weeklyExpenses: List<DailyExpense>,
    totalSpent: Double,
    percentageChange: Int = 0,
    maxHeight: Float = 100f,
    modifier: Modifier = Modifier
) {
    val maxAmount = weeklyExpenses.maxOfOrNull { it.amount } ?: maxHeight.toDouble()
    var isAnimationStarted by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isAnimationStarted = true
    }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Toplam harcama ve yüzde değişim
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "$",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
            )
            Text(
                text = String.format("%.2f", totalSpent),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            
            if (percentageChange != 0) {
                val textColor = if (percentageChange < 0) Color.Green else Color.Red
                val prefix = if (percentageChange < 0) "↓" else "↑"
                
                Text(
                    text = "$prefix $percentageChange%",
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        
        Text(
            text = "Total spent this week",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Çubuk grafik
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            weeklyExpenses.forEach { dailyExpense ->
                BarChart(
                    day = dailyExpense.day,
                    amount = dailyExpense.amount,
                    maxAmount = maxAmount,
                    maxHeight = maxHeight,
                    isAnimated = isAnimationStarted
                )
            }
        }
    }
}

@Composable
fun BarChart(
    day: String,
    amount: Double,
    maxAmount: Double,
    maxHeight: Float,
    isAnimated: Boolean,
    modifier: Modifier = Modifier
) {
    val normalizedHeight = if (maxAmount > 0) (amount / maxAmount) * maxHeight else 0.0
    val animatedHeight by animateFloatAsState(
        targetValue = if (isAnimated) normalizedHeight.toFloat() else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = 100),
        label = "Bar Animation"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(30.dp)
                .height(animatedHeight.dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(
                    color = if (amount > 0) Color(0xFF1B1D36) else Color.LightGray
                )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = day,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
} 