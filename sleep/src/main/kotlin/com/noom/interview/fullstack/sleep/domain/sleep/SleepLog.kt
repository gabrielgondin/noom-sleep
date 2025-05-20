package com.noom.interview.fullstack.sleep.domain.sleep

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity(name = "sleep_logs")
data class SleepLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @OneToOne(fetch = FetchType.LAZY)
    val user: User,
    val day: LocalDate,
    @Enumerated(EnumType.STRING)
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
