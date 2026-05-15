package com.example.skillexchangeapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "need_posts",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class NeedPost(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val description: String,
    val skillRequired: String,
    val estimatedHours: Int,
    val urgencyLevel: String,
    val location: String,
    val status: String = "Open", // Open, In Progress, Closed
    val createdAt: Long = System.currentTimeMillis()
)
