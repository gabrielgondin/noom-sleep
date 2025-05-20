package com.noom.interview.fullstack.sleep.domain.sleep

import com.noom.interview.fullstack.sleep.DatabaseConfig
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import java.time.LocalDate
import java.time.LocalDateTime

class SleepAggregationViewRepositoryTest @Autowired constructor(
    val sleepAggregationViewRepository: SleepAggregationViewRepository,
    val sleepLogRepository: SleepLogRepository
): DatabaseConfig() {

    private val sleepLogService = SleepLogServiceImpl()

    @BeforeEach
    fun createSleepLogs() {
        val day = LocalDate.of(2025, 5, 5)
        val start = LocalDateTime.of(2025, 5, 5, 22, 10, 0)
        val end = LocalDateTime.of(2025, 5, 6, 6, 35, 0)
        val user = super.createUser()
        sleepLogRepository.save(SleepLog(user = user, day = day, mood = SleepLog.MoodMorning.BAD,
            startAt = start, endAt = end, totalMinutes = sleepLogService.calculateSleepDuration(start, end)))
        sleepLogRepository.save(SleepLog(user = user, day = LocalDate.now().minusDays(2), mood = SleepLog.MoodMorning.BAD,
            startAt = start.minusDays(2), endAt = end.minusDays(2), totalMinutes = sleepLogService.calculateSleepDuration(start, end)))
        sleepLogRepository.save(SleepLog(user = user, day = LocalDate.now().minusDays(3), mood = SleepLog.MoodMorning.GOOD,
            startAt = start.minusDays(3), endAt = end.plusDays(3), totalMinutes = sleepLogService.calculateSleepDuration(start, end)))
    }

    @Test
    fun `should return sleep aggregation view when sleep logs exist`() {
        val userId = 1L
        val fromDay = LocalDate.of(2025, 5, 1)

        val result = sleepAggregationViewRepository.findAveragesByUserIdAndFromDay(userId, fromDay)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(1515, result?.totalMinutes)
    }

}
