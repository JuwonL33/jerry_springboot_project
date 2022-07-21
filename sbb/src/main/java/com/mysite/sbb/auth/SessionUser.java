package com.mysite.sbb.auth;

import java.io.Serializable;

import com.mysite.sbb.user.SiteUser;

public class SessionUser implements Serializable {
	private String username, email;
	
	public SessionUser(SiteUser user) {
		this.username = username;
		this.email = email;
	}
}
