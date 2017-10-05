package de.abd.avt.persistence.dao;


public class Elevator extends DaoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -711328056445907190L;

	private int id;

	// Da machineNumber bei mehreren Herstellern vorkommen kann, also nicht eindeutig ist, setzt sich der Primärschlüssel aus machineNumber
	// und manufacturer zusammen
	private String machineNumber;
	private String manufacturer;

	private String installationAddress;
	private String position;
	// Aufzugstype (brand) ist abhängig vom Hersteller (z.B. Sunshine35 --> Auswahl entsprechend per Ajax in View
	private String brand;
	private Customer customer;
	// Aufzugart: Seil-/Hydraulikaufzug 
	private String technique;
	private boolean equippedWithMachineRoom;
	private String typeOfBuilding;
	private String accessToBuilding;
	// ********* noch prüfen, ob das so sinnvoll ist; ggf. Pflege diverser Status in eigener Tabelle / Klasse?? **** //
	private String status;
	private Person contactPerson;

	public Elevator() {
		this.contactPerson = new Person();
		this.equippedWithMachineRoom = true;
		this.customer = new Customer();
	}
	
	public String getMachineNumber() {
		return machineNumber;
	}

	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getInstallationAddress() {
		return installationAddress;
	}

	public void setInstallationAddress(String installationAddress) {
		this.installationAddress = installationAddress;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getTechnique() {
		return technique;
	}

	public void setTechnique(String technique) {
		this.technique = technique;
	}

	public boolean isEquippedWithMachineRoom() {
		return equippedWithMachineRoom;
	}

	public void setEquippedWithMachineRoom(boolean equippedWithMachineRoom) {
		this.equippedWithMachineRoom = equippedWithMachineRoom;
	}

	public String getTypeOfBuilding() {
		return typeOfBuilding;
	}

	public void setTypeOfBuilding(String typeOfBuilding) {
		this.typeOfBuilding = typeOfBuilding;
	}

	public String getAccessToBuilding() {
		return accessToBuilding;
	}

	public void setAccessToBuilding(String accessToBuilding) {
		this.accessToBuilding = accessToBuilding;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Person getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(Person contactPerson) {
		this.contactPerson = contactPerson;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	
}