package com.romeo.eatmeapp.data.model

interface HasId<out ID> {
    val id: ID
}