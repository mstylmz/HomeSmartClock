package com.yilmaz.homesmartclock.data.remote

object Singleton {
    @Volatile
    var instance: Singleton? = null
        get() {
            if (field == null) {
                synchronized(Singleton::class.java) {
                    if (field == null) {
                        field = Singleton
                    }
                }
            }
            return field
        }
        private set
}