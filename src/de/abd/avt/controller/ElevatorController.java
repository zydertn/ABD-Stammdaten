package de.abd.avt.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.DaoObject;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.dao.Manufacturer;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;
import de.abd.avt.persistence.dao.controller.ElevatorDaoController;
import de.abd.avt.persistence.dao.controller.PersonDaoController;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;
import de.abd.avt.sort.PersonComparator;
import de.abd.avt.util.ObjectValidityChecker;


public class ElevatorController extends SearchController {

	private final static Logger LOGGER = Logger.getLogger(ElevatorController.class.getName()); 
	private String countryName;
	private List<Customer> customerList;
	private String customernumber;
	private ObjectValidityChecker ovc;
	private List<String> manufacturerBrands;
	private List<Person> contactPersons;
	private String contactPersonID;
	private String hiddenAddress;
	
	public ElevatorController() {
		LOGGER.info("Instatiate: ElevatorController");

		elevator = new Elevator();
		componentDisabled = true;
		
		ovc = new ObjectValidityChecker();
		
		List<Manufacturer> manufacturers = model.getConfiguration().getManufacturers();
		Manufacturer manufacturer = manufacturers.iterator().next();
		this.manufacturerBrands = new ArrayList<>(manufacturer.getBrands());
		Collections.sort(this.manufacturerBrands);
		
		initiateCustomerList();
	}
	
	private void initiateCustomerList() {
		CustomerDaoController cdc = new CustomerDaoController();
		List<DaoObject> cusDaoList = cdc.listObjects();
		Iterator<DaoObject> it = cusDaoList.iterator();
		customerList = new ArrayList<Customer>();
		String customerNumber = null;
		Set<Person> pers = null;
		// if person has been added, customernumber visible in view will still be the same. 
		// persons of this previous chosen customer must be shown
		if (getRequest().getAttribute("customerNumber") != null) {
			customerNumber = (String) getRequest().getAttribute("customerNumber");
		}

		// if no customer is pre-selected, persons of first customer must be shown
		if (it.hasNext()) {
			Customer firstCus = (Customer) it.next();
			customerList.add(firstCus);
			if (customerNumber != null) {
				pers = checkCurrentCustomer(customerNumber, firstCus);
			} else {
				pers = firstCus.getContactPersons();
			}

				
		}
		while(it.hasNext()) {
			Customer customer = (Customer) it.next();
			if (customerNumber != null && pers == null) {
				pers = checkCurrentCustomer(customerNumber, customer);
			}
			customerList.add(customer);
		}
		
		contactPersons = new ArrayList<Person>();
		for (Iterator<Person> iter = pers.iterator(); iter.hasNext();) {
			contactPersons.add(iter.next());
		}
		Collections.sort(contactPersons, new PersonComparator());
	}
	
	private Set<Person> checkCurrentCustomer(String customerNumber, Customer customer) {
		if (customer.getCustomernumber().equals(customerNumber)) {
			return customer.getContactPersons();
		}
		return null;
	}

	public void createElevator() {
		LOGGER.info("Method: createElevator");

		String message = "";
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			if (elevator.getMachineNumber() != null
					&& elevator.getMachineNumber().length() > 0
					&& elevator.getManufacturer() != null
					&& elevator.getManufacturer().length() > 0) {
			} else {
				message = "Die Felder Maschinennummer und Hersteller müssen befüllt sein!";
				LOGGER.warn(message);
				getRequest().setAttribute("message", message);
				return;
			}
		} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("HibernateException: Error rolling back transaction; "+ e);
				}
				// throw again the first exception
				throw e;
			}

		}

	
		CustomerDaoController cdc = new CustomerDaoController();
		Customer customer = cdc.findCustomer(customernumber);
		if (customer != null) {
			elevator.setCustomer(customer);
		}
		
		// initiale Checks, um Leereinträge in der DB zu verhindern; Eintrag soll nur vorgenommen werden,
		// wenn Schlüsselinformationen vorliegen --> muss bei UpdateElevator wieder geprüft werden
		if (!ovc.checkPersonIsValid(elevator.getContactPerson())) elevator.setContactPerson(null);
		PersonDaoController pdc = new PersonDaoController();
        Person p = pdc.findPerson(Integer.parseInt(contactPersonID));
        elevator.setContactPerson(p);
		
		ElevatorDaoController edc = new ElevatorDaoController();
		String retMessage = edc.createObject(elevator);
		if (retMessage != null && retMessage.length() == 0) {
			
			message = "Neue Aufzuganlage wurde erfolgreich angelegt! Maschinennummer:"
					+ elevator.getMachineNumber() + "; Hersteller: "
					+ elevator.getManufacturer();

			getRequest().setAttribute("message", message);
			LOGGER.info(message);
		} else {
			getRequest().setAttribute("message", retMessage);
			LOGGER.warn(retMessage);
		}
		tx.commit();
		elevator = new Elevator();
	}

	public void machineNumberChange(ValueChangeEvent event) {
		super.machineNumberChange(event);
		if (elevatorFound) {
			customerList = new ArrayList<Customer>();
			ElevatorDaoController edc = new ElevatorDaoController();
			// lazy load customer
			Elevator dbElevator = edc.fetchElevator(elevator.getId());
			Person p = dbElevator.getContactPerson();
			Customer customer = dbElevator.getCustomer();
			customerList.add(customer);
			contactPersons = new ArrayList<Person>();
			customer.getContactPersons();
			for (Iterator<Person> it = customer.getContactPersons().iterator(); it.hasNext();) {
				Person per = it.next();
				if (p.getId() != per.getId()) {
					contactPersons.add(per);
				}
			}
			Collections.sort(contactPersons, new PersonComparator());
			contactPersons.add(0, p);
		}
	}
	
	public void manufacturerChange(ValueChangeEvent event) {
		super.manufacturerChange(event);

		Manufacturer man = getManufacturer((String) event.getNewValue());
		if (man != null) {
			manufacturerBrands = man.getBrands();
		}
	}
	
	public void customerChange(ValueChangeEvent event) {
//		super.customerChange(event);
        String customerNumber = (String) event.getNewValue();
		CustomerDaoController cdc = new CustomerDaoController();

		Customer customer = cdc.findCustomer(customerNumber);
		Set<Person> persons = customer.getContactPersons();
		contactPersons = new ArrayList<Person>();
		for (Iterator<Person> it = persons.iterator(); it.hasNext();) {
			contactPersons.add(it.next());
		}
		Collections.sort(contactPersons, new PersonComparator());
	}
	
	private Manufacturer getManufacturer(String manufacturer) {
		List<Manufacturer> manufacturers = model.getConfiguration().getManufacturers();
		Iterator<Manufacturer> it = manufacturers.iterator();
		while (it.hasNext()) {
			Manufacturer man = it.next();
			if (man.getName().equals(manufacturer)) {
				return man;
			}
		}
		return null;
	}

