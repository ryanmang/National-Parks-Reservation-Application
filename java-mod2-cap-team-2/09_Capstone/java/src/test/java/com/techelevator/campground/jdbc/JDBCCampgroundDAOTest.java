package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
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

import com.techelevator.campground.Campground;
import com.techelevator.campground.CampgroundDAO;

public class JDBCCampgroundDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private CampgroundDAO dao;
	private JdbcTemplate jdbcTemplate;
	List<Campground> camps = new ArrayList<Campground>();
		
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
		dao = new JDBCCampgroundDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		String sqlInsertTestCamp = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) VALUES (8, 3, 'Campground', 1, 12, 40.00)";
		jdbcTemplate.update(sqlInsertTestCamp);
//		String sqlString = "SELECT park_id, campground_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ?";
//		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, 3L);
//		while (results.next()) {
//			Campground theCamps = new Campground();
//			Long campId = results.getLong("campground_id");
//			theCamps.setCampgroundId(campId);
//			Long IdPark = results.getLong("park_id");
//			theCamps.setParkId(IdPark);
//			String name = results.getString("name");
//			theCamps.setName(name);
//			int fromDate = results.getInt("open_from_mm");
//			theCamps.setOpenFromMnth(fromDate);
//			int toDate = results.getInt("open_to_mm");
//			theCamps.setOpenToMnth(toDate);
//			double fee = results.getDouble("daily_fee");
//			theCamps.setFee(fee);
//			camps.add(theCamps);
//		}
		
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
	public void testIfCampgroundsAreSelectedByParkId() {
		List<Campground> campsTest = dao.viewCampgrounds(3L);
		int indexLocation = campsTest.size()-1;
		String actualName = campsTest.get(indexLocation).getName();
		assertEquals("Campground", actualName);
	}

//	@Test
//	public void testViewCampgroundsIndex0OpenMonth() {
//		List<Campground> campsTest = dao.viewCampgrounds(3L);
//		
//		int actualOpenMonth = campsTest.get(0).getOpenFromMnth();
//		int expectedOpenMonth = camps.get(0).getOpenFromMnth();
//		
//		assertEquals(expectedOpenMonth, actualOpenMonth);
//	}
//	
//	@Test
//	public void testViewCampgroundsIndex0CampgroundId() {
//		List<Campground> campsTest = dao.viewCampgrounds(3L);
//		
//		Long actualCampId = campsTest.get(0).getCampgroundId();
//		Long expectedCampId = camps.get(0).getCampgroundId();
//		
//		assertEquals(expectedCampId, actualCampId);
//	}
//	
//	@Test
//	public void testViewCampgroundsIndex0DailyFee() {
//		List<Campground> campsTest = dao.viewCampgrounds(3L);
//		
//		double actualFee = campsTest.get(0).getFee();
//		double expectedFee = camps.get(0).getFee();
//		
//		assertEquals(expectedFee, actualFee, 0);
//	}

	@Test
	public void testToMonthInt0IsBlank() {
		String month = dao.toMonth(0);
		
		assertEquals("", month);
	}
	
	@Test
	public void testToMonthInt6IsJune() {
		String month = dao.toMonth(6);
		
		assertEquals("June", month);
	}
	
	@Test
	public void testToMonthInt7IsJuly() {
		String month = dao.toMonth(7);
		
		assertEquals("July", month);
	}

}
