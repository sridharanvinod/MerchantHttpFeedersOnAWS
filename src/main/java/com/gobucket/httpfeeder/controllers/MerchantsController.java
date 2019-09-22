package com.gobucket.httpfeeder.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gobucket.dynamodb.repositories.models.coupon.Coupon;
import com.gobucket.httpfeeder.services.MerchantFeederServices;
import com.gobucket.models.HttpResponse;

@RequestMapping("/gobasket")
@RestController
public class MerchantsController {
	@Autowired MerchantFeederServices merchantFeederServices;
	@Autowired 
	@Qualifier("http.feeder.request.channel")
	MessageChannel httpFeedRequestChannel;
	
	@Autowired 
	@Qualifier("http.feeder.response.channel")
	PollableChannel httpFeedResponseChannel;
	
	@GetMapping(value="/feeds/{action}/merchant1", produces = "application/json")
	public @ResponseBody HttpResponse triggerHttpFeeder(@PathVariable String action) {
		HttpResponse objHttpResponse = null;
		if(StringUtils.hasText(action) && "get".equalsIgnoreCase(action)){
			List<Coupon> coupons = merchantFeederServices.getCoupons();
			objHttpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),"Action:["+action+"]", coupons);	
		}else if(StringUtils.hasText(action) && "count".equalsIgnoreCase(action)){
			objHttpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),"Action:["+action+"]", merchantFeederServices.getCouponsCount());	
		}else if(StringUtils.hasText(action) && "load".equalsIgnoreCase(action)){
			MessagingTemplate mt = new MessagingTemplate();
			mt.send(httpFeedRequestChannel, new GenericMessage<>("merchant1"));
			Message<?> message = mt.receive(httpFeedResponseChannel);
			objHttpResponse = new HttpResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(),"Action:["+action+"]:"+message.getHeaders().toString(), message.getPayload());
		}else if(StringUtils.hasText(action) && "delete".equalsIgnoreCase(action)){
			objHttpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),"Action:["+action+"]", merchantFeederServices.deleteCoupons());	
		}else objHttpResponse = new HttpResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),"Action:["+action+"]: NOT SUPPORTED", "");
		
		return objHttpResponse;
	}
	@GetMapping(value="/coupons/id/{couponId}")
	public @ResponseBody Coupon getCoupon(@PathVariable Long couponId) {
		Optional<Coupon> coupon = merchantFeederServices.getCoupon(couponId);
		if(coupon.isPresent()) {
			return coupon.get();
		}
		return null;
	}
	@GetMapping(value="/coupons/brand/{brand}")
	public @ResponseBody List<Coupon> getCoupon(@PathVariable String brand) {
		return merchantFeederServices.getCoupon(brand);
		
	}
}