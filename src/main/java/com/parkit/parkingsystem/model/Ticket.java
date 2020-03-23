package com.parkit.parkingsystem.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Ticket {
	private int id;
	private ParkingSpot parkingSpot;
	private String vehicleRegNumber;
	private double price;
	private LocalDateTime inTimeDay;
	private LocalDateTime outTimeDay;
	private boolean isLoyalCustomer;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	public void setPrice(double price) {
		this.price = (double) Math.round(price * 100) / 100;
	}

	public double getPrice() {
		return price;
	}

	public void setInTimeDay(LocalDateTime inTimeDay) {
		this.inTimeDay = inTimeDay;
	}

	public LocalDateTime getInTimeDay() {
		return inTimeDay;
	}

	public void setOutTimeDay(LocalDateTime outTimeDay) {
		this.outTimeDay = outTimeDay;
	}

	public LocalDateTime getOutTimeDay() {
		return outTimeDay;
	}

	public void setInTimeDay(Timestamp timestamp) {
		this.setInTimeDay(timestamp.toLocalDateTime());

	}

	public void setOutTimeDay(Timestamp timestamp) {
		this.setOutTimeDay(timestamp.toLocalDateTime());

	}

	public void setLoyalCustomer(boolean loyalCustomer) {
		isLoyalCustomer = loyalCustomer;
	}

	public boolean isLoyalCustomer() {
		return isLoyalCustomer;
	}

}