package com.noom.interview.fullstack.sleep.application

import com.noom.interview.fullstack.sleep.domain.sleep.Sleep
import java.time.LocalDate
import java.time.LocalDateTime

class SleepDto(val day: LocalDate, val startAt: String, val endAt: String, val totalSleep: String, val mood: String) {

    companion object{
        fun fromSleep(sleep: Sleep): SleepDto {
            return SleepDto(
                sleep.day,
                sleep.startAt.getMeridiemTimeFormat(),
                sleep.endAt.getMeridiemTimeFormat(),
                sleep.totalMinutes.getTotalSleepFormatted(),
                SleepMoodEnum.getDescriptionFrom(sleep.mood)
            )
        }


        fun LocalDateTime.getMeridiemTimeFormat(): String {
            var hour = this.hour
            var meridiem = "am"
            if (this.hour >= 12) {
                hour = this.hour - 12
                meridiem = "pm"
            }
            return String.format("%02d:%02d %s", hour, this.minute, meridiem)

        }

        fun Long.getTotalSleepFormatted(): String {
            val hours = Sleep.getHours(this)
            val minutes = Sleep.getMinutes(this)
            return "$hours h $minutes min"
        }
    }
}
