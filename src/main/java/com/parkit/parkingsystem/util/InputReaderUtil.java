package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputReaderUtil {

	Scanner scan = new Scanner(System.in, "UTF-8");

	private static final Logger logger = LogManager.getLogger("InputReaderUtil");

	public int readSelection() {
		try {

			return Integer.parseInt(scan.nextLine());
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}

	public String readVehicleRegistrationNumber() {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0
					|| vehicleRegNumber.trim().length() > 7) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter a valid string for vehicle registration number");

		}
		return null;
	}

}