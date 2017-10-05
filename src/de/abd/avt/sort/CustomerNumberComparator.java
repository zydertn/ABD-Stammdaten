package de.abd.avt.sort;

import java.util.Comparator;

import de.abd.avt.persistence.dao.Customer;
import de.abd.avt.persistence.dao.DaoObject;

public class CustomerNumberComparator implements Comparator<DaoObject> {

	@Override
	public int compare(DaoObject c1, DaoObject c2) {
		// TODO Auto-generated method stub
		return ((Customer) c1).getCustomernumber().compareTo(((Customer) c2).getCustomernumber());
	}

}
