package de.vivistra.losungsbot.controller;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vivistra.losungsbot.model.AlleEmpfaenger;
import de.vivistra.losungsbot.model.AlleLosungen;
import de.vivistra.losungsbot.model.Empfaenger;
import de.vivistra.losungsbot.model.Losungen;
import de.vivistra.telegrambot.model.message.TextMessage;
import de.vivistra.telegrambot.sender.Sender;

/**
 * Thread der einmal täglich die Losung an alle pushed.
 * 
 * @author danielketterer
 *
 */
public class PushLosung extends Thread {
	private static final Logger LOG = LogManager.getLogger();
	private static final PushLosung INSTANCE = new PushLosung();
	private boolean running = true;

	private PushLosung() {
	}

	public static PushLosung getInstance() {
		return INSTANCE;
	}

	public void run() {

		AlleEmpfaenger alleEmpfaenger = AlleEmpfaenger.getInstance();

		// Important! Initial load of timeset.
		alleEmpfaenger.reloadTimeSet();

		while (running) {
			try {
				GregorianCalendar date = new GregorianCalendar();
				int actualMinuteOfDay = date.get(Calendar.HOUR_OF_DAY) * 60 + date.get(Calendar.MINUTE);

				// Falls keine Zeiten vorhanden
				if (alleEmpfaenger.getTimes().isEmpty()) {

					LOG.debug("There is no time registered, sleep very long.");

					Thread.sleep(Integer.MAX_VALUE);

					continue;
				}

				// prüft ob heute noch
				int nextFireTime = Integer.MAX_VALUE;
				boolean searchBeforeActualTime = true;

				for (int time : alleEmpfaenger.getTimes()) {
					if (nextFireTime > time && time > actualMinuteOfDay) {
						nextFireTime = time;

						searchBeforeActualTime = false;
					}
				}
				// wenn heute nicht mehr, dann frühester termin morgen
				if (searchBeforeActualTime) {
					for (int time : alleEmpfaenger.getTimes()) {
						if (nextFireTime > time) {
							nextFireTime = time;
						}
					}
				}

				LOG.debug("Next fireTime is: " + nextFireTime / 60 + ":" + nextFireTime % 60);

				Thread.sleep(this.waitingTime(nextFireTime));

				date = new GregorianCalendar();
				Losungen losung = AlleLosungen.getInstance().suchen(date);

				// Sende die losung an alle registrierten Empfänger.
				for (Empfaenger recipient : alleEmpfaenger.getAll(nextFireTime)) {
					Sender.send(new TextMessage(recipient.getId(), losung.toString()));
				}

			} catch (InterruptedException e) {
				LOG.warn("Push thread interrupted.");
			}
		}
	}

	private long waitingTime(int nextFireTime) {
		LocalDateTime now = LocalDateTime.now();

		int hour = nextFireTime / 60;
		int minute = nextFireTime % 60;

		LocalDateTime runTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hour, minute);

		LOG.debug("now: " + now.toString());
		LOG.debug("run: " + runTime.toString());

		if (runTime.isBefore(now)) {
			runTime = runTime.plusDays(1);
			LOG.debug("Adding day, cause run-time is bevor actual-time. New run-time is: " + runTime.toString());
		}

		Instant nowInstant = now.toInstant(ZoneOffset.UTC);
		Instant runTimeInstant = runTime.toInstant(ZoneOffset.UTC);

		long sleep = Duration.between(nowInstant, runTimeInstant).toMillis();

		LOG.info("Thread is sleeping for " + sleep + "ms.");

		return sleep;
	}
}
