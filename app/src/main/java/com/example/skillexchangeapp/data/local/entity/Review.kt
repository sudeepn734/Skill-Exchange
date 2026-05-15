package com.example.skillexchangeapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(entity = Swap::class, parentColumns = ["id"], childColumns = ["swapId"]),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["reviewerId"])
    ]
)
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val swapId: Long,
    val reviewerId: Long,
    val rating: Int,
    val comment: String
)
