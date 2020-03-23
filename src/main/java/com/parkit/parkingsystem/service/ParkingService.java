package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

public class ParkingService {

	public static final Logger logger = LogManager.getLogger("ParkingService");

	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

	public InputReaderUtil inputReaderUtil;
	public ParkingSpotDAO parkingSpotDAO;
	public TicketDAO ticketDAO;
	LocalDateTime inTimeDay;
	LocalDateTime outTimeDay;

	public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
		this.inputReaderUtil = inputReaderUtil;
		this.parkingSpotDAO = parkingSpotDAO;
		this.ticketDAO = ticketDAO;
	}

	public ParkingType askForVehichleType() {
		System.out.println("Please select vehicle type from menu");
		System.out.println("1 CAR");
		System.out.println("2 BIKE");
		int input = inputReaderUtil.readSelection();
		switch (input) {
		case 1:

			return ParkingType.CAR;

		case 2:
			return ParkingType.BIKE;

		default:
			System.out.println("Incorrect input provided");
			throw new IllegalArgumentException("Entered input is invalid");
		}
	}

	public String askForVehichleRegNumber() throws Exception {
		System.out.println("Please type the vehicle registration number and press enter key");
		return inputReaderUtil.readVehicleRegistrationNumber();

	}

	public void processIncomingVehicle(ParkingSpot parkingSpot) {
		try {

			parkingSpot.setId(parkingSpotDAO.getNextAvailableSlot(parkingSpot.getParkingType()));

			if (parkingSpot.getId() > 0) {

				parkingSpot.setAvailable(false);
				parkingSpotDAO.updateParking(parkingSpot);
				// allot this parking space and mark it's
				// availability as false

				String vehicleRegNumber = parkingSpot.getVehicleRegNumber().toUpperCase();

				inTimeDay = LocalDateTime.now();

				Ticket ticket = new Ticket();

				ticket.setParkingSpot(parkingSpot);
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setInTimeDay(inTimeDay);
				ticketDAO.saveTicket(ticket);

				System.out.println("Dear new customer, Welcome to our parking !");
				System.out.println("Generated Ticket and saved in DB");
				System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
				System.out.println("Recorded in-time for vehicle number:" + vehicleRegNumber + " is :"
						+ inTimeDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

			} else {
				System.out.println("We are sorry but the parking might be full");
			}
		} catch (Exception e) {
			logger.error("Unable to process incoming vehicle", e);
			logger.error("Error fetching next available parking slot", e);
		}
	}

	public void processExitingVehicle() {
		try {
			String vehicleRegNumber = askForVehichleRegNumber().toUpperCase();
			Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
			outTimeDay = LocalDateTime.now();
			ticket.setOutTimeDay(outTimeDay);

			if (ticketDAO.isLoyalCustomer(vehicleRegNumber)) {
				ticket.setLoyalCustomer(true);
				System.out.println(
						"Happy to see you again, as loyal customer, you get 5% discount on your parking fare !");

			} else {
				System.out.println("Thank you for coming!");
			}

			fareCalculatorService.calculateFare(ticket);

			if (ticketDAO.updateTicket(ticket)) {
				ParkingSpot parkingSpot = ticket.getParkingSpot();
				parkingSpot.setAvailable(true);
				parkingSpotDAO.updateParking(parkingSpot);

				System.out.println("Please pay the parking fare:" + ticket.getPrice() + " €");
				System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:"
						+ outTimeDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
			} else {
				System.out.println("Unable to update ticket information. Error occurred");
			}
		} catch (Exception e) {
			logger.error("Unable to process exiting vehicle", e);
		}
	}
}