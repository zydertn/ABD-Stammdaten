package de.abd.avt.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.icesoft.faces.component.ext.HtmlInputHidden;
import com.icesoft.faces.component.ext.HtmlSelectOneListbox;
import com.icesoft.faces.component.ext.HtmlSelectOneMenu;

import de.abd.avt.persistence.dao.Bill;
import de.abd.avt.persistence.dao.Country;
import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Customergroup;
import de.abd.avt.persistence.dao.DaoObject;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.dao.InvoiceConfiguration;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.CountryDaoController;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;
import de.abd.avt.persistence.dao.controller.PersonDaoController;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;
import de.abd.avt.report.CustomerReportGenerator;
import de.abd.avt.sort.CustomerNumberComparator;
import de.abd.avt.util.FacesUtil;
import de.abd.avt.util.ObjectValidityChecker;

public class CustomerController extends ActionController {

	private final static Logger LOGGER = Logger.getLogger(CustomerController.class .getName()); 

	private boolean opened = false;
	private List<Customer> customerList;
	private String selectedCustomer;
	@SuppressWarnings("unused")
	private boolean componentDisabled = true;
	private String countryName;
	private List<Bill> bills;
	private ObjectValidityChecker ovc;
	private List<Elevator> elevatorList;
	private String relation;
	private String address;
	private String invoiceAddress;
	private String contactPersonID;
	private String invoicePersonID;
	private String groupName;
	
	public CustomerController() {
		LOGGER.info("Instantiate: CustomerController");
		customer = new Customer();
		customerList = findAllCustomers();
		ovc = new ObjectValidityChecker();
	}
	
	public void createCustomer() {
		LOGGER.info("Method: createCustomer");
		
		// initiale Checks, um Leereinträge in der DB zu verhindern; Eintrag soll nur vorgenommen werden,
		// wenn Schlüsselinformationen vorliegen --> muss bei UpdateCustomer wieder geprüft werden
		if (!ovc.checkPersonIsValid(customer.getContactPerson())) customer.setContactPerson(null);
		
		CustomerDaoController cdc = new CustomerDaoController();
		CountryDaoController countryController = new CountryDaoController();
		Country country = countryController.findCountry(countryName);
		customer.setCountry(country);

		Person contactPerson = null;
		PersonDaoController pdc = new PersonDaoController();
		if (contactPersonID != null && contactPersonID.length() > 0 && (new Integer(contactPersonID) > 0)) {
			contactPerson = pdc.findPerson(new Integer(contactPersonID));
			customer.setContactPerson(contactPerson);
		}
		if (invoicePersonID != null && invoicePersonID.length() > 0 && (new Integer(invoicePersonID) > 0)) {
			Person invoiceContactPerson = pdc.findPerson(new Integer(invoicePersonID));
			customer.setInvoiceContactPerson(invoiceContactPerson);
		}

		if (getSession().getAttribute("newAddress") != null) {
			customer.setAddress(""+getSession().getAttribute("newAddress"));
			getSession().removeAttribute("newAddress");
		}
		if (getSession().getAttribute("newInvoiceAddress") != null) {
			customer.setInvoiceAddress(""+getSession().getAttribute("newInvoiceAddress"));
			getSession().removeAttribute("newInvoiceAddress");
		}

		Customergroup group = cdc.findCustomergroup(groupName);
		customer.setGroup(group);
		
		String retMessage = cdc.createObject(customer);

		if (getSession().getAttribute("personPending") != null) {
			Person p = (Person) getSession().getAttribute("personPending");
			getSession().removeAttribute("personPending");
			p.setCustomer(customer);
			pdc.createObject(p);
			
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();
			Customer cus = cdc.findCustomer(customer.getCustomernumber());
			cus.setContactPerson(p);
			if (new Integer(invoicePersonID) == 0) {
				cus.setInvoiceContactPerson(p);
			}
			tx.commit();
		}

		
		if (!(retMessage != null && retMessage.length() > 0)) {
			retMessage = "Neuer Kunde wurde erfolgreich angelegt!" + customer.getCustomernumber();
			LOGGER.info(retMessage);
		} else {
			LOGGER.warn(retMessage);
		}
		
		getRequest().setAttribute("message", retMessage);
		getSession().setAttribute("mycustomer", customer);
		getSession().setAttribute("refreshCustomerList", true);
	}

