package com.noom.interview.fullstack.sleep.domain.sleep

import org.springframework.data.repository.CrudRepository

interface SleepLogRepository: CrudRepository<SleepLog, Long> {

    fun findFirstByUserIdOrderByDayDesc(userId: Long): SleepLog?
}