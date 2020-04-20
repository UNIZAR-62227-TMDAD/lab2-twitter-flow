## Laboratorio 2 : Completar una integración sobre el API de Streaming de Twitter

La versión de partida de este repositorio ya es Streaming y abre un stream de tweets geolocalizados.

### Preparación

* Finalizar [la práctica 1](https://github.com/UNIZAR-62227-TMDAD/lab1-twitter-stream) (o estar en ello)
* Conocer [los parámetros](https://dev.twitter.com/streaming/overview/request-parameters) que se pueden utilizar en API de Twitter 
* Investigar el proyecto [Spring Integration](http://projects.spring.io/spring-integration/) version [4.3.14](https://docs.spring.io/spring-integration/docs/4.3.14.RELEASE/reference/html/).

### Objetivos

* Funcionamiento en el lado del servidor:
    * Utilizar Spring Integration para gestionar el stream de Tweets.
	   1. Crear tantos *TargetedTweet* (clase proporcionada) como tópicos activos con los que coincidan (preguntas enviadas por clientes)
       1. Modificar el texto de cada *TargetedTweet* para señalar las coincidencias en el texto 
       1. Enviar cada copia modificada de *TargetedTweet* a los tópicos que corresponda
* Funcionamiento en el lado del cliente: No se esperan cambios

