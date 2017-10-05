package de.abd.avt.persistence.dao.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;

public class PersonDaoController extends DaoController implements
		IDaoController {

	private final static Logger LOGGER = Logger
			.getLogger(PersonDaoController.class.getName());

	public Person findPerson(int id) {
		LOGGER.info("Method findPerson; PersonID = " + id);
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		boolean transactionStarted = false;
		if (session.getTransaction() == null || !session.getTransaction().isActive()) {
			tx = session.beginTransaction();
			transactionStarted = true;
		} else {
			tx = session.getTransaction();
		}

		Person person = null;
		try {

			tx = session.beginTransaction();
			String select = "select person from Person as person where person.id = '"
					+ id + "'";
			LOGGER.info("Select = " + select);
			person = (Person) session.createQuery(select).list().get(0);
			if (transactionStarted)
				tx.commit();
			} catch (RuntimeException e) {
			LOGGER.error("RuntimeException: " + e);
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					LOGGER.error("HibernateException: Error rolling back transaction; "
							+ e1);
				}
				// throw again the first exception
				throw e;
			}

		}

		return person;
	}

}
