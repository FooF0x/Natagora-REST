package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.repository.PasswordRepository;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
	
	private final PasswordRepository pwdRepo;
	
	public PasswordService(PasswordRepository pwdSrv) {
		this.pwdRepo = pwdSrv;
	}
}
