package com.noom.interview.fullstack.sleep.application.api

import com.noom.interview.fullstack.sleep.application.SleepDto
import com.noom.interview.fullstack.sleep.application.service.SleepsApplicationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sleeps")
class SleepsController(
    private val sleepsApplicationService: SleepsApplicationService
) {

    @PostMapping
    fun lastSleep(@RequestBody request: CreateSleepRequest): SleepDto {
        this.validateParams(request)
        val sleepLog = sleepsApplicationService.addSleepLog(request)
        return SleepDto.fromSleep(sleepLog)
    }

    private fun validateParams(request: CreateSleepRequest) {
        if (request.startAt.isAfter(request.endAt)) {
            throw IllegalArgumentException("Start time must be before end time")
        }
    }
}