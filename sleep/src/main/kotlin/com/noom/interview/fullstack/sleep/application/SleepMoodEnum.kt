package com.noom.interview.fullstack.sleep.application

import com.noom.interview.fullstack.sleep.domain.sleep.Sleep

enum class SleepMoodEnum(val description: String, val moodMorning: Sleep.MoodMorning) {
    GOOD("Good", Sleep.MoodMorning.GOOD),
    BAD("Bad", Sleep.MoodMorning.BAD),
    OK("OK", Sleep.MoodMorning.OK);

    
    
    companion object {
        fun getDescriptionFrom(moodMorning: Sleep.MoodMorning): String {
            return values().find { it.moodMorning == moodMorning }?.description ?: "--"
        }

        fun from(moodMorning: Sleep.MoodMorning): SleepMoodEnum {
            return values().find { it.moodMorning == moodMorning }
                ?: throw IllegalArgumentException("No SleepMoodEnum found for MoodMorning: $moodMorning")
        }
    }
}