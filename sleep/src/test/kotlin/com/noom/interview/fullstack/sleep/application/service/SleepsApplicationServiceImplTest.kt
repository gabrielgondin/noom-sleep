package com.noom.interview.fullstack.sleep.application.service

import com.noom.interview.fullstack.sleep.application.SleepMoodEnum
import com.noom.interview.fullstack.sleep.application.api.CreateSleepRequest
import com.noom.interview.fullstack.sleep.domain.sleep.Sleep
import com.noom.interview.fullstack.sleep.domain.sleep.SleepRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
class SleepsApplicationServiceImplTest {

    private lateinit var sleepRepository: SleepRepository
    private lateinit var sleepsApplicationServiceImpl: SleepsApplicationServiceImpl

    @BeforeEach
    fun setup() {
        sleepRepository = mock(SleepRepository::class.java)
        sleepsApplicationServiceImpl = SleepsApplicationServiceImpl(sleepRepository)
    }

    @Test
    fun `addSleepLog should create and save a valid Sleep entry`() {
        // Arrange
        val command = CreateSleepRequest(
            day = LocalDate.of(2025, 5, 1),
            startAt = LocalDateTime.of(2025, 1, 1,22, 0),
            endAt = LocalDateTime.of(2025, 1,2, 6, 0),
            mood = SleepMoodEnum.OK
        )
        val expectedStartAt = LocalDate.of(2025, 5, 1).atTime(22, 0)
        val expectedEndAt = LocalDate.of(2025, 5, 2).atTime(6, 0)
        val savedSleep = Sleep(
            day = command.day,
            startAt = expectedStartAt,
            endAt = expectedEndAt,
            mood = Sleep.MoodMorning.OK,
            totalMinutes = 480
        )

        `when`(sleepRepository.save(savedSleep)).thenReturn(savedSleep)

        // Act
        val result = sleepsApplicationServiceImpl.addSleepLog(command)

        // Assert
        assertNotNull(result)
        assertEquals(command.day, result.day)
        assertEquals(expectedStartAt, result.startAt)
        assertEquals(expectedEndAt, result.endAt)
        assertEquals(Sleep.MoodMorning.OK, result.mood)
        assertEquals(480, result.totalMinutes)
        verify(sleepRepository).save(savedSleep)
    }

    @Test
    fun `addSleepLog should throw an exception for invalid date`() {
        // Arrange
        val command = CreateSleepRequest(
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
}