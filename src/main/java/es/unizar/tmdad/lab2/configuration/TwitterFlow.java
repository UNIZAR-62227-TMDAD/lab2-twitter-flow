package es.unizar.tmdad.lab2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Tweet;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@ComponentScan
public class TwitterFlow {

	@Bean
	public DirectChannel requestChannel() {
		return new DirectChannel();
	}

	// Tercer paso
	// Los mensajes se leen de "requestChannel" y se envian al método "sendTweet" del
	// componente "streamSendingService"
	@Bean
	public IntegrationFlow sendTweet() {
        //
        // CAMBIOS A REALIZAR:
        //
        // Usando Spring Integration DSL
        //
        // Filter --> asegurarnos que el mensaje es un Tweet
        // Transform --> convertir un Tweet en un TargetedTweet con tantos tópicos como coincida
        // Split --> dividir un TargetedTweet con muchos tópicos en tantos TargetedTweet como tópicos haya
        // Transform --> señalar el contenido de un TargetedTweet
        //
		return IntegrationFlows.from(requestChannel()).
				log().
				handle("streamSendingService", "sendTweet").get();
	}

}

// Segundo paso
// Los mensajes recibidos por este @MessagingGateway se dejan en el canal "requestChannel"
@MessagingGateway(name = "integrationStreamListener")
interface MyStreamListener extends StreamListener {

	@Gateway(requestChannel = "requestChannel", headers = @GatewayHeader(name = "method", value="onTweet"))
	void onTweet(Tweet tweet);

	@Gateway(headers = @GatewayHeader(name = "method", value="onDelete"))
	void onDelete(StreamDeleteEvent deleteEvent);
}
