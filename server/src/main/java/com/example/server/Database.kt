package com.example.mall_navigation

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object Shops : Table() {
    val id = text("id")
    val name = text("name")
    val floor = integer("floor")
    val x = double("x")
    val y = double("y")
    val category = text("category")
    override val primaryKey = PrimaryKey(id)
}

fun initDatabase() {
    Database.connect("jdbc:sqlite:mall.db", driver = "org.sqlite.JDBC")
    transaction {
        SchemaUtils.create(Shops)
    }
}