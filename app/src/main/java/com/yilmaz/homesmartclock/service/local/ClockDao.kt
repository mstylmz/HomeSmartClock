package com.yilmaz.homesmartclock.service.local

import androidx.room.*
import com.yilmaz.homesmartclock.model.Clock
import kotlinx.coroutines.flow.Flow

@Dao
interface ClockDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(coin: Clock)

    @Delete
    suspend fun delete(coin: Clock)

    @Query("SELECT * FROM clock_table ORDER BY name ASC")
    fun getAllClocks(): Flow<List<Clock>>

    @Insert
    suspend fun insertAll(vararg coins: Clock): List<Long>

    @Query("SELECT * FROM clock_table WHERE id = :coin_id")
    suspend fun getCoins(coin_id:Int): Clock

    @Query("DELETE FROM clock_table")
    suspend fun deleteAll()

}