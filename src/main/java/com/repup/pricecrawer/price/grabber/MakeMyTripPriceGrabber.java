package com.repup.pricecrawer.price.grabber;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.utility.RepupDateUtility;

public class MakeMyTripPriceGrabber {

	private String hotelUrl = "";
	private Logger logger = Logger.getLogger(this.getClass());
	//SAMPLE DATE FORMAT  09302015 
	private int totalAttempt = 0;
	private String additionalParameter = "&ajaxCall=T&i=null&codd=snew";
	
	public MakeMyTripPriceGrabber(String hotelUrl) {
	
		this.hotelUrl = hotelUrl;
	}
	
	
	public HotelPriceDetail getMakeMyTripHotelPriceURL()
	{
		HotelPriceDetail mmtPriceDetail = new HotelPriceDetail();
		try
		{
			cookies = new HashMap<String, String>();
			
			logger.info("Fetching price information MMT: "+hotelUrl);
			
			Connection connetion1 =   Jsoup.connect(hotelUrl).header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer("http://www.google.com");
			Response response1 = connetion1.execute();
			
			cookies.putAll(response1.cookies());
			
			
			Document hotelDoc = response1.parse();
			
			
			try
			{
				String city_code = hotelDoc.getElementById("search_city_code").val().trim();
				String country_code = hotelDoc.getElementById("search_country_code").val().trim();
				
				
				//String check_in_date = hotelDoc.getElementById("check_in_date").val().trim();
				String check_in_date =  RepupDateUtility.getMMTStartDate();
				//String check_out_date = hotelDoc.getElementById("check_out_date").val().trim();
				String check_out_date = RepupDateUtility.getMMTEndDate();
				logger.debug("Check in date: "+check_in_date+" Check out date: "+check_out_date);
				
				String search_text = URLEncoder.encode(hotelDoc.getElementById("search_text").val().trim(), "UTF-8");
				String search_type = hotelDoc.getElementById("search_type").val().trim();
				String search_htl_code = hotelDoc.getElementById("search_htl_code").val().trim();
				String search_data = hotelDoc.getElementById("search_data").val().trim();
				String autocomplete_data = hotelDoc.getElementById("city_name_autocomplete").val().trim();
				String error_code = "N";
					
				if (!search_data.equals(autocomplete_data) ||autocomplete_data.equals("Region, City, Area or Hotel Name (Worldwide)") || autocomplete_data.trim().equals("")) {
			        logger.info("lease tell us where you want to go >> Information missing");
			        error_code = "Y";
			    }
			    // Check if days are more than 10
			   /* if (Integer.parseInt(hotelDoc.getElementById("night_count").text().trim()) >= 90) {
			      
			    	logger.info("Your search exceeds 90 nights. Please reduce the duration or call us at 1800-102-8747 to make this booking");
			      
			        error_code = "Y";
			    }
			    else if (Integer.parseInt(hotelDoc.getElementById("night_count").text().trim()) > 10) {
			    	
			    	logger.info("User searching for rate of 10");
					  
			    	error_code = "Y";
			        
			    }*/
			    // Submission Part
			    if (error_code.equals("N")) {
			        String roomStayQualifier = "";
			        String[] age_only;
			        for (int i = 1; i <= Integer.parseInt(hotelDoc.getElementById("room_count").val().trim()); i++) {
			            roomStayQualifier += hotelDoc.getElementById("room_" + i + "_adult").text().trim() + "e";
			            roomStayQualifier += hotelDoc.getElementById("room_" + i + "_child").text().trim() + "e";
			            for (int j = 1; j <= Integer.parseInt(hotelDoc.getElementById("room_" + i + "_child").text().trim()); j++) {
			                age_only = (hotelDoc.getElementById("room_" + i + "_age_child_" + j + "_value").text()).split(" ");
			                roomStayQualifier += age_only[0] + "e";
			            }
			        }
			        String funnel_url = "";
			        String ajaxPriceUrl = "";
			       
			        
			        String base_funnel_url = "http://hotelz.makemytrip.com/makemytrip/site/hotels/detail";
			        String parameters = "";
			        if (search_type.equals("CTY")) {
			        	
			        	 parameters = "?roomStayQualifier=" + roomStayQualifier + "&city=" + city_code + "&country=" + country_code + "&checkin=" + check_in_date + "&checkout=" + check_out_date + "&searchText=" + search_text + "&hotelId=&area=";
			             funnel_url = base_funnel_url + parameters;
			            
				           
			        } else if (search_type.equals("AREA")) {
			            
			        	 parameters = "?roomStayQualifier=" + roomStayQualifier + "&city=" + city_code + "&country=" + country_code + "&checkin=" + check_in_date + "&checkout=" + check_out_date + "&searchText=" + search_text + "&hotelId=&area=" + search_text;
			        	funnel_url = base_funnel_url+parameters;
			        	
			        } else {
			        	 parameters = "?roomStayQualifier=" + roomStayQualifier + "&city=" + city_code + "&country=" + country_code + "&checkin=" + check_in_date + "&checkout=" + check_out_date + "&searchText=" + search_text + "&hotelId=" + search_htl_code + "&area=";
			            funnel_url = base_funnel_url+parameters;
			        }
			        funnel_url = funnel_url + "&codd=snew";
			        
			        getAjaxPriceUrl(parameters,funnel_url,mmtPriceDetail);
			       
			        mmtPriceDetail.setHotelPriceUrl(funnel_url);
			       
			        return mmtPriceDetail;
			        
			    }else
			    {
			    	mmtPriceDetail.setHotelPriceDetailUrl("false");
			    	mmtPriceDetail.setHotelPriceUrl("false");
			    	return mmtPriceDetail;
			    	
			    }
				
			}catch(Exception ex)
			{
				logger.error("Error in fetching hotel information: ",ex);
				mmtPriceDetail.setHotelPriceDetailUrl("false");
		    	mmtPriceDetail.setHotelPriceUrl("false");
				return mmtPriceDetail;
				
			}
			
		}catch(Exception ex)
		{
			logger.error("Error in connecting with url: ",ex);
			if(totalAttempt<3)
			{
				totalAttempt++;
				return getMakeMyTripHotelPriceURL();
			}else
			{
				mmtPriceDetail.setHotelPriceDetailUrl("false");
		    	mmtPriceDetail.setHotelPriceUrl("false");
				return mmtPriceDetail;
			}
			
		}
		
		
	}

