package com.repup.pricecrawer.dao.command;

import java.util.ArrayList;

import com.repup.pricecrawer.dao.manager.PriceDAOManager;
import com.repup.pricecrawler.model.HotelPriceDetail;

public interface DAOCommand {
	
	public ArrayList<HotelPriceDetail> execute(PriceDAOManager daoManager);
}
