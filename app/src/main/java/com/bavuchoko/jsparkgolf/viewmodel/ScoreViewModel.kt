package com.bavuchoko.jsparkgolf.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bavuchoko.jsparkgolf.dto.request.QuickGameRequestDto
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.repository.ScoreRepository
import com.bavuchoko.jsparkgolf.vo.GameVo
import com.bavuchoko.jsparkgolf.vo.ScoreVo
import kotlinx.coroutines.launch

class ScoreViewModel(private val scoreRepository: ScoreRepository): ViewModel() {
    private val _gameList = MutableLiveData<List<GameVo>>()

    val gameList: LiveData<List<GameVo>> get() = _gameList

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error


    fun saveScores(gameId: Long, scores: List<ScoreVo>) {
        viewModelScope.launch {
            try {
                val response = scoreRepository.saveScores(gameId, scores)
                // 점수 저장 성공 시 처리
            } catch (e: Exception) {
                // 에러 처리
                Log.e("ScoreViewModel", "Error saving scores", e)
            }
        }
    }

}