package com.repup.pricecrawler.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.PriceGrabberService;

@Path("/details")
public class PriceResources {

	private Logger logger = Logger.getLogger(this.getClass());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPriceResources(
			@QueryParam(value = "hotelid") String hotelid) {
		
		
		
		PriceGrabberService priceGrabberService = new PriceGrabberService(
				hotelid);
		logger.debug(": Hotel Id :" + hotelid);
		List<HotelPriceDetail> priceList = priceGrabberService.grabPriceDetails();
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

}
