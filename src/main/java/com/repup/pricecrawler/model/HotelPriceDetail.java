package com.repup.pricecrawler.model;

import java.util.Date;

public class HotelPriceDetail {

	private final String COLUMN_NAME_PRICE_URL_VIEWER = "price_url";
	private String hotelPriceUrl;
	private final String COLUMN_NAME_PRICE_DETAIL_URL = "price_detail_url";
	private String hotelPriceDetailUrl;
	private final String COLUMN_NAME_PRICE = "price";
	private String hotelPrice;
	private final String COLUMN_NAME_PRICE_CURRENCY = "currency";
	private String hotelPriceCurrency;
	private final String COLUMN_NAME_PRICE_SOURCE = "price_source";
	private String hotelPriceSource;
	private final String COLUMN_NAME_PRICE_CREATE_TIME = "create_time";
	private Date createTime;
	
	public String getHotelPriceUrl() {
		return hotelPriceUrl;
	}
	public void setHotelPriceUrl(String hotelPriceUrl) {
		this.hotelPriceUrl = hotelPriceUrl;
	}
	public String getHotelPriceDetailUrl() {
		return hotelPriceDetailUrl;
	}
	public void setHotelPriceDetailUrl(String hotelPriceDetailUrl) {
		this.hotelPriceDetailUrl = hotelPriceDetailUrl;
	}
	public String getHotelPrice() {
		return hotelPrice;
	}
	public void setHotelPrice(String hotelPrice) {
		this.hotelPrice = hotelPrice;
	}
	public String getHotelPriceCurrency() {
		return hotelPriceCurrency;
	}
	public void setHotelPriceCurrency(String hotelPriceCurrency) {
		this.hotelPriceCurrency = hotelPriceCurrency;
	}
	public String getHotelPriceSource() {
		return hotelPriceSource;
	}
	public void setHotelPriceSource(String hotelPriceSource) {
		this.hotelPriceSource = hotelPriceSource;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCOLUMN_NAME_PRICE_URL_VIEWER() {
		return COLUMN_NAME_PRICE_URL_VIEWER;
	}
	public String getCOLUMN_NAME_PRICE_DETAIL_URL() {
		return COLUMN_NAME_PRICE_DETAIL_URL;
	}
	public String getCOLUMN_NAME_PRICE() {
		return COLUMN_NAME_PRICE;
	}
	public String getCOLUMN_NAME_PRICE_CURRENCY() {
		return COLUMN_NAME_PRICE_CURRENCY;
	}
	public String getCOLUMN_NAME_PRICE_SOURCE() {
		return COLUMN_NAME_PRICE_SOURCE;
	}
	public String getCOLUMN_NAME_PRICE_CREATE_TIME() {
		return COLUMN_NAME_PRICE_CREATE_TIME;
	}
	
	
	@Override
	public String toString() {
		return "HotelPriceDetail [hotelPriceUrl=" + hotelPriceUrl
				+ ", hotelPriceDetailUrl=" + hotelPriceDetailUrl
				+ ", hotelPrice=" + hotelPrice + ", hotelPriceCurrency="
				+ hotelPriceCurrency + ", hotelPriceSource=" + hotelPriceSource
				+ ", createTime=" + createTime + "]";
	}
	
	
	
	


}
