package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.techelevator.campground.Site;
import com.techelevator.campground.SiteDAO;

public class JDBCSiteDAOTest {

	private static SingleConnectionDataSource dataSource;
	private SiteDAO dao;
	private JdbcTemplate jdbcTemplate;
	
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
		dao = new JDBCSiteDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);

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
	public void testResultsSqlAndSelectCampSitesGetSiteId() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String arrival = "2019/10/31";
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		String departure = "2020/11/07";
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		int lengthStay = dao.lengthOfStay(arrivalDate, departureDate);
		String sqlString = "SELECT A.site_id, A.campground_id, A.site_number, A.max_occupancy, A.accessible, A.max_rv_length, A.utilities, B.daily_fee " + 
				"FROM site A INNER JOIN campground B ON A.campground_id = B.campground_id " + 
				"WHERE A.campground_id = 1 AND A.site_id NOT IN (SELECT site_id FROM reservation WHERE (to_date BETWEEN ? AND ?) "+
				"OR (from_date BETWEEN ? AND ?) OR (to_date < ? AND from_date > ?)) LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, arrivalDate, departureDate, arrivalDate, departureDate, arrivalDate, departureDate);
		List<Site> sitesSql = dao.sqlCampSites(arrivalDate, departureDate, 1L, lengthStay);
		Long siteId1 = sitesSql.get(0).getSiteId();
		List<Site> sitesSelect = dao.selectCampSites(results, lengthStay);
		Long siteId2 = sitesSelect.get(0).getSiteId();
		
		assertEquals(siteId1, siteId2);
	}
	
	@Test
	public void testResultsSqlAndSelectCampSitesGetDailyFee() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String arrival = "2019/10/31";
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		String departure = "2020/11/07";
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		int lengthStay = dao.lengthOfStay(arrivalDate, departureDate);
		String sqlString = "SELECT A.site_id, A.campground_id, A.site_number, A.max_occupancy, A.accessible, A.max_rv_length, A.utilities, B.daily_fee " + 
				"FROM site A INNER JOIN campground B ON A.campground_id = B.campground_id " + 
				"WHERE A.campground_id = 1 AND A.site_id NOT IN (SELECT site_id FROM reservation WHERE (to_date BETWEEN ? AND ?) "+
				"OR (from_date BETWEEN ? AND ?) OR (to_date < ? AND from_date > ?)) LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, arrivalDate, departureDate, arrivalDate, departureDate, arrivalDate, departureDate);
		List<Site> sitesSql = dao.sqlCampSites(arrivalDate, departureDate, 1L, lengthStay);
		double dailyFee1 = sitesSql.get(0).getDailyFee();
		List<Site> sitesSelect = dao.selectCampSites(results, lengthStay);
		double dailyFee2 = sitesSelect.get(0).getDailyFee();
		
		assertEquals(dailyFee1, dailyFee2, 0);
	}

	@Test
	public void testLengthOfStay7DaysInJanuary2020() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String arrival = "2020/01/01";
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		String departure = "2020/01/08";
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		
		int actualLengthOfStay = dao.lengthOfStay(arrivalDate, departureDate);
		
		assertEquals(7, actualLengthOfStay);
	}
	
	@Test
	public void testLengthOfStay10DaysIn2019To2020() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String arrival = "2019/12/24";
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		String departure = "2020/01/03";
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		
		int actualLengthOfStay = dao.lengthOfStay(arrivalDate, departureDate);
		
		assertEquals(10, actualLengthOfStay);
	}

}
