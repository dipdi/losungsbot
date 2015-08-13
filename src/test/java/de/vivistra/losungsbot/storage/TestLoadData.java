package de.vivistra.losungsbot.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import de.vivistra.losungsbot.model.AlleLosungen;
import de.vivistra.losungsbot.model.Losungen;

public class TestLoadData {
	@BeforeClass
	public static void loadLosungen() {
		LoadData.ladeAlleLosungen();
	}

	@Test
	public void testXML() {
		AlleLosungen alleLosungen = AlleLosungen.getInstance();
		System.out.println(alleLosungen.getAlleLosungen().get(0));
		GregorianCalendar date = new GregorianCalendar();
		date.set(2015, 5, 30);
		System.out.println(date);
		Losungen losung = alleLosungen.suchen(date);
		System.out.println(losung);
		assertNotNull(losung);
		assertTrue(losung.getLehrText().equals("Der Herr ist treu; der wird euch stärken und bewahren vor dem Bösen."));

	}

	@Test
	public void textIfAlleLosungen() {
		AlleLosungen alleLosungen = AlleLosungen.getInstance();
		assertNotNull(alleLosungen);
	}

	@Test
	public void testifElementeInAlleLosungen() {
		AlleLosungen alleLosungen = AlleLosungen.getInstance();
		assertNotNull(alleLosungen.getAlleLosungen());
	}

}
