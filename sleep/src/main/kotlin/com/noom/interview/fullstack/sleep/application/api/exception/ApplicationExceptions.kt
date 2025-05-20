package com.noom.interview.fullstack.sleep.application.api.exception

class OverlapSleepTimeException(
    override val message: String
) : RuntimeException()

class SleepLogNotFoundException(
    override val message: String
) : RuntimeException()