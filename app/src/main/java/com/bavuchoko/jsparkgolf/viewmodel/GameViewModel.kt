package com.bavuchoko.jsparkgolf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavuchoko.jsparkgolf.dto.request.QuickGameRequestDto
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.vo.GameVo
import kotlinx.coroutines.launch

class GameViewModel(private val gameRepository: GameRepository): ViewModel() {
    private val _gameList = MutableLiveData<List<GameVo>>()
    private val _gameVo = MutableLiveData<GameVo>()
    val gameList: LiveData<List<GameVo>> get() = _gameList
    val gameView: LiveData<GameVo> get()= _gameVo
    var city: String? = null
    var status: String ="PLAYING"
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

    fun quickStartGame(request: QuickGameRequestDto) {
        viewModelScope.launch {
            val result = gameRepository.quickStartGame(request)
            if (result.isSuccess) {
                val gameVo = result.getOrNull()
                _gameVo.value = gameVo
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }


    fun getGameById(gameId: Long) {
        viewModelScope.launch {
            val response = gameRepository.getGameById(gameId)
            if (response.isSuccess) {
                val gameVo = response.getOrNull()
                _gameVo.value = gameVo
            } else {
                _error.value = response.exceptionOrNull()?.message
            }
        }
    }
}