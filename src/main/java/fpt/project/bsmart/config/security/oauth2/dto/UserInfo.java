package fpt.project.bsmart.config.security.oauth2.dto;


import org.springframework.beans.factory.annotation.Value;

import java.util.List;


public class UserInfo {
	private String id, displayName, email;
	private List<String> roles;

	public UserInfo(String id, String displayName, String email, List<String> roles) {
		this.id = id;
		this.displayName = displayName;
		this.email = email;
		this.roles = roles;
	}

	public UserInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}