package de.abd.avt.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.DaoObject;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.CustomerDaoController;
import de.abd.avt.persistence.dao.controller.DaoController;
import de.abd.avt.persistence.dao.controller.IDaoController;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;

public class CustomerCheck extends DaoController implements IDaoController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CustomerCheck cmda = new CustomerCheck();
		CustomerDaoController cdc = new CustomerDaoController();
//		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
//		Transaction tx = null;
//		tx = session.beginTransaction();
//		List<DaoObject> customers = session.createQuery("from Customer as customer where customer.customernumber = 'K00001'").list();
//		
//		Customer customer = (Customer) customers.get(0);
//		Set elevators = customer.getElevators();

		Customer customer = cdc.findCustomer("K00001");
//		Set elevators = customer.getElevators();
//		for (Iterator iter = elevators.iterator(); iter.hasNext();) {
//			Elevator el = (Elevator) iter.next();
//			System.out.println(el.getMachineNumber());
//			System.out.println(el.getStatus());
//		}
//		System.out.println(elevators.size());

		Set persons = customer.getContactPersons();
		System.out.println("Persons: " + persons.size());
		for (Iterator iter = persons.iterator(); iter.hasNext();) {
			Person per = (Person) iter.next();
			System.out.println("#############################################################################");
			System.out.println(per.getFirstname() + ", " + per.getName());
//			Set elevators = per.getElevators();
//			System.out.println("Elevators: " + elevators.size());
//			for (Iterator<Elevator> iter2 = elevators.iterator(); iter2.hasNext();) {
//				Elevator e = iter2.next();
//				System.out.println(e.getMachineNumber());
//			}
		}

		Customer customer2 = cdc.findCustomer("K00002");
//		Set elevators2 = customer2.getElevators();
//		for (Iterator iter = elevators2.iterator(); iter.hasNext();) {
//			Elevator el = (Elevator) iter.next();
//			System.out.println(el.getMachineNumber());
//			System.out.println(el.getStatus());
//		}
//		System.out.println(elevators.size());

		Set persons2 = customer2.getContactPersons();
		System.out.println("Persons: " + persons2.size());
		for (Iterator iter = persons2.iterator(); iter.hasNext();) {
			Person per = (Person) iter.next();
			System.out.println("#############################################################################");
			System.out.println(per.getFirstname() + ", " + per.getName());
//			Set elevators = per.getElevators();
//			System.out.println("Elevators: " + elevators.size());
//			for (Iterator<Elevator> iter2 = elevators.iterator(); iter2.hasNext();) {
//				Elevator e = iter2.next();
//				System.out.println(e.getMachineNumber());
//			}
		}

//		 tx.commit();
	}
}
