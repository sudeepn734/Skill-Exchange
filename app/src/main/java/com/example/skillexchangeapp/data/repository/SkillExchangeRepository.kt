package com.example.skillexchangeapp.data.repository

import com.example.skillexchangeapp.data.local.dao.*
import com.example.skillexchangeapp.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class SkillExchangeRepository(
    private val userDao: UserDao,
    private val skillDao: SkillDao,
    private val needPostDao: NeedPostDao,
    private val offerDao: OfferDao,
    private val swapDao: SwapDao,
    private val reviewDao: ReviewDao,
    private val messageDao: MessageDao
) {
    // User operations
    suspend fun registerUser(user: User): Long = userDao.insertUser(user)
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    fun getUserById(id: Long): Flow<User?> = userDao.getUserById(id)
    suspend fun updateUser(user: User) = userDao.updateUser(user)

    // Skill operations
    suspend fun addSkill(skill: Skill) = skillDao.insertSkill(skill)
    fun getSkillsForUser(userId: Long) = skillDao.getSkillsForUser(userId)

    // Need Post operations
    suspend fun postNeed(post: NeedPost) = needPostDao.insertNeedPost(post)
    fun getAllOpenNeeds(): Flow<List<NeedPost>> = needPostDao.getAllOpenNeeds()
    fun getNeedsByUser(userId: Long): Flow<List<NeedPost>> = needPostDao.getNeedsByUser(userId)
    suspend fun getNeedById(id: Long): NeedPost? = needPostDao.getNeedById(id)

    // Offer operations
    suspend fun submitOffer(offer: Offer) = offerDao.insertOffer(offer)
    fun getOffersForPost(postId: Long): Flow<List<Offer>> = offerDao.getOffersForPost(postId)
    suspend fun updateOffer(offer: Offer) = offerDao.updateOffer(offer)

    // Swap operations
    suspend fun createSwap(swap: Swap): Long = swapDao.insertSwap(swap)
    fun getSwapsForUser(userId: Long): Flow<List<Swap>> = swapDao.getSwapsForUser(userId)
    suspend fun updateSwap(swap: Swap) = swapDao.updateSwap(swap)
    suspend fun getSwapById(id: Long): Swap? = swapDao.getSwapById(id)

    // Review operations
    suspend fun submitReview(review: Review) = reviewDao.insertReview(review)

    // Message operations
    suspend fun sendMessage(message: Message) = messageDao.insertMessage(message)
    fun getMessages(userId: Long, otherId: Long) = messageDao.getMessagesBetweenUsers(userId, otherId)
}
