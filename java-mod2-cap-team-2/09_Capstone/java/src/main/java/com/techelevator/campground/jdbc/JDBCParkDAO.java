package com.techelevator.campground.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Park;
import com.techelevator.campground.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> parkNames() {

		List<Park> parks = new ArrayList<Park>();

		String parkInfoByNameAlphabetically = "SELECT * FROM park ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(parkInfoByNameAlphabetically);
		while (results.next()) {
			Park park = new Park();
			Long parkId = results.getLong("park_id");
			park.setId(parkId);
			String name = results.getString("name");
			park.setName(name);
			String location = results.getString("location");
			park.setLocation(location);
			LocalDate date = results.getDate("establish_date").toLocalDate();
			park.setDate(date);
			Long area = results.getLong("area");
			park.setArea(area);
			Long visitors = results.getLong("visitors");
			park.setVisitors(visitors);
			String description = results.getString("description");
			park.setDescription(description);
			parks.add(park);
		}
		return(parks);
	}
	
	@Override
	public String[] returnParkNames(List<Park> parks) {
		int arraySize = parks.size() + 1;
		String[] parkArray = new String[arraySize];
		for (int i = 0; i < parks.size(); i++) {
			parkArray[i] = parks.get(i).getName();
		}
		parkArray[arraySize - 1] = "Quit";
		return parkArray;
	}

	@Override
	public Park parkInformation(String parkInput) {

		Park parks = new Park();

		String sqlString = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park WHERE name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, parkInput);
		while (results.next()) {
			Long parkId = results.getLong("park_id");
			parks.setId(parkId);
			String name = results.getString("name");
			parks.setName(name);
			String location = results.getString("location");
			parks.setLocation(location);
			LocalDate date = results.getDate("establish_date").toLocalDate();
			parks.setDate(date);
			Long area = results.getLong("area");
			parks.setArea(area);
			Long visitors = results.getLong("visitors");
			parks.setVisitors(visitors);
			String description = results.getString("description");
			parks.setDescription(description);
		}
		return parks;
	}
}
