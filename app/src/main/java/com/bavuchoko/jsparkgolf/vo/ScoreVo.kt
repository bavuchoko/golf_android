package com.bavuchoko.jsparkgolf.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ScoreVo(
    val gameId: Long,
    val playerId: Long,
    val turn: Int,
    val half: Int,
    val hole: Int,
    val hit: Int
): Parcelable
