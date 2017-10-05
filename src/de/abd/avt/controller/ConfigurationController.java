package de.abd.avt.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import de.abd.avt.model.Model;
import de.abd.avt.persistence.dao.Manufacturer;
import de.abd.avt.persistence.dao.controller.ConfigurationDaoController;
import de.abd.avt.util.ManufacturerBrand;


public class ConfigurationController extends ActionController {

	private final static Logger LOGGER = Logger.getLogger(ConfigurationController.class .getName()); 
	private String manufacturer;
	private String newManufacturer;
	private String brand;
	private String newBrand;
	private String technique;
	private String typeOfBuilding;
	private String elevatorStatus;
	private Boolean dialogOpen;
	
	public ConfigurationController() {
		LOGGER.info("Instatiate: ConfigurationController");
		dialogOpen = false;
	}

	public List<ManufacturerBrand> getManufacturerBrands() {
		List<ManufacturerBrand> mb = new ArrayList<>();
		model = new Model();
		Iterator<Manufacturer> it = model.getConfiguration().getManufacturers().iterator();
		while (it.hasNext()) {
			Manufacturer m = it.next();
			Iterator<String> brandIt = m.getBrands().iterator();
			while (brandIt.hasNext()) {
				String brand = brandIt.next();
				mb.add(new ManufacturerBrand(m.getName(), brand));
			}
		}
		return mb;
	}
	
	public List<String> getElevatorTechniques() {
		model = new Model();
		return model.getConfiguration().getTechniques();
	}

	public List<String> getTypesOfBuilding() {
		model = new Model();
		return model.getConfiguration().getTypesOfBuilding();
	}

	public List<String> getElevatorStates() {
		model = new Model();
		return model.getConfiguration().getElevatorStates();
	}

	
	public String addBrand() {
		ConfigurationDaoController cdc = new ConfigurationDaoController();
		try {
			cdc.addBrand(manufacturer, brand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "refresh";
	}

	public String addTechnique() {
		ConfigurationDaoController cdc = new ConfigurationDaoController();
		try {
			cdc.addTechnique(technique);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "refresh";
	}

	public void addManufacturer() {
		ConfigurationDaoController cdc = new ConfigurationDaoController();
		dialogOpen = false;
		try {
			cdc.addBrand(newManufacturer, newBrand);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String addTypeOfBuilding() {
		ConfigurationDaoController cdc = new ConfigurationDaoController();
		try {
			cdc.addTypeOfBuilding(typeOfBuilding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "refresh";
	}

	public String addElevatorStatus() {
		ConfigurationDaoController cdc = new ConfigurationDaoController();
		try {
			cdc.addElevatorStatus(elevatorStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "refresh";
	}
	
	public void openDialog() {
		dialogOpen = true;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getTechnique() {
		return technique;
	}

	public void setTechnique(String technique) {
		this.technique = technique;
	}

	public String getTypeOfBuilding() {
		return typeOfBuilding;
	}

	public void setTypeOfBuilding(String typeOfBuilding) {
		this.typeOfBuilding = typeOfBuilding;
	}

	public String getElevatorStatus() {
		return elevatorStatus;
	}

	public void setElevatorStatus(String elevatorStatus) {
		this.elevatorStatus = elevatorStatus;
	}

	public Boolean getDialogOpen() {
		return dialogOpen;
	}

	public void setDialogOpen(Boolean dialogOpen) {
		this.dialogOpen = dialogOpen;
	}

	public String getNewManufacturer() {
		return newManufacturer;
	}

	public void setNewManufacturer(String newManufacturer) {
		this.newManufacturer = newManufacturer;
	}

	public String getNewBrand() {
		return newBrand;
	}

	public void setNewBrand(String newBrand) {
		this.newBrand = newBrand;
	}
}