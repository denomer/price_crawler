package com.repup.pricecrawler.model;

public class HotelData {
	
	private String hotedId;
	private String bookingUrl;
	private String expediaUrl;
	private String goibiboUrl;
	private String makeMyTripUrl;
	
	public String getHotedId() {
		return hotedId;
	}
	public void setHotedId(String hotedId) {
		this.hotedId = hotedId;
	}
	public String getBookingUrl() {
		return bookingUrl;
	}
	public void setBookingUrl(String bookingUrl) {
		this.bookingUrl = bookingUrl;
	}
	public String getExpediaUrl() {
		return expediaUrl;
	}
	public void setExpediaUrl(String expediaUrl) {
		this.expediaUrl = expediaUrl;
	}
	public String getGoibiboUrl() {
		return goibiboUrl;
	}
	public void setGoibiboUrl(String goibiboUrl) {
		this.goibiboUrl = goibiboUrl;
	}
	public String getMakeMyTripUrl() {
		return makeMyTripUrl;
	}
	public void setMakeMyTripUrl(String makeMyTripUrl) {
		this.makeMyTripUrl = makeMyTripUrl;
	}
	
	@Override
	public String toString() {
		return "HotelData [hotedId=" + hotedId + ", bookingUrl=" + bookingUrl
				+ ", expediaUrl=" + expediaUrl + ", goibiboUrl=" + goibiboUrl
				+ ", makeMyTripUrl=" + makeMyTripUrl + "]";
	}
	
	
	

	
}
