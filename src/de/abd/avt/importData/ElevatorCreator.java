package de.abd.avt.importData;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import de.abd.avt.model.Model;
import de.abd.avt.persistence.dao.Configuration;
import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.dao.Manufacturer;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;
import de.abd.avt.persistence.dao.controller.ElevatorDaoController;

public class ElevatorCreator {

	/**
	 * @param args
	 */
	static final Logger logger = Logger.getLogger(ElevatorCreator.class);

	public static void main(String[] args) {
		new ElevatorCreator();
	}

	public ElevatorCreator() {
		execute();
	}

	private void execute() {
		int maxI = 1000;
		ElevatorDaoController edc = new ElevatorDaoController();

		Model model = new Model();
		Configuration con = model.getConfiguration();
		List<Manufacturer> manufacturers = con.getManufacturers();
		List<String> typesOfBuilding = con.getTypesOfBuilding();
		CustomerDaoController cdc = new CustomerDaoController();
		Customer customer = cdc.findCustomer("K00001");
		Iterator<Person> c1p = customer.getContactPersons().iterator();
		Person c1p1 = c1p.next();
		Person c1p2 = c1p.next();
		Customer customer2 = cdc.findCustomer("K00002");
		Iterator<Person> c2p = customer2.getContactPersons().iterator();
		Person c2p1 = c2p.next();
		Person c2p2 = c2p.next();
		Customer customer3 = cdc.findCustomer("K00003");
		Customer customer4 = cdc.findCustomer("K00004");

		for (int i = 0; i < maxI; i++) {
			Elevator elevator = new Elevator();

			int cus = (int) ((Math.random()) * 4);
			int per = (int) ((Math.random()) * 4);
			if (cus == 1) {
				elevator.setCustomer(customer);
				if (per == 1) {
					elevator.setContactPerson(c1p1);
				} else {
					elevator.setContactPerson(c1p2);
				}
			} else if (cus == 2) {
				elevator.setCustomer(customer2);
				if (per == 1) {
					elevator.setContactPerson(c2p1);
				} else {
					elevator.setContactPerson(c2p2);
				}
			} else if (cus == 3) {
				elevator.setCustomer(customer3);
				if (per == 1) {
					elevator.setContactPerson(c1p1);
				} else {
					elevator.setContactPerson(c1p2);
				}
			} else {
				elevator.setCustomer(customer4);
				if (per == 1) {
					elevator.setContactPerson(c2p1);
				} else {
					elevator.setContactPerson(c2p2);
				}
			}

			// elevator.setCustomer(customer);

			String machineNumString = "M";
			if (i < 10) {
				machineNumString += "0000" + i;
			} else if (i < 100) {
				machineNumString += "000" + i;
			} else {
				machineNumString += "00" + i;
			}

			elevator.setMachineNumber(machineNumString);

			int manPos = (int) ((Math.random()) * manufacturers.size());
			Manufacturer manufacturer = manufacturers.get(manPos);
			elevator.setManufacturer(manufacturer.getName());

			List<String> manBrands = manufacturer.getBrands();
			elevator.setBrand(manBrands.get((int) ((Math.random()) * manBrands
					.size())));

			int technique = (int) ((Math.random()) * 2);
			if (technique == 1) {
				elevator.setTechnique("Hydraulikaufzug");
			} else {
				elevator.setTechnique("Seilaufzug");
			}

			int machineRoom = (int) ((Math.random()) * 2);
			if (machineRoom == 1) {
				elevator.setEquippedWithMachineRoom(true);
			} else {
				elevator.setEquippedWithMachineRoom(false);
			}

			int tobPos = (int) ((Math.random()) * typesOfBuilding.size());
			elevator.setTypeOfBuilding(typesOfBuilding.get(tobPos));

			if ((int) ((Math.random()) * 10) < 1) {
				int elState = (int) ((Math.random()) * con.getElevatorStates()
						.size());
				elevator.setStatus(con.getElevatorStates().get(elState));
			} else {
				elevator.setStatus("in Ordnung");
			}

			elevator.setInstallationAddress(null);

			edc.createObject(elevator);
		}
	}

}