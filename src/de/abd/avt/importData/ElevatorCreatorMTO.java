package de.abd.avt.importData;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.Elevator;
import de.abd.avt.persistence.hibernate.SessionFactoryUtil;

public class ElevatorCreatorMTO {

	/**
	 * @param args
	 */
		static final Logger logger = Logger.getLogger(ElevatorCreatorMTO.class);
			
		public static void main(String[] args) {
			ElevatorCreatorMTO c = new ElevatorCreatorMTO();
			c.execute();
		}
		
	private void execute() {
	    System.out.println("Hibernate one to many (XML Mapping)");
	   	Session session = SessionFactoryUtil.getInstance().openSession();
	   	session.beginTransaction();

	   	@SuppressWarnings("unchecked")
		List<Customer> customers = session
				.createQuery(
						"from Customer as customer where customer.customernumber = 'K00001'")
				.list();
	   	Customer customer = customers.get(0);
	   	session.getTransaction().commit();
	   	
	   	session.beginTransaction();
	   	
	   	Elevator elevator = new Elevator();
		elevator.setCustomer(customer);
		elevator.setMachineNumber("M00001");
		elevator.setTechnique("Hydraulikaufzug");
		elevator.setStatus("in Ordnung");
        session.save(elevator);

	   	Elevator elevator2 = new Elevator();
		elevator2.setCustomer(customer);
		elevator2.setMachineNumber("M00002");
		elevator2.setTechnique("Hydraulikaufzug");
		elevator2.setStatus("in Ordnung");
        session.save(elevator2);

//        customer.getElevators().add(elevator);
//        customer.getElevators().add(elevator2);
//        session.save(customer);

	   	session.getTransaction().commit();
	   	System.out.println("Done");
	}
		
}