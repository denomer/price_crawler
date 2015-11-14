package com.repup.pricecrawer.price.grabber;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.utility.RepupDateUtility;

public class GoibiboPriceGrabber {

	private Logger logger = Logger.getLogger(this.getClass());
	private String hotelUrl;
	private int totalAttempt = 0;
	
	public GoibiboPriceGrabber(String hotelUrl) {
		
		this.hotelUrl = hotelUrl;
	}
	

	public HotelPriceDetail getGoibiboPriceDetail()
	{
		
		HotelPriceDetail goibiboPriceDetail = new HotelPriceDetail();
		try
		{
			logger.debug("Crawling Goibibo Url: "+hotelUrl);
			Document hotelDoc = Jsoup.connect(hotelUrl).header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer("http://www.google.com").get();
			
			Elements element = hotelDoc.select("script");
			Element selectedElement = null;
		//	System.out.println("Total scripts : "+element.size());
			
			for(int counter=0;counter<element.size();counter++)
			{
				if(element.get(counter).html().toLowerCase().trim().contains("hotel_city_id"))
				{
					selectedElement = element.get(counter);
					//System.out.println("Breaking at : "+counter);
					break;
				}
					
			}
			//System.out.println(selectedElement.toString());
			if(selectedElement!=null)
			{
				//System.out.println("##########################################");
				//System.out.println(selectedElement.html());
				String[] scripts = selectedElement.toString().replaceAll("<script>", "").replaceAll("</script>", "").trim().split("\\r?\\n");
				
				//System.out.println("Total Break Length : "+scripts.length);
				String proto = "" ;
				String cycloneDomain = "" ;
				String cycloneFlavour = "";
				String hotelCityName = "";
				String hotel_id = "";
				String hotel_name = "";
				String hotel_city_id = "";
				
				for(int counter=0;counter<20;counter++)
				{
					if(scripts[counter].toLowerCase().trim().contains("proto".toLowerCase()))
					{
						proto = scripts[counter].trim().split("=")[1].trim().replaceAll("\"", "").replaceAll(";","").trim();
						
					}else if(scripts[counter].toLowerCase().trim().contains("cycloneDomain".toLowerCase()))
					{
						cycloneDomain = scripts[counter].trim().split("=")[1].trim().replaceAll("\"", "").replaceAll(";","").trim();
					}else if(scripts[counter].toLowerCase().trim().contains("cycloneFlavour".toLowerCase()))
					{
						cycloneFlavour =  scripts[counter].trim().split("=")[1].trim().replaceAll("\"", "").replaceAll(";","").trim();
					}else if(scripts[counter].toLowerCase().trim().contains("hotel_cityName".toLowerCase()))
					{
						hotelCityName =  scripts[counter].trim().split("=")[1].trim().replaceAll("\"", "").replaceAll(";","").trim();
					}else if(scripts[counter].toLowerCase().trim().contains("hotel_id".toLowerCase()))
					{
						hotel_id = scripts[counter].trim().split("=")[1].trim().replaceAll("\"", "").replaceAll(";","").trim();
					}else if(scripts[counter].toLowerCase().trim().contains("hotel_name".toLowerCase()))
					{
						hotel_name = scripts[counter].trim().split("=")[1].trim().replaceAll("\"", "").replaceAll(";","").trim();
					}else if(scripts[counter].toLowerCase().trim().contains("hotel_city_id".toLowerCase()))
					{
						hotel_city_id = scripts[counter].trim().split("=")[1].trim().replaceAll("\"", "").replaceAll(";","").trim();
					}
				}
				
				
				proto = "http:";
				logger.debug("Script Values: Proto: "+proto+" Cyclone Domain: "+cycloneDomain
						+" Cyclone Flavour: "+cycloneFlavour
						+" Hotel Id: "+hotel_id
						+" Hotel Name: "+hotel_name
						+" Hotel City Name : "+hotelCityName
						+" Hotel City id: "+hotel_city_id
						);
				String cyclneUrl = proto + "//"+cycloneDomain+"/rest/hoteldetails/?";
				String checkIn = RepupDateUtility.getGoibiboStartDate();
				String checkOut = RepupDateUtility.getGoibiboEndDate();
				String roomStr = "1-1_0";
				String queryStr = checkIn+"-"+checkOut+"-"+roomStr;
				String vendor = cycloneFlavour;
				String fwdp = "{}";
				String ad = "";
				
				if(vendor.trim().equals("na"))
				{
					ad = "[{\"vhid\":\""+hotel_id+"\",\"query\":\""+hotel_city_id+"-"+queryStr+"-foo\",\"fwdp\":"+fwdp+"}]";
				}else
				{
					ad = "[{\"vhid\":\""+hotel_id+"\",\"ibp\":\""+vendor+"\",\"query\":\""+hotel_city_id+"-"+queryStr+"-foo\",\"fwdp\":"+fwdp+"}]";
				}
				
				
				String contracttype = hotelDoc.select("input[name='contracttype']").val();
				//System.out.println(contracttype);
				
				
				HashMap<String, String> urlParams = new HashMap<String, String>();
				urlParams.put("userid", "asd");
				urlParams.put("hash", "2837423032023");
				urlParams.put("application", "web");
				urlParams.put("actionData", ad);
				urlParams.put("flavour", cycloneFlavour);
				urlParams.put("formatcode", "f2");
				urlParams.put("contracttype", contracttype);
				urlParams.put("cur", "INR");
				
				String urlParam = getPriceFetchUrl(urlParams)+"&callback=foo";
				
				
				String getUrl = cyclneUrl + urlParam;
				logger.debug("URL Encoded is: "+getUrl);
				
				Document jsonDoc = Jsoup.connect(getUrl).header("Accept-Encoding", "gzip, deflate, sdch")
						.header("Accept-Language", "en-US,en;q=0.8")
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
						.referrer(hotelUrl).get();
				
				String jsonResponse = jsonDoc.body().text().trim().replaceAll("foo\\(", "").trim().replaceAll("}\\)", "}");
				
				JSONObject jsonObj = new JSONObject(jsonResponse);
				JSONArray data = jsonObj.getJSONArray("data");
				
				if(data.length()>0)
				{
					JSONObject obj = (JSONObject)  data.get(0);
				
					int minimumPrice = obj.getInt("mp");
					goibiboPriceDetail.setHotelPriceDetailUrl(getUrl);
					goibiboPriceDetail.setHotelPriceUrl(hotelUrl);
					goibiboPriceDetail.setHotelPrice(String.valueOf(minimumPrice));
					logger.debug("Minimum Price : " + minimumPrice);		
				}else
				{
					goibiboPriceDetail.setHotelPriceDetailUrl(getUrl);
					goibiboPriceDetail.setHotelPriceUrl(hotelUrl);
					goibiboPriceDetail.setHotelPrice("sold out");
				}
				
				
				
			}else
			{
				goibiboPriceDetail.setHotelPrice("na");
				goibiboPriceDetail.setHotelPriceDetailUrl("false");
				goibiboPriceDetail.setHotelPriceUrl("false");
			}
			
			
		} catch (Exception ex) {
			
			logger.debug(
					"Exception occured at fetching prive at Gobibibo:", ex);
			if (totalAttempt < 3) {
				totalAttempt++;
				return getGoibiboPriceDetail();
			} else {
				
				goibiboPriceDetail.setHotelPrice("na");
				goibiboPriceDetail.setHotelPriceDetailUrl("false");
				goibiboPriceDetail.setHotelPriceUrl("false");
			}
		}
		
		return goibiboPriceDetail;
	}
	
	private String getPriceFetchUrl(HashMap<String, String> urlParams) {

		String url = "";
		
		try {
			boolean isFirst = true;

			for (Entry<String, String> urlParam : urlParams.entrySet()) {
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
		} catch (Exception ex) {
			
		}

		return url;
	}

}
