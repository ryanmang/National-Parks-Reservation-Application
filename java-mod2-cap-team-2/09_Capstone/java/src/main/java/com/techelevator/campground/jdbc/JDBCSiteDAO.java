package com.techelevator.campground.jdbc;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Site;
import com.techelevator.campground.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Site> sqlCampSites(LocalDate arrivalDate, LocalDate departureDate, Long campId, int lengthStay) {
		String sqlString = "SELECT A.site_id, A.campground_id, A.site_number, A.max_occupancy, A.accessible, A.max_rv_length, A.utilities, B.daily_fee " + 
				"FROM site A INNER JOIN campground B ON A.campground_id = B.campground_id " + 
				"WHERE A.campground_id = ? AND A.site_id NOT IN (SELECT site_id FROM reservation WHERE (to_date BETWEEN ? AND ?) "+
				"OR (from_date BETWEEN ? AND ?) OR (to_date < ? AND from_date > ?)) LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, campId, arrivalDate, departureDate, arrivalDate, departureDate, arrivalDate, departureDate);
		List<Site> sites = selectCampSites(results, lengthStay);
		return sites;
	}

	@Override
	public List<Site> selectCampSites(SqlRowSet results, int lengthStay) {
		List<Site> sitesInfo = new ArrayList<Site>();
		while (results.next()) {
			Site site = new Site();
			Long siteId = results.getLong("site_id");
			site.setSiteId(siteId);
			Long campId2 = results.getLong("campground_id");
			site.setSiteId(campId2);
			int siteNum = results.getInt("site_number");
			site.setSiteNumber(siteNum);
			int maxocc = results.getInt("max_occupancy");
			site.setMaxOccupancy(maxocc);
			boolean accessible = results.getBoolean("accessible");
			site.setAccessible(accessible);
			int maxRv = results.getInt("max_rv_length");
			site.setMaxRv(maxRv);
			boolean utilities = results.getBoolean("utilities");
			site.setAccessible(utilities);
			double dailyFee = results.getDouble("daily_fee");
			double totalFee = dailyFee * lengthStay;
			site.setDailyFee(totalFee);
			sitesInfo.add(site);
		}
		return sitesInfo;
	}
	
	@Override
	public int lengthOfStay(LocalDate arrival, LocalDate departure) {
		Period period = Period.between(arrival, departure);
		int lengthDays = period.getDays();
		return lengthDays;
	}
	
//	@Override
//	public String campgroundId(String camp) {
//		if(camp.equals("0")) {
//			System.out.print("Thank you.");
//			System.exit(0);
//		} else if(camp.equals("1") || camp.equals("2") || camp.equals("3") || camp.equals("4") || camp.equals("5") || camp.equals("6") || camp.equals("7")) {
//			return camp;
//		} else {
//			System.out.println("Invalid Id. Enter a number.");
//			searchCampSites();
//		}
//		return camp;
//	}
}
