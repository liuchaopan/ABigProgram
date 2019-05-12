package com.pan.abigprogram.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pan.abigprogram.entity.ReceivedEvent

@Dao
interface UserReceivedEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(receivedEvents: List<ReceivedEvent>)

    @Query("SELECT * FROM user_received_events ORDER BY indexInResponse ASC")
    fun queryEvents(): DataSource.Factory<Int, ReceivedEvent>

    @Query("DELETE FROM user_received_events")
    fun clearReceivedEvents()

    @Query("SELECT MAX(indexInResponse) + 1 FROM user_received_events")
    fun getNextIndexInReceivedEvents(): Int
}