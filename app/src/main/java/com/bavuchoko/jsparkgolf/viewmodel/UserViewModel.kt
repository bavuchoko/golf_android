package com.bavuchoko.jsparkgolf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavuchoko.jsparkgolf.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _jwtToken = MutableLiveData<String?>()
    val jwtToken: LiveData<String?> get() = _jwtToken

    private val _userRegion = MutableLiveData<String?>()
    val userRegion: LiveData<String?> get() = _userRegion

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val (accessToken, region) = userRepository.login(username, password)
            _jwtToken.value = accessToken
            _userRegion.value = region
        }
    }
}
