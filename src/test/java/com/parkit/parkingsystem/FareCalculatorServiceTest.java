package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();

	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();

	}

	@DisplayName("Calculate Fare of Bike or Car")
	@Test
	public void calculate_Fare_Car() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 17, 45);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(11.63);

	}

	@DisplayName("Calculate Fare of Bike or Car")
	@Test
	public void calculate_Fare_Bike() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 17, 45);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(7.75);
	}

	@DisplayName("Calculate Fare of UnknownType")
	@Test
	public void calculate_Fare_UnkownType() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 17, 45);
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// THEN
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@DisplayName("Calculate Fare of Bike or Car with InTimeDay value is defined after ouTimeDay value")
	@Test
	public void calculate_Fare_Car_With_Future_InTimeDay() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 17, 45);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// THEN
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@DisplayName("Calculate Fare of Bike or Car with InTimeDay value is defined after ouTimeDay value")
	@Test
	public void calculate_Fare_Bike_With_Future_InTimeDay() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 17, 45);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// THEN
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@DisplayName("Calculate Fare of Bike or Car with less than one hour of Parking Time")
	@Test
	public void calculate_Fare_Car_With_Less_Than_OneHour_ParkingTime() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 11, 25);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0.63);
	}

	@DisplayName("Calculate Fare of Bike or Car with less than one hour of Parking Time")
	@Test
	public void calculate_Fare_Bike_With_Less_Than_OneHour_ParkingTime() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 11, 25);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0.42);
	}

	@DisplayName("Calculate Fare of Bike or Car with less than a half hour of Parking Time")
	@Test
	public void calculate_Fare_Car_With_Less_Than_OneHalfHour_ParkingTime() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 10, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0);
	}

	@DisplayName("Calculate Fare of Bike or Car with less than a half hour of Parking Time")
	@Test
	public void calculate_Fare_Bike_With_Less_Than_OneHalfHour_ParkingTime() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 10, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0);
	}

	@DisplayName("Calculate Fare of Bike or Car with more than a day of Parking Time")
	@Test
	public void calculate_Fare_Car_With_More_Than_A_Day_ParkingTime() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 16, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(36.75);
	}

	@DisplayName("Calculate Fare of Bike or Car with more than a day of Parking Time")
	@Test
	public void calculate_Fare_Bike_With_More_Than_A_Day_ParkingTime() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 16, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(24.50);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different months")
	@Test
	public void calculate_Fare_Car_Between_Two_Months() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 30, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 05, 02, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(72.75);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different months")
	@Test
	public void calculate_Fare_Bike_Between_Two_Months() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 30, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 05, 02, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(48.50);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different years")
	@Test
	public void calculate_Fare_Car_Between_Two_Years() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 12, 30, 20, 45);
		LocalDateTime outTimeDay = LocalDateTime.of(2021, 01, 01, 05, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(49);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different years")
	@Test
	public void calculate_Fare_Bike_Between_Two_Years() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 12, 30, 20, 45);
		LocalDateTime outTimeDay = LocalDateTime.of(2021, 01, 01, 05, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(32.67);
	}

	@DisplayName("Calculate Fare when Customer is loyal")
	@Test
	public void calculate_Fare_Car_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 17, 45);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(11.04);

	}

	@DisplayName("Calculate Fare when Customer is loyal")
	@Test
	public void calculate_Fare_Bike_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 17, 45);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(7.36);
	}

	@DisplayName("Calculate Fare of Bike or Car with less than one hour of Parking Time when Customer is loyal")
	@Test
	public void calculate_Fare_Car_With_Less_Than_One_Hour_ParkingTime_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 11, 25);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0.59);
	}

	@DisplayName("Calculate Fare of Bike or Car with less than one hour of Parking Time when Customer is loyal")
	@Test
	public void calculate_Fare_Bike_With_Less_Than_OneHour_ParkingTime_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 11, 25);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0.40);
	}

	@DisplayName("Calculate Fare of Bike or Car with less than a half hour of Parking Time, when Customer is loyal")
	@Test
	public void calculate_Fare_Car_With_Less_Than_OneHalfHour_ParkingTime_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 10, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0);
	}

	@DisplayName("Calculate Fare of Bike or Car with less than a half hour of Parking Time, when Customer is loyal")
	@Test
	public void calculate_Fare_Bike_With_Less_Than_OneHalfHour_ParkingTime_For_Loyal_Custome() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 10, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 15, 10, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(0);
	}

	@DisplayName("Calculate Fare of Bike or Car with more than a day of Parking Time, when Customer is loyal")
	@Test
	public void calculate_Fare_Car_With_More_Than_ADay_ParkingTime_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 16, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(34.91);
	}

	@DisplayName("Calculate Fare of Bike or Car with more than a day of Parking Time, when Customer is loyal")
	@Test
	public void calculate_Fare_Bike_With_More_Than_ADay_ParkingTime_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 15, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 04, 16, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// THEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(23.28);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different months, when Customer is loyal")
	@Test
	public void calculate_Fare_Car_Between_Two_Months_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 30, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 05, 02, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// GIVEN
		assertThat(ticket.getPrice()).isEqualTo(69.11);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different months, when Customer is loyal")
	@Test
	public void calculate_Fare_Bike_Between_Two_Months_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 04, 30, 9, 30);
		LocalDateTime outTimeDay = LocalDateTime.of(2020, 05, 02, 10, 30);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// GIVEN
		assertThat(ticket.getPrice()).isEqualTo(46.08);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different years,when Customer is loyal")
	@Test
	public void calculate_Fare_Car_Between_Two_Years_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 12, 30, 20, 45);
		LocalDateTime outTimeDay = LocalDateTime.of(2021, 01, 01, 05, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(46.55);
	}

	@DisplayName("Calculate Fare of Bike or Car between two different years,when Customer is loyal")
	@Test
	public void calculate_Fare_Bike_Between_Two_Years_For_Loyal_Customer() {

		// GIVEN
		LocalDateTime inTimeDay = LocalDateTime.of(2020, 12, 30, 20, 45);
		LocalDateTime outTimeDay = LocalDateTime.of(2021, 01, 01, 05, 55);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
		boolean loyalCustomer = true;
		ticket.setInTimeDay(inTimeDay);
		ticket.setOutTimeDay(outTimeDay);
		ticket.setParkingSpot(parkingSpot);
		ticket.setLoyalCustomer(loyalCustomer);

		// WHEN
		fareCalculatorService.calculateFare(ticket);

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(31.03);
	}
}