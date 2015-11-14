package com.repup.pricecrawler.services;

import org.apache.log4j.Logger;

import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.controller.PriceGrabberController;
import com.repup.pricecrawer.price.grabber.*;
public class MakeMyTripPriceGrabberService implements PriceGrabberController {

	private String hotelId;
	private String hotelUrl;
	private MakeMyTripPriceGrabber makeMyTripUrlBuilder;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public MakeMyTripPriceGrabberService(String hotelId,String hotelUrl) {
		
		this.hotelId = hotelId;
		this.hotelUrl = hotelUrl;
		this.makeMyTripUrlBuilder = new MakeMyTripPriceGrabber(hotelUrl);
	}
	
	
	@Override
	public HotelPriceDetail grabPriceDetials() {
		
		HotelPriceDetail  priceDetail = makeMyTripUrlBuilder.getMakeMyTripHotelPriceURL();
		
		logger.info("Price URL :" + priceDetail);
		if(priceDetail.getHotelPriceUrl().equals("false"))
		{
			logger.info("Error occured in fetching price :");
		}else
		{
			logger.info("Price details: "+priceDetail);
		}
		
		return priceDetail;
		
	}
	
	public static void main(String[] args)
	{
		MakeMyTripPriceGrabberService makeMyTripUrlBuilder = new MakeMyTripPriceGrabberService("1234","http://www.makemytrip.com/hotels/radisson_blu_marina_hotel_connaught_place-details-delhi.html");
		System.out.println(makeMyTripUrlBuilder.grabPriceDetials());
	}
	
	
	
	
}
