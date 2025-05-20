package com.noom.interview.fullstack.sleep.application.api

import com.noom.interview.fullstack.sleep.SleepApplication.Companion.UNIT_TEST_PROFILE
import com.noom.interview.fullstack.sleep.domain.sleep.SleepLog
import com.noom.interview.fullstack.sleep.domain.sleep.SleepLogRepository
import com.noom.interview.fullstack.sleep.domain.sleep.User
import com.noom.interview.fullstack.sleep.domain.sleep.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional

@SpringBootTest
@ActiveProfiles(UNIT_TEST_PROFILE)
@AutoConfigureMockMvc
class SleepsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sleepLogRepository: SleepLogRepository

    @MockBean
    private lateinit var userRepository: UserRepository
    

    @Test
    fun contextLoads() {
        Assertions.assertThat(true).isTrue()
    }

    @Test
    fun `should return bad request when posting new sleep is missing data`() {
        mockMvc.perform(
            post("/api/v1/sleeps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"startTime": "2023-01-01T10:00:00", "endTime": "2023-01-01T18:00:00"}""")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should create new sleep successfully`() {
        `when`(userRepository.findById(any())).thenReturn(Optional.of(User(1L, "User", LocalDateTime.now(), LocalDateTime.now())))
        `when`(sleepLogRepository.save(any(SleepLog::class.java))).thenAnswer { it.arguments[0] }
        mockMvc.perform(
            post("/api/v1/sleeps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "userId": "1", 
                        "day": "2023-01-01", 
                        "startAt": "2023-01-01T22:00:00", 
                        "endAt": "2023-01-02T08:01:00",
                        "mood": "GOOD"
                    }
                """.trimIndent()
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.startAt").value("10:00 pm"))
            .andExpect(jsonPath("$.endAt").value("08:01 am"))
            .andExpect(jsonPath("$.totalSleep").value("10 h 1 min"))
            .andExpect(jsonPath("$.mood").value("Good"))
    }


    @Test
    fun `should return latest sleep log successfully`() {
        val user = User(1L, "User", LocalDateTime.now(), LocalDateTime.now())
        val sleepLog = SleepLog(
            1L,
            user,
            LocalDate.of(2023, 1, 1),
            SleepLog.MoodMorning.GOOD,
            LocalDateTime.of(2023, 1, 1, 22, 0),
            LocalDateTime.of(2023, 1, 2, 8, 1),
            601
        )
        `when`(userRepository.findById(any())).thenReturn(Optional.of(user))
        `when`(sleepLogRepository.findFirstByUserIdOrderByDayDesc(1L)).thenReturn(sleepLog)

        mockMvc.perform(
            get("/api/v1/sleeps/1/latest")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.startAt").value("10:00 pm"))
            .andExpect(jsonPath("$.endAt").value("08:01 am"))
            .andExpect(jsonPath("$.totalSleep").value("10 h 1 min"))
            .andExpect(jsonPath("$.mood").value("Good"))
    }
}
