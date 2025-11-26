package com.shani.moneymanger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun FinancialSummaryCard(){val repository = TransactionRepository.getInstance(LocalContext.current)
    val transactions by repository.transactions.collectAsState()
    // Get real data from repository instead of sample data
    val Summary = remember(transactions){ repository.getFinancialSummary()}
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ){
            SummaryItem(label = "Total Income",
                amount = Summary.totalIncome,
                color = androidx.compose.ui.graphics.Color(0xFF4CAF50) // Green
            )
            SummaryItem(label = "Total Expense",
                amount = Summary.totalExpense,
                color = androidx.compose.ui.graphics.Color(0xFFFF5722) // Red
            )
            SummaryItem(label = "Savings",
                amount = Summary.Savings,
                color = androidx.compose.ui.graphics.Color(0xFF2196F3)
            )

        }
    }

}
@Composable
fun SummaryItem(
    label: String,
    amount: Double, color:
    androidx.compose.ui.graphics.Color){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = label, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "₹${amount.toString()}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color)
    }

}
