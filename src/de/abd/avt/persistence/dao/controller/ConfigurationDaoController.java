package de.abd.avt.persistence.dao.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.abd.avt.persistence.dao.Configuration;
import de.abd.avt.persistence.dao.Manufacturer;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;

public class ConfigurationDaoController extends DaoController {

	private final static Logger LOGGER = Logger.getLogger(ConfigurationDaoController.class .getName()); 

	Transaction tx;
	
	public ConfigurationDaoController() {
		LOGGER.info("Instantiate ConfigurationController");
	}
	
	public void updateConfiguration(List<Manufacturer> manufacturers, Map<String, Set<String>> brands, List<String> elevatorStates) {
		LOGGER.info("updateConfiguration");
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		tx = session.beginTransaction();
		String select = "from Configuration";

		@SuppressWarnings("unchecked")
		List<Configuration> list = session.createQuery(select).list();
		Configuration c = null;
		if (list.size() > 0) {
			c = new Configuration();	
		} else {
			c = list.get(0);
		}
		
		if (manufacturers != null) {
			c.setManufacturers(manufacturers);
		}
		
		if (brands != null) {
//			c.setBrands(brands);
		}

		if (elevatorStates != null) {
			c.setElevatorStates(elevatorStates);
		}

		tx.commit();
	}
	
	public Configuration getConfiguration(boolean commit) {
		LOGGER.info("Method getConfiguration");
		tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		tx = session.beginTransaction();

		Configuration con = null;
		List<Configuration> configs = session.createQuery("from Configuration as configuration").list();
		if (configs != null && configs.size() > 0) {
			con = configs.get(0);
		}
		
		if (commit)
			tx.commit();
		
		return con;
	}
	
	public void addBrand(String manufacturer, String brand) throws Exception {
		Configuration con = getConfiguration(false);
		Iterator<Manufacturer> it = con.getManufacturers().iterator();
		while (it.hasNext()) {
			Manufacturer dbManufacturer = it.next();
			if (dbManufacturer.getName().equals(manufacturer)) {
				dbManufacturer.getBrands().add(brand);
				break;
			}
		}
		
		// New manufacturer
		ArrayList<String> brands = new ArrayList<String>();
		brands.add(brand);
		Manufacturer newManufacturer = new Manufacturer(manufacturer, brands);
		con.getManufacturers().add(newManufacturer);
		
		tx.commit();
	}

	public void addManufacturer(String manufacturer, String brand) throws Exception {
		Configuration con = getConfiguration(false);
		List<Manufacturer> manufacturers = con.getManufacturers();
		Iterator<Manufacturer> it = manufacturers.iterator();
		boolean isNew = true;
		while (it.hasNext()) {
			Manufacturer man = it.next();
			if (man.getName().equals(manufacturer)) {
				isNew = false;
				break;
			}
		}

		if (isNew) {
			ArrayList<String> brands = new ArrayList<String>();
			brands.add(brand);
			manufacturers.add(new Manufacturer(manufacturer, brands));
			con.setManufacturers(manufacturers);
		}

		tx.commit();
	}

	public void addTechnique(String technique) throws Exception {
		Configuration con = getConfiguration(false);
		con.getTechniques().add(technique);
		tx.commit();
	}

	public void addTypeOfBuilding(String typeOfBuilding) {
		Configuration con = getConfiguration(false);
		con.getTypesOfBuilding().add(typeOfBuilding);
		tx.commit();
	}

	public void addElevatorStatus(String elevatorStatus) {
		Configuration con = getConfiguration(false);
		con.getElevatorStates().add(elevatorStatus);
		tx.commit();
	}

}
