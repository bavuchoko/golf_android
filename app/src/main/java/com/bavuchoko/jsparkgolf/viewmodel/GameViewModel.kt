package com.bavuchoko.jsparkgolf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.vo.GameVo
import kotlinx.coroutines.launch

class GameViewModel(private val gameRepository: GameRepository): ViewModel() {
    private val _gameList = MutableLiveData<List<GameVo>>()
    val gameList: LiveData<List<GameVo>> get() = _gameList
    var city: String? = null
    var status: String ="OPEN"
    var player: Boolean =true
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun getList(page: Int, size: Int) {
        viewModelScope.launch {
            val result =  gameRepository.getList(page, size, status, player, city)
            if (result.isSuccess) {
                _gameList.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
}