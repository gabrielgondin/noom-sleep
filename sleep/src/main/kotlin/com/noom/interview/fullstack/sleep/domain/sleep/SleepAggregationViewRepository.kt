package com.noom.interview.fullstack.sleep.domain.sleep

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface SleepAggregationViewRepository: CrudRepository<SleepAggregationView, Long> {

    @Query(
        nativeQuery = true,
        value = """
        SELECT 
            s.user_id as user_id,
            AVG(start_at::::time::::interval)::::time as average_start_time,
            AVG(end_at::::time::::interval)::::time as average_end_time,
            SUM(total_minutes) as total_minutes,
            COUNT(*) FILTER (WHERE mood = 'GOOD') as good_count,
            COUNT(*) FILTER (WHERE mood = 'BAD') as bad_count,
            COUNT(*) FILTER (WHERE mood = 'OK') as ok_count
        FROM sleep_logs s 
        WHERE s.user_id = :userId 
        AND s.day >= :startFrom  
        GROUP BY s.user_id
    """
    )
    fun findAveragesByUserIdAndFromDay(userId: Long, startFrom: LocalDate): SleepAggregationView?
}
