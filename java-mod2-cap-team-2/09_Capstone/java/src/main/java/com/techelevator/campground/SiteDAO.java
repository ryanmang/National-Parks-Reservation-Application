package com.techelevator.campground;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface SiteDAO {
	
	public List<Site> sqlCampSites(LocalDate arrivalDate, LocalDate departureDate, Long campId, int lengthStay);
//	public String campgroundId(String camp);
	public List<Site> selectCampSites(SqlRowSet results, int lengthStay);
	public int lengthOfStay(LocalDate arrival, LocalDate departure);
}
