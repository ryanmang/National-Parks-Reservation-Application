package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import com.techelevator.campground.Campground;
import com.techelevator.campground.CampgroundDAO;
import com.techelevator.campground.Park;
import com.techelevator.campground.ParkDAO;
import com.techelevator.campground.ReservationDAO;
import com.techelevator.campground.Site;
import com.techelevator.campground.SiteDAO;
import com.techelevator.campground.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.jdbc.JDBCParkDAO;
import com.techelevator.campground.jdbc.JDBCReservationDAO;
import com.techelevator.campground.jdbc.JDBCSiteDAO;

public class CampgroundCLI {

	private static final String PARK_MENU_OPTION_VIEW = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH = "Search for Reservation";
	private static final String PARK_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] PARK_MENU_OPTIONS = {PARK_MENU_OPTION_VIEW, PARK_MENU_OPTION_SEARCH, PARK_MENU_OPTION_RETURN};
	
	private static final String CAMP_MENU_OPTION_SEARCH = "Search for Available Reservation";
	private static final String CAMP_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] CAMP_MENU_OPTIONS = {CAMP_MENU_OPTION_SEARCH, CAMP_MENU_OPTION_RETURN};

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		this.parkDAO = new JDBCParkDAO(datasource);
		this.campgroundDAO = new JDBCCampgroundDAO(datasource);
		this.reservationDAO = new JDBCReservationDAO(datasource);
		this.siteDAO = new JDBCSiteDAO(datasource);
	}

	public void run() {
		while (true) {
			List<Park> parks = parkDAO.parkNames();
			printHeading("Select a Park for Further Details");
			String choice = (String) menu.getChoiceFromOptions(parkDAO.returnParkNames(parks));
			if (choice.contentEquals("Quit")) {
				System.out.println("Thank you.");
				System.exit(1);
			}
			Park thePark = parkDAO.parkInformation(choice);
			listParks(thePark);
		}
	}

	private void listParks(Park thePark) {
		System.out.println();
		printHeading(thePark.getName() + " National Park");
		System.out.println(String.format("%-20s", "Location:") +thePark.getLocation());
		System.out.println(String.format("%-20s", "Established:") +thePark.getDate());
		System.out.println(String.format("%-20s", "Area:") +thePark.getArea() + " sq km");
		System.out.println(String.format("%-20s", "Annual Visitors:") +thePark.getVisitors());
		System.out.println();
		String description = thePark.getDescription();
		String[] paragraph = description.split("\\.");
		for(int i = 0; i < paragraph.length; i++) {
			System.out.println(paragraph[i] +".");
		}
		parkMenu(thePark);
	}
	
	private void parkMenu(Park thePark) {
		printHeading("Select a Command");
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if(choice.equals(PARK_MENU_OPTION_VIEW)) {
			Long parkId = thePark.getId();
			List <Campground> theCamps = campgroundDAO.viewCampgrounds(parkId);
			
			listCampgrounds(theCamps, thePark);
		} else if (choice.equals(PARK_MENU_OPTION_SEARCH)) {
			campMenu(thePark);
		} else if (choice.equals(PARK_MENU_OPTION_RETURN)) {
			run();
		}
	}
	
	private void listCampgrounds(List <Campground> theCamps, Park thePark) {
		System.out.println();
		printHeading(thePark.getName() + " National Park Campgrounds");
		System.out.println("Id " + String.format("%-35s", "Name") + String.format("%-15s", "Open") + String.format("%-15s", "Close") + "Daily Fee");
		for(int i = 0; i < theCamps.size(); i ++) {
			System.out.println("#"+theCamps.get(i).getCampgroundId() +" "+ String.format("%-35s", theCamps.get(i).getName()) +
					String.format("%-15s", campgroundDAO.toMonth(theCamps.get(i).getOpenFromMnth())) + String.format("%-15s", campgroundDAO.toMonth(theCamps.get(i).getOpenToMnth())) +
					"$"+ String.format("%.2f",theCamps.get(i).getFee()));
		}
		campMenu(thePark);
	}
	
	public void campMenu(Park thePark) {
		printHeading("Select a Command");
		String choice = (String)menu.getChoiceFromOptions(CAMP_MENU_OPTIONS);
		if (choice.equals(CAMP_MENU_OPTION_SEARCH)) {
			searchCampSites();
		} else if (choice.equals(CAMP_MENU_OPTION_RETURN)) {
			parkMenu(thePark);
		}
	}
	
	public void searchCampSites() {
		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter campground Id number (enter 0 to cancel): ");
		String camp = input.nextLine();
		if(camp.equals("0")) {
			System.out.print("Thank you.");
			System.exit(0);
		}
//		String theCamp = siteDAO.campgroundId(camp);
		Long campId = Long.parseLong(camp);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		System.out.print("What is the arrival date? (YYYY/MM/DD) ");
		String arrival = input.nextLine();
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		System.out.print("What is the departure date? (YYYY/MM/DD) ");
		String departure = input.nextLine();
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		int lengthStay = siteDAO.lengthOfStay(arrivalDate, departureDate);
		
		List<Site> theSites = siteDAO.sqlCampSites(arrivalDate, departureDate, campId, lengthStay);

		listCampSites(theSites, arrivalDate, departureDate, campId);
	}
	
	private void listCampSites(List<Site> theSites, LocalDate arrivalDate, LocalDate departureDate, Long campId) {
		if (theSites == null || theSites.isEmpty()) {
			tryAgain();
		}
		System.out.println();
		printHeading("Results Matching Your Search Criteria");
		System.out.println(String.format("%-10s", "Site No.") + String.format("%-13s", "Max Occup.") + String.format("%-13s", "Accessible?") +
				String.format("%-15s", "Max RV Length") + String.format("%-10s", "Utility") + "Cost");
		for(int i = 0; i < theSites.size(); i ++) {
			System.out.println(String.format("%-10s", theSites.get(i).getSiteNumber()) + String.format("%-13s", theSites.get(i).getMaxOccupancy()) +
					String.format("%-13s", theSites.get(i).isAccessible()) + String.format("%-15s", theSites.get(i).getMaxRv()) +
					String.format("%-10s", theSites.get(i).isUtilities()) + "$"+String.format("%.2f",theSites.get(i).getDailyFee()));
		}
		System.out.println();
		reservationDAO.reservationDates(arrivalDate, departureDate, campId);
	}

	public void tryAgain() {
		Scanner input = new Scanner(System.in);
		System.out.println("Dates unavailable.");
		System.out.print("Would you like to enter new dates? (Y/N) ");
		String yesNo = input.next().toUpperCase();
		if(yesNo.equals("Y")) {
			searchCampSites();
		} else if(yesNo.equals("N")) {
			System.out.println("Thank you.");
			System.exit(0);
		} else {
			System.out.println("Try Again.");
			tryAgain();
		}
		input.close();
	}
	
	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
