package com.repup.pricecrawler.services;

import org.apache.log4j.Logger;
import com.repup.pricecrawer.price.grabber.*;
import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.controller.PriceGrabberController;
public class BookingPriceGrabberService  implements PriceGrabberController {

	private String hotelId;
	private String hotelUrl;
	private BookingPriceGrabber bookingPriceGrabber;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public BookingPriceGrabberService(String hotelId,String hotelUrl)
	{
		this.hotelId = hotelId;
		this.hotelUrl = hotelUrl;	
		this.bookingPriceGrabber = new BookingPriceGrabber(hotelUrl);
	}
	
	public HotelPriceDetail grabPriceDetials() {
		
		
		HotelPriceDetail  priceDetail = bookingPriceGrabber.getBookingPriceDetail();
		
		logger.info("Price URL :" + priceDetail);
		if(priceDetail.getHotelPriceDetailUrl().equals("false"))
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
		String url = "http://www.booking.com/hotel/in/radisson-blu-dwarka-new-delhi.html";
		new BookingPriceGrabberService("1234", url).grabPriceDetials();
	}
}
