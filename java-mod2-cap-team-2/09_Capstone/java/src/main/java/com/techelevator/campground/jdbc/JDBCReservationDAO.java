package com.techelevator.campground.jdbc;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Reservation;
import com.techelevator.campground.ReservationDAO;
import com.techelevator.campground.Site;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void reservationDates(LocalDate arrival, LocalDate departure, Long campId) {
		Reservation reservation = new Reservation();
		reservation.setFromDate(arrival);
		reservation.setToDate(departure);
		makeReservation(reservation, campId);
	}
	
	@Override
	public void makeReservation(Reservation reservation, Long campId) {
		Scanner input = new Scanner(System.in);
		
		System.out.print("Which Site No. should be reserved (enter 0 to cancel)? ");
		String theSite = input.nextLine();
		int siteNum = Integer.parseInt(theSite);
		if(siteNum == 0) {
			System.out.print("Thank you.");
			System.exit(0);
		}
		System.out.print("What name should the reservation be made under? ");
		String customer = input.nextLine();
		input.close();
		retrieveSiteIdForReservation(siteNum, customer, reservation, campId);
	}
	
	@Override
	public void retrieveSiteIdForReservation(int siteNum, String customer, Reservation reservation, Long campId) {
		String sqlSiteId = "SELECT site_id FROM site WHERE campground_id = ? AND site_number = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSiteId, campId, siteNum);
		Site site = new Site();
		while (results.next()) {
			Long siteId = results.getLong("site_id");
			site.setSiteId(siteId);
		}
		Long siteId = site.getSiteId();
		LocalDate arrival = reservation.getFromDate();
		LocalDate departure = reservation.getToDate();
		insertReservation(arrival, departure, siteId, customer);
	}
	
	@Override
	public void insertReservation(LocalDate arrival, LocalDate departure, Long siteId, String customer) {
		String insertReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) "+
		"VALUES(?, ?, ?, ?, now())";
		jdbcTemplate.update(insertReservation, siteId, customer, arrival, departure);
		
		int confirmationIdNum = ThreadLocalRandom.current().nextInt();
		System.out.println("The reservation has been made and the confirmation id number: " + confirmationIdNum);
		System.out.println("Thank you.");
		System.exit(confirmationIdNum);
	}

}
