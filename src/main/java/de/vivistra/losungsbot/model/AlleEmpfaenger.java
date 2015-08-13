package de.vivistra.losungsbot.model;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vivistra.losungsbot.controller.PushLosung;
import de.vivistra.losungsbot.storage.LoadData;

@XmlRootElement(name = "recipients")
public class AlleEmpfaenger {
	private static final Logger LOG = LogManager.getLogger();
	private static final AlleEmpfaenger INSTANCE = new AlleEmpfaenger();

	private static Set<Integer> timeSet = new HashSet<>();

	private static Set<Empfaenger> alleEmpfaenger = new HashSet<>();

	private AlleEmpfaenger() {
	}

	public static AlleEmpfaenger getInstance() {
		return INSTANCE;
	}

	public void add(Empfaenger empfaenger) {
		if (getEmpfaengerById(empfaenger.getId()) != null) {
			// Empfänger bereits im Set
			return;
		}

		alleEmpfaenger.add(empfaenger);

		reloadTimeSet();

		saveToDisk();
	}

	public void remove(Integer empfaenger) {
		Empfaenger empf = getEmpfaengerById(empfaenger);

		if (empf == null) {
			LOG.debug("Empfaenger to remove not found. Folgende id wurde gesucht:" + empfaenger);
			return;
		}

		alleEmpfaenger.remove(empf);

		reloadTimeSet();

		saveToDisk();
	}

	public Set<Empfaenger> getAll() {
		return alleEmpfaenger;
	}

	public Set<Empfaenger> getAll(int minuteOfDay) {
		int hour = minuteOfDay / 60;
		int minute = minuteOfDay % 60;

		Set<Empfaenger> empfaenger = new HashSet<>();

		for (Empfaenger empf : alleEmpfaenger) {
			if (empf.getHour() == hour && empf.getMinute() == minute) {
				empfaenger.add(empf);
			}
		}

		return empfaenger;
	}

	@XmlElement(name = "recipient")
	public void setAll(Set<Empfaenger> aEmpfaenger) {
		alleEmpfaenger = aEmpfaenger;

		reloadTimeSet();
	}

	public void saveToDisk() {
		LoadData.speichereEmpfaenger();
	}

	public Empfaenger getEmpfaengerById(int id) {
		for (Empfaenger empf : alleEmpfaenger) {
			if (empf.getId() == id) {
				return empf;
			}
		}

		return null;
	}

	public void reloadTimeSet() {
		timeSet = new HashSet<>();

		for (Empfaenger empf : alleEmpfaenger) {

			int minuteOfDay = empf.getHour() * 60 + empf.getMinute();

			timeSet.add(minuteOfDay);
		}

		PushLosung.getInstance().interrupt();
	}

	public Set<Integer> getTimes() {
		return timeSet;
	}
}
