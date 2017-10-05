package de.abd.avt.importData;

import org.apache.log4j.Logger;

import de.abd.avt.controller.CustomergroupController;
import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Customergroup;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;

public class CustomerCreator {

	/**
	 * @param args
	 */
	static final Logger logger = Logger.getLogger(CustomerCreator.class);

	public static void main(String[] args) {
		new CustomerCreator();
	}

	public CustomerCreator() {
		execute();
	}

	private void execute() {
		CustomergroupController cc = new CustomergroupController();
		Customergroup cn = cc.createCustomergroup("-");
		Customergroup cs = cc.createCustomergroup("Schindler");
		Customergroup ck = cc.createCustomergroup("Kone");

		
		CustomerDaoController cdc = new CustomerDaoController();
		Customer customer = new Customer();
		customer.setName("Kone");
		customer.setBranch("Stuttgart");
		customer.setContactPerson(null);
		customer.setAddress(null);
		customer.setCustomernumber("K00001");
		customer.setInvoiceAddress(null);
		customer.setInvoiceConfiguration(null);
		customer.setGroup(cs);
		cdc.createObject(customer);

		customer = new Customer();
		customer.setName("Schindler");
		customer.setBranch("München");
		customer.setContactPerson(null);
		customer.setAddress(null);
		customer.setCustomernumber("K00002");
		customer.setInvoiceAddress(null);
		customer.setInvoiceConfiguration(null);
		customer.setGroup(ck);
		cdc.createObject(customer);

		customer = new Customer();
		customer.setName("Kone");
		customer.setBranch("Bremen");
		customer.setContactPerson(null);
		customer.setAddress(null);
		customer.setCustomernumber("K00003");
		customer.setInvoiceAddress(null);
		customer.setInvoiceConfiguration(null);
		customer.setGroup(cs);
		cdc.createObject(customer);

		customer = new Customer();
		customer.setName("Schindler");
		customer.setBranch("Frankfurt");
		customer.setContactPerson(null);
		customer.setAddress(null);
		customer.setCustomernumber("K00004");
		customer.setInvoiceAddress(null);
		customer.setInvoiceConfiguration(null);
		customer.setGroup(ck);
		cdc.createObject(customer);
	}
}