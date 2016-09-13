package de.vivistra.losungsbot.settings;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSettings {

	@Test
	public void testGetPathToLosungen() {
		assertEquals("data/Losungen Free 2016.xml", Settings.getPathToLosungen());
	}

	@Test
	public void testGetApiUrl() {
		assertEquals("https://api.telegram.org/bot", Settings.getApiUrl());
	}
}
