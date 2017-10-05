package de.abd.avt.sort;

import java.util.Comparator;

import de.abd.avt.persistence.dao.Country;
import de.abd.avt.persistence.dao.DaoObject;

public class CountryComparator implements Comparator<DaoObject> {

	@Override
	public int compare(DaoObject c1, DaoObject c2) {
		// TODO Auto-generated method stub
		return ((Country) c1).getName().toLowerCase().compareTo(((Country) c2).getName().toLowerCase());
	}

}
