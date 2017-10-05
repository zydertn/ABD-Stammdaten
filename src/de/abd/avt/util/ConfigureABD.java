package de.abd.avt.util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import de.abd.avt.persistence.dao.Configuration;
import de.abd.avt.persistence.dao.Manufacturer;
import de.abd.avt.persistence.dao.controller.DaoController;
import de.abd.avt.persistence.dao.controller.IDaoController;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;

public class ConfigureABD extends DaoController implements IDaoController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ConfigureABD();
	}

	public ConfigureABD() {
		execute();
	}
	
	private void execute() {
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		tx = session.beginTransaction();
		String select = "from Configuration";

		List<Configuration> list = session.createQuery(select).list();
		Configuration c = null;
		if (list.size() > 0) {
			c = list.get(0);
		} else {
			c = new Configuration();
		}
			
		ArrayList<Manufacturer> manufacturers = new ArrayList<Manufacturer>();
//		
		ArrayList<String> brands = new ArrayList<String>();
		brands.add("Sunshine35");
		brands.add("wolke7");
		manufacturers.add(new Manufacturer("Kone", brands));

		brands = new ArrayList<String>();
		brands.add("brand1");
		brands.add("brand2");
		brands.add("brand3");
		manufacturers.add(new Manufacturer("Schindler", brands));
		
		brands = new ArrayList<String>();
		brands.add("a&b1");
		brands.add("a&b2");
		brands.add("a&b3");
		manufacturers.add(new Manufacturer("A&B", brands));
		c.setManufacturers(manufacturers);

		
		List<String> elevatorStates = new ArrayList<String>();
		elevatorStates.add("in Ordnung");
		elevatorStates.add("Aufzug steht");
		elevatorStates.add("Türe öffnet nicht");
		elevatorStates.add("Türe schließt nicht");
		elevatorStates.add("Aufzug steht unbündig");
		elevatorStates.add("Aufzug zeigt Fehlercode an");
		elevatorStates.add("Beleuchtung brummt");
		elevatorStates.add("Druckknopf klemmt");
		elevatorStates.add("Aufzug macht Fahrgeräusche");
		elevatorStates.add("Türe quietscht beim Öffnen / Schließen");
		c.setElevatorStates(elevatorStates);

		List<String> techniques = new ArrayList<>();
		techniques.add("Seilaufzug");
		techniques.add("Hydraulikaufzug");
		c.setTechniques(techniques);

		List<String> typesOfBuilding = new ArrayList<>();
		typesOfBuilding.add("Industrie");
		typesOfBuilding.add("Krankenhaus");
		typesOfBuilding.add("Wohnhaus");
		c.setTypesOfBuilding(typesOfBuilding);

		if (list.size() == 0) {
			this.createObject(c);
		}

		
		tx.commit();
	}
}
