package com.acme.spring_test_database_restoration_poc

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

@ExtendWith(DatabaseRestorationExtension::class)
@SpringBootTest(classes = [Application::class])
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class BTest {

  @Autowired
  private lateinit var entityManager: EntityManager

  @Test
  @Order(0)
  fun verifyRestoration() {
    println("Running ${javaClass.simpleName}#verifyRestoration")
    val count = (entityManager.createQuery("select count(*) from User").singleResult as Number).toLong()
    assertEquals(0, count)
  }

}
