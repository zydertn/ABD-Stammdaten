package de.abd.avt.persistence.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Customer extends DaoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -711328056445907190L;
	private int id;
	private String name;
	private String branch;
	
	private String address;
	private String invoiceAddress;

	private Person contactPerson;
	private Person invoiceContactPerson;
	private String customernumber;
	private String paymentMethod;
	private String comment;
	private Date lastCalculationDate;
/* AUSKOMMENTIERT ABD-AVT
	private Map<String, Bill> bills;
 */
	private String vatNumber;
	private Country country;
	private Customergroup group;
	
	private Set<Elevator> elevators = 
			new HashSet<Elevator>();
	private Set<Person> contactPersons = 
			new HashSet<Person>();
	
	private InvoiceConfiguration invoiceConfiguration;

	public Customer() {
		this.name = "";
		this.branch = "";
		this.contactPerson = new Person();
		this.customernumber = "";
		this.comment = "";
		this.invoiceConfiguration = new InvoiceConfiguration();
		this.paymentMethod = "";
		this.vatNumber = "";
	}
	
	public String getListString() {
//		String s = name + " - " + branch + "; " + address.getAddressString();
		String st = new String(name + " - " + branch);
		if (address != null)
			st += "; " + address;
		
		if (st.length() > 92) {
			st = st.substring(0, 91);
		}
		st += "; " + customernumber;
		st = st.replaceAll("&", " ");
		return st;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(Person contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getCustomernumber() {
		return customernumber;
	}

	public void setCustomernumber(String customernumber) {
		this.customernumber = customernumber;
	}

	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public InvoiceConfiguration getInvoiceConfiguration() {
		return invoiceConfiguration;
	}

	public void setInvoiceConfiguration(InvoiceConfiguration invoiceConfiguration) {
		this.invoiceConfiguration = invoiceConfiguration;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getLastCalculationDate() {
		return lastCalculationDate;
	}

	public void setLastCalculationDate(Date lastCalculationDate) {
		this.lastCalculationDate = lastCalculationDate;
	}

/* AUSKOMMENTIERT ABD-AVT
	public Map<String, Bill> getBills() {
		return bills;
	}

	public void setBills(Map<String, Bill> bills) {
		this.bills = bills;
	}
 */

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Set<Elevator> getElevators() {
		return elevators;
	}

	public void setElevators(Set<Elevator> elevators) {
		this.elevators = elevators;
	}

	public Set<Person> getContactPersons() {
		return contactPersons;
	}

	public void setContactPersons(Set<Person> contactPersons) {
		this.contactPersons = contactPersons;
	}

	public Customergroup getGroup() {
		return group;
	}

	public void setGroup(Customergroup group) {
		this.group = group;
	}

	public Person getInvoiceContactPerson() {
		return invoiceContactPerson;
	}

	public void setInvoiceContactPerson(Person invoiceContactPerson) {
		this.invoiceContactPerson = invoiceContactPerson;
	}
	
}