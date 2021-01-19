package com.example.shopping_api.domain.entities

import javax.persistence.*

enum class Role {
    Employee,
    Manager
}

@Entity
class User(
    @Id @GeneratedValue
    var id: Long? = null,
    var username: String,
    @Enumerated(EnumType.STRING)
    var role: Role
)