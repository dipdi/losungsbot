package de.vivistra.losungsbot.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This file maps the settings from the properties file to java constants.
 */
public class Settings {
	private static final Logger LOG = LogManager.getLogger();

	private static final String PROPERTIES_FILENAME = "LosungsBot.properties";

	private static final Properties PROPERTIES = new Properties();
	@SuppressWarnings("unused")
	private static final Settings INSTANCE = new Settings();

	/**
	 * Initiates once, when a property is required the first time. After that
	 * all properties are in the memory and will be used from there.
	 */
	private Settings() {
		// Read properties file
		InputStream propInputStream = null;

		// Use properties file from working directory
		File propertyFile = new File("./" + PROPERTIES_FILENAME);

		if (propertyFile.exists()) {
			LOG.info("Using properties file from working directory: " + propertyFile.getAbsolutePath());

			// Open file
			try {
				propInputStream = new FileInputStream(propertyFile);
			} catch (FileNotFoundException e) {
				LOG.error("File exists, but not found!?", e);
			}
		} else {
			// Use fallback to resources folder
			propInputStream = getClass().getResourceAsStream("/" + PROPERTIES_FILENAME);
			LOG.info("Using default properties file: " + propertyFile.getAbsolutePath());
		}

		try {
			// Read file
			PROPERTIES.load(propInputStream);
		} catch (FileNotFoundException e) {
			LOG.error("Could not find properties file", e);
		} catch (IOException e) {
			LOG.error("Could not read properties file", e);
		} finally {
			// Close file
			try {
				propInputStream.close();
			} catch (IOException e) {
				LOG.error("Could not close file stream", e);
			}
		}
	}

	/**
	 * Get the path to the Losungen.xml file
	 * 
	 * @return String
	 */
	public static String getPathToLosungen() {
		return PROPERTIES.getProperty("xml.losungen");
	}

	/**
	 * Get the path to the Empfaenger.xml file
	 * 
	 * @return String
	 */
	public static String getPathToEmpfaenger() {
		return PROPERTIES.getProperty("xml.empfaenger");
	}

	/**
	 * Get the url for the Telegram API
	 * 
	 * @return String
	 */
	public static String getApiUrl() {
		return PROPERTIES.getProperty("bot.url");
	}

	/**
	 * Get the token for the Telegram API
	 * 
	 * @return String
	 */
	public static String getApiToken() {
		return PROPERTIES.getProperty("bot.token");
	}

	/**
	 * @return int; Die Stunde zu der täglich gepusht wird.
	 * 
	 *         Bekommt Info aus losungsBot.properties von hoursOfPush. Fallback
	 *         zu 9
	 */
	public static int getHourOfPush() {
		String hourProp = PROPERTIES.getProperty("push.hour");

		int hour = 9;

		try {
			hour = Integer.parseInt(hourProp);
		} catch (NumberFormatException e) {
			LOG.warn("Hours could not be parsed. Using default.");
		}
		return hour;
	}

	/**
	 * @return int; Die Minute zu der täglich gepusht wird.
	 * 
	 *         Bekommt Info aus losungsBot.properties von hoursOfPush. Fallback
	 *         zu 0
	 */
	public static int getMinutesOfPush() {
		String minuteProp = PROPERTIES.getProperty("push.minute");

		int minute = 0;

		try {
			minute = Integer.parseInt(minuteProp);
		} catch (NumberFormatException e) {
			LOG.warn("Minutes could not be parsed. Using default.");
		}

		return minute;
	}

	/**
	 * Returns the id of the GroupChat or Person to receive loggins via Telegram
	 * 
	 * @return
	 */
	public static int getLogChat() throws NumberFormatException {
		String logChatProp = PROPERTIES.getProperty("bot.logchat");

		int logChat = Integer.parseInt(logChatProp);

		return logChat;
	}
}
