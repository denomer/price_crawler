package com.repup.pricecrawler.services;

import java.util.ArrayList;

import com.repup.pricecrawer.dao.PriceDAO;
import com.repup.pricecrawler.model.HotelPriceDetail;

public class PriceEntryService {

	private HotelPriceDetail hotelPriceDetail;
	private String hotelId;
	private PriceDAO priceDAO;


	public PriceEntryService(String hotelid,PriceDAO priceDAO) 
	{
		this.hotelId = hotelid;
		this.priceDAO = priceDAO;
	
	}

	public PriceEntryService(String hotelId, HotelPriceDetail hotelPriceDetail,
			PriceDAO priceDAO) {

		this.hotelId = hotelId;
		this.hotelPriceDetail = hotelPriceDetail;
		this.priceDAO = priceDAO;
	}

	public boolean saveHotelPriceDetail() {

		return priceDAO.saveHotelPrice(hotelPriceDetail, hotelId);

	}

	public ArrayList<HotelPriceDetail> getHotelPriceDetail() {

		return priceDAO.getHotelPrice(hotelId);
	}

}
