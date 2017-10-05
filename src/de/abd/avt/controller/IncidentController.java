package de.abd.avt.controller;

import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;

import de.abd.avt.persistence.dao.Elevator;


public class IncidentController extends SearchController {

	private final static Logger LOGGER = Logger.getLogger(IncidentController.class.getName()); 
	private String incidentNr;
	private String address,types;
	private String url = "https://maps.google.com/maps/place";
	private String initaddress;
	
	
	public IncidentController() {
		LOGGER.info("Instatiate: ElevatorController");
			initaddress = "Max-Eyth-Straße 35, 71088 Holzgerlingen, Deutschland";
			elevator = new Elevator();
	}

	
/*
	public void search() {
		LOGGER.info("Method: IncidentController.search;");
		ElevatorDaoController edc = new ElevatorDaoController();
		try {
			this.elevators = edc.searchElevators(elevator.getManufacturer(), elevator.getMachineNumber(), true);
			
			if (this.elevators.size() == 1) {
				this.elevator = this.elevators.get(0);
				return;
			}
			
//			componentDisabled = false;
//			customernumber = elevator.getCustomer().getCustomernumber();
			if (this.elevators.size() > 0) {
				dialogOpen = true;
			} else {
				getRequest().setAttribute("message", "Kein Aufzug gefunden!");
			}
		} catch (Exception e) {
			getRequest().setAttribute("message", e.getMessage());
		}
	}
*/
	
	public void machineNumberChange(ValueChangeEvent event) {
		super.machineNumberChange(event);
	}
	
	public String getIncidentNr() {
		return incidentNr;
	}

	public void setIncidentNr(String incidentNr) {
		this.incidentNr = incidentNr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.elevator.setInstallationAddress(address);
		this.address = address;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInitaddress() {
		return initaddress;
	}

	public void setInitaddress(String initaddress) {
		this.initaddress = initaddress;
	}

}