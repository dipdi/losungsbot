package de.vivistra.losungsbot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vivistra.telegrambot.model.message.Message;
import de.vivistra.telegrambot.model.message.TextMessage;
import de.vivistra.telegrambot.receiver.IReceiverService;

/**
 * Dieser Service wird aufgerufen wenn eine Nachricht empfangen wurde.
 * 
 * @author danielketterer
 */
public class GetLosung implements IReceiverService {
	private static final Logger LOG = LogManager.getLogger();

	@Override
	public void received(Message message) {
		switch (message.getMessageType()) {
		case TEXT_MESSAGE:
			LOG.info(message.getSender().getFirstName() + " [" + message.getSender().getId() + "] wrote: "
					+ message.getMessage());

			String text = (String) message.getMessage();

			if (text.charAt(0) == '/') {
				LOG.debug("Command message received");

				ProcessMessage.command((TextMessage) message);

			} else {
				LOG.debug("Normal message received");
			}

			break;
		default:
			LOG.debug("Ignore received message.");
		}
	}
}
