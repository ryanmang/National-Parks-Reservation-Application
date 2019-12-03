package com.techelevator.campground;

public class Campground {
	
	private Long campgroundId;
	private Long parkId;
	private String name;
	private int openFromMnth;
	private int openToMnth;
	private double fee;
	
	public Long getCampgroundId() {
		return campgroundId;
	}
	
	public void setCampgroundId(Long campgroundId) {
		this.campgroundId = campgroundId;
	}
	
	public Long getParkId() {
		return parkId;
	}
	
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getOpenFromMnth() {
		return openFromMnth;
	}
	
	public void setOpenFromMnth(int openFromMnth) {
		this.openFromMnth = openFromMnth;
	}
	
	public int getOpenToMnth() {
		return openToMnth;
	}
	
	public void setOpenToMnth(int openToMnth) {
		this.openToMnth = openToMnth;
	}
	
	public double getFee() {
		return fee;
	}
	
	public void setFee(double fee) {
		this.fee = fee;
	}
}
