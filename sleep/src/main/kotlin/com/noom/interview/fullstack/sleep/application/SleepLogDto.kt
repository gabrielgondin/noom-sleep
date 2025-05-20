package com.noom.interview.fullstack.sleep.application

import com.noom.interview.fullstack.sleep.domain.sleep.SleepLog
import java.time.LocalDate

class SleepLogDto(val day: LocalDate, val startAt: String, val endAt: String, val totalSleep: String, val mood: String) {

    companion object{
        fun fromSleep(sleepLog: SleepLog): SleepLogDto {
            return SleepLogDto(
                sleepLog.day,
                sleepLog.startAt.getMeridiemTimeFormat(),
                sleepLog.endAt.getMeridiemTimeFormat(),
                sleepLog.totalMinutes.getTotalSleepFormatted(),
                SleepMoodEnum.getDescriptionFrom(sleepLog.mood)
            )
        }
    }
}
