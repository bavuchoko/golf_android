package com.bavuchoko.jsparkgolf.vo

import android.os.Parcelable
import android.util.Half
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProgressVo (
    val id: Long,
    val turn: Int,
    val half: Int,
    val hole: Int,
    val progressTime: String,
    val status: String
): Parcelable