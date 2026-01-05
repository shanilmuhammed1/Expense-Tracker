package com.shani.moneymanger

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

enum class ViewMode(val displayName: String) {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month")
}

data class DateRange(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val viewMode: ViewMode,
    val displayText: String
) {
    companion object {
        fun forDay(date: LocalDate): DateRange {
            val start = date.atStartOfDay()
            val end = date.atTime(23, 59, 59)

            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
            val displayText = date.format(formatter)

            return DateRange(
                startDate = start,
                endDate = end,
                viewMode = ViewMode.DAY,
                displayText = displayText
            )
        }

        fun forWeek(date: LocalDate): DateRange {
            val monday = date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            val sunday = monday.plusDays(6)

            val start = monday.atStartOfDay()
            val end = sunday.atTime(23, 59, 59)

            val formatter = DateTimeFormatter.ofPattern("MMM dd")
            val displayText = "${monday.format(formatter)} - ${sunday.format(formatter)}"

            return DateRange(
                startDate = start,
                endDate = end,
                viewMode = ViewMode.WEEK,
                displayText = displayText
            )
        }

        fun forMonth(year: Int, month: Int): DateRange {
            val firstDay = LocalDate.of(year, month, 1)
            val lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth())

            val start = firstDay.atStartOfDay()
            val end = lastDay.atTime(23, 59, 59)

            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            val displayText = firstDay.format(formatter)

            return DateRange(
                startDate = start,
                endDate = end,
                viewMode = ViewMode.MONTH,
                displayText = displayText
            )
        }

        fun current(viewMode: ViewMode): DateRange {
            val today = LocalDate.now()
            return when (viewMode) {
                ViewMode.DAY -> forDay(today)
                ViewMode.WEEK -> forWeek(today)
                ViewMode.MONTH -> forMonth(today.year, today.monthValue)
            }
        }
    }

    fun previous(): DateRange {
        return when (viewMode) {
            ViewMode.DAY -> {
                val prevDay = startDate.toLocalDate().minusDays(1)
                forDay(prevDay)
            }
            ViewMode.WEEK -> {
                val prevWeek = startDate.toLocalDate().minusWeeks(1)
                forWeek(prevWeek)
            }
            ViewMode.MONTH -> {
                val prevMonth = startDate.toLocalDate().minusMonths(1)
                forMonth(prevMonth.year, prevMonth.monthValue)
            }
        }
    }

    fun next(): DateRange {
        return when (viewMode) {
            ViewMode.DAY -> {
                val nextDay = startDate.toLocalDate().plusDays(1)
                forDay(nextDay)
            }
            ViewMode.WEEK -> {
                val nextWeek = startDate.toLocalDate().plusWeeks(1)
                forWeek(nextWeek)
            }
            ViewMode.MONTH -> {
                val nextMonth = startDate.toLocalDate().plusMonths(1)
                forMonth(nextMonth.year, nextMonth.monthValue)
            }
        }
    }
}