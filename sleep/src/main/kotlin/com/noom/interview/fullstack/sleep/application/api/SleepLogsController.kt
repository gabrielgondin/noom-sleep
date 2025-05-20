package com.noom.interview.fullstack.sleep.application.api

import com.noom.interview.fullstack.sleep.application.SleepLogDto
import com.noom.interview.fullstack.sleep.application.service.SleepLogsApplicationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sleeps")
class SleepLogsController(
    private val sleepLogsApplicationService: SleepLogsApplicationService
) {

    @PostMapping
    fun lastSleep(@RequestBody request: CreateSleepLogRequest): SleepLogDto {
        this.validateParams(request)
        val sleepLog = sleepLogsApplicationService.addSleepLog(request)
        return SleepLogDto.fromSleep(sleepLog)
    }

    private fun validateParams(request: CreateSleepLogRequest) {
        if (request.startAt.isAfter(request.endAt)) {
            throw IllegalArgumentException("Start time must be before end time")
        }
    }
}