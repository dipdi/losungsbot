package de.vivistra.losungsbot.model;

import de.vivistra.losungsbot.settings.Settings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "de.vivistra.losungsbot.model.AlleEmpfaenger")
public class Empfaenger {
	@XmlElement(type=Long.class)
	private long id;
	private int hour;
	private int minute;
	private String firstName;

	@SuppressWarnings("unused")
	private Empfaenger() {
		// Used for JAXB only

		// Using defaults, if not set in XML.
		this.hour = Settings.getHourOfPush();
		this.minute = Settings.getMinutesOfPush();
	}

	public Empfaenger(long id, int hour, int minute, String firstName) {
		this.id = id;
		this.hour = hour;
		this.minute = minute;
		this.firstName = firstName;
	}

	public Empfaenger(long id, String firstName) {
		this(id, Settings.getHourOfPush(), Settings.getMinutesOfPush(), firstName);
	}

	public long getId() {
		return id;
	}

	@XmlElement
	private void setId(int id) {
		this.id = id;
	}

	public int getHour() {
		return hour;
	}

	@XmlElement(required = false)
	private void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	@XmlElement(required = false)
	private void setMinute(int minute) {
		this.minute = minute;
	}

	public String getFirstName() {
		return firstName;
	}

	@XmlElement
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setTime(int hour, int minute) {
		setHour(hour);
		setMinute(minute);

		AlleEmpfaenger.getInstance().saveToDisk();

		AlleEmpfaenger.getInstance().reloadTimeSet();
	}
}
