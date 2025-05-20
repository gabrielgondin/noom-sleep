package com.noom.interview.fullstack.sleep.application

import com.noom.interview.fullstack.sleep.domain.sleep.SleepLog
import java.time.LocalDateTime

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
    val hours = SleepLog.getHours(this)
    val minutes = SleepLog.getMinutes(this)
    return "$hours h $minutes min"
}