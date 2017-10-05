package de.abd.avt.persistence.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Manufacturer extends DaoObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7681723230865794429L;

	private String name;
    private List<String> brands = new ArrayList<String>();

	public Manufacturer() {
		
	}
	
	public Manufacturer(String name, List<String> brands) {
		this.name = name;
		this.brands = brands;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getBrands() {
		return brands;
	}

	public void setBrands(List<String> brands) {
		this.brands = brands;
	}


}