package com.noom.interview.fullstack.sleep.domain.sleep

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "users")
data class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val updatedAt: LocalDateTime,
    val createdAt: LocalDateTime
)