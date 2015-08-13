package de.vivistra.losungsbot.storage;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import de.vivistra.losungsbot.model.AlleLosungen;
import de.vivistra.losungsbot.model.Losungen;

public class TestMarshal {
	@Test
	public void marshal() {
		AlleLosungen alleLosungen = AlleLosungen.getInstance();
		ArrayList<Losungen> losungen = new ArrayList<Losungen>();
		Losungen heute = new Losungen();
		heute.setLehrText("Lehrtext");
		heute.setLehrTextVers("Lehrtextvers");
		heute.setLosungsText("losungsText");
		heute.setLosungsVers("losungsVers");
		heute.setwTag("Montag");
		losungen.add(heute);

		Losungen morgen = new Losungen();
		morgen.setLehrText("Lehrtext2");
		morgen.setLehrTextVers("Lehrtextvers2");
		morgen.setLosungsText("losungsText2");
		morgen.setLosungsVers("losungsVers2");
		morgen.setwTag("Montag2");
		losungen.add(morgen);
		alleLosungen.setAlleLosungen(losungen);

		try {
			JAXBContext jc = JAXBContext.newInstance(AlleLosungen.class);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			File XMLfile = new File("data/test.xml");
			m.marshal(alleLosungen, XMLfile);
			m.marshal(alleLosungen, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}
}
