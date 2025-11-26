package com.shani.moneymanger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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