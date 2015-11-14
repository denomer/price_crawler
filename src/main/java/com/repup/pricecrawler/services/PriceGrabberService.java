package com.repup.pricecrawler.services;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.repup.pricecrawler.model.HotelData;
import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.controller.PriceGrabberController;
import com.repup.pricecrawler.services.factory.PriceGrabberFactory;

public class PriceGrabberService {

	private String hotelId;
	private String hotelfetchurl = "http://52.4.240.156:8080/RepUpEngine/getHotelsToCrawl.repup";
	private String hotelFetchOTAUrls = "http://52.4.240.156:8080/RepUpEngine/getHotelOtaUrls.repup?hotelId="; 
	private Logger logger = Logger.getLogger(this.getClass());
	private HotelData hotelData;
	private ArrayList<HotelPriceDetail> priceListing;
	
	public PriceGrabberService(String hotelId)
	{
		this.hotelId = hotelId;
		hotelData = new HotelData();
		//this.hotelUrl = hotelUrl;
		hotelData.setHotedId(hotelId);
		priceListing = new ArrayList<HotelPriceDetail>();
	}
	
	public ArrayList<HotelPriceDetail> grabPriceDetails()
	{
	   boolean status = verifyHotelId();
	   logger.info("Hotel Validation status: "+status);
	   //status = false;
	   if(status)
	   {
		   logger.debug("Total Hotel OTA Urls: "+hotelData);
		   
		   //MakeMyTrip 
		   PriceGrabberController priceGrabber = null;
		   
		   if(!hotelData.getMakeMyTripUrl().trim().toLowerCase().equals("na"))
		   {
			   priceGrabber = PriceGrabberFactory.getPriceGrabberService(hotelId, hotelData.getMakeMyTripUrl(), PriceGrabberFactory.FLAG_MAKEMYTRIP);
			   HotelPriceDetail priceDetail = priceGrabber.grabPriceDetials();
			   priceDetail.setHotelPriceSource(PriceGrabberFactory.FLAG_MAKEMYTRIP);
//			   logger.info(" Make My Trip Price Details:"+priceDetail);
			   priceListing.add(priceDetail);
		   }else
		   {
			   logger.debug("Not Make My Trip OTA found for hotelid: "+hotelId);
		   }
		   
		   //GOIBIBO
		   if(!hotelData.getGoibiboUrl().trim().toLowerCase().equals("na"))
		   {
			   priceGrabber = PriceGrabberFactory.getPriceGrabberService(hotelId, hotelData.getGoibiboUrl(), PriceGrabberFactory.FLAG_GOIBIBO);
			   HotelPriceDetail goibiboPriceDetail = priceGrabber.grabPriceDetials();
			   goibiboPriceDetail.setHotelPriceSource(PriceGrabberFactory.FLAG_GOIBIBO);
//			   logger.info("Goibibo Price Details: "+goibiboPriceDetail);
			   priceListing.add(goibiboPriceDetail);
		   }else
		   {
			   logger.info(" Goibibo OTA Not available: "+hotelId);
		   }
		   
		   //Expedia
		   if(!hotelData.getExpediaUrl().trim().toLowerCase().equals("na"))
		   {
			   priceGrabber = PriceGrabberFactory.getPriceGrabberService(hotelId, hotelData.getExpediaUrl(), PriceGrabberFactory.FLAG_EXPEDIA);
			   HotelPriceDetail expediaPriceDetail = priceGrabber.grabPriceDetials();
			   expediaPriceDetail.setHotelPriceSource(PriceGrabberFactory.FLAG_EXPEDIA);
//			   logger.info("Expedia Price Details: "+expediaPriceDetail);
			   priceListing.add(expediaPriceDetail);
		   }else
		   {
			   logger.info("Expedia as OTA not available: "+hotelId);
		   }
		   
		   //Booking
		   if(!hotelData.getBookingUrl().trim().toLowerCase().equals("na"))
		   {
			   priceGrabber = PriceGrabberFactory.getPriceGrabberService(hotelId, hotelData.getBookingUrl(), PriceGrabberFactory.FLAG_BOOKING);
			   HotelPriceDetail bookingPriceDetail = priceGrabber.grabPriceDetials();
			   bookingPriceDetail.setHotelPriceSource(PriceGrabberFactory.FLAG_BOOKING);
			   //logger.info("Booking Price Details: "+bookingPriceDetail);
			   priceListing.add(bookingPriceDetail);
		   }else
		   {
			   logger.info("Booking as OTA not available: "+hotelId);
		   }
		   
	   }
	   
	   for(HotelPriceDetail hotelPriceDetail:priceListing)
	   {
		   System.out.println("Price Date: "+hotelPriceDetail.getHotelPrice()+" Hotel Type: "+hotelPriceDetail.getHotelPriceSource());
	   }
	   
	   return priceListing;
	  
	}

	private boolean verifyHotelId() {
		
		try
		{
			logger.info(" Fetch hotel information for id: "+hotelId);
			String hotelResponseJSON = IOUtils.toString(new URL(hotelfetchurl),
				Charset.forName("UTF-8"));
			
			JSONArray jsObj = new JSONArray(hotelResponseJSON);
			ArrayList<String> hotelList = new ArrayList<String>();
			if (jsObj.length() > 0) {

				for (int counter = 0; counter < jsObj.length(); counter++) {
					JSONObject jsonObject = jsObj.getJSONObject(counter);
					hotelList.add(jsonObject.getString("hotelId"));
				}
			}
			logger.info("Total valid hotel list: "+hotelList);
			if(hotelList.contains(hotelId))
			{
				
				return getHotelOTAsUrls(hotelData);
				
			}else
			{
				return false;
			}
		}catch(Exception ex)
		{
			logger.error(" Exception at validating hotels: ",ex);
			return false;
		}		
		
	}
	
	private boolean getHotelOTAsUrls(HotelData hotelData2) {
		
		try
		{
			logger.info(" Fetch hotel information ");
			String hotelResponseJSON = IOUtils.toString(new URL(hotelFetchOTAUrls+hotelData2.getHotedId()),
				Charset.forName("UTF-8"));
			
			JSONObject jsObj = new JSONObject(hotelResponseJSON);
			
			//BOOKING 

			String bookingUrl = jsObj.getString("booking").trim();
		//	logger.debug("Booking Url: "+bookingUrl);
			hotelData2.setBookingUrl(bookingUrl);
			
			//mmt			
			String mmtUrl = jsObj.getString("mmt").trim();
		//	logger.debug("Make My Trip Url: "+mmtUrl);
			hotelData2.setMakeMyTripUrl(mmtUrl);
			//expedia
			String expediaUrl = jsObj.getString("expedia").trim();
		//	logger.debug("Expedia Url: "+expediaUrl);
			hotelData2.setExpediaUrl(expediaUrl);
			
			//goibibo
			String goibiboUrl = jsObj.getString("goibibo").trim();
		//	logger.debug("Goibibo Url: "+goibiboUrl);
			hotelData2.setGoibiboUrl(goibiboUrl);
			
				
			return true;
		}catch(Exception ex)
		{
			logger.error(" Exception at get hotel ota urls: ",ex);
			return false;
		}		
	}

	public static void main(String[] args)
	{
		PriceGrabberService priceGrabberService = new PriceGrabberService("e4da9ee94f73b7f7014f73c9fc740001");
		priceGrabberService.grabPriceDetails();
		
	}
	
	
}
