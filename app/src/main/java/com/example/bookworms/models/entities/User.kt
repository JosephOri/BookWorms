package com.example.bookworms.models.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "users")
data class User(
    @PrimaryKey
    var uid: String,
    var name: String,
    var email: String,
    var phone: String,

): Serializable

