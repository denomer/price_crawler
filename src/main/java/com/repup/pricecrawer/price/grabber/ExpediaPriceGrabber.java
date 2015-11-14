package com.repup.pricecrawer.price.grabber;

import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.utility.RepupDateUtility;

public class ExpediaPriceGrabber {

	private Logger logger = Logger.getLogger(this.getClass());
	private String hotelUrl;
	private String URL_PREFIX1 = "?chkin=";
	private String URL_PREFIX2  = "&chkout=";
	private String URL_PREFIX3  = "&rm1=a2#rooms-and-rates";
	private int totalAttempt = 0;
	private String getUrl;
	
	public ExpediaPriceGrabber(String hotelUrl) {
	

		this.hotelUrl = hotelUrl;
	}
	
	public HotelPriceDetail getExpediaPriceDetail() {
		
		HotelPriceDetail hotelPriceDetail = new HotelPriceDetail();
		try
		{
			String checkInDate = RepupDateUtility.getExpediaStartDate();
			String checkOutDate = RepupDateUtility.getExpediaEndDate();
			
			getUrl = hotelUrl+URL_PREFIX1+URLEncoder.encode(checkInDate, "UTF-8")+URL_PREFIX2+URLEncoder.encode(checkOutDate,"UTF-8")+URL_PREFIX3;
			
			logger.debug("Expedia: Get Hotel Price Url: "+getUrl);
			
			Document hotelDoc = Jsoup.connect(getUrl).header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer("http://www.google.com").get();
			
			Element rooms_and_rates_segment = hotelDoc.select(".rooms-and-rates-segment").get(0);
			Element table = rooms_and_rates_segment.select("table").get(0);
			Element first_rate = table.select("[data-room-index=0]").get(0);
			Element rate_plan_first = first_rate.select(".rate-plan-first").get(0);
			Element avg_rate = rate_plan_first.select(".avg-rate").get(0);
			Element room_price_info_wrapper = avg_rate.select(".room-price-info-wrapper").get(0);
			Element one_night_room_price = room_price_info_wrapper.select(".one-night-room-price").get(0);
			
			hotelPriceDetail.setHotelPriceDetailUrl(getUrl);
			hotelPriceDetail.setHotelPriceUrl(getUrl);
			
			String one_night_price = one_night_room_price.text().replaceAll("Rs", "").trim().replaceAll(",","").split("\\.")[1];
			
			logger.debug("Price selected: "+one_night_price);
			
			hotelPriceDetail.setHotelPrice(one_night_price);
			return hotelPriceDetail;
			
		}catch(IndexOutOfBoundsException ex)
		{

			hotelPriceDetail.setHotelPriceDetailUrl(getUrl);
			hotelPriceDetail.setHotelPriceUrl(getUrl);
			hotelPriceDetail.setHotelPrice("sold out");
			return hotelPriceDetail;
		}
		catch(Exception ex)
		{
			logger.error("Exception at connecting with Expedia: "+hotelUrl,ex);
			if(totalAttempt<3)
			{
				
				totalAttempt++;
				return getExpediaPriceDetail();
			}else
			{
				hotelPriceDetail.setHotelPriceDetailUrl(getUrl);
				hotelPriceDetail.setHotelPriceUrl(getUrl);
				hotelPriceDetail.setHotelPrice("sold out");
				return hotelPriceDetail;
			}
		}
	
	}

	
	public static void main(String[] args)
	{
		String url = "http://www.booking.com/hotel/in/radisson-blu-dwarka-new-delhi.html";
		new ExpediaPriceGrabber(url).getExpediaPriceDetail();
	}
}
