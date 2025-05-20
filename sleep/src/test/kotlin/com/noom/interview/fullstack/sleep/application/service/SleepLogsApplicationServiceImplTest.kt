package com.noom.interview.fullstack.sleep.application.service

import com.noom.interview.fullstack.sleep.application.SleepMoodEnum
import com.noom.interview.fullstack.sleep.application.api.CreateSleepLogRequest
import com.noom.interview.fullstack.sleep.application.api.exception.OverlapSleepTimeException
import com.noom.interview.fullstack.sleep.domain.sleep.SleepAggregationView
import com.noom.interview.fullstack.sleep.domain.sleep.SleepAggregationViewRepository
import com.noom.interview.fullstack.sleep.domain.sleep.SleepLog
import com.noom.interview.fullstack.sleep.domain.sleep.SleepLogRepository
import com.noom.interview.fullstack.sleep.domain.sleep.SleepLogService
import com.noom.interview.fullstack.sleep.domain.sleep.User
import com.noom.interview.fullstack.sleep.domain.sleep.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.any
import org.springframework.dao.DataIntegrityViolationException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Optional

class SleepLogsApplicationServiceImplTest {

    private lateinit var sleepLogRepository: SleepLogRepository
    private lateinit var sleepLogService: SleepLogService
    private lateinit var userRepository: UserRepository
    private lateinit var sleepAggregationViewRepository: SleepAggregationViewRepository
    private lateinit var sleepsApplicationServiceImpl: SleepLogsApplicationServiceImpl

    @BeforeEach
    fun setup() {
        sleepLogRepository = mock(SleepLogRepository::class.java)
        sleepLogService = mock(SleepLogService::class.java)
        userRepository = mock(UserRepository::class.java)
        sleepAggregationViewRepository = mock(SleepAggregationViewRepository::class.java)
        sleepsApplicationServiceImpl = SleepLogsApplicationServiceImpl(sleepLogRepository, sleepAggregationViewRepository, sleepLogService, userRepository)
    }

    @Test
    fun `addSleepLog should create and save a valid Sleep entry`() {
        // Arrange
        val command = CreateSleepLogRequest(
            userId = 1L,
            day = LocalDate.of(2025, 5, 1),
            startAt = LocalDateTime.of(2025, 5, 1,22, 0),
            endAt = LocalDateTime.of(2025, 5,2, 6, 0),
            mood = SleepMoodEnum.OK
        )
        val expectedStartAt = LocalDate.of(2025, 5, 1).atTime(22, 0)
        val expectedEndAt = LocalDate.of(2025, 5, 2).atTime(6, 0)
        `when`(userRepository.findById(any())).thenReturn(Optional.of(User(1L, "User", LocalDateTime.now(), LocalDateTime.now())))
        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenAnswer {  it.arguments[0] }


        // Act
        val result = sleepsApplicationServiceImpl.addSleepLog(command)

        // Assert
        assertEquals(command.day, result.day)
        assertEquals(expectedStartAt, result.startAt)
        assertEquals(expectedEndAt, result.endAt)
        assertEquals(SleepLog.MoodMorning.OK, result.mood)
        verify(sleepLogRepository).save(any())
    }

    @Test
    fun `addSleepLog should throw an exception for invalid date`() {
        // Arrange
        val command = CreateSleepLogRequest(
            1L,
            day = LocalDate.of(2025, 5, 1),
            startAt =  LocalDateTime.of(2025, 1, 1,22, 0),
            endAt =  LocalDateTime.of(2025, 1, 1,21, 0), // Invalid time sequence
            mood = SleepMoodEnum.BAD
        )

        // Act & Assert
        assertThrows <IllegalArgumentException> {
            sleepsApplicationServiceImpl.addSleepLog(command)
        }
    }


    @Test
    fun `addSleepLog should throw an exception when dates don't match day parameter`() {
        // Arrange
        val command = CreateSleepLogRequest(
            userId = 1L,
            day = LocalDate.of(2025, 5, 1),
            startAt = LocalDateTime.of(2025, 6, 1, 22, 0),  // Different month
            endAt = LocalDateTime.of(2025, 6, 2, 6, 0),     // Different month
            mood = SleepMoodEnum.OK
        )

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            sleepsApplicationServiceImpl.addSleepLog(command)
        }
    }

    @Test
    fun `addSleepLog should throw an exception for an Integrity Violation`() {
        // Arrange
        val command = CreateSleepLogRequest(
            userId = 1L,
            day = LocalDate.of(2025, 5, 1),
            startAt = LocalDateTime.of(2025, 5, 1, 22, 0),
            endAt = LocalDateTime.of(2025, 5, 2, 6, 0),
            mood = SleepMoodEnum.OK
        )

        `when`(userRepository.findById(any())).thenReturn(Optional.of(User(1L, "User", LocalDateTime.now(), LocalDateTime.now())))

        `when`(sleepLogRepository.save(any())).thenThrow(DataIntegrityViolationException("Generic violation"))

        // Act & Assert
        assertThrows<DataIntegrityViolationException> {
            sleepsApplicationServiceImpl.addSleepLog(command)
        }
    }

    @Test
    fun `addSleepLog should throw an OverlapSleepTimeException for overlapping sleep logs`() {
        // Arrange
        val command = CreateSleepLogRequest(
            userId = 1L,
            day = LocalDate.of(2025, 5, 1),
            startAt = LocalDateTime.of(2025, 5, 1, 22, 0),
            endAt = LocalDateTime.of(2025, 5, 2, 6, 0),
            mood = SleepMoodEnum.OK
        )

        `when`(userRepository.findById(any())).thenReturn(Optional.of(User(1L, "User", LocalDateTime.now(), LocalDateTime.now())))

        `when`(sleepLogRepository.save(any())).thenThrow(DataIntegrityViolationException("overlapping_sleeps"))

        // Act & Assert
        assertThrows<OverlapSleepTimeException> {
            sleepsApplicationServiceImpl.addSleepLog(command)
        }
    }

    @Test
    fun `retrieveSleepAverage should return average sleep data when exists`() {
        // Arrange
        val userId = 1L
        val fromDay = LocalDate.of(2025, 5, 1)
        val user = User(userId, "User", LocalDateTime.now(), LocalDateTime.now())
        val expectedAverage = SleepAggregationView(1L,
            LocalTime.of(22, 1, 2),
            LocalTime.of(6, 1, 2),
            480L, 2,0,1)

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(sleepAggregationViewRepository.findAveragesByUserIdAndFromDay(userId, fromDay))
            .thenReturn(expectedAverage)

        // Act
        val result = sleepsApplicationServiceImpl.retrieveSleepAverage(userId, fromDay)

        // Assert
        assertEquals(expectedAverage, result)
    }

    @Test
    fun `retrieveSleepAverage should throw IllegalArgumentException when user not found`() {
        // Arrange
        val userId = 1L
        val fromDay = LocalDate.of(2025, 5, 1)

        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            sleepsApplicationServiceImpl.retrieveSleepAverage(userId, fromDay)
        }
    }

    @Test
    fun `retrieveSleepAverage should return null when no sleep logs found`() {
        // Arrange
        val userId = 1L
        val fromDay = LocalDate.of(2025, 5, 1)
        val user = User(userId, "User", LocalDateTime.now(), LocalDateTime.now())

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(user))
        `when`(sleepAggregationViewRepository.findAveragesByUserIdAndFromDay(userId, fromDay))
            .thenReturn(null)

        // Act
        val result = sleepsApplicationServiceImpl.retrieveSleepAverage(userId, fromDay)

        // Assert
        assertEquals(null, result)
    }
}