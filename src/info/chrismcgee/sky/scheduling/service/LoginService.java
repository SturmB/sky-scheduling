package info.chrismcgee.sky.scheduling.service;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import info.chrismcgee.login.PasswordHash;
import info.chrismcgee.sky.scheduling.beans.User;
import info.chrismcgee.sky.tables.UserManager;

public class LoginService implements Serializable {
	
	/**
	 * Serialization!
	 */
	private static final long serialVersionUID = -2467980527655447758L;

	public User login(String username, char[] password) throws LoginException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		User user = UserManager.getRow(username);
		
		if (user != null && PasswordHash.validatePassword(password, user.getHashPass())) {
			return user;
		}
		
		throw new LoginException();
	}

}
