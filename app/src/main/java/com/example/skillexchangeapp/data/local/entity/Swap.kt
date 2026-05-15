package com.example.skillexchangeapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "swaps",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userAId"]),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userBId"]),
        ForeignKey(entity = NeedPost::class, parentColumns = ["id"], childColumns = ["needPostId"])
    ]
)
data class Swap(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userAId: Long,
    val userBId: Long,
    val needPostId: Long,
    val agreedHours: Int,
    val status: String = "Ongoing", // Ongoing, Completed
    val completionDate: Long? = null
)
