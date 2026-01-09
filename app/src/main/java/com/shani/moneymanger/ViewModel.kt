package com.shani.moneymanger

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PeriodViewModel : ViewModel() {
    private val _currentPeriod = MutableStateFlow(DateRange.current(ViewMode.DAY))
    val currentRange: StateFlow<DateRange> = _currentPeriod
    // This line declares a variable named currentRange whose type is StateFlow containing DateRange values.

    fun updateRange(newRange: DateRange) {
        _currentPeriod.value = newRange
    }
}