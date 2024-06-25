package com.example.bookworms.Model.ModelRoom


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookworms.BookWormsApp
import com.example.bookworms.Model.entities.User
import com.example.bookworms.Model.ModelRoom.dao.UserDao


@Database(entities = [User::class], version = 3, exportSchema = false)
abstract class AppLocalDB : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        // Define a singleton instance of the database
        @Volatile private var instance: AppLocalDB? = null
        private const val DB_NAME = "BOOKWORMS_DB"

        fun getInstance(): AppLocalDB {
            return instance?: synchronized(this) {
                instance?: Room.databaseBuilder(
                    BookWormsApp.getInstance().applicationContext,
                    AppLocalDB::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}