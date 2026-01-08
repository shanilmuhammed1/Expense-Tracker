package com.shani.moneymanger

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodSelector(
    currentRange: DateRange,
    onRangeChanged: (DateRange) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentRange.startDate
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ⭐ View mode selector (Day/Week/Month tabs)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ViewMode.values().forEach { mode ->
                    FilterChip(
                        selected = currentRange.viewMode == mode,
                        onClick = {
                            val newRange = DateRange.current(mode)
                            onRangeChanged(newRange)
                        },
                        label = { Text(mode.displayName) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ⭐ Navigation row (Previous / Display / Next)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous button
                IconButton(
                    onClick = {
                        val prevRange = currentRange.previous()
                        onRangeChanged(prevRange)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous"
                    )
                }

                // Current period display
                Text(
                    text = currentRange.displayText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable{
                        showDatePicker  = true
                    }
                )

                // Next button
                IconButton(
                    onClick = {
                        val nextRange = currentRange.next()
                        onRangeChanged(nextRange)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
if (showDatePicker) {
    DatePickerDialog(
        onDismissRequest = {
            // User tapped outside or pressed back
            showDatePicker = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Get the selected date from the picker
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convert milliseconds to LocalDate
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        // Create a new DateRange based on current ViewMode
                        val newRange = DateRange.fromDate(
                            date = selectedDate,
                            mode = currentRange.viewMode
                        )
                        onRangeChanged(newRange)
                    }
                    showDatePicker = false
                }
            ) {
                Text("Select")
            }
        },
        dismissButton = {
            TextButton(onClick = { showDatePicker = false }) {
                Text("Cancel")
            }
        }
    ) {
        // The actual date picker content
        DatePicker(state = datePickerState)
    }
}
}