package com.repup.pricecrawer.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.repup.pricecrawler.model.HotelPriceDetail;
import com.repup.pricecrawler.services.factory.PriceGrabberFactory;

public class PriceDAO implements PRICE_DAO_CONS{
	
	private MongoClient mongoConnection;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public PriceDAO(MongoClient mongoConnection) {
		
		this.mongoConnection = mongoConnection;
		
	}
	private final String FLAG_BOOKING = "booking";
	private final String FLAG_GOIBIBO = "goibibo";
	private final String FLAG_EXPEDIA = "expedia";
	private final String FLAG_MMT = "mmt";
	
	public ArrayList<HotelPriceDetail> getHotelPrice(String hotelId)
	{
		
		ArrayList<HotelPriceDetail> priceList = new ArrayList<HotelPriceDetail>();
		
		HotelPriceDetail goibiboPriceDetail = createHotelDetail(hotelId,FLAG_GOIBIBO);
		if(goibiboPriceDetail!=null)
		{
			priceList.add(goibiboPriceDetail);
		}
		HotelPriceDetail bookingPriceDetail = createHotelDetail(hotelId,FLAG_BOOKING);
		if(bookingPriceDetail!=null)
		{
			priceList.add(bookingPriceDetail);
		}
		HotelPriceDetail expediaHotelDetail = createHotelDetail(hotelId,FLAG_EXPEDIA);
		if(expediaHotelDetail!=null)
		{
			priceList.add(expediaHotelDetail);
		}
		HotelPriceDetail mmtHotelDetail = createHotelDetail(hotelId,FLAG_MMT);
		if(expediaHotelDetail!=null)
		{
			priceList.add(mmtHotelDetail);
		}
		
		return priceList;

	}


	private HotelPriceDetail createHotelDetail(String hotelId,String flag) {
		HotelPriceDetail hotelPriceDetail = new HotelPriceDetail();
		DBCursor dbCursor = null;
		
		try {
			// System.out.println("Fetching Values from database:");
			logger.debug("Fetching Values from database");
			DB db = mongoConnection.getDB(DB_HOTEL_PRICE);
			DBCollection dbCollection = db.getCollection(hotelId + "_"
					+ COLLECTION_PRICE_PREFIX+"_"+flag);

			DBObject dbObject = new BasicDBObject();
			dbObject.put(hotelPriceDetail.getCOLUMN_NAME_PRICE_CREATE_TIME(), -1);
			dbCursor = dbCollection.find().sort(dbObject).limit(1);
			
			while (dbCursor.hasNext()) {
				// System.out.println(dbCursor.next());
				BasicDBObject reviewObject = (BasicDBObject) dbCursor.next();
				
				hotelPriceDetail.setCreateTime(reviewObject.getDate(hotelPriceDetail.getCOLUMN_NAME_PRICE_CREATE_TIME()));
				hotelPriceDetail.setHotelPrice(reviewObject.getString(hotelPriceDetail.getCOLUMN_NAME_PRICE()));
				hotelPriceDetail.setHotelPriceCurrency(reviewObject.getString(hotelPriceDetail.getCOLUMN_NAME_PRICE_CURRENCY()));
				hotelPriceDetail.setHotelPriceDetailUrl(reviewObject.getString(hotelPriceDetail.getCOLUMN_NAME_PRICE_DETAIL_URL()));
				hotelPriceDetail.setHotelPriceSource(reviewObject.getString(hotelPriceDetail.getCOLUMN_NAME_PRICE_SOURCE()));
				hotelPriceDetail.setHotelPriceUrl(reviewObject.getString(hotelPriceDetail.getCOLUMN_NAME_PRICE_URL_VIEWER()));
				
			}
			// System.out.println("Reviews Returned from database:"+reviews.size());
			logger.debug("Hotel Price Detail obtained from database :"
					+ hotelPriceDetail);
			return hotelPriceDetail;

		} catch (Exception ex) {
			// System.out.println("Exception Occurred:"+ex.getMessage());
			logger.error("Exception Occurred on obtained Price Deatail from database:"
					+ ex.getMessage(), ex);
			return null;
		} finally {

			if (dbCursor != null) {
				dbCursor.close();
			}

		}
	}
	
	
	public boolean saveHotelPrice(HotelPriceDetail hotelPriceDetial,
			String hotelid) {

		try {
			// System.out.println("Saving Reviews To Database total reviews:"+reviews.size());
			logger.debug("Saving hotel price information in mongodb:"
					+ hotelPriceDetial.toString());
			DB db = mongoConnection.getDB(DB_HOTEL_PRICE);
			
			DBCollection dbCollection = db.getCollection(hotelid + "_"
					+ COLLECTION_PRICE_PREFIX+"_"+hotelPriceDetial.getHotelPriceSource());

			BasicDBObject basicObject = new BasicDBObject();
			basicObject.put(hotelPriceDetial.getCOLUMN_NAME_PRICE_SOURCE(),
					hotelPriceDetial.getHotelPriceSource());
			basicObject.put(hotelPriceDetial.getCOLUMN_NAME_PRICE(),
					hotelPriceDetial.getHotelPrice());
			basicObject.put(hotelPriceDetial.getCOLUMN_NAME_PRICE_CURRENCY(),
					hotelPriceDetial.getHotelPriceCurrency());
			basicObject.put(hotelPriceDetial.getCOLUMN_NAME_PRICE_DETAIL_URL(),
					hotelPriceDetial.getHotelPriceDetailUrl());
			basicObject.put(hotelPriceDetial.getCOLUMN_NAME_PRICE_URL_VIEWER(),
					hotelPriceDetial.getHotelPriceUrl());
			basicObject.put(
					hotelPriceDetial.getCOLUMN_NAME_PRICE_CREATE_TIME(),
					hotelPriceDetial.getCreateTime());

			dbCollection.insert(basicObject);
			// System.out.println("Reviews Saved to database");
			logger.debug("Hotel Price Saved to price");
			return true;
		} catch (Exception ex) {
			// System.out.println("Error Message:"+ex.getMessage());
			logger.error("Exception at saving price:", ex);
			return false;
		} finally {

		}

	}
	
	
}
