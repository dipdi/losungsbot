package de.vivistra.losungsbot.storage;

import de.vivistra.losungsbot.model.AlleLosungen;
import de.vivistra.losungsbot.model.Losungen;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		date.set(2016, 0, 1); // 0 = January
		System.out.println(date);
		Losungen losung = alleLosungen.suchen(date);
		System.out.println(losung);
		assertNotNull(losung);
		assertEquals("Danket dem Herrn aller Herren, der allein große Wunder tut, denn seine Güte währet ewiglich.", losung.getLosungsText());

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
