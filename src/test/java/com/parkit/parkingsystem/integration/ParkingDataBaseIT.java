package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private static ParkingType parkingType;
	private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

	LocalDateTime outTimeDay;
	LocalDateTime inTimeDay;
	String vehicleRegNumber;

	@Mock
	private InputReaderUtil inputReaderUtil;

	@Mock
	private ParkingSpot parkingSpot;

	@Mock
	private Ticket ticket;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() {
		try {

			parkingType = ParkingType.CAR;
			vehicleRegNumber = ("898LM22");
			parkingSpot = new ParkingSpot();
			parkingSpot.setId(1);
			parkingSpot.setParkingType(parkingType);
			parkingSpot.setVehicleRegNumber(vehicleRegNumber);
			inTimeDay = LocalDateTime.of(2020, 03, 15, 14, 20);
			outTimeDay = LocalDateTime.of(2020, 03, 15, 16, 20);

			ticket = new Ticket();
			ticket.setInTimeDay(inTimeDay);
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber(vehicleRegNumber);
			ticket.setOutTimeDay(outTimeDay);

			dataBasePrepareService.clearDataBaseEntries();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@AfterAll
	private static void tearDown() {

	}

	@Tag("ParkingSpotDAOGetNextAvailableSlot")
	@DisplayName("At vehicle entering, check if database check parking availability with parking Number and parkingType.")
	@Test
	public void testParkingAvailabilityCarEntering() {

		// WHEN

		parkingSpotDAO.getNextAvailableSlot(parkingType);

		// THEN
		assertThat(parkingSpot.getId()).isEqualTo(1);
		assertThat(parkingSpot.isAvailable()).isFalse();
		assertThat(parkingSpot.getParkingType()).isEqualTo(parkingType);

		// TODO: check that a ticket is actually saved in DB and Parking table is
		// updated
		// with availability
	}

	@Tag("ParkingSpotDAOUpdateParkingIncomingVehicule")
	@DisplayName("At vehicle entering, check if Parking table is updated with availability.")
	@Test
	public void testParkingACarEntering() {

		// WHEN
		parkingSpotDAO.updateParking(parkingSpot);

		// THEN
		assertThat(parkingSpot.getId()).isEqualTo(1);
		assertThat(parkingSpot.isAvailable()).isFalse();
		assertThat(parkingSpot.getParkingType()).isEqualTo(parkingType);

		// TODO: check that a ticket is actually saved in DB and Parking table is
		// updated
		// with availability
	}

	@Tag("ParkingSpotDAOUpdateParkingExitingVehicule")
	@DisplayName("At vehicle exiting, check if Parking table is updated with availability.")
	@Test
	public void testParkingACarExiting() {

		parkingSpotDAO.updateParking(parkingSpot);

		assertThat(parkingSpot.getId()).isEqualTo(1);

		assertThat(parkingSpotDAO.updateParking(parkingSpot)).isTrue();

	}

	@Tag("TicketDAOSaveTicket")
	@DisplayName("At vehicle entering, check if a ticket is saved in the database.")
	@Test
	public void testSaveTicketForACarEntering() throws Exception {

		// WHEN
		ticketDAO.saveTicket(ticket);

		// THEN

		assertThat(ticket.getParkingSpot().getId()).isEqualTo(1);
		assertThat(ticket.getVehicleRegNumber()).isEqualTo(vehicleRegNumber);
		assertThat(ticket.getPrice()).isEqualTo(0.00);
		assertThat(ticket.getInTimeDay()).isEqualTo(inTimeDay);

	}

	@Tag("TicketDAOGetTicket")
	@DisplayName("At vehicle exiting,, check if a ticket came from the database.")
	@Test
	public void testGetTicketForACarExiting() throws Exception {

		// WHEN
		ticketDAO.getTicket(vehicleRegNumber);

		// THEN

		assertThat(ticket.getParkingSpot().getId()).isEqualTo(1);
		assertThat(ticket.getVehicleRegNumber()).isEqualTo(vehicleRegNumber);
		assertThat(ticket.getInTimeDay()).isEqualTo(inTimeDay);

	}

	@Tag("TicketDAOUpdateTicket")
	@DisplayName("At vehicle exiting,, check if a ticket is updated with price and outTimeDay.")
	@Test
	public void testUpdateTicketForACarExiting() {

		// GIVEN
		ticket.setLoyalCustomer(false);
		fareCalculatorService.calculateFare(ticket);
		// WHEN

		ticketDAO.updateTicket(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(2.25);
		assertThat(ticket.getOutTimeDay()).isEqualTo(outTimeDay);
		assertThat(ticketDAO.isLoyalCustomer(vehicleRegNumber)).isFalse();
		assertThat(ticketDAO.updateTicket(ticket)).isTrue();
	}

	@Tag("TicketDAOUpdateTicketForLoyalCustomer")
	@DisplayName("At vehicle exiting,, check if a ticket is updated with price for loyal Customer.")
	@Test
	public void testUpdateTicketForoyalCustomer() {

		// GIVEN
		ticket.setLoyalCustomer(true);
		fareCalculatorService.calculateFare(ticket);

		// WHEN
		ticketDAO.isLoyalCustomer(vehicleRegNumber);
		ticketDAO.updateTicket(ticket);

		// THEN
		assertThat(ticketDAO.updateTicket(ticket)).isTrue();
		assertThat(ticket.getPrice()).isEqualTo(2.14);
		assertThat(ticket.getOutTimeDay()).isEqualTo(outTimeDay);

	}
}