package com.gobucket.httpfeeder.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.xml.transformer.UnmarshallingTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.gobucket.httpfeeder.models.Coupons;
import com.gobucket.httpfeeder.services.MerchantFeederServices;

@Configuration
public class HttpFeederConfig {

	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	MerchantFeederServices merchantFeederServices;
	
	@Bean(name = "http.feeder.request.channel")
	public DirectChannel httpFeederRequestChannel() {
		return new DirectChannel();
	}

	@Bean(name = "http.feeder.response.channel")
	public PollableChannel httpFeederResponseChannel() {
		return new QueueChannel();
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller objJaxb2Marshaller = new Jaxb2Marshaller();
		objJaxb2Marshaller.setPackagesToScan("com.gobucket.httpfeeder.models");
		return objJaxb2Marshaller;
	}

	@Bean
	public UnmarshallingTransformer unmarshallingTransformer() {
		UnmarshallingTransformer objUnmarshallingTransformer = new UnmarshallingTransformer(jaxb2Marshaller());
		return objUnmarshallingTransformer;
	}

	/*
	 * Below code can be enabled for doing poller based population of data
	@Bean
	public IntegrationFlow triggerRequestToHttpFeederGateway() {
		return IntegrationFlows
				.from(() -> new GenericMessage<>(""), configure -> configure.poller(Pollers.fixedDelay(100000)))
				.channel(httpFeederRequestChannel()).get();
	}
	*/
	@Bean
	public IntegrationFlow httpFeederGateway() {
		return IntegrationFlows.from(httpFeederRequestChannel())
				.handle(Http.outboundGateway("http://rss.coupons.com/xmlserve.asp?go=13903xh2010&bid=1492770001")
						.httpMethod(HttpMethod.GET).expectedResponseType(String.class))
				.transform(unmarshallingTransformer())
				.handle(httpFeederMessageHandler())
				.get();
	}

	@Bean
	public MessageHandler httpFeederMessageHandler() {
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				Coupons coupons = (Coupons) message.getPayload();
				merchantFeederServices.saveToDB(coupons);
				MessagingTemplate template = new MessagingTemplate();
				Message<String> msg = MessageBuilder.withPayload("success")
						.copyHeaders(message.getHeaders())
						.build();
				template.send(httpFeederResponseChannel(),msg);		
			}
		};
	}
}
