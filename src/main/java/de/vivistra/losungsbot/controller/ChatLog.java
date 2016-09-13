package de.vivistra.losungsbot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vivistra.losungsbot.settings.Settings;
import de.vivistra.telegrambot.model.message.TextMessage;
import de.vivistra.telegrambot.sender.Sender;

public class ChatLog {
	private static final Logger LOG = LogManager.getLogger();

	public static void send(String message) {

		try {
			long chat = Settings.getLogChat();

			Sender.send(new TextMessage(chat, message));

		} catch (NumberFormatException e) {
			LOG.info("Could not send log via Telegram, cause chat id is invalid.");
		}
	}
}
