package com.yilmaz.homesmartclock.common

data class AckMessage(
    var status:Byte,        //1: başarılı 0: errorType alanına bak
    var errorType:Byte,
    var ackSource: Double,
    var ackMessageId:Int
)

data class Message(
    var messageType:Byte,
    var messageSubtype:Byte,
    var messageId:Byte,
    var message:String
)

enum class MessageType(i: Byte)
{
    privateMessage(0),
    brightness(1),
    
}

enum class AckErrorType(i: Byte) {
    SUCCESSFUL(0),
    OLD_DATA(1),
    UNKNOWN_DATA(2),
    WRONG_DATA(3)
}
