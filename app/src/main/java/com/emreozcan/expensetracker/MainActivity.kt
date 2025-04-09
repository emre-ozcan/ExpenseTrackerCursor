package com.emreozcan.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emreozcan.expensetracker.ui.navigation.BottomNavItem
import com.emreozcan.expensetracker.ui.navigation.BottomNavigation
import com.emreozcan.expensetracker.ui.screens.AddExpenseScreen
import com.emreozcan.expensetracker.ui.screens.ExpensesScreen
import com.emreozcan.expensetracker.ui.screens.HomeScreen
import com.emreozcan.expensetracker.ui.theme.ExpenseTrackerTheme
import com.emreozcan.expensetracker.viewmodel.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    ExpenseTrackerApp()
                }
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()
    
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Box(
                modifier = Modifier.background(Color.White)
            ) {
                BottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onAddExpense = {
                        navController.navigate("add_expense")
                    }
                )
            }
            composable(BottomNavItem.Stats.route) {
                val viewModel: ExpenseViewModel = hiltViewModel()
                ExpensesScreen(
                    weeklyExpenses = viewModel.weeklyExpenses.collectAsState().value,
                    totalSpent = viewModel.totalSpentThisWeek.value,
                    percentageChange = viewModel.getWeeklyExpensePercent(),
                    expensesByCategory = viewModel.weeklyExpensesByCategory.collectAsState().value
                )
            }
            composable(BottomNavItem.Reports.route) {
                // TODO: Reports Screen
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    // Placeholder for Reports Screen
                }
            }
            composable("add_expense") {
                AddExpenseScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ExpenseTrackerTheme {
        ExpenseTrackerApp()
    }
}