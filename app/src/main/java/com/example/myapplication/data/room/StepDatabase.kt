package com.example.myapplication.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StepEntity::class],
    version = 1
)
abstract class StepDatabase: RoomDatabase() {
    abstract val dao: StepDao
}