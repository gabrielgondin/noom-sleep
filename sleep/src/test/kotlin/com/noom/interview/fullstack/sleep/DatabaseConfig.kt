package com.noom.interview.fullstack.sleep

import com.noom.interview.fullstack.sleep.domain.sleep.User
import com.noom.interview.fullstack.sleep.domain.sleep.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("unittest")
class DatabaseConfig {

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository.save(User(1L, "User", LocalDateTime.now(), LocalDateTime.now()))
    }
}