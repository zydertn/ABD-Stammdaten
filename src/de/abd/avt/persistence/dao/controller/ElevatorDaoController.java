package de.abd.avt.persistence.dao.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.DaoObject;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.dao.Person;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;
import de.abd.avt.util.AVTLogger;
import de.abd.avt.util.ObjectValidityChecker;


public class ElevatorDaoController extends DaoController implements IDaoController {
	
	private final static Logger LOGGER = Logger.getLogger(ElevatorDaoController.class .getName()); 
	Transaction tx;
	ObjectValidityChecker ovc;

	public ElevatorDaoController() {
		LOGGER.info("Instantiate ElevatorController");
		ovc = new ObjectValidityChecker();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DaoObject> listObjects() {
		LOGGER.info("Method listObjects");
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		List<DaoObject> elevators = null;
		try {
			tx = session.beginTransaction();
			String select = "select distinct elevator from Elevator as elevator where elevator.machineNumber != ''";

			LOGGER.info("Select = " + select);
			List<Customer> list = session.createQuery(select).list();
			if (list != null) {
				LOGGER.info(list.size() + " elevators found");
			} else {
				LOGGER.warn("No elevators found!");
			}
			elevators = new ArrayList<DaoObject>();
			for (Iterator it=list.iterator();it.hasNext();) {
				Elevator elevator = (Elevator) it.next();
				elevators.add(elevator);
			}

			tx.commit();
/* ABD-AVT auskommentiert
			Comparator<DaoObject> comparator = new CustomerComparator();
			Collections.sort(customers, comparator);
*/			
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
		
		return elevators;
	}

	public Elevator searchElevator(String manufacturer, String machineNumber, boolean commit) throws Exception {
		Elevator elevator = null;
		if (manufacturer != null && manufacturer.length() > 0 && machineNumber != null && machineNumber.length() > 0) {
			tx = null;
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			try {
				tx = session.beginTransaction();
				String select = "select distinct elevator from Elevator as elevator "
						+ "where elevator.manufacturer = '" + manufacturer + "'"
						+ " and elevator.machineNumber = '" + machineNumber + "'";

				LOGGER.info("Select = " + select);
				List<Elevator> list = session.createQuery(select).list();
				if (list != null) {
					LOGGER.info(list.size() + " elevators found");
				} else {
					LOGGER.warn("No elevators found!");
				}
				elevator = list.get(0);

				if (commit)
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
		} else {
			LOGGER.warn("Hersteller und Maschinennummer müssen eingegeben werden für Aufzugssuche!");
			throw new Exception("Hersteller und Maschinennummer müssen eingegeben werden für Aufzugssuche!");
		}
		
		return elevator;
	}

	public List<Elevator> searchElevators(String manufacturer, String machineNumber, boolean commit) throws Exception {
		List<Elevator> elevators = null;
		tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			String select = "select distinct elevator from Elevator as elevator where ";
			select += "elevator.manufacturer = '" + manufacturer + "'";
			if (machineNumber.length() > 0) {
				select += " and elevator.machineNumber LIKE '%" + machineNumber + "%'";
			}

			LOGGER.info("Select = " + select);
			elevators = session.createQuery(select).list();
			if (elevators != null) {
				LOGGER.info(elevators.size() + " elevators found");
			} else {
				LOGGER.warn("No elevators found!");
			}

			if (commit)
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
		
		return elevators;
	}

	public List<Elevator> searchElevatorsByAddress(String address) {
		List<Elevator> elevators = null;
		tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			String select = "select distinct elevator from Elevator as elevator where ";
			select += "elevator.installationAddress = '" + address + "'";

			elevators = session.createQuery(select).list();
			if (elevators != null) {
				LOGGER.info(elevators.size() + " elevators found");
			} else {
				LOGGER.warn("No elevators found!");
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
		
		return elevators;
	}
	
	public void updateElevator(Elevator elevator) throws Exception  {
		Elevator dbElevator = searchElevator(elevator.getManufacturer(), elevator.getMachineNumber(), false);
		dbElevator.setAccessToBuilding(elevator.getAccessToBuilding());
		dbElevator.setBrand(elevator.getBrand());

		if (ovc.checkPersonIsValid(elevator.getContactPerson())) {
			// Falls der Kunde noch keine KontaktPerson in der DB hinterlegt hat, muss diese instanziiert werden, sonst können 
			// die Felder in der View nicht befüllt werden bzw. liefern NullPointer
			if (dbElevator.getContactPerson() == null) dbElevator.setContactPerson(new Person());
			Person cp = dbElevator.getContactPerson();
			Person cpp = elevator.getContactPerson();
			cp.setFirstname(cpp.getFirstname());
			cp.setGender(cpp.getGender());
			cp.setName(cpp.getName());
			cp.setEmail(cpp.getEmail());
			cp.setDe_mail(cpp.getDe_mail());
			cp.setPhoneNr(cpp.getPhoneNr());
		}
		
		if (dbElevator.getCustomer().getId() != elevator.getCustomer().getId()) {
			dbElevator.setCustomer(elevator.getCustomer());
		}
		dbElevator.setEquippedWithMachineRoom(elevator.isEquippedWithMachineRoom());
		dbElevator.setInstallationAddress(new String(elevator.getInstallationAddress()));
		dbElevator.setStatus(elevator.getStatus());
		dbElevator.setTechnique(elevator.getTechnique());
		dbElevator.setTypeOfBuilding(elevator.getTypeOfBuilding());

		tx.commit();
	}
	
	public void deleteObject(Elevator elevator) throws Exception {
		Elevator dbElevator = searchElevator(elevator.getManufacturer(), elevator.getMachineNumber(), true);
		if (dbElevator != null)
			super.deleteObject(dbElevator);
	}

	private boolean checkParams(String postcode, String city, String street) {
		if ((postcode != null && postcode.length() > 0) || (city != null && city.length() > 0) || (street != null && street.length() > 0)) {
			return true;
		}
		return false;
	}

	public Elevator fetchElevator(int id) {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Elevator elevator = null;
		try {
			tx = session.beginTransaction();
			String select = "select distinct elevator from Elevator elevator JOIN FETCH elevator.customer JOIN FETCH elevator.contactPerson where ";
			select += "elevator.id = '" + id + "'";
			elevator = (Elevator) session.createQuery(select).list().get(0);
//			Hibernate.initialize(elevator.getCustomer());
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
		
		return elevator;
	}


	
/* ABD-AVT Später für Aufzugsuche
	public List<DaoObject> searchCustomerCards(Customer cus) {
		LOGGER.info("Method searchCustomerCards");
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		List<DaoObject> cards = null;
		try {
			tx = session.beginTransaction();
			String whereClause = "";
			if (cus != null) {
				whereClause = " where card.customer = '" + cus.getId() + "'";
			}

			LOGGER.info("Select = select distinct card from CardBean card" + whereClause);
			cards = session.createQuery("select distinct card from CardBean card" + whereClause).list();
			if (cards != null) {
				LOGGER.info(cards.size() + " cards found");
			} else {
				LOGGER.warn("No cards found!");
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
		return cards;
	}
*/	
}