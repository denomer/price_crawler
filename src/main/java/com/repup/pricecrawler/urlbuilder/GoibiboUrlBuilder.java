package com.repup.pricecrawler.urlbuilder;

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

public class GoibiboUrlBuilder {

	private Logger logger = Logger.getLogger(this.getClass());
	private String hotelUrl;
	
	public GoibiboUrlBuilder(String hotelUrl) {
	
		this.hotelUrl = hotelUrl;
	}
	
	
	public void testGoibiboPrice()
	{
		try
		{
			logger.debug("Crawling Goibibo Url: ");
			Document hotelDoc = Jsoup.connect(hotelUrl).header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer("http://www.google.com").get();
			
			Elements element = hotelDoc.select("script");
			Element selectedElement = null;
			System.out.println("Total scripts : "+element.size());
			
			for(int counter=0;counter<element.size();counter++)
			{
				if(element.get(counter).html().toLowerCase().trim().contains("hotel_city_id"))
				{
					selectedElement = element.get(counter);
					System.out.println("Breaking at : "+counter);
					break;
				}
					
			}
			//System.out.println(selectedElement.toString());
			if(selectedElement!=null)
			{
				//System.out.println("##########################################");
				//System.out.println(selectedElement.html());
				String[] scripts = selectedElement.toString().replaceAll("<script>", "").replaceAll("</script>", "").trim().split("\\r?\\n");
				
				System.out.println("Total Break Length : "+scripts.length);
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
				System.out.println("Script Values: Proto: "+proto+" Cyclone Domain: "+cycloneDomain
						+" Cyclone Flavour: "+cycloneFlavour
						+" Hotel Id: "+hotel_id
						+" Hotel Name: "+hotel_name
						+" Hotel City Name : "+hotelCityName
						+" Hotel City id: "+hotel_city_id
						);
				String cyclneUrl = proto + "//"+cycloneDomain+"/rest/hoteldetails/?";
				String checkIn = "20150915";
				String checkOut = "20150916";
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
				System.out.println(contracttype);
				
				
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
				System.out.println("URL Encoded is: "+getUrl);
				
				Document jsonDoc = Jsoup.connect(getUrl).header("Accept-Encoding", "gzip, deflate, sdch")
						.header("Accept-Language", "en-US,en;q=0.8")
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
						.referrer(hotelUrl).get();
				
				String jsonResponse = jsonDoc.body().text().trim().replaceAll("foo\\(", "").trim().replaceAll("}\\)", "}");
				
				JSONObject jsonObj = new JSONObject(jsonResponse);
				JSONArray data = jsonObj.getJSONArray("data");
				
				
				JSONObject obj = (JSONObject)  data.get(0);
				int minimumPrice = obj.getInt("mp");
				System.out.println("Minimum Price : " + minimumPrice);		
				
				
			}
			
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
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


	public void getGoibiboPriceUrl()
	{
		
		try
		{
			logger.debug("Crawling Goibibo Url: "+hotelUrl);
			Document hotelDoc = Jsoup.connect(hotelUrl).header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-US,en;q=0.8")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.referrer("http://www.google.com").get();
			
			String proto = "http://";
			String cycloneDomain = "cyclone.goibibo.com";
			String cyclneUrl = proto + "//"+cycloneDomain+"/rest/hoteldetails/";
			String checkIn = "20150915";
			String checkOut = "20150916";
			String roomStr = "1-1_0";
			String queryStr = checkIn+"-"+checkOut+"-"+roomStr;
			String vendor = "na";
			String city_id = "2820046943342890302";
			String fwdp = "{}";
			String hotelCode = "2354262762760247957";
			
			String ad = "[{\"vhid\":\""+hotelCode+"\",\"query\":\""+city_id+"-"+queryStr+"\"-foo\",\"fwdp\":"+fwdp+"}]";
			//String contractType = 
			
			
			
			
			
			
		}catch(Exception ex)
		{
			logger.debug("Exception at Fetching price details :",ex);
		}
		
		
		//var proto = http://
		//cycloneDomain = cyclone.goibibo.com
		//room 
		/*var self = this;
		 * 
        var cycloneUrl = proto + '//'+cycloneDomain+'/rest/hoteldetails/';
        var flavour = cycloneFlavour;
		var d1 = gi.hotels.checkin.format('yyyyMMdd');
		var d2 = gi.hotels.checkout.format('yyyyMMdd');
		var roomConf = gi.hotels.roomString;
		var _fwdp = (typeof gi.hotels.fwdp == 'string') ? JSON.parse(gi.hotels.fwdp) : gi.hotels.fwdp;
		var queryStr  = d1+'-'+d2+'-'+roomConf;
		this.controllerQueryStr = queryStr;
		gi.hotels.isCycloneUsed = true;

		//Set SessionId blank on iteneary changes for TBO/TRAM
		if(this.bookWidgetUpdate && !(_.isEmpty(_fwdp))){
			if(_fwdp){
				_fwdp['sessionId']="";
				gi.hotels.fwdp = JSON.stringify(_fwdp);
			}
		}
		if(gi.hotels.vendor == 'na'){
			var ad = '[{"vhid":"'+gi.hotels.hotelCode+'","query":"'+self.city_id+'-'+queryStr+'-foo","fwdp":'+gi.hotels.fwdp+'}]';
		}else{
			var ad = '[{"vhid":"'+gi.hotels.hotelCode+'","ibp":"'+gi.hotels.vendor+'","query":"'+self.city_id+'-'+queryStr+'-foo","fwdp":'+gi.hotels.fwdp+'}]';
		}
		var contracttype = document.querySelector("input[name='contracttype']").value;
        var param = {
            'userid': 'asd',
            'hash': '2837423032023',
            'application': 'web',
            'actionData': ad ,
            'flavour': flavour,
            'formatcode' :'f2',
            'contracttype' : contracttype,
            'cur':gi.hotels.cycloneCurrMap[gi.hotels.domainName]
        };
       if (this.cycloneAjax){this.cycloneAjax.abort();this.cycloneAjax = undefined;}
       this.cycloneAjax = $.ajax({
            url: cycloneUrl,
            type : "GET",
            data : param,
            dataType : "jsonp",
            jsonpCallback : "foo",
            beforeSend : function(){
	            	$('.room_update,.fetch_room').show();
				}
		});

       this.cycloneAjax.done(function(resp,status){
			$("#roomtype_holder .posRel").removeClass('dn');
            if ('data' in resp && resp.error.length == 0 && resp.data.length > 0){
                self.processRoomData(resp.data);
            }
           else
            {
                self.cycloneErrorHandling();
            }
		});
	   this.cycloneAjax.fail(function(err){
//		   logError();
		   self.cycloneErrorHandling();
		});*/
		
	}
	
	
	public static void main(String[] args)
	{
		new GoibiboUrlBuilder("http://www.goibibo.com/hotels/international-inn-hotel-in-delhi-2354262762760247957/").testGoibiboPrice();
	}
	
}
