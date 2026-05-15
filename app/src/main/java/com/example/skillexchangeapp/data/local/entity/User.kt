package com.example.skillexchangeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val village: String,
    val primarySkill: String,
    val secondarySkills: String,
    val experienceYears: Int,
    var trustScore: Float = 0f,
    var completedSwaps: Int = 0,
    val profileImageUri: String? = null,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)
