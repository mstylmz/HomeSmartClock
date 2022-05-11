package com.yilmaz.homesmartclock.enum

enum class AckErrorType(i: Int) {
    SUCCESSFUL(0),
    OLD_DATA(1),
    UNKNOWN_DATA(2),
    WRONG_DATA(3)
}