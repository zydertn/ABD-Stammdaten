package de.abd.avt.controller;


import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlInputHidden;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;
import de.abd.avt.persistence.dao.controller.PersonDaoController;


public class PersonController extends ActionController {

	private final static Logger LOGGER = Logger.getLogger(PersonController.class.getName()); 
	private Person person;
	private Boolean dialogOpen;
	private HtmlInputHidden hiddenCustomerNumber;

	public PersonController() {
		LOGGER.info("Instatiate: PersonController");
		dialogOpen = false;
		person = new Person();
	}

	public void createPerson() {
		PersonDaoController pdc = new PersonDaoController();
		CustomerDaoController cdc = new CustomerDaoController();
		if (hiddenCustomerNumber != null && hiddenCustomerNumber.getValue() != null) {
			String hCusNum = hiddenCustomerNumber.getValue().toString();
			Customer customer = cdc.findCustomer(hCusNum);
			person.setCustomer(customer);
			String retMessage = pdc.createObject(person);
			if (retMessage.length() == 0) {
				getRequest().setAttribute("message", "Neue Person " + person.getFirstname() + " " + person.getName() + " wurde angelegt und dem Kunde " + customer.getCustomernumber() + " zugeordnet.");
				getRequest().setAttribute("updateContactPersonList", true);
				getRequest().setAttribute("customerNumber", customer.getCustomernumber());
			} else {
				getRequest().setAttribute("message", retMessage);
			}
		} else {
			getRequest().setAttribute("addPendingPersonToList", true);
			getSession().setAttribute("personPending", person);
//			getRequest().setAttribute("message", "Keine Zuordnung zu Kunde möglich. Person noch nicht angelegt!");
		}
		
		closeDialog();
	}
	
	public void openDialog() {
		person = new Person();
		dialogOpen = true;
	}
	
	private void closeDialog() {
		dialogOpen = false;
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Boolean getDialogOpen() {
		return dialogOpen;
	}

	public void setDialogOpen(Boolean dialogOpen) {
		this.dialogOpen = dialogOpen;
	}

	public HtmlInputHidden getHiddenCustomerNumber() {
		return hiddenCustomerNumber;
	}

	public void setHiddenCustomerNumber(HtmlInputHidden hiddenCustomerNumber) {
		this.hiddenCustomerNumber = hiddenCustomerNumber;
	}
	
}