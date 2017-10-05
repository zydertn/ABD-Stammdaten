package de.abd.avt.persistence.dao;

import java.util.Set;


public class Customergroup extends DaoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -711328056445907190L;
	private String groupName;
 	private Set<Customer> customers;

	
	public Customergroup() {
	}

	public Set<Customer> getCustomers() {
		return customers;
	}


	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}