package com.scratch.xray.stream

import mu.KotlinLogging
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Processor
import org.springframework.context.annotation.Configuration
import org.springframework.integration.support.MessageBuilder
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.SendTo

private val logger = KotlinLogging.logger {}

@Configuration
@EnableBinding(Processor::class)
class StreamConfig {

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    fun processor(myObj: MyObj) = myObj
        .also { logger.info { "received obj: $it" } }
        .let(MyObj::toMessage)

}

private fun MyObj.toMessage() = MessageBuilder.withPayload(this).setHeader(KafkaHeaders.MESSAGE_KEY, toMessageKey()).build()
private fun MyObj.toMessageKey() = id1.toByteArray()

data class MyObj(
    val id1: String,
    val id2: String
)
