package com.bavuchoko.jsparkgolf.dto.request

import com.bavuchoko.jsparkgolf.vo.FieldsVo

data class QuickGameRequestDto(
    val fields: FieldsVo?,
    val sido: String?,
    val tempNames: Array<String>?

)
