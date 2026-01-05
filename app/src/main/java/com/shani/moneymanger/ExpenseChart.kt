package com.shani.moneymanger

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExpenseChart(
    transactions: List<Transaction>
) {
    // Calculate expenses from filtered transactions
    val expenses = remember(transactions) {
        transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    val totalExpense = expenses.values.sum()

    // Color palette for pie chart
    val colors = listOf(
        Color(0xFFFF5722), // Red-Orange
        Color(0xFF2196F3), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFFFFC107), // Amber
        Color(0xFF9C27B0), // Purple
        Color(0xFFFF9800), // Orange
        Color(0xFF00BCD4), // Cyan
        Color(0xFFE91E63), // Pink
        Color(0xFF8BC34A), // Light Green
        Color(0xFF673AB7), // Deep Purple
        Color(0xFF009688), // Teal
        Color(0xFFCDDC39)  // Lime
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Expense by Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (expenses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses for this period",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Pie Chart
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val radius = minOf(canvasWidth, canvasHeight) / 2.5f
                    val centerX = canvasWidth / 2
                    val centerY = canvasHeight / 2

                    var startAngle = -90f // Start from top

                    expenses.entries.forEachIndexed { index, entry ->
                        val sweepAngle = ((entry.value / totalExpense) * 360).toFloat()
                        val color = colors[index % colors.size]

                        // Draw pie slice
                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true,
                            topLeft = Offset(centerX - radius, centerY - radius),
                            size = Size(radius * 2, radius * 2)
                        )

                        // Draw slice border
                        drawArc(
                            color = Color.White,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true,
                            topLeft = Offset(centerX - radius, centerY - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = 2f)
                        )

                        startAngle += sweepAngle
                    }

                    // Draw center circle (donut effect - optional)
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.4f,
                        center = Offset(centerX, centerY)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Legend
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    expenses.entries.forEachIndexed { index, entry ->
                        val color = colors[index % colors.size]
                        val percentage = (entry.value / totalExpense * 100)

                        LegendItem(
                            color = color,
                            category = entry.key,
                            amount = entry.value,
                            percentage = percentage
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    category: Category,
    amount: Double,
    percentage: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Color indicator
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color, CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Category icon and name
            Text(
                text = category.icon,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = category.displayName,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Amount and percentage
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "₹${"%.2f".format(amount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${"%.1f".format(percentage)}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}