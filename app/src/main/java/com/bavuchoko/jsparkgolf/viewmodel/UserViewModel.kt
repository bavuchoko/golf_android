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

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val token = userRepository.login(username, password)
            _jwtToken.value = token
        }
    }
}
