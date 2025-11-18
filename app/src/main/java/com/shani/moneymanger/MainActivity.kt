package com.shani.moneymanger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shani.moneymanger.ui.theme.MoneyMangerTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyMangerTheme {
                Homescreen()
            }
        }
    }
}
@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    onConfirm: (Transaction) -> Unit
){
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var selectedCategory by remember { mutableStateOf(Category.BILLS) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var typeDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = { Text("Add Transaction") },
        text = {
            Column {
                // Amount input
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Type dropdown (Income/Expense)
                Box {
                    OutlinedTextField(
                        value = selectedType.displayName,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Type") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { typeDropdownExpanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = typeDropdownExpanded,
                        onDismissRequest = { typeDropdownExpanded = false }
                    ) {
                        TransactionType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    selectedType = type
                                    typeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category dropdown
                Box {
                    OutlinedTextField(
                        value = selectedCategory.displayName,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { categoryDropdownExpanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        Category.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    selectedCategory = category
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue != null && amountValue > 0) {
                        val transaction = Transaction(
                            amount = amountValue,
                            type = selectedType,
                            category = selectedCategory,
                            description = description
                        )
                        onConfirm(transaction)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun Homescreen(){
    val repository = TransactionRepository.getInstance(LocalContext.current)
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "My Expense Tracker",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            PeriodSelector()
            Spacer(modifier = Modifier.height(16.dp))
            FinancialSummaryCard()
            Spacer(modifier = Modifier.height(16.dp))
            TransactionsList()
            Spacer(modifier = Modifier.height(80.dp)) // Extra space for FAB
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    // Add Transaction Dialog
    if (showAddDialog) {
        AddTransactionDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { transaction ->
                repository.addTransaction(transaction)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PeriodSelector(){
    var selectedPeriod by remember { mutableStateOf(TimePeriod.DAILY) }
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "${selectedPeriod.displayName} ▼")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TimePeriod.values().forEach { period ->
                DropdownMenuItem(
                    text = { Text(text = period.displayName) },
                    onClick = {
                        selectedPeriod = period
                        expanded = false
                    }
                )
            }

        }
    }

}
@Composable
fun TransactionsList(){
    val repository = TransactionRepository.getInstance(LocalContext.current)
    val transactions by repository.transactions.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = "Recent Transactions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (transactions.isEmpty()) {
                // Show message when no transactions exist
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "No transactions yet. Add your first transaction!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(transactions) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                }
            }
        }
    }
}
@Composable
fun TransactionItem(transaction: Transaction){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // Category icon
            CategoryIcon(category = transaction.category)

            Spacer(modifier = Modifier.width(12.dp))

            // Transaction details (takes remaining space)
            Column(modifier = Modifier.weight(1f)){
                Text(
                    text = transaction.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = transaction.category.displayName,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Amount (right side)
            Text(
                text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}₹${transaction.amount.toInt()}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.INCOME) {
                    androidx.compose.ui.graphics.Color(0xFF4CAF50) // Green for income
                } else {
                    androidx.compose.ui.graphics.Color(0xFFFF5722) // Red for expense
                }
            )
        }
    }
}
@Composable
fun CategoryIcon(category: Category){
    val backgroundColor = when (category) {
        Category.SALARY, Category.BUSINESS, Category.INVESTMENT, Category.GIFTS ->
            androidx.compose.ui.graphics.Color(0xFF4CAF50) // Green for income categories
        Category.FOOD -> androidx.compose.ui.graphics.Color(0xFFFF9800) // Orange
        Category.TRANSPORTATION -> androidx.compose.ui.graphics.Color(0xFF2196F3) // Blue
        Category.ENTERTAINMENT -> androidx.compose.ui.graphics.Color(0xFF9C27B0) // Purple
        Category.SHOPPING -> androidx.compose.ui.graphics.Color(0xFFE91E63) // Pink
        Category.BILLS, Category.RENT -> androidx.compose.ui.graphics.Color(0xFF607D8B) // Blue Grey
        Category.HEALTH -> androidx.compose.ui.graphics.Color(0xFFF44336) // Red
        Category.OTHER -> androidx.compose.ui.graphics.Color(0xFF795548) // Brown
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = backgroundColor.copy(alpha = 0.2f),
                shape = androidx.compose.foundation.shape.CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = category.displayName.first().toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = backgroundColor
        )
    }
}
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






