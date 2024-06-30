package com.example.bookworms.Model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey
    var id: String = "",
    var img: String = "",
    var title: String = "",
    var description: String = "",
    var uid: String = ""
) :Serializable {
    fun fromMap(map: Map<String?, Any?>) {
        img = map["image"].toString()
        title = map["title"].toString()
        description = map["description"].toString()
        uid = map["uid"].toString()
    }
}
