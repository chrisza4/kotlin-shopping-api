package com.example.shopping_api.domain.entities

object Fixtures {
    fun employeeChris() = User(username = "Chris", role = Role.Employee)
    fun employeeJames() = User(username = "James", role = Role.Employee)
    fun managerMark() = User(username = "Mark", role = Role.Manager)
}