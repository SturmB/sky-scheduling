package info.chrismcgee.sky.scheduling.ui.views.login;

import info.chrismcgee.sky.scheduling.beans.User;

public class LoginEvent {

	private User user;

	public LoginEvent(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

}
