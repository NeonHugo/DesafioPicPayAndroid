package com.picpay.repository

import com.picpay.dao.PeopleDao
import com.picpay.entity.People


/*
* Created By neomatrix on 2019-07-22
*/
class PeopleRepository(private val peopleDao: PeopleDao) {

    fun savePeople(creditCard: People): Boolean {
        return peopleDao.upsert(creditCard)
    }

    fun processPeopleList(peoples: ArrayList<People>) {
        peopleDao.upsert(peoples)
    }

    fun getPeopleById(id: Long): People? {
        return peopleDao.getPeopleById(id)
    }

    fun getPeopleList(): ArrayList<People> {
        return peopleDao.getAllPeoples() as ArrayList<People>
    }

    fun getPeopleCount(): Int {
        return peopleDao.getPeopleCount()
    }

    fun cleanPeoples() {
        peopleDao.clearAllData()
    }

}