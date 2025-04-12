package com.noom.interview.fullstack.sleep.domain.sleep

import com.noom.interview.fullstack.sleep.DatabaseConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class SleepRepositoryImplTest @Autowired constructor(
    val sleepRepository: SleepRepository
): DatabaseConfig() {

    @Test
    fun `should save sleep entity`() {
        val startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 16, 25))
        val endTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(6, 25, 12))
        val sleep = Sleep(
            day = LocalDate.now(),
            mood = Sleep.MoodMorning.BAD,
            startAt = startTime,
            endAt = endTime,
            totalMinutes = SleepServiceImpl.calculateSleepDuration(startTime, endTime)
        )
        sleepRepository.save(sleep)
        val sleepFound = sleepRepository.findById(sleep.id!!)

        assert(sleepFound.isPresent)
        assert(sleepFound.get().day == sleep.day)
        assert(sleepFound.get().mood == sleep.mood)
    }
}
