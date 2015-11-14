package com.repup.pricecrawler.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.repup.pricecrawer.dao.DAOFactory;
import com.repup.pricecrawer.dao.manager.PriceDAOManager;
import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.PriceEntryService;


@Path("/price/{hotelid}/")
public class PriceEntryResources {

	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHotelPrice(
			@PathParam(value = "hotelid") String hotelid) {
		
		logger.debug("Paramenters Obtained: "+"HotelId: "+hotelid);
		
		PriceDAOManager reviewDAOManager = DAOFactory.getReviewDAOManager();
		
		PriceEntryService entryService = new PriceEntryService(hotelid,reviewDAOManager.getPriceDAO());
		
		ArrayList<HotelPriceDetail> priceList = entryService.getHotelPriceDetail();
		
		logger.debug(": Hotel Id :" + hotelid);

		GenericEntity<List<HotelPriceDetail>> entity = new GenericEntity<List<HotelPriceDetail>>(priceList) {};
		
		return Response
				.status(200)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers",
						"origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600")
				.entity(entity).build();
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response saveHotelPrice(
			@PathParam(value = "hotelid") String hotelid,@FormParam("price_url")String priceUrl,
			@FormParam("price")String price,@FormParam("currency")String currency,@FormParam("price_source")String price_source) {
		
		logger.debug("Paramenters Obtained: "+"HotelId: "+hotelid+" Price URL: "+priceUrl+" Price: "+price+" Price Currency: "+currency+" Price Source: "+price_source);
		
		HotelPriceDetail priceDetail = new HotelPriceDetail();
		
		priceDetail.setCreateTime(new Date());
		priceDetail.setHotelPrice(price);
		priceDetail.setHotelPriceCurrency(currency);
		priceDetail.setHotelPriceDetailUrl("na");
		priceDetail.setHotelPriceUrl(priceUrl);
		priceDetail.setHotelPriceSource(price_source);

		logger.debug("Price Deatail: "+priceDetail);
		
		PriceDAOManager reviewDAOManager = DAOFactory.getReviewDAOManager();
		
		PriceEntryService entryService = new PriceEntryService(hotelid,priceDetail,reviewDAOManager.getPriceDAO());
		
		boolean response = entryService.saveHotelPriceDetail();
		
		logger.debug(": Hotel Id :" + hotelid);

		GenericEntity<String> entity = new GenericEntity<String>(String.valueOf(response)) {};
		
		DAOFactory.closeDAOConnection();
		
		return Response
				.status(200)
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers",
						"origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600")
				.entity(entity).build();
	}
	

	


}
