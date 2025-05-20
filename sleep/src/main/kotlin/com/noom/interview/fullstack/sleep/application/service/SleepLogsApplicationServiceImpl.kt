package com.noom.interview.fullstack.sleep.application.service

import com.noom.interview.fullstack.sleep.application.api.CreateSleepLogRequest
import com.noom.interview.fullstack.sleep.application.api.exception.OverlapSleepTimeException
import com.noom.interview.fullstack.sleep.domain.sleep.*
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SleepLogsApplicationServiceImpl(
    private val sleepLogRepository: SleepLogRepository,
    private val sleepAggregationViewRepository: SleepAggregationViewRepository,
    private val sleepLogService: SleepLogService,
    private val userRepository: UserRepository
) : SleepLogsApplicationService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun addSleepLog(request: CreateSleepLogRequest): SleepLog {
        val duration = sleepLogService.calculateSleepDuration(request.startAt, request.endAt)
        if (duration < 0) throw IllegalArgumentException("Invalid time to calculate sleep duration: $duration, startAt: $request.startAt, endAt: $request.endAt")

        return try {
            logger.debug("Saving sleep log for user: ${request.userId}, day: ${request.day}, startAt: ${request.startAt}, endAt: ${request.endAt}")
            sleepLogRepository.save(
                SleepLog(
                    user = getUser(request.userId),
                    day = request.day,
                    startAt = request.startAt,
                    endAt = request.endAt,
                    mood = request.mood.moodMorning,
                    totalMinutes = duration
                )
            )
        } catch (e: DataIntegrityViolationException) {
            when {
                e.message?.contains("overlapping_sleeps") == true ||
                        e.message?.contains("sleep_logs_user_id_day_start_at_end_at_key") == true ->
                    throw OverlapSleepTimeException("Error saving sleep log: ${e.message}")

                else -> throw e
            }
        }
    }

    override fun retrieveLatestSleepLog(userId: Long): SleepLog? {
        logger.debug("Retrieving latest sleep for user: $userId")
        return sleepLogRepository.findFirstByUserIdOrderByDayDesc(this.getUser(userId).id!!)
    }

    override fun retrieveSleepAverage(
        userId: Long,
        fromDay: LocalDate
    ): SleepAggregationView? {
        logger.debug("Retrieving sleep average for user: $userId, from day: $fromDay")
        return sleepAggregationViewRepository.findAveragesByUserIdAndFromDay(this.getUser(userId).id!!, fromDay)
    }

    private fun getUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User with id: $userId not found") }
    }
}
