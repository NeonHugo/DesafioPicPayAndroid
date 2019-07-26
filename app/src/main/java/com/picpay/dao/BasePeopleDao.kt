package com.picpay.dao

import com.picpay.base.BaseDao
import com.picpay.entity.People

/*
* Created By neomatrix on 2019-07-12
*/
interface BasePeopleDao : BaseDao<People> {
    fun upsert(people: People): Boolean

    fun upsert(peoples: List<People>)

    fun getPeopleById(id: Long): People?

    fun getAllPeoples(): List<People>

    fun getPeopleCount(): Int

    fun clearAllData()
}