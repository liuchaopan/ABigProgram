package com.pan.abigprogram.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pan.abigprogram.entity.ReceivedEvent
import com.pan.abigprogram.entity.Repo

@Database(
        entities = [ReceivedEvent::class, Repo::class],
        version = 1
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userReceivedEventDao(): UserReceivedEventDao

    abstract fun userReposDao(): UserReposDao
}