package com.yilmaz.homesmartclock.enum

enum class MqttConnectionState(val id:Byte) {
    /** Client is Connecting **/
    CONNECTING(0),
    /** Client is Connected **/
    CONNECTED(1),
    /** Client is Disconnecting **/
    DISCONNECTING(2),
    /** Client is Disconnected **/
    DISCONNECTED(3),
    /** Client has encountered an Error **/
    ERROR(4),
    /** Status is unknown **/
    NONE(5);
    fun id():Byte{
        return id
    }
}