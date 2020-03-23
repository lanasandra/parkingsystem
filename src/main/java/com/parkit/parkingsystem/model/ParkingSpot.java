package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

public class ParkingSpot {
	private int number;
	private ParkingType parkingType;
	private boolean isAvailable;
	private String vehicleRegNumber;

	public ParkingSpot() {

	}

	public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
		this.number = number;
		this.parkingType = parkingType;
		this.isAvailable = isAvailable;
	}

	public int getId() {
		return number;
	}

	public void setId(int number) {
		this.number = number;
	}

	public ParkingType getParkingType() {
		return parkingType;
	}

	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}
}