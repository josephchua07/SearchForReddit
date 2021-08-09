package com.chua.searchforreddit.domain

interface DomainMapper <DTO, DOMAIN> {
    fun toDomain(data: DTO) : DOMAIN

    fun toListOfDomain(dataList: List<DTO>) : List<DOMAIN>
}