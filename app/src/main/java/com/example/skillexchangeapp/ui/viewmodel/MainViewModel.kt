package com.example.skillexchangeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillexchangeapp.data.local.entity.NeedPost
import com.example.skillexchangeapp.data.local.entity.Offer
import com.example.skillexchangeapp.data.local.entity.User
import com.example.skillexchangeapp.data.repository.SkillExchangeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: SkillExchangeRepository) : ViewModel() {

    val allOpenNeeds: StateFlow<List<NeedPost>> = repository.getAllOpenNeeds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getUser(userId: Long) = repository.getUserById(userId)

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }

    fun postNeed(post: NeedPost) {
        viewModelScope.launch {
            repository.postNeed(post)
        }
    }

    fun submitOffer(offer: Offer) {
        viewModelScope.launch {
            repository.submitOffer(offer)
        }
    }

    fun getSwaps(userId: Long) = repository.getSwapsForUser(userId)
}
