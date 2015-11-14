package com.repup.pricecrawer.price.grabber;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.utility.RepupDateUtility;

public class BookingPriceGrabber {

	private Logger logger = Logger.getLogger(this.getClass());
	private HashMap<String, String> paramMap ;
	private String URL_PREFIX = "http://www.booking.com";
	private String hotelUrl;
	private int totalAttempt = 0;
	
	public BookingPriceGrabber(String hotelUrl) {
	
		this.hotelUrl = hotelUrl;
		paramMap = new HashMap<String, String>();
	}

	public HotelPriceDetail getBookingPriceDetail() {
		
		HotelPriceDetail bookingPriceDetail = new HotelPriceDetail();
		try
		{
			logger.info("Fetching price data for booking: "+hotelUrl);
			
			Document hotelDoc = Jsoup.connect(hotelUrl).header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer("http://www.google.com").get();
			
			String checkin_monthday=RepupDateUtility.getBookingCheckInData(RepupDateUtility.FLAG_BOOKING_DAY);
			String checkin_year_month=RepupDateUtility.getBookingCheckInData(RepupDateUtility.FLAG_BOOKING_YEAR_MONTH);
			String checkout_monthday=RepupDateUtility.getBookingCheckOutData(RepupDateUtility.FLAG_BOOKING_DAY);
			String checkout_year_month=RepupDateUtility.getBookingCheckOutData(RepupDateUtility.FLAG_BOOKING_YEAR_MONTH);
			logger.info("Checkin Day: "+checkin_monthday+" Checkin year-month"+checkin_year_month);
			logger.info("Checkout Day: "+checkout_monthday+" Checkout year-month"+checkout_year_month);
			String tab = hotelDoc.select("input[name=tab]").get(0).val().trim();
			String origin = hotelDoc.select("input[name=origin]").get(0).val().trim();
			String error_url = hotelDoc.select("input[name=error_url]").get(0).val().trim();
			String do_availability_check = hotelDoc.select("input[name=do_availability_check]").get(0).val().trim();
			String aid = hotelDoc.select("input[name=aid]").get(0).val().trim();
			String dcid = hotelDoc.select("input[name=dcid]").get(0).val().trim();
			String label = hotelDoc.select("input[name=label]").get(0).val().trim();
			String sid = hotelDoc.select("input[name=sid]").get(0).val().trim();
			String no_redirect_check = hotelDoc.select("input[name=no_redirect_check]").get(0).val().trim();
			String dest_type = hotelDoc.select("input[name=dest_type]").get(0).val().trim();
			String dest_id = hotelDoc.select("input[name=dest_id]").get(0).val().trim();
			String highlighted_hotels = hotelDoc.select("input[name=highlighted_hotels]").get(0).val().trim();
			addParameter("checkin_monthday", checkin_monthday);
			addParameter("checkin_year_month", checkin_year_month);
			addParameter("checkout_monthday", checkout_monthday);
			addParameter("checkout_year_month", checkout_year_month);
			addParameter("tab", tab);
			addParameter("origin", origin);
			addParameter("error_url", error_url);
			addParameter("do_availability_check", do_availability_check);
			addParameter("aid", aid);
			addParameter("dcid", dcid);
			addParameter("label", label);
			addParameter("sid", sid);
			addParameter("no_redirect_check", no_redirect_check);
			addParameter("dest_type", dest_type);
			addParameter("dest_id", dest_id);
			addParameter("highlighted_hotels", highlighted_hotels);
			
			String GET_URL = hotelDoc.getElementById("hotelpage_availform").attr("action")+"?";
			
			String getUrl = URL_PREFIX+GET_URL+getParameters();
			
			String price = getPriceInformation(getUrl);
			if(price.equals(""))
			{
				bookingPriceDetail.setHotelPrice("na");
			}else
			{
				bookingPriceDetail.setHotelPrice(price);
			}
			
			bookingPriceDetail.setHotelPrice(price);
			bookingPriceDetail.setHotelPriceUrl(getUrl);
			bookingPriceDetail.setHotelPriceDetailUrl(getUrl);
			
			logger.debug("Price Information from Booking: "+price);
			
			return bookingPriceDetail;
		}catch(Exception ex)
		{
			logger.error("Exception occured at fetching pricing detail : ",ex);
//			ex.printStackTrace();
			if(totalAttempt<3)
			{
				totalAttempt++;
				return getBookingPriceDetail();
			}else
			{
				bookingPriceDetail.setHotelPrice("na");
				bookingPriceDetail.setHotelPriceUrl("false");
				bookingPriceDetail.setHotelPriceDetailUrl("false");
				return bookingPriceDetail;
			}
		}
	}
	
	
	int totalAttempPriceGrabber = 0 ;
	
	private String getPriceInformation(String getUrl)
	{
		try
		{
			Document hotelDoc = Jsoup.connect(getUrl).header("Accept-Encoding", "gzip, deflate, sdch")
				.header("Accept-Language", "en-US,en;q=0.8")
				.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
				.referrer(hotelUrl).get();
		    Element priceElement = hotelDoc.select(".room_details").get(0).select(".featuredRooms").get(0).select("tbody").get(0).select("tr").get(0).select(".roomPrice").get(0);
		    
		    String priceDoc = priceElement.text().trim().replaceAll("Rs\\.","").trim().replaceAll("," ,"").trim();
		 
		    return priceDoc;
		    
		}catch(Exception ex)
		{
			logger.debug("Exception at grabbing price for Booking",ex);
			if(totalAttempPriceGrabber<3)
			{
				totalAttempPriceGrabber++;
				return getPriceInformation(getUrl);
			}else
			{
				return "";
			}
				
			
		}
	}
	
	private void addParameter(String key,String value)
	{
		paramMap.put(key, value);
	}
	
	private String getParameters() {

		String url = "";
		
		try {
			boolean isFirst = true;

			for (Entry<String, String> urlParam : paramMap.entrySet()) {
				
				String key = urlParam.getKey();
				String value = urlParam.getValue();

				if (isFirst) {
					url = key + "=" + URLEncoder.encode(value, "ISO-8859-1");
					isFirst = false;
				} else {
					url += "&" + key + "="
							+ URLEncoder.encode(value, "ISO-8859-1");
				}

			}
		} catch (Exception ex) {}

		return url;
	}
	
	public static void main(String[] args)
	{
		new BookingPriceGrabber("http://www.booking.com/hotel/in/radisson-blu-dwarka-new-delhi.html").getBookingPriceDetail();
	}
}
