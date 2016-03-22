package info.chrismcgee.sky.scheduling.beans;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 3128338558945671851L;
	
	// The variables this bean holds.
	private String userName; // Username
	private String hashPass; // Password in hashed form. NEVER store plain text passwords!
	private int accessFlags;
	// accessLevel has been changed to accessFlags, a set of flags for this revision of the Scheduling program.
	// It should remain as an Integer for now, to allow for up to a 32-bit number. (Since a Byte is only an 8-bit number.)
	// It may be updated as new features are added. Its flags are as follows ("*" means dangerous):
	//   00000000   (0):  Read-only access. Basic access that only allows for viewing information.
	//   00000001   (1):  Mark as done. User is allowed to mark jobs as being "done" (and reverting in case of a mistake).
	//   00000010   (2):  Edit maximums. User can edit the maximum number of plates allowed for each day.
	//   00000100   (4):  Hold orders. User can place an order on hold.
	//   00001000   (8): *Cancel orders. User can cancel an order entirely, removing it from the database.
	//   00010000  (16): *Change password. User can change passwords other than his/her own.
	//   00100000  (32):  Add user. User can add new users to the database.
	//   01000000  (64): *Delete user. User can delete other users.
	//   10000000 (128): *User privileges. User can update any user's privileges. (i.e., these flags)

	
	// All of the getters and setters.
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHashPass() {
		return hashPass;
	}
	public void setHashPass(String hashPass) {
		this.hashPass = hashPass;
	}
	public int getAccessFlags() {
		return accessFlags;
	}
	public void setAccessFlags(int accessFlags) {
		this.accessFlags = accessFlags;
	}

}
