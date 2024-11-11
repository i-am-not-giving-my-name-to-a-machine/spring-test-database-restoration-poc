package com.acme.spring_test_database_restoration_poc

import org.h2.store.fs.FilePath
import org.h2.util.IOUtils
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.sql.DriverManager
import kotlin.jvm.optionals.getOrNull

open class DatabaseRestorationExtension : BeforeEachCallback {

  override fun beforeEach(context: ExtensionContext) {
    val goldenCopy = FilePath.get("memFS:golden-copy.mv.db")
    val workingCopy = FilePath.get("memFS:working-copy.mv.db")

    if (context.testMethod.getOrNull()?.annotations?.any { it.annotationClass == ReuseDatabase::class } == true) {
      return
    }

    // Clear the connection pool and shutdown the database.
    if (workingCopy.exists()) {
      ApplicationContextAccessor.instance?.softEvictConnections()
      DriverManager.getConnection("jdbc:h2:memFS:working-copy", "sa", "sa").use { connection ->
        connection.createStatement().use { statement ->
          statement.execute("shutdown immediately")
        }
      }
    }

    // Replace the working copy with a new copy of the golden copy.
    IOUtils.copy(
      goldenCopy.newInputStream(),
      workingCopy.newOutputStream(false)
    )
  }

}
