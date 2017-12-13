package com.helmo.archi.google.googleuse.tools;

import java.sql.Timestamp;
import java.util.Date;

public class Time {
	
	public static Timestamp getTime() {
		return new Timestamp(new Date().getTime());
	}
}
