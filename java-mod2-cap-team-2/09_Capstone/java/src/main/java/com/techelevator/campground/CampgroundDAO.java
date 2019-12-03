package com.techelevator.campground;

import java.util.List;

public interface CampgroundDAO {
	
	public List <Campground> viewCampgrounds(Long parkId);
	public String toMonth(int month);

}
