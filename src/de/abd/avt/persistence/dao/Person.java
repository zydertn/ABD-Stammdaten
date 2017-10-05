package de.abd.avt.persistence.dao;

import java.util.Set;

public class Person extends DaoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8754205128095926999L;
	private int id;
	private String name;
	private String firstname;
	private String gender;
	private String phoneNr;
	private String email;
	private String de_mail;
	private Customer customer;
	private Set<Elevator> elevators;
	
	public Person() {
		this.name = "";
		this.firstname = "";
		this.gender = "Herr";
		this.phoneNr = "";
		this.email = "";
		this.de_mail = "";
	}

	public String getNameString() {
		String nameS = "";
		if (gender != null)
			nameS += gender + " ";
		if (firstname != null)
			nameS += firstname + " ";
		if (name != null)
			nameS += name;
		return nameS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDe_mail() {
		return de_mail;
	}

	public void setDe_mail(String de_mail) {
		this.de_mail = de_mail;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Elevator> getElevators() {
		return elevators;
	}

	public void setElevators(Set<Elevator> elevators) {
		this.elevators = elevators;
	}

	public String getPhoneNr() {
		return phoneNr;
	}

	public void setPhoneNr(String phoneNr) {
		this.phoneNr = phoneNr;
	}

}