package com.repup.pricecrawer.dao.manager;

import java.util.ArrayList;

import com.mongodb.MongoClient;
import com.repup.pricecrawer.dao.PriceDAO;
import com.repup.pricecrawer.dao.command.DAOCommand;
import com.repup.pricecrawler.model.HotelPriceDetail;

public class PriceDAOManager {

	private MongoClient mongoConnection;
	private PriceDAO priceDAO = null;

	public PriceDAOManager(MongoClient mongoConnection) {

		this.mongoConnection = mongoConnection;
	}

	public PriceDAO getPriceDAO() {
		
		if (priceDAO == null) {
			
			priceDAO = new PriceDAO(mongoConnection);
		}

		return priceDAO;
	}

	private ArrayList<HotelPriceDetail> transaction(DAOCommand daoCommand) {

		ArrayList<HotelPriceDetail> returnValue = daoCommand.execute(this);

		return returnValue;

	}

	public ArrayList<HotelPriceDetail> transactionAndClose(
			final DAOCommand daoCommand) {
		return executeAndClose(new DAOCommand() {

			public ArrayList<HotelPriceDetail> execute(
					PriceDAOManager daoManager) {

				return daoManager.transaction(daoCommand);
			}
		});
	}

	private ArrayList<HotelPriceDetail> executeAndClose(DAOCommand daoCommand) {
		try {
			return daoCommand.execute(this);
		} finally {
			// mongoConnection.close();
		}
	}

}
