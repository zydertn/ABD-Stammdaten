package de.abd.avt.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import javax.swing.GroupLayout;

import org.apache.log4j.Logger;

import com.icesoft.faces.component.ext.HtmlSelectOneMenu;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Customergroup;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;
import de.abd.avt.sort.PersonComparator;

public class CustomergroupController extends ActionController {

	private final static Logger LOGGER = Logger.getLogger(CustomergroupController.class.getName());
	private String groupName;
	private List<Customergroup> customergroups;
	private List<Person> contactPersons;
	private HtmlSelectOneMenu groupBinding;
	private HtmlSelectOneMenu contactPersonBinding;
	private HtmlSelectOneMenu invoiceContactPersonBinding;
	
	public CustomergroupController() {
		LOGGER.info("Instantiate: CustomergroupController");
		CustomerDaoController cdc = new CustomerDaoController();
		customergroups = cdc.findCustomergroups();
//		initializeCGs(null);
	}

	public Customergroup createCustomergroup(String groupName) {
		LOGGER.info("Method: createCustomer");

		CustomerDaoController cdc = new CustomerDaoController();
		Customergroup cg = new Customergroup();
		cg.setGroupName(groupName);
		cdc.createObject(cg);
		return cg;
	}
	
	private void initializeCGs(String groupName) {
		CustomerDaoController cdc = new CustomerDaoController();
		customergroups = cdc.findCustomergroups();
		if (groupName != null) {
			cgChange(groupName);
//			// reorder Grouplist
//			for (Iterator<Customergroup> it = customergroups.iterator(); it.hasNext();) {
//				Customergroup cg = it.next();
//				if (cg.getGroupName().equals(groupName)) {
//					customergroups.remove(cg);
//					customergroups.add(0, cg);
//					break;
//				}
//			}
			groupBinding.setValue(groupName);
		
		} else {
			cgChange(customergroups.get(0).getGroupName());
		}
	}

	public void customergroupChange(ValueChangeEvent event) {
        String groupName = (String) event.getNewValue();
        if (groupName.equals("-"))
        	groupName = "";
        cgChange(groupName);
	}
	
	private void cgChange(String groupName) {
		CustomerDaoController cdc = new CustomerDaoController();
		Customergroup cg = cdc.findCustomergroup(groupName);
		if (cg != null) {
			Set<Customer> customers = cg.getCustomers();
			contactPersons = new ArrayList<Person>();
			for (Iterator<Customer> it = customers.iterator(); it.hasNext();) {
				for (Iterator<Person> it2 = it.next().getContactPersons().iterator(); it2.hasNext();) {
					contactPersons.add(it2.next());
				}
			}
			Collections.sort(contactPersons, new PersonComparator());
		} else {
			contactPersons = new ArrayList<Person>();
		}
	}
	
	public List<Customergroup> getCustomergroups() {
		HttpSession session = getSession();
		if (session.getAttribute("updateCustomergroups") != null) {
			session.removeAttribute("updateCustomergroups");
			Customer cus = (Customer) session.getAttribute("customer");
			session.removeAttribute("customer");
			initializeCGs(cus.getGroup().getGroupName());
//			contactPersons.remove(cus.getContactPerson());
//			contactPersons.add(0, cus.getContactPerson());
			if (cus.getContactPerson() != null) {
				contactPersonBinding.setLabel(cus.getContactPerson().getNameString() + " - " + cus.getContactPerson().getPhoneNr());
				contactPersonBinding.setValue(cus.getContactPerson().getId());
			}
			if (cus.getInvoiceContactPerson() != null) {
				invoiceContactPersonBinding.setLabel(cus.getInvoiceContactPerson().getNameString() + " - " + cus.getInvoiceContactPerson().getPhoneNr());
				invoiceContactPersonBinding.setValue(cus.getInvoiceContactPerson().getId());
			}
		}
		return customergroups;
	}

	public void setCustomergroups(List<Customergroup> customergroups) {
		this.customergroups = customergroups;
	}

	public List<Person> getContactPersons() {
		if (getRequest().getAttribute("updateContactPersonList") != null) {
			getRequest().removeAttribute("updateContactPersonList");
			if (getRequest().getAttribute("customergroup") != null) {
				Customergroup group = (Customergroup) getRequest().getAttribute("customergroup");
				initializeCGs(group.getGroupName());
			} else {
				initializeCGs(null);
			}
		}
		if (getRequest().getAttribute("addPendingPersonToList") != null) {
			getRequest().removeAttribute("addPendingPersonToList");
			Person p = (Person) getSession().getAttribute("personPending");
			contactPersons.add(0, p);
		}

		return contactPersons;	}

	public void setContactPersons(List<Person> contactPersons) {
		this.contactPersons = contactPersons;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public HtmlSelectOneMenu getGroupBinding() {
		return groupBinding;
	}

	public void setGroupBinding(HtmlSelectOneMenu groupBinding) {
		this.groupBinding = groupBinding;
	}

	public HtmlSelectOneMenu getContactPersonBinding() {
		return contactPersonBinding;
	}

	public void setContactPersonBinding(HtmlSelectOneMenu contactPersonBinding) {
		this.contactPersonBinding = contactPersonBinding;
	}

	public HtmlSelectOneMenu getInvoiceContactPersonBinding() {
		return invoiceContactPersonBinding;
	}

	public void setInvoiceContactPersonBinding(
			HtmlSelectOneMenu invoiceContactPersonBinding) {
		this.invoiceContactPersonBinding = invoiceContactPersonBinding;
	}

}