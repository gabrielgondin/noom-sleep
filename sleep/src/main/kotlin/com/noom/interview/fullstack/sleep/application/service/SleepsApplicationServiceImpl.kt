package com.noom.interview.fullstack.sleep.application.service

import com.noom.interview.fullstack.sleep.application.api.CreateSleepRequest
import com.noom.interview.fullstack.sleep.domain.sleep.Sleep
import com.noom.interview.fullstack.sleep.domain.sleep.SleepRepository
import com.noom.interview.fullstack.sleep.domain.sleep.SleepServiceImpl
import org.springframework.stereotype.Service

@Service
class SleepsApplicationServiceImpl(
    private val sleepRepository: SleepRepository
) : SleepsApplicationService {

    override fun addSleepLog(command: CreateSleepRequest): Sleep {
        val startAt = command.startAt
        val endAt = command.endAt
        val duration = SleepServiceImpl.calculateSleepDuration(startAt, endAt)
        if (duration < 0) throw IllegalArgumentException("Invalid time to calculate sleep duration: $duration, startAt: $startAt, endAt: $endAt")

        return sleepRepository.save(
            Sleep(
                day = command.day, startAt = startAt, endAt = endAt, mood = command.mood.moodMorning,
                totalMinutes = duration
            )
        )
    }
}
