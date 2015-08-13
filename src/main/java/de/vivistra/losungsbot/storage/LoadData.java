package de.vivistra.losungsbot.storage;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vivistra.losungsbot.model.AlleEmpfaenger;
import de.vivistra.losungsbot.model.AlleLosungen;
import de.vivistra.losungsbot.settings.Settings;

/**
 * Loads the data and maps it to the model.
 * 
 * @author danielketterer
 */
public class LoadData {
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Hier werden die Losungen aus der XML in Java Objekte geladen. Hierfür
	 * wird JAXB verwendet.
	 * 
	 * @return AlleLosungen
	 */
	public static void ladeAlleLosungen() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AlleLosungen.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			File xmlFile = new File(Settings.getPathToLosungen());
			LOG.info(Settings.getPathToLosungen());
			unmarshaller.unmarshal(xmlFile);
			LOG.debug("XML wurde gelesen und Objekte erstellt.");

		} catch (JAXBException e) {
			e.printStackTrace();
			LOG.error("XML wurde nicht korrekt gelesen.");
		}
	}

	/**
	 * Liest die Empfänger aus einer XML Datei.
	 */
	public static void ladeEmpfaenger() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AlleEmpfaenger.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			// Datei aus der Propertiesdatei nutzen
			String fileName = Settings.getPathToEmpfaenger();
			File xmlFile = new File(fileName);

			if (!xmlFile.exists()) {
				throw new IOException("Empfänger-XML-Datei: " + fileName + " existiert nicht.");
			}

			LOG.info("Lese aus Empfänger-XML-Datei: " + fileName);
			unmarshaller.unmarshal(xmlFile);
			LOG.debug("XML Datei der Empfänger wurde eingelesen.");

		} catch (IOException e) {
			LOG.info(e.getMessage());
		} catch (JAXBException e) {
			LOG.error("XML wurde nicht korrekt gelesen.", e);
		}
	}

	/**
	 * Speichert die Empfänger in einer XML Datei.
	 */
	public static void speichereEmpfaenger() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AlleEmpfaenger.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// Datei aus der Propertiesdatei nutzen
			File xmlFile = new File(Settings.getPathToEmpfaenger());
			LOG.info("Schreibe in Empfänger-XML-Datei: " + Settings.getPathToEmpfaenger());
			marshaller.marshal(AlleEmpfaenger.getInstance(), xmlFile);
			LOG.debug("XML Datei wurde geschrieben.");
		} catch (JAXBException e) {
			LOG.error("XML wurde nicht korrekt geschrieben.", e);
		}
	}
}
