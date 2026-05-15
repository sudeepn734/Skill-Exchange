package com.example.skillexchangeapp.data.local.dao

import androidx.room.*
import com.example.skillexchangeapp.data.local.entity.Offer
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {
    @Insert
    suspend fun insertOffer(offer: Offer)

    @Update
    suspend fun updateOffer(offer: Offer)

    @Query("SELECT * FROM offers WHERE needPostId = :postId")
    fun getOffersForPost(postId: Long): Flow<List<Offer>>

    @Query("SELECT * FROM offers WHERE offeredByUserId = :userId")
    fun getOffersByUser(userId: Long): Flow<List<Offer>>
}
