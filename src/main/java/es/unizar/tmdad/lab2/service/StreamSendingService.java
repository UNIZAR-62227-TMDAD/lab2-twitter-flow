package es.unizar.tmdad.lab2.service;

import es.unizar.tmdad.lab2.domain.TargetedTweet;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
public class StreamSendingService {


	private final SimpMessageSendingOperations ops;

	private final TwitterTemplate twitterTemplate;

	private final TwitterLookupService lookupService;

	private Stream stream;

	private final StreamListener integrationStreamListener;

	public StreamSendingService(SimpMessageSendingOperations ops, TwitterTemplate twitterTemplate, TwitterLookupService lookupService, StreamListener integrationStreamListener) {
		this.ops = ops;
		this.twitterTemplate = twitterTemplate;
		this.lookupService = lookupService;
		this.integrationStreamListener = integrationStreamListener;
	}

	@PostConstruct
	public void initialize() {
		FilterStreamParameters parameters = new FilterStreamParameters();
		parameters.addLocation(-180, -90, 180, 90);

		// Primer paso
		// Registro un gateway para recibir los mensajes
		// Ver @MessagingGateway en MyStreamListener en TwitterFlow.java
		stream = twitterTemplate.streamingOperations().filter(parameters, Collections.singletonList(integrationStreamListener));
	}

	// Cuarto paso
	// Recibe un tweet y hay que enviarlo a tantos canales como preguntas hay registradas en lookupService
	//
	public void sendTweet(Tweet tweet) {
		Map<String, Object> headers = new HashMap<>();
		headers.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);

		// Expresión lambda: si el tweet contiene s, devuelve true
		Predicate<String> tweetContainsTopic = topic -> tweet.getText().contains(topic);

		// Expresión lambda: envía un tweet al canal asociado al tópico s
		Consumer<String> convertAndSendTweet = topic -> ops.convertAndSend("/queue/search/" + topic, tweet, headers);

		lookupService.getQueries().stream().filter(tweetContainsTopic).forEach(convertAndSendTweet);
	}

	public void sendTweet(TargetedTweet tweet) {
		//
		// CAMBIOS A REALIZAR:
		//
		// Crea un mensaje que envie un tweet a un único tópico destinatario
		//

	}


	public Stream getStream() {
		return stream;
	}

}
