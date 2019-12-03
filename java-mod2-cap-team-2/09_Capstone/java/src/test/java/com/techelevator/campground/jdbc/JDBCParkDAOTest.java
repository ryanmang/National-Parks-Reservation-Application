package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.ParkDAO;
import com.techelevator.campground.Park;

public class JDBCParkDAOTest {

	private static SingleConnectionDataSource dataSource;
	private ParkDAO dao;
	private JdbcTemplate jdbcTemplate;
	List<Park> parks = new ArrayList<Park>();
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setUp() {
		dao = new JDBCParkDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		String descParkInfoByName = "SELECT * FROM park ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(descParkInfoByName);
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
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	/* This method provides access to the DataSource for subclasses so that
	 * they can instantiate a DAO for testing */
	protected DataSource getDataSource() {
		return dataSource;
	}

	@Test
	public void testParkNamesListSize() {
		List<Park> parkNames = dao.parkNames();
		int expectedSize = parks.size();
		int actualSize = parkNames.size();
		
		assertEquals(expectedSize, actualSize);
	}
	
	@Test
	public void testParkNamesIfIndex0IsTheSameParkId() {
		List<Park> parkNames = dao.parkNames();
		Long actualParkId = parkNames.get(0).getId();
		Long expectedParkId = parks.get(0).getId();
		
		assertEquals(expectedParkId,actualParkId);
	}
	
	@Test
	public void testParkNamesIfIndex1IsTheSameName() {
		List<Park> parkNames = dao.parkNames();
		String actualName = parkNames.get(0).getName();
		String expectedName = parks.get(0).getName();
		
		assertEquals(expectedName,actualName);
	}
	
	@Test
	public void testParkNamesIfIndex2IsTheSameDate() {
		List<Park> parkNames = dao.parkNames();
		LocalDate actualDate = parkNames.get(0).getDate();
		LocalDate expectedDate = parks.get(0).getDate();
		
		assertEquals(expectedDate,actualDate);
	}
	
	@Test
	public void testreturnParkNamesArraySize() {
		int arraySize = parks.size() + 1;
		String[] parkArray = new String[arraySize];
		for (int i = 0; i < parks.size(); i++) {
			parkArray[i] = parks.get(i).getName();
		}
		parkArray[arraySize - 1] = "Quit";
				
		String[] parkNames = dao.returnParkNames(parks);
		int expectedSize = parkArray.length;
		int actualSize = parkNames.length;
		
		assertEquals(expectedSize, actualSize);
	}
	
	@Test
	public void testreturnParkNamesArraySizeAndIndex0Name() {
		List<Park> testList = new ArrayList<Park>();
		Park park = new Park();
		Long parkId = 10L;
		park.setId(parkId);
		String name = "Belle Isle State Park";
		park.setName(name);
		String location = "Detroit, Michigan";
		park.setLocation(location);
		testList.add(park);
		Long parkId2 = 11L;
		park.setId(parkId2);
		String name2 = "Central Park";
		park.setName(name2);
		String location2 = "New York City";
		park.setLocation(location2);
		testList.add(park);
		
		int arraySize = testList.size() + 1;
		String[] parkArray = new String[arraySize];
		for (int i = 0; i < testList.size(); i++) {
			parkArray[i] = testList.get(i).getName();
		}
		parkArray[arraySize - 1] = "Quit";
				
		String[] parkNames = dao.returnParkNames(testList);
		int expectedSize = parkArray.length;
		int actualSize = parkNames.length;
		
		assertEquals(expectedSize, actualSize);
		assertEquals(parkArray[0], parkNames[0]);
	}

}
