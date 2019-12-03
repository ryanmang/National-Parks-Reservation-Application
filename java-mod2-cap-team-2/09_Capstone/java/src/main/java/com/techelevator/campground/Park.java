package com.techelevator.campground;

import java.time.LocalDate;

public class Park {

	private String name;
	private Long id;
	private String location;
	private LocalDate date;
	private Long area;
	private Long visitors;
	private String description;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public Long getArea() {
		return area;
	}
	
	public void setArea(Long area) {
		this.area = area;
	}
	
	public Long getVisitors() {
		return visitors;
	}
	
	public void setVisitors(Long visitors) {
		this.visitors = visitors;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}