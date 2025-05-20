package com.noom.interview.fullstack.sleep.domain.sleep

import java.time.LocalDateTime

interface SleepLogService {
    fun calculateSleepDuration(startAt: LocalDateTime, endAt: LocalDateTime): Long
}