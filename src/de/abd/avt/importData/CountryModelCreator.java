package de.abd.avt.importData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.abd.avt.persistence.dao.Country;
import de.abd.avt.persistence.dao.controller.CountryDaoController;

public class CountryModelCreator {

	/**
	 * @param args
	 */
	static final Logger logger = Logger.getLogger(CountryModelCreator.class);

	public static void main(String[] args) {
		new CountryModelCreator();
	}

	public CountryModelCreator() {
		execute();
	}

	private void execute() {
		List<Country> countries = getCountriesToCreate();
		createCountriesInDB(countries);
	}

	private List<Country> getCountriesToCreate() {
		List<Country> countries = new ArrayList<Country>();
		countries.add(new Country("Deutschland", "Deutschland", "DE", "+49"));
		countries.add(new Country("D‰nemark", "Denmark", "DK", "+45"));
		countries.add(new Country("Irland", "Ireland", "IE", "+353"));
		countries.add(new Country("Groﬂbritannien", "Great Britain", "GB",
				"+44"));
		countries.add(new Country("÷sterreich", "÷sterreich", "AT", "+43"));
		countries.add(new Country("Polen", "Poland", "PL", "+48"));
		countries.add(new Country("Rum‰nien", "Romania", "RO", "+40"));
		countries
				.add(new Country("Tschechien", "Czech Republic", "CZ", "+420"));
		return countries;
	}

	public void createCountriesInDB(List<Country> countries) {
		CountryDaoController cc = new CountryDaoController();
		for (Country country : countries) {
			cc.createObject(country);
		}
	}

}