package com.bavuchoko.jsparkgolf.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FieldsVo (
    val id : Int,
    val name: String,
    val address: String
): Parcelable