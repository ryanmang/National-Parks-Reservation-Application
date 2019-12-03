package com.techelevator.campground.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Campground;
import com.techelevator.campground.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List <Campground> viewCampgrounds(Long parkId) {
		List <Campground> camps = new ArrayList <Campground>();

		String sqlString = "SELECT park_id, campground_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, parkId);
		while (results.next()) {
			Campground theCamps = new Campground();
			Long campId = results.getLong("campground_id");
			theCamps.setCampgroundId(campId);
			Long IdPark = results.getLong("park_id");
			theCamps.setParkId(IdPark);
			String name = results.getString("name");
			theCamps.setName(name);
			int fromDate = results.getInt("open_from_mm");
			theCamps.setOpenFromMnth(fromDate);
			int toDate = results.getInt("open_to_mm");
			theCamps.setOpenToMnth(toDate);
			double fee = results.getDouble("daily_fee");
			theCamps.setFee(fee);
			camps.add(theCamps);
		}
		return camps;
	}
	
	@Override
	public String toMonth(int month) {
		String[] months = new String[13];
		months[0] = "";
		months[1] = "January";
		months[2] = "February";
		months[3] = "March";
		months[4] = "April";
		months[5] = "May";
		months[6] = "June";
		months[7] = "July";
		months[8] = "August";
		months[9] = "September";
		months[10] = "October";
		months[11] = "November";
		months[12] = "December";
		
		String theMonth = months[month];
		return theMonth;
	}

}
