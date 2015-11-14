package com.repup.pricecrawer.dao;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.MongoClient;
import com.repup.pricecrawer.dao.manager.PriceDAOManager;

public class DAOFactory {

	private static PriceDAOManager hotelDAOManager = null;
	private static MongoClient mongoConnection = null;
	private static Logger logger = Logger.getLogger(DAOFactory.class);
	//private static String DB_CONNECTION_STRING="52.4.240.156:27017";
	
	public static PriceDAOManager getReviewDAOManager()
	{
		if(hotelDAOManager==null)
		{
			hotelDAOManager = new PriceDAOManager(getMongoDBConnection());
		}		
		return hotelDAOManager;
	}
	
	
	private static MongoClient getMongoDBConnection()
	{
		if(mongoConnection==null)
		{
			try {
				logger.debug("Opening database connection:");
				mongoConnection = new MongoClient("localhost",27017);
			//	mongoConnection = new MongoClient(DB_CONNECTION_STRING);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return mongoConnection;
		
		
	}

	public static void closeDAOConnection() {
		logger.debug("Closing database connection:");
		if(mongoConnection!=null)
		{
			mongoConnection.close();
		}
		mongoConnection = null;
		hotelDAOManager = null;
	}
}
