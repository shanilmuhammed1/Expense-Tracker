package com.shani.moneymanger
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier

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