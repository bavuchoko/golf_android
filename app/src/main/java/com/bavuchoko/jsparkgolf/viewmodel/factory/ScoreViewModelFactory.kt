package com.bavuchoko.jsparkgolf.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bavuchoko.jsparkgolf.repository.GameRepository
import com.bavuchoko.jsparkgolf.repository.ScoreRepository
import com.bavuchoko.jsparkgolf.viewmodel.GameViewModel
import com.bavuchoko.jsparkgolf.viewmodel.ScoreViewModel
import com.bavuchoko.jsparkgolf.viewmodel.UserViewModel

class ScoreViewModelFactory(private val scoreRepository: ScoreRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoreViewModel(scoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}