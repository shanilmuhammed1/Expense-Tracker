package com.shani.moneymanger

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun ExpenseChart() {
    val repository = TransactionRepository.getInstance(LocalContext.current)
    val transactions by repository.transactions.collectAsState()

    val expensesByDay = remember(transactions) {
        transactions.filter { it.type == TransactionType.EXPENSE }.groupBy { it.date.toLocalDate() }
            .mapValues { entry -> entry.value.sumOf { it.amount } }.toSortedMap()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Expense Trend",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (expensesByDay.isEmpty()) {
                Text(
                    text = "No expense data to display",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val dataPoints = expensesByDay.values.toList()
                val maxExpense = dataPoints.maxOrNull() ?: 0.0
                val dates = expensesByDay.keys.toList()
                Canvas(
                    modifier = Modifier.fillMaxWidth().height(250.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val spacing = width / (dataPoints.size + 1)
                    val path = Path()
                    dataPoints.forEachIndexed { index, value ->
                        val x = spacing * (index + 1)
                        val y = height - 40f - (value / maxExpense * (height - 80f) * 0.8f).toFloat()
                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                        drawCircle(
                            color = Color(0xFFFF5722),
                            radius = 8f, center = Offset(x, y)
                        )
                        drawContext.canvas.nativeCanvas.drawText(
                                "₹${value.toInt()}",
                        x,
                        y - 20f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 30f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        )
                        drawContext.canvas.nativeCanvas.drawText(
                            "${dates[index].dayOfMonth}/${dates[index].monthValue}",
                            x,
                            height - 10f,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.GRAY
                                textSize = 25f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFF4CAF50),
                        style = Stroke(width = 3f)
                    )
                }
            }
        }
    }
}