package de.abd.avt.controller;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import de.abd.avt.model.Model;
import de.abd.avt.persistence.dao.Customer;

public class ActionController {

	private final static Logger LOGGER = Logger.getLogger(ActionController.class .getName());

	protected Model model;
	protected Customer customer;

	// JavaServerFaces related variables
	protected UIOutput uiMessage;

	// Constructor
	public ActionController() {
		model = new Model();
//		CustomerDaoController cusDaoController = new CustomerDaoController();
//		List customerList = cusDaoController.listObjects();
		HttpSession session = getSession();
		if (session != null) {
			session.setAttribute("model", model);
//			session.setAttribute("customerList", customerList);
		}
		
	}

	
	/***************** ActionListener **************/
	public void processAction(ActionEvent event)
			throws AbortProcessingException {
		// TODO Auto-generated method stub
		getRequest().setAttribute("newCustomer", true);
		LOGGER.info("ID = " + event.getComponent().getId());
		if (uiMessage != null)
			uiMessage.setRendered(false);
	}
	
	public String openAddElevatorDialog() {
		LOGGER.info("openAddElevatorDialog");
		return "openAddElevatorDialog";
	}

	public String openUpdateElevatorDialog() {
		LOGGER.info("openUpdateElevatorDialog");
		return "openUpdateElevatorDialog";
	}
	
	public String openConfigureElevatorDialog() {
		LOGGER.info("openConfigureElevatorDialog");
		return "openConfigureElevatorDialog";
	}	
	
	public String openAddCustomerDialog() {
		LOGGER.info("openAddCustomerDialog");
		return "openAddCustomerDialog";
	}
	
	public String openUpdateCustomerDialog() {
		getRequest().setAttribute("newCustomer", true);
		getRequest().setAttribute("componentDisabled", true);
		return "openUpdateCustomerDialog";
	}
	
	public String openShowCustomersDialog() {
		return "openShowCustomersDialog";
	}
	
	public String openCreateIncidentDialog() {
		return "openCreateIncidentDialog";
	}
	
	public String openShowCustomerElevatorsDialog() {
		return "openShowCustomerElevatorsDialog";
	}
	
	
	protected HttpSession getSession() {
		if (FacesContext.getCurrentInstance() != null) {
			return (HttpSession) FacesContext.getCurrentInstance()
					.getExternalContext().getSession(false);
		} else {
			return null;
		}
	}
	
	protected HttpServletRequest getRequest() {
		return ((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest());
	}
	
	public String getRequestMessage() {
		if (getRequest().getAttribute("message") != null) {
			String message = "" + getRequest().getAttribute("message"); 
			return message;
		}
		return "";
	}
	
	public String cancel() {
		uiMessage.setRendered(true);
		uiMessage.setValue("Aktion abgebrochen! Neue Karte wurde NICHT angelegt!");
		return "failure";
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	public Customer getCustomer() {
		if (getRequest().getAttribute("newCustomer") != null) {
			customer = new Customer();
			getRequest().removeAttribute("newCustomer");
		}

		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
