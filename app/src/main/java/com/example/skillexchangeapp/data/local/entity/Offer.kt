package com.example.skillexchangeapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "offers",
    foreignKeys = [
        ForeignKey(
            entity = NeedPost::class,
            parentColumns = ["id"],
            childColumns = ["needPostId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["offeredByUserId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Offer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val needPostId: Long,
    val offeredByUserId: Long,
    val offeredSkill: String,
    val offeredHours: Int,
    val message: String,
    val status: String = "Pending" // Pending, Accepted, Rejected
)
