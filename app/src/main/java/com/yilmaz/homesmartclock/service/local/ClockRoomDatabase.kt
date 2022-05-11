package com.yilmaz.homesmartclock.service.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yilmaz.homesmartclock.constants.CLOCKS_DATABASE_NAME
import com.yilmaz.homesmartclock.model.Clock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database( entities = [Clock::class], version = 1)
abstract class ClockRoomDatabase: RoomDatabase() {

    abstract fun clockDao(): ClockDao

    companion object {
        @Volatile
        private var INSTANCE: ClockRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ClockRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClockRoomDatabase::class.java,
                    "clock_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.clockDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(wordDao: ClockDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
        }
    }
}