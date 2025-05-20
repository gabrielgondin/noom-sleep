package com.noom.interview.fullstack.sleep.domain.sleep

import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class SleepAggregationView(
    @Id
    val userId: Long,
    val averageStartTime: LocalTime,
    val averageEndTime: LocalTime,
    val totalMinutes: Long,
    val goodCount: Long,
    val badCount: Long,
    val okCount: Long
) {
    fun getMoodMap(): Map<String, Int>{
        val moodMap = mutableMapOf<String, Int>()
        moodMap[SleepLog.MoodMorning.GOOD.name] = goodCount.toInt()
        moodMap[SleepLog.MoodMorning.BAD.name] = badCount.toInt()
        moodMap[SleepLog.MoodMorning.OK.name] = okCount.toInt()
        return moodMap
    }

    fun getAverageSleepDuration(startAt: LocalDate, endAt: LocalDate): Long {
        val days = endAt.toEpochDay() - startAt.toEpochDay()
        return totalMinutes.div(days)
    }
}
