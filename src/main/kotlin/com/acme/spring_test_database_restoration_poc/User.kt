package com.acme.spring_test_database_restoration_poc

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_")
class User(
  @Id
  @Column(name = "id_")
  val id: String,
  @Column(name = "name_")
  val name: String,
  @Column(name = "email")
  val email: String,
)
