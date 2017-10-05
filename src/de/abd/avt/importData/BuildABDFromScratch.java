package de.abd.avt.importData;

import org.apache.log4j.Logger;

import de.abd.avt.util.ConfigureABD;

public class BuildABDFromScratch {

	/**
	 * @param args
	 */
	static final Logger logger = Logger.getLogger(BuildABDFromScratch.class);

	public static void main(String[] args) {
		BuildABDFromScratch c = new BuildABDFromScratch();
		c.execute();
	}

	private void execute() {
		new CountryModelCreator();
		new ConfigureABD();
		new CustomerCreator();
		new PersonCreator();
		new ElevatorCreator();
	}
}