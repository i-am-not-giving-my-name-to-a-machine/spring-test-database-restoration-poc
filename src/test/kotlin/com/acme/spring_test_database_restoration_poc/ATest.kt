package com.acme.spring_test_database_restoration_poc

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@ExtendWith(DatabaseRestorationExtension::class)
@SpringBootTest(classes = [Application::class])
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ATest {

  @Autowired
  private lateinit var entityManager: EntityManager

  @Test
  @Order(0)
  @Transactional
  @Commit
  fun polluteDatabase() {
    println("Running ${javaClass.simpleName}#polluteDatabase")
    for (i in 1..1000) {
      val s = i.toString()
      entityManager.persist(User(s, s, s))
    }
  }

  @Test
  @Order(1)
  @Transactional
  @ReuseDatabase // Reuses the database from the previous test. Usually not a good idea, but sometimes it is needed or desired.
  fun verifyPollution() {
    println("Running ${javaClass.simpleName}#verifyPollution")
    val count = (entityManager.createQuery("select count(*) from User").singleResult as Number).toLong()
    assertEquals(1000, count)
  }

  @Test
  @Order(2)
  @Transactional
  @Commit
//  @DirtiesContext // Not needed anymore, but can still be used, because the golden copy of the database is always used.
  fun verifyRestorationAndPolluteDatabaseAgain() {
    println("Running ${javaClass.simpleName}#verifyRestorationAndPolluteDatabaseAgain")
    val count = (entityManager.createQuery("select count(*) from User").singleResult as Number).toLong()
    assertEquals(0, count)
    for (i in 1..1000) {
      val s = i.toString()
      entityManager.persist(User(s, s, s))
    }
  }

  @Test
  @Order(3)
  fun verifyRestoration() {
    println("Running ${javaClass.simpleName}#verifyRestoration")
    val count = (entityManager.createQuery("select count(*) from User").singleResult as Number).toLong()
    assertEquals(0, count)
  }

  @Test
  @Order(4)
  @Transactional
  @Commit
  fun polluteDatabaseYetAgain() {
    println("Running ${javaClass.simpleName}#polluteDatabaseYetAgain")
    for (i in 1..1000) {
      val s = i.toString()
      entityManager.persist(User(s, s, s))
    }
  }

}