/*	public void personChange(ValueChangeEvent event) {
//		super.personChange(event);
        Integer personID = Integer.parseInt((String) event.getNewValue());
        PersonDaoController pdc = new PersonDaoController();
        Person p = pdc.findPerson(personID);
        elevator.setContactPerson(p);
	}
*/	
	public void updateElevatorAction() {
		LOGGER.info("Method: updateElevatorAction;");
		updateElevator();
	}
	
	public String updateElevator() {
		LOGGER.info("Method: updateElevator");
		ElevatorDaoController edc = new ElevatorDaoController();
		try {
			edc.updateElevator(elevator);
		} catch (Exception e) {
			getRequest().setAttribute("message", e.getMessage());		
		}

		getRequest().setAttribute("message", "Aufzug in Datenbank aktualisiert!");
		return "";
	}

	public String deleteElevatorAction() {
		LOGGER.info("Method: deleteElevatorAction; Elevator: " + elevator.getManufacturer() + ", " + elevator.getMachineNumber());
		ElevatorDaoController controller = new ElevatorDaoController();
		try {
			controller.deleteObject(elevator);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			getRequest().setAttribute("message", "Fehler: Aufzug konnte NICHT gelöscht werden!");
		}

		getRequest().setAttribute("message", "Aufzug wurde erfolgreich gelöscht!");
		return "";
	}

	public void searchElevator() {
		LOGGER.info("Method: searchElevator; Hersteller: " + elevator.getManufacturer() + "; Maschinennummer: " + elevator.getMachineNumber());
		ElevatorDaoController edc = new ElevatorDaoController();
		try {
			elevator = edc.searchElevator(elevator.getManufacturer(), elevator.getMachineNumber(), true);
			if (elevator.getContactPerson() == null) elevator.setContactPerson(new Person());

			componentDisabled = false;
			customernumber = elevator.getCustomer().getCustomernumber();
			this.address = elevator.getInstallationAddress();
		} catch (Exception e) {
			getRequest().setAttribute("message", e.getMessage());
		}

	}

	public String createElevatorNext() {
		LOGGER.info("Method: createElevatorNext");
		createElevator();
		elevator = new Elevator();
		return null;
	}

	public String createElevatorFinish() {
		LOGGER.info("Method: createElevatorFinish");
		createElevator();
		elevator = new Elevator();
		return "finish";
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	public String getCustomernumber() {
		return customernumber;
	}

	public void setCustomernumber(String customernumber) {
		this.customernumber = customernumber;
	}

	public List<String> getManufacturerBrands() {
		return manufacturerBrands;
	}

	public void setManufacturerBrands(List<String> manufacturerBrands) {
		this.manufacturerBrands = manufacturerBrands;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.elevator.setInstallationAddress(address);
		this.address = address;
	}

	public String getHiddenAddress() {
		return hiddenAddress;
	}

	public void setHiddenAddress(String hiddenAddress) {
		this.hiddenAddress = hiddenAddress;
	}

	public List<Person> getContactPersons() {
		if (getRequest().getAttribute("updateContactPersonList") != null) {
			getRequest().removeAttribute("updateContactPersonList");
			initiateCustomerList();
		}
		return contactPersons;
	}

	public void setContactPersons(List<Person> contactPersons) {
		this.contactPersons = contactPersons;
	}

	public String getContactPersonID() {
		return contactPersonID;
	}

	public void setContactPersonID(String contactPersonID) {
		this.contactPersonID = contactPersonID;
	}


}