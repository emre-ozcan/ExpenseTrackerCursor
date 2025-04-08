package com.emreozcan.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emreozcan.expensetracker.model.Expense
import com.emreozcan.expensetracker.model.ExpenseCategory
import com.emreozcan.expensetracker.ui.components.CategoryIcon
import com.emreozcan.expensetracker.viewmodel.ExpenseViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.OTHER) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "New Expense") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Miktar giriş alanı
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { 
                        // Sadece sayısal değer ve nokta karakterine izin ver
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "0.00",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "$",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            ),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Kategori seçimi
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showBottomSheet = true
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji kullanarak kategori gösterimi
                    Text(
                        text = getCategoryEmoji(selectedCategory),
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    Text(
                        text = selectedCategory.name.lowercase().replaceFirstChar { it.uppercase() },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Select category",
                        tint = Color.Gray
                    )
                }
            }
            
            // Not ekleme
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Note") },
                placeholder = { Text("Add a note...") }
            )
            
            // Tarih gösterimi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                
                Text(
                    text = "Today",
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Kaydet butonu
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    
                    if (amountValue > 0) {
                        val expense = Expense(
                            amount = amountValue,
                            description = description.ifEmpty { selectedCategory.name },
                            category = selectedCategory,
                            timestamp = LocalDateTime.now()
                        )
                        
                        viewModel.addExpense(expense)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        
        // Kategori seçme bottom sheet
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "EXPENSES",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    GridExpenseCategories(
                        onCategorySelected = { category ->
                            selectedCategory = category
                            scope.launch {
                                sheetState.hide()
                                showBottomSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GridExpenseCategories(
    onCategorySelected: (ExpenseCategory) -> Unit
) {
    val categories = listOf(
        ExpenseCategory.FOOD to "Food" to "🍔",
        ExpenseCategory.TRANSPORTATION to "Transport" to "🚗",
        ExpenseCategory.ENTERTAINMENT to "Entertainment" to "🎬",
        ExpenseCategory.SHOPPING to "Shopping" to "🛍️",
        ExpenseCategory.UTILITIES to "Utilities" to "💧",
        ExpenseCategory.RENT to "Rent" to "🏠",
        ExpenseCategory.HEALTH to "Health" to "🏥",
        ExpenseCategory.EDUCATION to "Education" to "🎓",
        ExpenseCategory.TRAVEL to "Travel" to "✈️",
        ExpenseCategory.PETS to "Pets" to "🐶",
        ExpenseCategory.COFFEE to "Coffee" to "☕",
        ExpenseCategory.SNACKS to "Snacks" to "🍪",
        ExpenseCategory.OTHER to "Other" to "📌"
    )
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 4 sütunlu bir grid tasarımı
        for (i in categories.indices step 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 0 until 4) {
                    val index = i + j
                    if (index < categories.size) {
                        val (categoryWithLabel, emoji) = categories[index]
                        val (category, label) = categoryWithLabel
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onCategorySelected(category) }
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center
                            )
                            
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else {
                        // Boş alan, düzeni korumak için
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Kategori için emoji alma fonksiyonu
@Composable
fun getCategoryEmoji(category: ExpenseCategory): String {
    return when (category) {
        ExpenseCategory.FOOD -> "🍔"
        ExpenseCategory.TRANSPORTATION -> "🚗"
        ExpenseCategory.ENTERTAINMENT -> "🎬"
        ExpenseCategory.SHOPPING -> "🛍️"
        ExpenseCategory.UTILITIES -> "💧"
        ExpenseCategory.RENT -> "🏠"
        ExpenseCategory.HEALTH -> "🏥"
        ExpenseCategory.EDUCATION -> "🎓"
        ExpenseCategory.TRAVEL -> "✈️"
        ExpenseCategory.PETS -> "🐶"
        ExpenseCategory.COFFEE -> "☕"
        ExpenseCategory.SNACKS -> "🍪"
        ExpenseCategory.SALARY -> "💰"
        ExpenseCategory.OTHER -> "📌"
    }
} 