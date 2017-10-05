package de.abd.avt.util;

import de.abd.avt.persistence.dao.Person;

public class ObjectValidityChecker {

	
	public boolean checkPersonIsValid(Person person) {
		if (person.getName().length() == 0) {
			return false;
		}
		return true;
	}

}
