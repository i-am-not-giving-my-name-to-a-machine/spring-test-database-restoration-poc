package com.acme.spring_test_database_restoration_poc

import com.zaxxer.hikari.HikariDataSource
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ApplicationContextAccessor {

  @Autowired
  private val applicationContext: ApplicationContext? = null

  @PostConstruct
  fun postConstruct() {
    instance = this
  }

  fun softEvictConnections() {
    val registry = applicationContext!!.autowireCapableBeanFactory as DefaultSingletonBeanRegistry
    val dataSource = registry.getSingleton("dataSource")
    (dataSource as HikariDataSource).hikariPoolMXBean.softEvictConnections()
  }

  companion object {
    var instance: ApplicationContextAccessor? = null
      private set
  }
}
