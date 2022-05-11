package com.yilmaz.homesmartclock.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "clock_table")
data class Clock(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id") val id:Long?,
    @ColumnInfo(name = "name") val name:String?,
 ) : Parcelable