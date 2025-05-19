package com.noom.interview.fullstack.sleep.application.api

import com.noom.interview.fullstack.sleep.application.SleepMoodEnum
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateSleepRequest(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = ["HH:mm:ss"])
    val day: LocalDate = LocalDate.now(),
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = ["yyyy-MM-dd HH:mm:ss"])
    val startAt: LocalDateTime,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, fallbackPatterns = ["yyyy-MM-dd HH:mm:ss"])
    val endAt: LocalDateTime,
    val mood: SleepMoodEnum
)
