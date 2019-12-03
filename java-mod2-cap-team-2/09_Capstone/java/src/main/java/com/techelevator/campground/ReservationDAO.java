package com.techelevator.campground;

import java.time.LocalDate;

public interface ReservationDAO {
	
	public void reservationDates(LocalDate arrival, LocalDate departure, Long campId);
	public void makeReservation(Reservation reservation, Long campId);
	public void retrieveSiteIdForReservation(int siteNum, String customer, Reservation reservation, Long campId);
	public void insertReservation(LocalDate arrival, LocalDate departure, Long siteId, String customer);
}
