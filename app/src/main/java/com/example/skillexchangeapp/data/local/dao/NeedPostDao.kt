package com.example.skillexchangeapp.data.local.dao

import androidx.room.*
import com.example.skillexchangeapp.data.local.entity.NeedPost
import kotlinx.coroutines.flow.Flow

@Dao
interface NeedPostDao {
    @Insert
    suspend fun insertNeedPost(post: NeedPost)

    @Update
    suspend fun updateNeedPost(post: NeedPost)

    @Query("SELECT * FROM need_posts WHERE status = 'Open' ORDER BY createdAt DESC")
    fun getAllOpenNeeds(): Flow<List<NeedPost>>

    @Query("SELECT * FROM need_posts WHERE userId = :userId")
    fun getNeedsByUser(userId: Long): Flow<List<NeedPost>>

    @Query("SELECT * FROM need_posts WHERE id = :id")
    suspend fun getNeedById(id: Long): NeedPost?
}
