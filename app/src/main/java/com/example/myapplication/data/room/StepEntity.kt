package com.example.myapplication.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "StepEntity")

data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    val id: Int = 0,

    @SerialName("date")
    val date: String = "",

    @SerialName("steps")
    val steps: Long = 0,

    )