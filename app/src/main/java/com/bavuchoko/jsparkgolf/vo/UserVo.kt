package com.bavuchoko.jsparkgolf.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserVo (
    val id: Long,
    val name: String,
    val birth: String,
    val portrait: String?,
    val gender: String
) : Parcelable