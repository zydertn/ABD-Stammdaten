package de.abd.avt.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.ElevatorDaoController;

public class SearchController extends ActionController {

	private final static Logger LOGGER = Logger.getLogger(SearchController.class.getName()); 
	protected List<Elevator> elevators;
	protected List<SelectItem> machineNumberSI;
	protected Elevator elevator;
	protected ElevatorDaoController edc;
	protected Boolean dialogOpen;
	protected String elevatorString;
	protected boolean componentDisabled;
	protected String address;
	protected boolean elevatorFound;


	public SearchController() {
		LOGGER.info("Instatiate: ElevatorController");
		edc = new ElevatorDaoController();
		elevatorFound = false;
	}
	
	public void manufacturerChange(ValueChangeEvent event) {
		System.out.println("***** ManufacturerChange *****");
        String manufacturer = (String) event.getNewValue();
		ElevatorDaoController edc = new ElevatorDaoController();

		try {
			this.elevators = edc.searchElevators(manufacturer, "", true);
			Iterator<Elevator> it = this.elevators.iterator();
			machineNumberSI = new ArrayList<SelectItem>();
			while (it.hasNext()) {
				machineNumberSI.add(new SelectItem(it.next().getMachineNumber()));
			}
		} catch (Exception e) {
	        System.out.println("******************manufacturerChange Exception****************");
			e.printStackTrace();
		}
	}
	
	protected void machineNumberChange(ValueChangeEvent event) {
        String value = (String) event.getNewValue();
        if (this.elevators != null && value.length() > 0) {
            Iterator<Elevator> it = this.elevators.iterator();
            while (it.hasNext()) {
            	Elevator el = it.next();
            	if (el.getMachineNumber().equals(value)) {
            		this.elevator = el;
            		elevatorFound = true;
            		this.componentDisabled = false;
            		if (el.getContactPerson() == null) {
                		// Person muss gesetzt werden, falls Person null ist --> sonst gibt es einen Null-Pointer
                		this.elevator.setContactPerson(new Person());
            		}
            		this.address = el.getInstallationAddress();
            		break;
            	}
            }
        }
	}
	
	public void searchElevatorsByAddress() {
		elevators = edc.searchElevatorsByAddress(elevator.getInstallationAddress());
		if (elevators.size() > 1) {
			dialogOpen = true;
		} else if (elevators.size() == 1) {
			elevator = elevators.get(0);
		} else {
			getRequest().setAttribute("message", "Kein Aufzug gefunden!");
		}
	}
	
	public void closeDialog() {
		Iterator<Elevator> it = elevators.iterator();
		while (it.hasNext()) {
			Elevator el = it.next();
			if (el.toString().equals(elevatorString)) {
				elevator = el;
				break;
			} else {
				System.out.println("List element: " + el.toString());
				System.out.println("elevatorString: " + elevatorString);
			}
		}
		dialogOpen = false;
	}
	
	public List<Elevator> getElevators() {
		return elevators;
	}

	public void setElevators(List<Elevator> elevators) {
		this.elevators = elevators;
	}
	
	public List<SelectItem> getMachineNumberSI() {
		return machineNumberSI;
	}

	public void setMachineNumberSI(List<SelectItem> machineNumberSI) {
		this.machineNumberSI = machineNumberSI;
	}
	
	public Elevator getElevator() {
		return elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}
	
	public Boolean getDialogOpen() {
		return dialogOpen;
	}

	public void setDialogOpen(Boolean dialogOpen) {
		this.dialogOpen = dialogOpen;
	}
	
	public String getElevatorString() {
		return elevatorString;
	}

	public void setElevatorString(String elevatorString) {
		this.elevatorString = elevatorString;
	}
	
	public boolean isComponentDisabled() {
		return componentDisabled;
	}

	public void setComponentDisabled(boolean componentDisabled) {
		this.componentDisabled = componentDisabled;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


}
