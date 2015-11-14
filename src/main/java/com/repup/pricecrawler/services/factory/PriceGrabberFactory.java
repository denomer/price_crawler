package com.repup.pricecrawler.services.factory;

import com.repup.pricecrawler.services.BookingPriceGrabberService;
import com.repup.pricecrawler.services.ExpediaPriceGrabberService;
import com.repup.pricecrawler.services.GoibiboPriceGrabberService;
import com.repup.pricecrawler.services.MakeMyTripPriceGrabberService;
import com.repup.pricecrawler.services.controller.PriceGrabberController;
public class PriceGrabberFactory {

	public final static	String FLAG_BOOKING = "BOOKING";
	public final static	String FLAG_GOIBIBO = "GOIBIBO";
	public final static	String FLAG_MAKEMYTRIP = "MAKEMYTRIP";
	public final static	String FLAG_EXPEDIA = "EXPEDIA";
	
	
	public static PriceGrabberController getPriceGrabberService(String hotelId,String hotelUrl,String flag)
	{
		PriceGrabberController priceGrabber = null;
		
		if(flag.equals(FLAG_BOOKING))
		{
			priceGrabber = new BookingPriceGrabberService(hotelId, hotelUrl);
		}else if(flag.equals(FLAG_EXPEDIA))
		{
			priceGrabber = new ExpediaPriceGrabberService(hotelId, hotelUrl);
		}else if(flag.equals(FLAG_GOIBIBO))
		{
			priceGrabber = new GoibiboPriceGrabberService(hotelId, hotelUrl);
		}else if(flag.equals(FLAG_MAKEMYTRIP))
		{
			priceGrabber = new MakeMyTripPriceGrabberService(hotelId, hotelUrl);
		}
		
		return priceGrabber;
	}
	
}
