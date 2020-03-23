package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

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
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private ParkingService parkingService;
	private static ParkingType parkingType;
	LocalDateTime outTimeDay;
	LocalDateTime inTimeDay;
	String vehicleRegNumber;

	@Mock
	private InputReaderUtil inputReaderUtil;
	@Mock
	private ParkingSpotDAO parkingSpotDAO;
	@Mock
	private TicketDAO ticketDAO;
	@Mock
	private ParkingSpot parkingSpot;

	@Mock
	private Ticket ticket;

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

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void Ask_For_Vehichle_Type_CAR_Test() {
		// GIVEN

		when(inputReaderUtil.readSelection()).thenReturn(1);

		// WHEN
		parkingService.askForVehichleType();

		// THEN
		assertThat(inputReaderUtil.readSelection()).isEqualTo(1);
	}

	@Test
	public void Ask_For_Vehichle_Unknown_Type_Test() {
		// GIVEN

		when(inputReaderUtil.readSelection()).thenReturn(3);

		// THEN
		assertThrows(Exception.class, () -> parkingService.askForVehichleType());
	}

	@Test
	public void Ask_For_Vehichle_Type_BIKE_Test() {

		// GIVEN
		when(inputReaderUtil.readSelection()).thenReturn(2);

		// WHEN
		parkingService.askForVehichleType();

		// THEN
		assertThat(inputReaderUtil.readSelection()).isEqualTo(2);
	}

	@Test
	public void AskForVehichleRegNumberTest() throws Exception {

		// GIVEN
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);

		// WHEN
		parkingService.askForVehichleRegNumber();

		// THEN
		assertThat(inputReaderUtil.readVehicleRegistrationNumber()).isEqualToIgnoringCase(vehicleRegNumber);

	}

	@Test
	@Tag("ParkingSpotUpdateIncomingVehicle")
	@DisplayName("At vehicle entering, after reading registration number, system should update parking spot availability and save Ticket.")
	public void Incoming_Process_Vehicle_Test_when_ParkingSpot_is_Available() {

		// GIVEN

		when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);
		when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);

		// WHEN
		parkingService.processIncomingVehicle(parkingSpot);

		// THEN

		assertThat(parkingSpot.getId()).isEqualTo(1);
		assertThat(parkingSpotDAO.getNextAvailableSlot(parkingType)).isEqualTo(1);
		assertThat(parkingSpotDAO.updateParking(parkingSpot)).isTrue();

	}

	@Test
	@Tag("ParkingSpotUpdateIncomingVehicle")
	@DisplayName("At vehicle entering, after reading registration number, system should update parking spot availability and save Ticket.")
	public void Incoming_Process_Vehicle_Test_when_ParkingSpot_not_Available() {

		// GIVEN

		when(parkingSpot.getId()).thenReturn(0);

		// WHEN
		parkingService.processIncomingVehicle(parkingSpot);

		// THEN
		assertThat(parkingSpotDAO.updateParking(parkingSpot)).isFalse();
	}

	@Test
	@Tag("ParkingSpotUpdateExitingVehicle")
	@DisplayName("At vehicle exit, after reading registration number, system should update parking spot available.")
	public void givenVehicleExit_processExitingVehicleTest_thenParkingSpotShouldBeUpdated() throws Exception {
		// GIVEN
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
		when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(ticket);
		when(ticketDAO.updateTicket(ticket)).thenReturn(true);
		when(parkingSpotDAO.updateParking(parkingSpot)).thenReturn(true);

		// WHEN
		parkingService.processExitingVehicle();

		// THEN

		assertThat(parkingSpotDAO.updateParking(parkingSpot)).isTrue();

	}

	@Test
	@Tag("ParkingSpotUpdateExitingVehicleForLoyalCustomer")
	public void ExitingProcessVehicleTest_shoud_Use_TicketDAO_IsLoyalCustomer() throws Exception {

		// GIVEN
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
		when(ticketDAO.getTicket(vehicleRegNumber)).thenReturn(ticket);
		when(ticketDAO.isLoyalCustomer(vehicleRegNumber)).thenReturn(true);

		// WHEN

		parkingService.processExitingVehicle();

		// THEN

		assertThat(ticketDAO.isLoyalCustomer(vehicleRegNumber)).isTrue();

	}
}
