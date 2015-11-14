package com.repup.pricecrawler.services;

import org.apache.log4j.Logger;

import com.repup.pricecrawer.price.grabber.GoibiboPriceGrabber;
import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.controller.PriceGrabberController;

public class GoibiboPriceGrabberService  implements PriceGrabberController {

	private String hotelId;
	private String hotelUrl;
	private GoibiboPriceGrabber goibiboPriceGrabber;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public GoibiboPriceGrabberService(String hotelId,String hotelUrl) {
		
		this.hotelId = hotelId;
		this.hotelUrl = hotelUrl;	
		this.goibiboPriceGrabber = new GoibiboPriceGrabber(hotelUrl);
	}


	@Override
	public HotelPriceDetail grabPriceDetials() {
		
		
		HotelPriceDetail  priceDetail = goibiboPriceGrabber.getGoibiboPriceDetail();
		
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
		new GoibiboPriceGrabberService("1234", "http://www.goibibo.com/hotels/radisson-blu-marina-connaught-place-hotel-in-delhi-6614225257718109937/").grabPriceDetials();
	}
	
	
}
