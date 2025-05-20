package com.noom.interview.fullstack.sleep.domain.sleep

import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class SleepLogServiceImpl : SleepLogService {

    override fun calculateSleepDuration(startAt: LocalDateTime, endAt: LocalDateTime): Long {
        val duration = Duration.between(startAt, endAt)
        return duration.toMinutes()
    }
}