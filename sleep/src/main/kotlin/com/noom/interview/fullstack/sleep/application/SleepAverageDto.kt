package com.noom.interview.fullstack.sleep.application

import com.noom.interview.fullstack.sleep.domain.sleep.SleepAggregationView
import java.time.LocalDate

class SleepAverageDto(
    val startAt: LocalDate,
    val endAt: LocalDate,
    val averageStartTime: String,
    val averageEndTime: String,
    val averageSleepDuration: String,
    val moodCount: Map<String, Int>
) {

    companion object{
        fun fromSleepView(sleepView: SleepAggregationView, startAt: LocalDate, endAt: LocalDate): SleepAverageDto {
            return SleepAverageDto(
                startAt,
                endAt,
                sleepView.averageStartTime.toString(),
                sleepView.averageEndTime.toString(),
                sleepView.getAverageSleepDuration(startAt, endAt).getTotalSleepFormatted(),
                sleepView.getMoodMap()
            )
        }
    }
}
