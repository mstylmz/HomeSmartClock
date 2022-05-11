package com.yilmaz.homesmartclock.constants

import org.eclipse.paho.client.mqttv3.MqttConnectOptions

object MqttConstants {
    const val broker = "tcp://erdemefe.keenetic.link"

    const val port = 9500

    const val name = "erdemefe"

    const val password = "19efe9028"

    const val clientId = "home_smart_clock"

    const val defaultQos = 0

    const val connectionTimeout = 15

    const val cleanSession = false

    const val inFlight = 60

    const val defaultKeepAlive = 10

    const val defaultSsl = false

    const val defaultRetained = false

    const val automaticReconnect = false

    const val version = MqttConnectOptions.MQTT_VERSION_3_1_1

    const val subscribeAliveTopic = "clock/14884307/alive"

    const val publishMessageTopic = "clock/14884307/message"

    const val topicTitle = "clock"

    const val publishMessage = "message"

    const val publishAlive = "alive"

}