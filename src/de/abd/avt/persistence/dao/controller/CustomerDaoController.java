package de.abd.avt.persistence.dao.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Customergroup;
import de.abd.avt.persistence.dao.DaoObject;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;
import de.abd.avt.sort.CustomerComparator;


public class CustomerDaoController extends DaoController implements IDaoController {
	
	private final static Logger LOGGER = Logger.getLogger(CustomerDaoController.class .getName()); 

	public CustomerDaoController() {
		LOGGER.info("Instantiate CustomerController");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DaoObject> listObjects() {
		LOGGER.info("Method listObjects");
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		List<DaoObject> customers = null;
		try {
			tx = session.beginTransaction();
			String select = "select distinct customer from Customer as customer";

			LOGGER.info("Select = " + select);
			List<Customer> list = session.createQuery(select).list();
			if (list != null) {
				LOGGER.info(list.size() + " customers found");
			} else {
				LOGGER.warn("No customers found!");
			}
			customers = new ArrayList<DaoObject>();
			for (Iterator it=list.iterator();it.hasNext();) {
				Customer customer = (Customer) it.next();
				customers.add(customer);
			}

			tx.commit();
			Comparator<DaoObject> comparator = new CustomerComparator();
			Collections.sort(customers, comparator);
			
		} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("HibernateException: Error rolling back transaction; " + e1);
				}
				// throw again the first exception
				throw e;
			}

		}
		
		return customers;
	}
	
	@SuppressWarnings("unchecked")
	public List<DaoObject> searchCustomer(String customerNumber, String customerName, boolean fetchCusGroup) {
		LOGGER.info("Method listObjects; CustomerNumber = " + customerNumber + ", customerName = " + customerName);
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		List<DaoObject> customers = null;
		try {
			tx = session.beginTransaction();
			String whereClause = "";
			if (customerNumber != null && customerNumber.length() > 0) {
				whereClause = " where customer.customernumber = '" + customerNumber + "'";
			} else if (customerName != null && customerName.length() > 0) {
				whereClause += " where customer.name LIKE '" + customerName + "%'";
			}

			LOGGER.info("Select = from Customer as customer" + whereClause);
			String select = "select distinct customer from Customer customer";
			if (fetchCusGroup) {
				select += " JOIN FETCH customer.group";
			}

			
			
			customers = session.createQuery(select + whereClause).list();
			if (customers != null) {
				LOGGER.info(customers.size() + " customers found");
			} else {
				LOGGER.warn("No customer found!");
			}
			tx.commit();
		} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("HibernateException: Error rolling back transaction; " + e1);
				}
				// throw again the first exception
				throw e;
			}

		}
		return customers;
	}

	public Set<Elevator> getElevators(Customer customer) {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		tx = session.beginTransaction();
		Set<Elevator> elevators = customer.getElevators();
		tx.commit();
		return elevators;
	}

	public List<Customergroup> findCustomergroups() {
		LOGGER.info("Method findCustomergroups");
		List<Customergroup> cglist = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		boolean transactionStarted = false;
		if (session.getTransaction() == null || !session.getTransaction().isActive()) {
			tx = session.beginTransaction();
			transactionStarted = true;
		} else {
			tx = session.getTransaction();
		}
		try {
			LOGGER.info("Select = from Customergroup as customergroup");
			cglist = session.createQuery("from Customergroup as customergroup").list();
			if (cglist != null) {
				LOGGER.info(cglist.size() + " customergroups found");
			} else {
				LOGGER.warn("No customergroup found!");
			}
			if (transactionStarted)
				tx.commit();
		} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("HibernateException: Error rolling back transaction; " + e1);
				}
				// throw again the first exception
				throw e;
			}

		}
		return cglist;
}
	
	public Customergroup findCustomergroup(String groupname) {
		LOGGER.info("Method findCustomer; Customergroup = " + groupname);
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Customergroup cg = null;
		Transaction tx = null;
		boolean transactionStarted = false;
		if (session.getTransaction() == null || !session.getTransaction().isActive()) {
			tx = session.beginTransaction();
			transactionStarted = true;
		} else {
			tx = session.getTransaction();
		}
		try {
			String whereClause = "";
			if (groupname != null) {
				whereClause = " where customergroup.groupName = '" + groupname + "'";
			}
			LOGGER.info("Select = from Customergroup as customergroup" + whereClause);
			List<DaoObject> cglist = session.createQuery("from Customergroup as customergroup" + whereClause).list();
			if (cglist != null && cglist.size() > 0) {
				cg = (Customergroup) cglist.get(0);
				LOGGER.info(cg.getGroupName() + " found");
			} else {
				LOGGER.warn("No customergroup found!");
			}
			if (transactionStarted)
				tx.commit();
		} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("HibernateException: Error rolling back transaction; " + e1);
				}
				// throw again the first exception
				throw e;
			}

		}
		return cg;
	}
	
	public Customer findCustomer(String customerNumber) {
		LOGGER.info("Method findCustomer; CustomerNumber = " + customerNumber);
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		List<DaoObject> customers = null;
		Transaction tx = null;
		boolean transactionStarted = false;
		if (session.getTransaction() == null || !session.getTransaction().isActive()) {
			tx = session.beginTransaction();
			transactionStarted = true;
		} else {
			tx = session.getTransaction();
		}
		try {
			String whereClause = "";
			if (customerNumber != null && customerNumber.length() > 0) {
				whereClause = " where customer.customernumber = '" + customerNumber + "'";
			}

			LOGGER.info("Select = from Customer as customer" + whereClause);
			customers = session.createQuery("from Customer as customer" + whereClause).list();
			if (customers != null) {
				LOGGER.info(customers.size() + " customers found");
			} else {
				LOGGER.warn("No customer found!");
			}
			if (transactionStarted)
				tx.commit();
		} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("HibernateException: Error rolling back transaction; " + e1);
				}
				// throw again the first exception
				throw e;
			}

		}
		Customer customer = (Customer) customers.get(0); 
		if (customer != null) {
			LOGGER.info("Customer found: ID = " + customer.getId() + ", number = " + customer.getCustomernumber());
		} else {
			LOGGER.warn("No customer found!");
		}
		return customer;
	}

}