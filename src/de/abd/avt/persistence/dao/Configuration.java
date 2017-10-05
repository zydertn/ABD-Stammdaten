package de.abd.avt.persistence.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration extends DaoObject {

	/**
	 * 
	 */
	/**
	 * 
	 */
	private static final long serialVersionUID = 3429161913114726492L;
//	private Map<Integer, Double> simPrices = new HashMap<Integer, Double>();
//	private Map<Integer, Double> dataOptionPrices = new HashMap<Integer, Double>();
//	private Map<Integer, String> sortingOptions = new HashMap<Integer, String>();
	private int id;
//	private int reportProgress;
//	private int customer;
//	private long lastReportUpdate;
	// manufacturers sollten in DB gespeichert werden, da via Konfiguration weitere hinzu kommen
	private List<Manufacturer> manufacturers;
	// Aufzugstype (brand) ist abhängig vom Hersteller (z.B. Sunshine35) und sollte in DB gespeichert werden, da via Konfiguration
	// weitere hinzu kommen
	private Map<String, Set<String>> brands;
	// elevatorStates sollten in DB gespeichert werden, da via Konfiguration weitere hinzu kommen
	private List<String> elevatorStates;
	// Stand jetzt sind nur zwei techniques geplant... evtl. kommen später aber doch weitere hinzu, daher konfigurierbar
	private List<String> techniques;
	// typesOfBuilding sollten in DB gespeichert werden, da via Konfiguration weitere hinzu kommen
	private List<String> typesOfBuilding;


	public Configuration() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(List<Manufacturer> manufacturers) {
		this.manufacturers = manufacturers;
	}



	public List<String> getElevatorStates() {
		return elevatorStates;
	}

	public void setElevatorStates(List<String> elevatorStates) {
		this.elevatorStates = elevatorStates;
	}

//	public Map<String, Set<String>> getBrands() {
//		return brands;
//	}
//
//	public void setBrands(Map<String, Set<String>> brands) {
//		this.brands = brands;
//	}

	public List<String> getTechniques() {
		return techniques;
	}

	public void setTechniques(List<String> techniques) {
		this.techniques = techniques;
	}

	public List<String> getTypesOfBuilding() {
		return typesOfBuilding;
	}

	public void setTypesOfBuilding(List<String> typesOfBuilding) {
		this.typesOfBuilding = typesOfBuilding;
	}



/* ABD-AVT auskommentiert
	public Map<Integer, Double> getSimPrices() {
		return simPrices;
	}

	public void setSimPrices(Map<Integer, Double> simPrices) {
		this.simPrices = simPrices;
	}


	public Map<Integer, Double> getDataOptionPrices() {
		return dataOptionPrices;
	}

	public void setDataOptionPrices(Map<Integer, Double> dataOptionPrices) {
		this.dataOptionPrices = dataOptionPrices;
	}

	public int getReportProgress() {
		return reportProgress;
	}

	public void setReportProgress(int reportProgress) {
		this.reportProgress = reportProgress;
	}

	public int getCustomer() {
		return customer;
	}

	public void setCustomer(int customer) {
		this.customer = customer;
	}

	public long getLastReportUpdate() {
		return lastReportUpdate;
	}

	public void setLastReportUpdate(long lastReportUpdate) {
		this.lastReportUpdate = lastReportUpdate;
	}

	public Map<Integer, String> getSortingOptions() {
		return sortingOptions;
	}

	public void setSortingOptions(Map<Integer, String> sortingOptions) {
		this.sortingOptions = sortingOptions;
	}
*/
}
