package de.abd.avt.sort;

import java.util.Comparator;

import de.abd.avt.persistence.dao.Person;

public class PersonComparator implements Comparator<Person> {

	@Override
	public int compare(Person p1, Person p2) {
		// TODO Auto-generated method stub
	    if (p1 == null) {
	        return -1;
	    }
	    if (p2 == null) {
	        return 1;
	    }
	    return p1.getName().compareTo(p2.getName());
	}

}
