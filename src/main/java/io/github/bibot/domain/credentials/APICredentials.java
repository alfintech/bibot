package io.github.bibot.domain.credentials;

public class APICredentials {
	
	public String key;
	public String secret;	

	public APICredentials(String key, String secret) {
		this.key = key;
		this.secret = secret;
	}
}
