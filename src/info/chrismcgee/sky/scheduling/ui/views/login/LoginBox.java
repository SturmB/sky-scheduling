package info.chrismcgee.sky.scheduling.ui.views.login;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import info.chrismcgee.sky.event.SchedulingEventBus;
import info.chrismcgee.sky.scheduling.beans.User;
import info.chrismcgee.sky.scheduling.service.LoginService;
import info.chrismcgee.sky.scheduling.util.MyTheme;

public class LoginBox extends VerticalLayout {
	
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -7016532154417130834L;
	
	private final static Logger logger = Logger.getLogger(LoginBox.class.getName()); // Logging!

	private LoginService loginService = new LoginService();
	private TextField username;
	private PasswordField password;
	
	public LoginBox() {
		setSizeFull();

		Component loginForm = buildLoginForm();
		addComponent(loginForm);
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
		
		Notification notification = new Notification("Welcome to Sky Scheduling");
		notification.setDescription("<span>This application is a demo for now</span>");
		notification.setHtmlContentAllowed(true);
		notification.setStyleName("tray dark small closable login-help");
		notification.setPosition(Position.BOTTOM_CENTER);
		notification.setDelayMsec(20000);
		notification.show(Page.getCurrent());
	}
	
	private void login() {
		try {
			User user = loginService.login(username.getValue(), password.getValue().toCharArray());
			SchedulingEventBus.post(new info.chrismcgee.sky.scheduling.ui.views.login.LoginEvent(user));
		} catch (LoginException e) {
			Notification.show("Login failed.", "Try again.", Type.WARNING_MESSAGE);
			logger.log(Level.WARNING, "Bad username/password.");
			username.focus();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			Notification.show("Login failed.", "Password decrypter is nonfunctioning; please inform Chris McGee.", Type.ERROR_MESSAGE);
			logger.log(Level.SEVERE, "Problem with the PasswordHash class.");
			username.focus();
		} catch (SQLException e) {
			Notification.show("Login failed.", "Database error; please inform Chris McGee.", Type.ERROR_MESSAGE);
			logger.log(Level.SEVERE, "SQL Exception.");
			username.focus();
		}
	}
	
	
	private Component buildLoginForm() {
		
		// The view root layout
		VerticalLayout loginPanel = new VerticalLayout();
		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		Responsive.makeResponsive(loginPanel);
		loginPanel.addStyleName("login-panel");
		
		loginPanel.addComponent(buildLabels());
		loginPanel.addComponent(buildFields());
		loginPanel.addComponent(new CheckBox("Remember me", true));
		
		return loginPanel;
	}
	
	private Component buildFields() {
				
		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.addStyleName("fields");
		
		username = new TextField("Username");
		username.setIcon(FontAwesome.USER);
		username.addStyleName(MyTheme.TEXTFIELD_INLINE_ICON);
		
		password = new PasswordField("Password");
		password.setIcon(FontAwesome.LOCK);
		password.addStyleName(MyTheme.TEXTFIELD_INLINE_ICON);
		
		final Button signin = new Button("Sign In", click -> login());
		signin.addStyleName(MyTheme.BUTTON_PRIMARY);
		signin.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		signin.focus();
		
		fields.addComponents(username, password, signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
		
		
		return fields;
	}


	private Component buildLabels() {
		
		CssLayout labels = new CssLayout();
		labels.addStyleName("labels");
		
		Label welcome = new Label("Welcome");
		welcome.setSizeUndefined();
		welcome.addStyleName(MyTheme.LABEL_H4);
		welcome.addStyleName(MyTheme.LABEL_COLORED);
		labels.addComponent(welcome);
		
		Label title = new Label("Sky <strong>Scheduling</strong>");
		title.setContentMode(ContentMode.HTML);
		title.setSizeUndefined();
		title.addStyleName(MyTheme.LABEL_H3);
		title.addStyleName(MyTheme.LABEL_LIGHT);
		labels.addComponent(title);
		
		return labels;
	}

}
