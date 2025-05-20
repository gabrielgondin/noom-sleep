package com.noom.interview.fullstack.sleep.application.api

import com.noom.interview.fullstack.sleep.application.SleepLogDto
import com.noom.interview.fullstack.sleep.application.api.exception.SleepLogNotFoundException
import com.noom.interview.fullstack.sleep.application.service.SleepLogsApplicationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/{userId}/latest")
    fun lastestSleep(@PathVariable userId: Long): SleepLogDto {
        return sleepLogsApplicationService.retrieveLatestSleepLog(userId)?.let {
            SleepLogDto.fromSleep(it)
        } ?: throw SleepLogNotFoundException("No sleep logs found for user with id: $userId")
    }

    private fun validateParams(request: CreateSleepLogRequest) {
        if (request.startAt.isAfter(request.endAt)) {
            throw IllegalArgumentException("Start time must be before end time")
        }
    }
}