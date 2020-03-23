package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	Duration duration;
	LocalDateTime inTimeDay;
	LocalDateTime outTimeDay;

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTimeDay() == null) || (ticket.getOutTimeDay().isBefore(ticket.getInTimeDay()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTimeDay().toString());
		}

		inTimeDay = ticket.getInTimeDay();
		outTimeDay = ticket.getOutTimeDay();

		duration = Duration.between(inTimeDay, outTimeDay);

		double durationcalcul = (duration.toMinutes() < 30) ? 0 : (duration.toMinutes() - 30);

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR:

			if (ticket.isLoyalCustomer()) {
				ticket.setPrice(((durationcalcul) * Fare.CAR_RATE_PER_MINUTE)
						- (0.05 * (durationcalcul) * Fare.CAR_RATE_PER_MINUTE));
			} else {
				ticket.setPrice((durationcalcul) * Fare.CAR_RATE_PER_MINUTE);
			}
			break;

		case BIKE:
			if (ticket.isLoyalCustomer()) {
				ticket.setPrice(((durationcalcul) * Fare.BIKE_RATE_PER_MINUTE)
						- (0.05 * (durationcalcul) * Fare.BIKE_RATE_PER_MINUTE));
			} else {
				ticket.setPrice((durationcalcul) * Fare.BIKE_RATE_PER_MINUTE);
			}
			break;

		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

}