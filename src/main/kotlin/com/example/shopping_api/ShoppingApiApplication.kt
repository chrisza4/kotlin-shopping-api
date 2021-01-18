package com.example.shopping_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShoppingApiApplication

fun main(args: Array<String>) {
	runApplication<ShoppingApiApplication>(*args)
}