package com.noom.interview.fullstack.sleep.application.api

import com.noom.interview.fullstack.sleep.application.SleepMoodEnum
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateSleepLogRequest(
    val userId: Long,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val day: LocalDate = LocalDate.now(),
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val startAt: LocalDateTime,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val endAt: LocalDateTime,
    val mood: SleepMoodEnum
)
