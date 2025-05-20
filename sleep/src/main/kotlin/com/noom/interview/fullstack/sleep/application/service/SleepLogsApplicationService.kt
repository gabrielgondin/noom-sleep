package com.noom.interview.fullstack.sleep.application.service

import com.noom.interview.fullstack.sleep.application.api.CreateSleepLogRequest
import com.noom.interview.fullstack.sleep.domain.sleep.SleepLog

interface SleepLogsApplicationService {

    fun addSleepLog(request: CreateSleepLogRequest): SleepLog

    fun retrieveLatestSleepLog(userId: Long): SleepLog?
}