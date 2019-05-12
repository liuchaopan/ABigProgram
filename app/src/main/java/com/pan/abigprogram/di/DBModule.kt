package com.pan.abigprogram.di

import androidx.room.Room
import com.pan.abigprogram.PanApplication

import com.pan.abigprogram.db.UserDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

private const val DB_MODULE_TAG = "DBModule"

val dbModule = Kodein.Module(DB_MODULE_TAG) {

    bind<UserDatabase>() with singleton {
        Room.databaseBuilder(PanApplication.INSTANCE, UserDatabase::class.java, "user")
                .fallbackToDestructiveMigration()
                .build()
    }
}