package com.example.skeleton.Model.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
//    @Insert
//    suspend fun insertData()

//    @Update
//    suspend fun updateData()

//    @Delete
//    suspend fun deleteData()

    @Query("Select * from TableName")
    fun getData(): Flow<List<Int>>
}