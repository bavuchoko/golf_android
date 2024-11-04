package com.bavuchoko.jsparkgolf.vo

import android.util.Half

data class ProgressVo (
    val id: Long,
    val turn: Int,
    val half: Int,
    val hole: Int,
    val progressTime: String,
    val status: String
)