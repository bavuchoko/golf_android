package com.bavuchoko.jsparkgolf.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavuchoko.jsparkgolf.common.CommonMethod
import com.bavuchoko.jsparkgolf.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _jwtToken = MutableLiveData<String?>()
    val jwtToken: LiveData<String?> get() = _jwtToken

    fun login(username: String, password: String, context: Context) {
        viewModelScope.launch {
            val (accessToken, refreshToken) = userRepository.login(username, password)
            _jwtToken.value = accessToken
            refreshToken?.let {
                CommonMethod.saveValue(context, "refresh_token", it)
            }
        }
    }
}
