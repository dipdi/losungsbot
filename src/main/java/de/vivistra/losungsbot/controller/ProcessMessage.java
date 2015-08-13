package de.vivistra.losungsbot.controller;

import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vivistra.losungsbot.model.AlleEmpfaenger;
import de.vivistra.losungsbot.model.AlleLosungen;
import de.vivistra.losungsbot.model.Empfaenger;
import de.vivistra.losungsbot.model.Losungen;
import de.vivistra.telegrambot.model.message.TextMessage;
import de.vivistra.telegrambot.sender.Sender;

public class ProcessMessage {
	private static final Logger LOG = LogManager.getLogger();

	public static void command(TextMessage message) {

		// Remove slash and normalize
		String text = message.getMessage().substring(1).toUpperCase();
		StringTokenizer messageTokenizer = new StringTokenizer(text);

		String command = messageTokenizer.hasMoreTokens() ? messageTokenizer.nextToken() : "";

		// If there are more than one bot in a chat, there will be a @NameOfBot,
		// ignore that ;)
		if (command.contains("@")) {
			String[] splitted = command.split("@");
			command = splitted[0];
		}

		AlleEmpfaenger alleEmpfaenger = AlleEmpfaenger.getInstance();

		// Empfänger: GruppenChat oder Einzelperson
		Integer recipient = message.isFromGroupChat() ? message.getGroupChat().getId() : message.getSender().getId();

		switch (command) {
		case "START":
		case "SUBSCRIBE":
		case "ABONNIEREN":

			alleEmpfaenger.add(new Empfaenger(message.getSender().getId(), message.getSender().getFirstName()));

			if (message.isFromGroupChat()) {
				// Gruppenchat
				// Gruppe informieren
				Sender.send(new TextMessage(recipient, "Glückwunsch! " + message.getSender().getFirstName()
						+ " wird in Zunkunft die Losung bekommen.\n"
						+ "Falls DU auch die Losung abonnieren willst, klicke /abonnieren."));
			}
			// Abonnent Infomieren
			Sender.send(new TextMessage(
					message.getSender().getId(),
					"Hallo "
							+ message.getSender().getFirstName()
							+ ",\n"
							+ "vielen Dank für das Abonnieren der Losungen! Ich werde sie dir jeden Tag um 9 Uhr hier schicken.\n"
							+ "Wenn du die Uhrzeit ändern möchtest verwende /uhrzeit Stunde:Minute mit 0-23 und 0-59."));

			LOG.info(message.getSender().toString() + " wanna have the Losung :)");

			// Send notification to Dev Chat
			ChatLog.send(message.getSender().toString() + " hat die Losung abonniert. =)");

			break;

		case "UNSUBSCRIBE":
		case "ABBESTELLEN":

			alleEmpfaenger.remove(message.getSender().getId());

			if (message.isFromGroupChat()) {
				// Gruppenchat
				// Gruppe informieren
				Sender.send(new TextMessage(recipient, "Schade! " + message.getSender().getFirstName()
						+ " wird in Zunkunft die Losung nicht mehr bekommen.\n"
						+ "Falls DU die Losung abonnieren willst, klicke /abonnieren."));
			}
			// Abonnent Infomieren
			Sender.send(new TextMessage(message.getSender().getId(), "Hallo " + message.getSender().getFirstName()
					+ ",\n" + "schade, dass du die Losungen abbestellt hast!\n"
					+ "Ich werde sie dir zukünftig nicht mehr schicken."));

			LOG.info(message.getSender().toString() + " does not want the Losung any more :(");

			// Send notification to Dev Chat
			ChatLog.send(message.getSender().toString() + " will die Losung nicht mehr. =(");

			break;

		case "SETTIME":
		case "UHRZEIT":

			Empfaenger empfaenger = alleEmpfaenger.getEmpfaengerById(message.getSender().getId());

			if (empfaenger == null) {

				Sender.send(new TextMessage(message.getSender().getId(),
						"Dies ist nur möglich, wenn Du die Losung abonniert hast. Um dies zu tun, verwende den Befehl /abonnieren."));

				LOG.info(message.getSender().toString() + " will die Zeit setzen, hat aber nicht abonniert.");

				break;
			}

			String timeStr = "Not set";

			try {
				// Keine Uhrzeit angegeben
				if (!messageTokenizer.hasMoreTokens()) {
					throw new IllegalArgumentException("No time argument");
				}

				// Zeitangabe aufsplitten
				timeStr = messageTokenizer.nextToken();
				String[] timeStrArry = timeStr.split("[.:]");

				// Falsches Format
				if (timeStrArry.length != 2) {
					throw new IllegalArgumentException("Wrong formatted time argument");
				}

				// In Integer umwandeln
				int hour = Integer.parseInt(timeStrArry[0]);
				int minute = Integer.parseInt(timeStrArry[1]);

				// Zu hohe oder zu niedrige Stunden / Minuten
				if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
					throw new IllegalArgumentException("Hour or minute out of range");
				}

				// Setzen
				empfaenger.setTime(hour, minute);

				String minuteFormatted = String.format("%02d", minute);

				// Benachrichtigung über Erfolg
				Sender.send(new TextMessage(message.getSender().getId(),
						"Die Uhrzeit für die Benachrichtigung wurde auf " + hour + ":" + minuteFormatted + " gesetzt."));

				LOG.info(message.getSender().toString() + " hat die Uhrzeit auf " + hour + ":" + minuteFormatted
						+ " gesetzt.");

				// Send notification to Dev Chat
				ChatLog.send(message.getSender().toString() + " hat die Uhrzeit auf " + hour + ":" + minuteFormatted
						+ " gesetzt.");

			} catch (Exception e) {

				Sender.send(new TextMessage(message.getSender().getId(),
						"Bitte gib eine Uhrzeit, im Format Stunde:Minute mit 0-23 und 0-59, an.\n"
								+ "Beispiel: /uhrzeit 13:37"));

				LOG.info(message.getSender().toString() + " hat die Uhrzeit in einem falschen Format angegeben ("
						+ timeStr + ").");
			}

			break;

		case "GETNOW":
		case "HEUTE":

			GregorianCalendar date = new GregorianCalendar();
			Losungen losung = AlleLosungen.getInstance().suchen(date);

			Sender.send(new TextMessage(recipient, losung.toString()));

			LOG.info(message.getSender().toString() + " get Losung now");

			// Send notification to Dev Chat
			ChatLog.send(message.getSender().toString() + " hat die Losung abgerufen. =)");

			break;

		default:
			Sender.send(new TextMessage(recipient, "Leider habe ich deinen Befehl nicht verstanden.\n"
					+ "Bitte probiere einen der folgenden:\n\n"
					+ "/abonnieren - Losungen abonieren, diese werden dir zukünftig einmal am Tag gesendet.\n"
					+ "/abbestellen - Losungen abbestellen, diese werden dir in Zunkunft nicht mehr gesendet.\n"
					+ "/heute - Die heutige Losung einmalig abrufen.\n"
					+ "/uhrzeit - Die Uhrzeit für die Losung verändern.\n"));

			LOG.debug("Sending help message");

			// Send notification to Dev Chat
			ChatLog.send(message.getSender().toString() + " interagiert mit dem Bot. ;)");

			break;
		}
	}
}
