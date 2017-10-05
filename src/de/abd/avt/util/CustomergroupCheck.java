package de.abd.avt.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Customergroup;
import de.abd.avt.persistence.dao.DaoObject;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;
import de.abd.avt.persistence.dao.controller.DaoController;
import de.abd.avt.persistence.dao.controller.IDaoController;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;

public class CustomergroupCheck extends DaoController implements IDaoController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CustomergroupCheck cmda = new CustomergroupCheck();
		CustomerDaoController cdc = new CustomerDaoController();
		Customergroup cg = cdc.findCustomergroup("Schindler");
		Set<Customer> customers = cg.getCustomers();
		System.out.println("Customers: " + customers.size());
		for (Iterator<Customer> iter = customers.iterator(); iter.hasNext();) {
			Customer cus = iter.next();
			System.out.println("#############################################################################");
			System.out.println(cus.getName() + ", " + cus.getCustomernumber() + ", " + cus.getBranch());
		}
	}
}
