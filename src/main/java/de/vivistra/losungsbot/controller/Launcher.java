package de.vivistra.losungsbot.controller;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vivistra.losungsbot.model.AlleEmpfaenger;
import de.vivistra.losungsbot.model.Empfaenger;
import de.vivistra.losungsbot.settings.Settings;
import de.vivistra.losungsbot.storage.LoadData;
import de.vivistra.telegrambot.model.message.TextMessage;
import de.vivistra.telegrambot.receiver.Receiver;
import de.vivistra.telegrambot.sender.Sender;
import de.vivistra.telegrambot.settings.BotSettings;

/**
 * This file launches the bot.
 */
public class Launcher {

	private static final Logger LOG = LogManager.getLogger();

	public static void main(String[] args) throws Exception {
		boolean manualMode = false;

		if (args.length == 1 && args[0].equals("-m")) {
			manualMode = true;
		}

		new Launcher(manualMode);
	}

	private Launcher(boolean manualMode) throws Exception {
		// Lade alle Empfänger aus der XML Datei
		LoadData.ladeEmpfaenger();

		// Set API Token
		BotSettings.setApiToken(Settings.getApiToken());

		// Lade die Losungen
		LoadData.ladeAlleLosungen();

		if (!manualMode) {

			// Create a receiver
			GetLosung get = new GetLosung();

			// Register the receiver
			Receiver.subscribe(get);

			// Start sending Losungen
			PushLosung push = PushLosung.getInstance();
			push.start();

		} else {
			LOG.info("I am in MANUAL MODE now.");

			System.out.println("\nWelcome to manual mode.");

			Scanner scanner = new Scanner(System.in);

			boolean run = true;
			AlleEmpfaenger alleEmpfaenger = AlleEmpfaenger.getInstance();
			while (run) {

				System.out.println("\nMenu:");
				System.out.println("1) Send message to ONE user.");
				System.out.println("2) Broadcast message to ALL users.");
				System.out.println("3) Exit.");

				Set<Empfaenger> recipients = new HashSet<>();

				switch (scanner.nextLine()) {
				case "1":
					try {
						System.out.println("\nType recipient id [0-9]+:");

						String recipientStr = scanner.nextLine();
						Empfaenger recipient = alleEmpfaenger.getEmpfaengerById(Integer.parseInt(recipientStr));

						recipients.add(recipient);
						System.out.println("Added " + recipient.getFirstName());
					} catch (NumberFormatException e) {
						continue;
					}

					break;

				case "2":
					for (Empfaenger empfaenger : alleEmpfaenger.getAll()) {
						recipients.add(empfaenger);
					}
					System.out.println("Added all users.");

					break;

				case "3":
					run = false;

				default:
					continue;
				}

				// This will be done in case 1 or 2

				System.out.println("\nType a message (end with two new lines):\n"
						+ "System will automatically replace <<vorname>> with the  firstname of each recipient!");

				boolean end = false;

				StringBuilder message = new StringBuilder();

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();

					if (end && !line.isEmpty()) {
						end = false;
					} else if (!end && line.isEmpty()) {
						end = true;
					} else if (end && line.isEmpty()) {
						break;
					}

					message.append(line);
				}

				System.out.println("\nDo you wanna send to "
						+ (recipients.size() > 1 ? "ALL" : recipients.iterator().next().getFirstName()) + ":");

				String messageStr = "Note: <<vorname>> will be replaced by the fistname.\n\n"
						+ message.toString().trim();

				System.out.println(messageStr);

				System.out.println("\n[y/N]");

				if ("y".equalsIgnoreCase(scanner.nextLine())) {
					for (Empfaenger recipient : recipients) {
						messageStr = messageStr.replace("<<vorname>>", recipient.getFirstName());
						Sender.send(new TextMessage(recipient.getId(), messageStr));
					}
				}
			}

			scanner.close();
		}

		// for (int i = 0; true; i++) {
		// Sender.send(new TextMessage(7872355, "[" + i + "] => Hallo =)"));
		// Thread.sleep(5000);
		// }
	}
}
