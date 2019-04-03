package com.jmj.mypatients.infrastructure.delivery

import com.jmj.mypatients.model.actions.FindTreatments
import com.jmj.mypatients.model.actions.InitTreatmentAction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class TreatmentRoutesConfig {

    @Bean
    fun treatmentRoutes(treatmentController: TreatmentController) = router {
        (accept(MediaType.APPLICATION_JSON) and "/api/v1").nest {
            POST("/treatments") { request -> request.bodyToMono(NewTreatment::class.java).flatMap { ServerResponse.ok().body(fromObject(treatmentController.createTreatment(it))) } }
            GET("/treatments") { ServerResponse.ok().body(fromObject(treatmentController.getTreatments())) }
        }
    }

    @Bean
    fun treatmentController(initTreatmentAction: InitTreatmentAction, findTreatments: FindTreatments): TreatmentController = TreatmentController(initTreatmentAction, findTreatments)


}