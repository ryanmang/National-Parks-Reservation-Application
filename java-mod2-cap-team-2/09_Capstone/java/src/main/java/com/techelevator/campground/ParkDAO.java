package com.techelevator.campground;

import java.util.List;

public interface ParkDAO {
	
	public List<Park> parkNames();
	public String[] returnParkNames(List<Park> parks);
	public Park parkInformation(String parkInput);
}
