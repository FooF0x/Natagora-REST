package com.helmo.archi.google.googleuse.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notifications_status")
@Getter @Setter
public class NotificationStatus extends IdentifiedModel {

	@Column(name = "name")
	private String name;
}
