package com.noom.interview.fullstack.sleep.domain.sleep

import com.noom.interview.fullstack.sleep.DatabaseConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class SleepLogRepositoryTest @Autowired constructor(
    val sleepLogRepository: SleepLogRepository
): DatabaseConfig() {

    private lateinit var user: User

    @BeforeEach
    fun setup() {
        user = super.createUser()
    }

    @Test
    fun `should save sleep entity`() {
        val startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 16, 25))
        val endTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(6, 25, 12))
        val sleepLog = SleepLog(
            user = user,
            day = LocalDate.now(),
            mood = SleepLog.MoodMorning.BAD,
            startAt = startTime,
            endAt = endTime,
            totalMinutes = 10L
        )
        sleepLogRepository.save(sleepLog)
        val sleepFound = sleepLogRepository.findById(sleepLog.id!!)

        assert(sleepFound.isPresent)
        assert(sleepFound.get().day == sleepLog.day)
        assert(sleepFound.get().mood == sleepLog.mood)
    }

    @Test
    fun `should find latest sleep log by user id`() {
        val yesterday = LocalDate.now().minusDays(1)
        val today = LocalDate.now()
        val oldSleepLog = SleepLog(
            user = user,
            day = yesterday,
            mood = SleepLog.MoodMorning.OK,
            startAt = LocalDateTime.of(yesterday, LocalTime.of(22, 0)),
            endAt = LocalDateTime.of(today, LocalTime.of(6, 0)),
            totalMinutes = 480L
        )

        val latestSleepLog = SleepLog(
            user = user,
            day = today,
            mood = SleepLog.MoodMorning.GOOD,
            startAt = LocalDateTime.of(today, LocalTime.of(23, 0)),
            endAt = LocalDateTime.of(today.plusDays(1), LocalTime.of(7, 0)),
            totalMinutes = 480L
        )

        sleepLogRepository.save(oldSleepLog)
        sleepLogRepository.save(latestSleepLog)

        val latest = sleepLogRepository.findFirstByUserIdOrderByDayDesc(user.id!!)

        assert(latest != null)
        assert(latest?.day == today)
        assert(latest?.id == latestSleepLog.id)
    }


}
