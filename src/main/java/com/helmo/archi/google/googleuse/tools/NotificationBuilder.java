package com.helmo.archi.google.googleuse.tools;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.Observation;

import java.sql.Timestamp;
import java.util.Date;

public class NotificationBuilder {
	
	public static Notification getNotification(
			String caption, String description, Timestamp time, boolean status, Observation obs) {
		Notification notif = new Notification();
		notif.setDate(time);
		notif.setCaption(caption);
		notif.setDescription(description);
		notif.setObservation(obs);
		notif.setStatus(status);
		return notif;
	}
	
	/**
	 * Return a notification with actual timestamp and status as false.
	 * @param caption The caption of the notification.
	 * @param description The description of the notification.
	 * @param obs The observation link with the notification.
	 * @return A notification.
	 */
	public static Notification getDefaultNotification(
			String caption, String description, Observation obs) {
		return getNotification(
				caption,
				description,
				new Timestamp(new Date().getTime()),
				false,
				obs
		);
	}
}
