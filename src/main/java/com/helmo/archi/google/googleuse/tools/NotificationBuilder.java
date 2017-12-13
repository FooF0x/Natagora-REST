package com.helmo.archi.google.googleuse.tools;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.NotificationStatus;
import com.helmo.archi.google.googleuse.model.Observation;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class NotificationBuilder {
	
	public static Notification getNotification(
		  String caption, String description, Timestamp time, NotificationStatus status, Observation obs) {
		Notification notification = new Notification();
		notification.setDate(time);
		notification.setCaption(caption);
		notification.setDescription(description);
		notification.setObservation(obs);
		notification.setStatus(status);
		return notification;
	}
	
	/**
	 * Return a notification with actual timestamp and status as <code>PENDING</code>.
	 *
	 * @param caption The caption of the notification.
	 * @param description The description of the notification.
	 * @param obs The observation link with the notification.
	 * @return A notification.
	 */
	public static Notification getDefaultNotification(
		  String caption, String description, NotificationStatus status, Observation obs) {
		return getNotification(
			  caption,
			  description,
			  new Timestamp(new Date().getTime()),
			  status,
			  obs
		);
	}
}
