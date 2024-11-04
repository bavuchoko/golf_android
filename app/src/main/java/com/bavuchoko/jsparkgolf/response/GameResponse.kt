package com.bavuchoko.jsparkgolf.response

import com.bavuchoko.jsparkgolf.vo.GameVo

data class GameResponse(
    val content: List<GameVo>,
    val pageable: Pageable,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean

)