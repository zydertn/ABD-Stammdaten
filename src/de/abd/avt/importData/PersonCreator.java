package de.abd.avt.importData;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.dao.controller.DaoController;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;

public class PersonCreator {

	/**
	 * @param args
	 */
	static final Logger logger = Logger.getLogger(PersonCreator.class);

	public static void main(String[] args) {
		new PersonCreator();
	}

	public PersonCreator() {
		execute();
	}

	private void execute() {
		Session session = SessionFactoryUtil.getInstance().openSession();
		DaoController dc = new DaoController();

		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Customer> customers = session
				.createQuery(
						"from Customer as customer where customer.customernumber = 'K00001'")
				.list();
		Customer customer = customers.get(0);
		session.getTransaction().commit();

		session.beginTransaction();
		Person p = new Person();
		p.setFirstname("Heinz");
		p.setName("Becker");
		p.setPhoneNr("0172/6788335");
		p.setCustomer(customer);
		dc.createObject(p);
		session.getTransaction().commit();

		session.beginTransaction();
		Person p2 = new Person();
		p2.setFirstname("Martina");
		p2.setName("Krause");
		p2.setPhoneNr("0175/6837499");
		p2.setCustomer(customer);
		dc.createObject(p2);
		session.getTransaction().commit();

		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Customer> customers2 = session
				.createQuery(
						"from Customer as customer where customer.customernumber = 'K00002'")
				.list();
		Customer customer2 = customers2.get(0);
		session.getTransaction().commit();

		session.beginTransaction();
		Person p3 = new Person();
		p3.setFirstname("Markus");
		p3.setName("Lanz");
		p3.setPhoneNr("0176/92893335");
		p3.setCustomer(customer2);
		dc.createObject(p3);
		session.getTransaction().commit();

		session.beginTransaction();
		Person p4 = new Person();
		p4.setFirstname("Torben");
		p4.setName("Marx");
		p4.setPhoneNr("0173/44335679");
		p4.setCustomer(customer2);
		dc.createObject(p4);
		session.getTransaction().commit();

	}
}