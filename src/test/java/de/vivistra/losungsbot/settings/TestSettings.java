package de.vivistra.losungsbot.settings;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSettings {

	@Test
	public void testGetPathToLosungen() {
		assertEquals("data/Losungen Free 2015.xml", Settings.getPathToLosungen());
	}

	@Test
	public void testGetApiUrl() {
		assertEquals("https://api.telegram.org/bot", Settings.getApiUrl());
	}
}
