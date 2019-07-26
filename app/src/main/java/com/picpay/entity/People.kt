package com.picpay.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Created By neomatrix on 2019-07-09
*/
@Entity(tableName = "people_tb")
data class People(
    @PrimaryKey
    @ColumnInfo(name = "people_id")
    var id: Long = -1L,
    var name: String = "",
    var img: String = "",
    var username: String = ""
)

