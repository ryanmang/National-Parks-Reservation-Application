package com.techelevator.campground;

public class Site {
	
	private Long siteId;
	private Long campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean accessible;
	private int maxRv;
	private boolean utilities;
	private double dailyFee;
	
	public Long getSiteId() {
		return siteId;
	}
	
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public int getSiteNumber() {
		return siteNumber;
	}
	
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	
	public boolean isAccessible() {
		return accessible;
	}
	
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	
	public int getMaxRv() {
		return maxRv;
	}
	
	public void setMaxRv(int maxRv) {
		this.maxRv = maxRv;
	}
	
	public boolean isUtilities() {
		return utilities;
	}
	
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	
	public double getDailyFee() {
		return dailyFee;
	}
	
	public void setDailyFee(double dailyFee) {
		this.dailyFee = dailyFee;
	}
}
