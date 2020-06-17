package com.scratch.xray.stream

import brave.propagation.Propagation
import brave.propagation.aws.AWSPropagation
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureBefore
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.sleuth.zipkin2.ZipkinAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import zipkin2.Span
import zipkin2.reporter.Reporter
import zipkin2.reporter.xray_udp.XRayUDPReporter

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ZipkinAutoConfiguration::class)
@AutoConfigureAfter(name = ["org.springframework.cloud.autoconfigure.RefreshAutoConfiguration"])
class ZipkinConfig {

    @Bean(ZipkinAutoConfiguration.REPORTER_BEAN_NAME)
    fun zipkinXRayUDPReporter(): Reporter<Span> {
        return XRayUDPReporter.create()
    }

    @Bean
    fun awsPropagationFactory(): Propagation.Factory {
        return AWSPropagation.FACTORY
    }

}
