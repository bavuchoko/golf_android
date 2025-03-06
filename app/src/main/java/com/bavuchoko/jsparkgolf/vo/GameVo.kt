package com.bavuchoko.jsparkgolf.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameVo (
   val id: Int,
   val competition: CompetitionVo?,
   val host: UserVo,
   val fields: FieldsVo,
   val playDate: String,
   val finishDate: String?,
   val progress: ProgressVo,
   val players: List<UserVo>
): Parcelable