	int totalAttempForAjaxPrice = 0;
	private Map<String, String> cookies;
	
	private void getAjaxPriceUrl(String parameters,String viewerUrl,HotelPriceDetail priceDetail) {
		
		try
		{
			logger.debug("Creating json based URL's : " + viewerUrl);
			
			Connection connection2 = Jsoup.connect(viewerUrl)
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer(hotelUrl);
			for (Entry<String, String> cookie : cookies.entrySet()) {
				connection2.cookie(cookie.getKey(), cookie.getValue());
			}
			Response response2 = connection2.execute();
			
			cookies.putAll(response2.cookies());
			Document reviewDoc = response2.parse();
			
			String session_cid = reviewDoc.getElementById("session_cid").val().trim();
			String base_ajax_price_url = "http://hotelz.makemytrip.com/makemytrip/site/hotels/detailJsonData";
			String ajaxPriceUrl = base_ajax_price_url + parameters + additionalParameter+"&session_cId="+session_cid;
			
			logger.debug("Ajax based url for fetching data MMT : "+ajaxPriceUrl);
			
			Connection connection3 = Jsoup.connect(ajaxPriceUrl).ignoreContentType(true).followRedirects(false)
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer(viewerUrl);
			
			for (Entry<String, String> cookie : cookies.entrySet()) {
				connection3.cookie(cookie.getKey(), cookie.getValue());
			}
			
			cookies.putAll(response2.cookies());
			Response response3 = connection3.execute();
			Document jsonResponse = response3.parse();
			logger.info("Obtained Json Response: "+jsonResponse.text());
			
			JSONObject arr=new JSONObject(jsonResponse.body().text());
		
			///System.out.println(arr.toString());
			JSONObject searchResponseDTO = arr.getJSONObject("searchResponseDTO");
			JSONArray hotelsList = searchResponseDTO.getJSONArray("hotelsList");
			JSONObject hotelList1 = (JSONObject) hotelsList.get(0);
			JSONObject displayFare = hotelList1.getJSONObject("displayFare");
			JSONObject actualPrice = displayFare.getJSONObject("slashedPrice");
			String value = actualPrice.getString("value");
			//System.out.println(value);
			
			priceDetail.setHotelPrice(value);
			
			priceDetail.setHotelPriceDetailUrl(ajaxPriceUrl);
			
		}catch(Exception ex)
		{
			logger.error("Connecting to MakeMyTrip Url: ",ex);
			if(totalAttempForAjaxPrice<3)
			{

				totalAttempForAjaxPrice++;
				getAjaxPriceUrl(parameters, viewerUrl, priceDetail);
				
			}else
			{
				priceDetail.setHotelPrice("NA");
				
				priceDetail.setHotelPriceDetailUrl("false");
				
			}
			
		}
		 
	}
}
