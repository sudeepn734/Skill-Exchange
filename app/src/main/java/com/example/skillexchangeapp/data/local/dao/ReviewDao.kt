package com.example.skillexchangeapp.data.local.dao

import androidx.room.*
import com.example.skillexchangeapp.data.local.entity.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert
    suspend fun insertReview(review: Review)

    @Query("SELECT * FROM reviews WHERE swapId = :swapId")
    fun getReviewsForSwap(swapId: Long): Flow<List<Review>>
}
