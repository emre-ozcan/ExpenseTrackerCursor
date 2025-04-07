package com.emreozcan.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.emreozcan.expensetracker.model.ExpenseCategory

@Composable
fun CategoryIcon(category: ExpenseCategory, modifier: Modifier = Modifier) {
    val (icon, backgroundColor) = when (category) {
        ExpenseCategory.FOOD -> Icons.Default.Fastfood to Color(0xFFF44336)
        ExpenseCategory.TRANSPORTATION -> Icons.Default.DirectionsCar to Color(0xFF2196F3)
        ExpenseCategory.ENTERTAINMENT -> Icons.Default.Movie to Color(0xFF9C27B0)
        ExpenseCategory.SHOPPING -> Icons.Default.ShoppingCart to Color(0xFF4CAF50)
        ExpenseCategory.COFFEE -> Icons.Default.LocalCafe to Color(0xFF795548)
        ExpenseCategory.PETS -> Icons.Default.Pets to Color(0xFFFF9800)
        ExpenseCategory.SNACKS -> Icons.Default.Fastfood to Color(0xFFE91E63)
        ExpenseCategory.SALARY -> Icons.Default.AttachMoney to Color(0xFF4CAF50)
        else -> Icons.Default.ShoppingCart to Color(0xFF607D8B)
    }
    
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Category: ${category.name}",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
} 