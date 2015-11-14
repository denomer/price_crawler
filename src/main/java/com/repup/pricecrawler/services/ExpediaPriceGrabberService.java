package com.repup.pricecrawler.services;

import org.apache.log4j.Logger;

import com.repup.pricecrawer.price.grabber.ExpediaPriceGrabber;
import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.controller.PriceGrabberController;

public class ExpediaPriceGrabberService  implements PriceGrabberController{

	private String hotelId;
	private String hotelUrl;
	private ExpediaPriceGrabber expediaPriceGrabber;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public ExpediaPriceGrabberService(String hotelId,String hotelUrl) {
		
		this.hotelId = hotelId;
		this.hotelUrl = hotelUrl;	
		this.expediaPriceGrabber = new ExpediaPriceGrabber(hotelUrl);
	
	}
	
	public HotelPriceDetail grabPriceDetials() {
		
		
		HotelPriceDetail  priceDetail = expediaPriceGrabber.getExpediaPriceDetail();
		
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
		String url = "https://www.expedia.co.in/Goa-Hotels-The-Leela-Goa.h438378.Hotel-Information";
		new ExpediaPriceGrabberService("1234", url).grabPriceDetials();
	}
	
}