	public void searchCustomer() {
		LOGGER.info("Method: searchCustomer");
		CustomerDaoController cc = new CustomerDaoController();
		List<DaoObject> customers =	cc.searchCustomer(customer.getCustomernumber(), customer.getName(), true);
		customerList = new ArrayList<Customer>();
		
		if (customers != null && customers.size() > 0) {
			LOGGER.info(customers.size() + " Kunden gefunden");
			
			if (customers.size() > 1) {
				Iterator<DaoObject> it = customers.iterator();
				while (it.hasNext()) {
					Customer c = (Customer) it.next();
					customerList.add(c);
				}
				opened = !opened;
			} else {
				customer = (Customer) customers.get(0);
				// SubKlassen müssen instanziiert werden, falls diese Null sind, sonst können Felder in View nicht befüllt werden bzw. werfen Fehler
				// Später Check bei Create/Update, ob die Felder befüllt sind, um Leereinträge in DB zu vermeiden
				if (customer.getContactPerson() == null) customer.setContactPerson(new Person());
				countryName = customer.getCountry().getName();
				
				getSession().setAttribute("updateCustomergroups", customer.getGroup());
				getSession().setAttribute("customer", customer);
				groupName = customer.getGroup().getGroupName();
				address = customer.getAddress();
				invoiceAddress = customer.getInvoiceAddress();
			}
			disableComponents(false);
		} else {
			LOGGER.warn("Kein Customer gefunden; " + customer.getCustomernumber() + "; " + customer.getName());
			getRequest().setAttribute("message", "Kein Customer gefunden; " + customer.getCustomernumber() + "; " + customer.getName());
		}
	}
	
	public String showInvoices() {
/* ABD-AVT später einbauen
		LOGGER.info("Method: showInvoices; Kundennummer: " + customer.getCustomernumber());
		BillController bc = new BillController();
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		bills = bc.findCustomerBills(session, transaction, Integer.parseInt(customer.getCustomernumber()));
		getSession().setAttribute("invoicesCustomerNumber", customer.getCustomernumber());
		LOGGER.info(bills.size() + " Rechnungen gefunden!");
		return "";
*/
		return "";
	}

	
	private void disableComponents(boolean b) {
		LOGGER.info("Method: disableComponents");
		getRequest().setAttribute("componentDisabled", b);
	}

	public void searchCustomerElevators() {
		LOGGER.info("Method: searchCustomerElevators; " + customer.getCustomernumber() + "; " + customer.getName());
		CustomerDaoController cc = new CustomerDaoController();
		List<DaoObject> customers =	cc.searchCustomer(customer.getCustomernumber(), customer.getName(), true);
		Customer cus = null;
		if (customers != null && customers.size() > 0) {
			cus = (Customer) customers.get(0);
		}

		
		elevatorList = new ArrayList<Elevator>();
		if (cus != null) {
			Set<Elevator> elevatorSet = cus.getElevators();
			LOGGER.info(elevatorSet.size() + " elevators found;");
			Iterator<Elevator> it = elevatorSet.iterator();
			while (it.hasNext()) {
				elevatorList.add(it.next());
			}
		}
	}
	
	public void updateCustomerAction() {
		LOGGER.info("Method: updateCustomerAction;");
		updateCustomer();
	}
	
