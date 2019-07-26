package com.picpay.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/*
* Created By neomatrix on 2019-07-19
*/
interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(obj: T): Long

    /**
     * Insert an array of objects in the database.
     *
     * @param objs the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(objs: List<T>): List<Long>

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(obj: T): Int

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    fun delete(obj: T)
}