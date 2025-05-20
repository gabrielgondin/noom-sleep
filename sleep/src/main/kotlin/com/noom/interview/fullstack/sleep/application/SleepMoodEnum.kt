package com.noom.interview.fullstack.sleep.application

import com.noom.interview.fullstack.sleep.domain.sleep.SleepLog

enum class SleepMoodEnum(val description: String, val moodMorning: SleepLog.MoodMorning) {
    GOOD("Good", SleepLog.MoodMorning.GOOD),
    BAD("Bad", SleepLog.MoodMorning.BAD),
    OK("OK", SleepLog.MoodMorning.OK);

    
    
    companion object {
        fun getDescriptionFrom(moodMorning: SleepLog.MoodMorning): String {
            return values().find { it.moodMorning == moodMorning }?.description ?: "--"
        }

        fun from(moodMorning: SleepLog.MoodMorning): SleepMoodEnum {
            return values().find { it.moodMorning == moodMorning }
                ?: throw IllegalArgumentException("No SleepMoodEnum found for MoodMorning: $moodMorning")
        }
    }
}