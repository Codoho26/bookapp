package com.fkh.bookapp.infrastructure.adapters.input.graphql

import com.fkh.bookapp.infrastructure.Configs
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@TestConfiguration
class GraphqlTestConfig {

    @Bean
    fun testRuntimeWiringConfigurer(): RuntimeWiringConfigurer = Configs().runtimeWiringConfigurer()

}