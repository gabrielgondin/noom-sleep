package com.noom.interview.fullstack.sleep.domain.sleep

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Sleep(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val day: LocalDate,
    val mood: MoodMorning,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val totalMinutes: Long
) {

    enum class MoodMorning {
        BAD, GOOD, OK
    }

    companion object {
        fun getHours(total: Long): Long = total.div(60)
        fun getMinutes(total: Long): Long = total.rem(60)
    }
}
