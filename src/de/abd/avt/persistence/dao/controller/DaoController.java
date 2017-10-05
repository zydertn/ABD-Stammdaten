package de.abd.avt.persistence.dao.controller;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import de.abd.avt.persistence.dao.DaoObject;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;
import de.abd.avt.util.AVTLogger;

public class DaoController implements IDaoController {

	private final static Logger LOGGER = Logger.getLogger(DaoController.class .getName()); 
	
	public DaoController() {
		LOGGER.info("Instantiate DaoController");
	}
	
	@Override
	public String createObject(DaoObject d) {
		LOGGER.info("Method: createObject");
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		String message = "";
		try {
			tx = session.getTransaction();
			if (tx != null && tx.isActive()) {
				session.save(d);
			} else {
				tx = session.beginTransaction();
				session.save(d);
				tx.commit();
			}
//			message = d.toString() + " erfolgreich in DB angelegt.";
		} catch (NonUniqueObjectException e) {
			AVTLogger.warn(LOGGER, "NonUniqueObjectException");
			AVTLogger.error(LOGGER, e);
			message = "Fehler!! Objekt existiert bereits und wurde nicht angelegt!";
			return message;
		} catch (ConstraintViolationException e) {
			if (e.getSQLException().getMessage().contains("Duplicate entry")) {
				AVTLogger.error(LOGGER, "ConstraintViolationException");
				AVTLogger.error(LOGGER, e);
				message = "Fehler!! Objekt existiert bereits und wurde nicht angelegt!";
				return message;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
					AVTLogger.error(LOGGER, e);
					return e.getMessage();
				} catch (HibernateException e1) {
					AVTLogger.warn(LOGGER, "Error rolling back transaction");
					AVTLogger.error(LOGGER, e);
				}
				// throw again the first exception
				throw e;
			}
		}
		return message;
	}

	@Override
	public void deleteObject(DaoObject d) {
		LOGGER.info("Method: deleteObject");
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			session.delete(d);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					AVTLogger.warn(LOGGER, "Error rolling back transaction");
					AVTLogger.error(LOGGER, e1);
				}
				// throw again the first exception
				throw e;
			}
		}
	}

	@Override
	public void updateObject(DaoObject d) {
		LOGGER.info("Method: updateObject");
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			session.save(d);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()) {
				try {
					// Second try catch as the rollback could fail as well
					tx.rollback();
				} catch (HibernateException e1) {
					AVTLogger.warn(LOGGER, "Error rolling back transaction");
					AVTLogger.error(LOGGER, e1);
				}
				// throw again the first exception
				throw e;
			}
		}
	}

	protected HttpServletRequest getRequest() {
		if (FacesContext.getCurrentInstance().getExternalContext() != null) {
			return ((HttpServletRequest) FacesContext.getCurrentInstance()
					.getExternalContext().getRequest());
		} else {
			return null;
		}
	}
	
	@Override
	public List<DaoObject> listObjects() {
		// TODO Auto-generated method stub
		return null;
	}

}