	public String updateCustomer() {
		LOGGER.info("Method: updateCustomer; Customer: " + customer.getCustomernumber());
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			String whereClause = "";
			whereClause = " where customer.customernumber = '" + customer.getCustomernumber() + "'";
			
			@SuppressWarnings("unchecked")
			List<Customer> customerList = session.createQuery("from Customer as customer" + whereClause).list();
			Customer dbCustomer = customerList.get(0);
			dbCustomer.setCustomernumber(customer.getCustomernumber());
			dbCustomer.setBranch(customer.getBranch());
			dbCustomer.setName(customer.getName());
			dbCustomer.setComment(customer.getComment());
			dbCustomer.setVatNumber(customer.getVatNumber());
			dbCustomer.setGroup(customer.getGroup());
			PersonDaoController pdc = new PersonDaoController();
			dbCustomer.setContactPerson(pdc.findPerson(new Integer(contactPersonID)));
			dbCustomer.setInvoiceContactPerson(pdc.findPerson(new Integer(invoicePersonID)));
			if (getSession().getAttribute("newAddress") != null) {
				dbCustomer.setAddress(""+ getSession().getAttribute("newAddress"));
				customer.setAddress(""+ getSession().getAttribute("newAddress"));
				address = ""+ getSession().getAttribute("newAddress");
				getSession().removeAttribute("newAddress");
			}

			if (getSession().getAttribute("newInvoiceAddress") != null) {
				dbCustomer.setInvoiceAddress(""+ getSession().getAttribute("newInvoiceAddress"));
				customer.setInvoiceAddress(""+ getSession().getAttribute("newInvoiceAddress"));
				address = ""+ getSession().getAttribute("newInvoiceAddress");
				getSession().removeAttribute("newInvoiceAddress");
			}

			CountryDaoController countryController = new CountryDaoController();
			Country country = countryController.findCountry(countryName);
			dbCustomer.setCountry(country);
			
			InvoiceConfiguration ic = dbCustomer.getInvoiceConfiguration();
			InvoiceConfiguration cic = customer.getInvoiceConfiguration();
			ic.setColumns(cic.getColumns());
			ic.setCreationFrequency(cic.getCreationFrequency());
			ic.setDataOptionSurcharge(cic.getDataOptionSurcharge());
			ic.setSortingOption(cic.getSortingOption());
			ic.setFormat(cic.getFormat());
			ic.setSimPrice(cic.getSimPrice());
			ic.setSeparateBilling(cic.getSeparateBilling());
			ic.setSeparateBillingCriteria(cic.getSeparateBillingCriteria());
			ic.setDebtOrder(cic.getDebtOrder());
			ic.setPaymentTarget(cic.getPaymentTarget());
			
			tx.commit();
			LOGGER.info("Customer " + customer.getCustomernumber() + " aktualisiert;");
		} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("Error rolling back transaction");
					LOGGER.error("HibernateException: " + e1);
				}
				// throw again the first exception
				throw e;
			}
		}

		FacesUtil.writeAttributeToRequest("message", "Änderung gespeichert!");
		customer = new Customer();
		if (FacesContext.getCurrentInstance() != null) {
			disableComponents(true);
		}

		
		return "openUpdateCustomerDialog";
	}
	
	public String createCustomerNext() {
		LOGGER.info("Method: createCustomerNext;");
		if (customer.getCustomernumber() != null && customer.getCustomernumber().length() > 0) {
			createCustomer();
			customer = new Customer();
		} else {
			getRequest().setAttribute("message", "Kundennummer muss eingegeben werden!");
		}
		return null;
	}

	public String createCustomerFinish() {
		LOGGER.info("Method: updateCustomerFinish;");
		createCustomer();
		customer = new Customer();
		return "finish";
	}

	public void selectCustomer() {
		Iterator<Customer> itrCust = customerList.iterator();
		boolean match = false;
		while (itrCust.hasNext()) {
			Customer customerFromList = itrCust.next();
			if (customerFromList.toString().equals(selectedCustomer)) {
				LOGGER.info("Customer " + customerFromList.getName() + " equals my chosen customer!!!");
				customer = customerFromList;
				match = true;
				getSession().setAttribute("updateCustomergroups", true);
				getSession().setAttribute("customer", customer);
				address = customer.getAddress();
				invoiceAddress = customer.getInvoiceAddress();
				break;
			}
		}

		if (!match) {
			LOGGER.warn("No customer found!");
		}
		
		disableComponents(false);
		opened = !opened;
	}
	
	public String deleteCustomer() {
		LOGGER.info("Method: deleteCustomer; Customer = " + customer.getCustomernumber());
		CustomerDaoController customerController = new CustomerDaoController();
		
		customerController.deleteObject(customer);
		getSession().setAttribute("refreshCustomerList", true);
		
		getRequest().setAttribute("message", "Kunde wurde erfolgreich gelöscht!");
		return "";
	}
	
	private List<Customer> findAllCustomers() {
		CustomerDaoController cc = new CustomerDaoController();
		List<DaoObject> list = cc.listObjects();
		List customers = new ArrayList<Customer>();
		for (DaoObject d : list) {
			customers.add((Customer) d);
		}
		CustomerNumberComparator cusComp = new CustomerNumberComparator();
		Collections.sort(customers, cusComp);
		return customers;
	}
	
	public String downloadCustomerList() {
		CustomerReportGenerator crg = new CustomerReportGenerator();
		crg.generateReport(customerList);

//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		ExternalContext externalContext = facesContext.getExternalContext();
//
//		String filename = "CustomerList.pdf";
//
//		externalContext.responseReset();
//		externalContext.setResponseContentType("application/pdf");
//		externalContext.setResponseHeader("Content-Disposition",
//				"attachment; filename=\"" + filename + "\"");
//
//		try {
//			OutputStream output = externalContext.getResponseOutputStream();
//
//			for (Customer cus : customers) {
//				output.write(cus.getCustomernumber().getBytes());
//			}
//
//			output.flush();
//			output.close();
//
//			facesContext.responseComplete();
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
		return "";
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	
	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	public String getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(String selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public boolean isComponentDisabled() {
		if (getRequest().getAttribute("componentDisabled") != null)
			return (Boolean) getRequest().getAttribute("componentDisabled");
		return false;
	}

	public void setComponentDisabled(boolean componentDisabled) {
		this.componentDisabled = componentDisabled;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}


	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}


	private void updateBillList() {
/* ABD-AVT später einbauen
		BillController bc = new BillController();
		if (getSession().getAttribute("invoicesCustomerNumber") != null) {
			int customerNumber = Integer.parseInt((String) getSession().getAttribute("invoicesCustomerNumber"));
			Session session = HibernateUtil.getSession();
			Transaction transaction = session.getTransaction();
			bills = bc.findCustomerBills(session, transaction, customerNumber);
		}
 */
	}

	public List<Bill> getBills() {
		if (getRequest().getAttribute("billListUpdate") != null && (Boolean) getRequest().getAttribute("billListUpdate")) {
			updateBillList();
		}
		return bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public List<Elevator> getElevatorList() {
		return elevatorList;
	}

	public void setElevatorList(List<Elevator> elevatorList) {
		this.elevatorList = elevatorList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if (getSession().getAttribute("newAddress") == null && address != null && address.length() > 0 && !address.equals(customer.getAddress())) {
			getSession().setAttribute("newAddress", address);
		}
		this.address = address;
	}

	public String getInvoiceAddress() {
		if (getSession().getAttribute("newInvoiceAddress") == null && invoiceAddress != null && invoiceAddress.length() > 0 && !invoiceAddress.equals(customer.getInvoiceAddress())) {
			getSession().setAttribute("newInvoiceAddress", invoiceAddress);
		}
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getContactPersonID() {
		return contactPersonID;
	}

	public void setContactPersonID(String contactPersonID) {
		this.contactPersonID = contactPersonID;
	}

	public String getInvoicePersonID() {
		return invoicePersonID;
	}

	public void setInvoicePersonID(String invoicePersonID) {
		this.invoicePersonID = invoicePersonID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}