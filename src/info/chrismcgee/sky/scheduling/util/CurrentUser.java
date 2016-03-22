package info.chrismcgee.sky.scheduling.util;

import com.vaadin.server.VaadinSession;

import info.chrismcgee.sky.scheduling.beans.User;

public class CurrentUser {
	
	private static final String KEY = "currentuser";

	public static void set(User user) {
		VaadinSession.getCurrent().setAttribute(KEY, user);
	}
	
	public static User get() {
		return (User) VaadinSession.getCurrent().getAttribute(KEY);
	}
	
	public static boolean isLoggedIn() {
		return get() != null;
	}

}
