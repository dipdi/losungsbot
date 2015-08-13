package de.vivistra.losungsbot.model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Daten Model zu AlleLosungen, es enthält eine ArrayList mit allen Losungen,
 * die gelesen werden.
 * 
 * @author danielketterer
 *
 */
@XmlRootElement
public class AlleLosungen {
	private static final Logger LOG = LogManager.getLogger();
	private static ArrayList<Losungen> alleLosungen;

	private static final AlleLosungen INSTANCE = new AlleLosungen();

	private AlleLosungen() {
	}

	public static AlleLosungen getInstance() {
		return INSTANCE;
	}

	public ArrayList<Losungen> getAlleLosungen() {
		return alleLosungen;
	}

	@XmlElement(name = "losungen")
	public void setAlleLosungen(ArrayList<Losungen> aLosungen) {
		alleLosungen = aLosungen;
	}

	/**
	 * @param date
	 * @return Losungen; gefundenes Element
	 * 
	 * 
	 */
	public Losungen suchen(GregorianCalendar date) {
		XMLGregorianCalendar dateXml;
		try {
			dateXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
			Iterator<Losungen> it = alleLosungen.iterator();
			LOG.debug("suchen...");
			while (it.hasNext()) {
				Losungen current = it.next();
				if (current.getDatum().getYear() == dateXml.getYear()
						&& current.getDatum().getMonth() == dateXml.getMonth()
						&& current.getDatum().getDay() == dateXml.getDay()) {
					return current;
				}
			}

		} catch (DatatypeConfigurationException e) {
			LOG.error("dateXml konnte nicht instanziert werden.", e);
		}

		throw new NoSuchElementException("Keine Losung zu diesem Datum.");
	}
}
