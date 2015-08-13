package de.vivistra.losungsbot.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Daten Model der Losung.
 *
 * @author danielketterer
 */
@XmlRootElement(namespace = "de.vivistra.losungsbot.model.AlleLosungen")
public class Losungen {

	// XMLGregorianCalender ist die Repräsentation von xs:dateTime in Java.
	private XMLGregorianCalendar datum;
	private String wTag;
	private String sonntag;
	private String losungsText;
	private String lehrText;
	private String losungsVers;
	private String lehrTextVers;

	public Losungen() {
	}

	public XMLGregorianCalendar getDatum() {
		return datum;
	}

	@XmlElement(name = "datum")
	public void setDatum(XMLGregorianCalendar datum) {
		this.datum = datum;
	}

	public String getSonntag() {
		return sonntag;
	}

	@XmlElement
	public void setSonntag(String sonntag) {
		this.sonntag = sonntag;
	}

	public String getLosungsText() {
		return losungsText;
	}

	@XmlElement
	public void setLosungsText(String losungsText) {
		this.losungsText = losungsText;
	}

	public String getLehrText() {
		return lehrText;
	}

	@XmlElement
	public void setLehrText(String lehrText) {
		this.lehrText = lehrText;
	}

	public String getLosungsVers() {
		return losungsVers;
	}

	@XmlElement
	public void setLosungsVers(String losungsVers) {
		this.losungsVers = losungsVers;
	}

	public String getLehrTextVers() {
		return lehrTextVers;
	}

	@XmlElement
	public void setLehrTextVers(String lehrTextVers) {
		this.lehrTextVers = lehrTextVers;
	}

	public String getwTag() {
		return wTag;
	}

	@XmlElement
	public void setwTag(String wTag) {
		this.wTag = wTag;
	}

	public String toString() {
		return "Die Losung vom " + datum.getDay() + "." + datum.getMonth() + "." + datum.getYear() + ":" + "\n\n"
				+ losungsText.replace("/", "") + "\n" + losungsVers + "\n\n" + lehrText.replace("/", "") + "\n"
				+ lehrTextVers;
	}

}
