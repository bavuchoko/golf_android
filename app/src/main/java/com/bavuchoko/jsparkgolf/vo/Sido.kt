package com.bavuchoko.jsparkgolf.vo


enum class Sido (val regionName: String, val number: String) {

    SEOUL("서울특별시", "02"),
    GYEONGGI("경기도", "031"),
    INCHEON("인천광역시", "032"),
    GANGWON("강원도", "033"),
    CHUNGCHEONG_NAM("충청남도", "041"),
    DAEJEON("대전광역시", "042"),
    CHUNGCHEONG_BUK("충청북도", "043"),
    SEJONG("세종특별자치시", "044"),
    BUSAN("부산광역시", "051"),
    ULSAN("울산광역시", "052"),
    DAEGU("대구광역시", "053"),
    GYEONGSANG_BUK("경상북도", "054"),
    GYEONGSANG_NAM("경상남도", "055"),
    JEOLLA_NAM("전라남도", "061"),
    GWANGJU("광주광역시", "062"),
    JEOLLA_BUK("전라북도", "063"),
    JEJU("제주특별자치도", "064");

    companion object {
        fun getAllDisplayNames(): List<String> = values().map { it.regionName }

        fun fromRegionName(regionName: String): Sido? {
            return entries.find { it.regionName == regionName }
        }

        fun getNumberByName(regionName: String): String? {
            return fromRegionName(regionName)?.number
        }
    }
}
