package com.noom.interview.fullstack.sleep.application.service

import com.noom.interview.fullstack.sleep.application.api.CreateSleepRequest
import com.noom.interview.fullstack.sleep.domain.sleep.Sleep

interface SleepsApplicationService {

    fun addSleepLog(command: CreateSleepRequest): Sleep
}