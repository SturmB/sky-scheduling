package info.chrismcgee.sky.scheduling;

import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import info.chrismcgee.dbutil.ConnectionManager;
import info.chrismcgee.sky.event.SchedulingEvent.BrowserResizeEvent;
import info.chrismcgee.sky.event.SchedulingEventBus;
import info.chrismcgee.sky.scheduling.ui.views.login.LoginBox;
import info.chrismcgee.sky.scheduling.ui.views.login.LoginEvent;
import info.chrismcgee.sky.scheduling.util.CurrentUser;
import info.chrismcgee.sky.scheduling.util.MyTheme;
import info.chrismcgee.sky.scheduling.util.event.LogoutEvent;
import info.chrismcgee.sky.scheduling.util.event.NavigationEvent;

@SuppressWarnings("serial")
@Theme("scheduling")
@Push
public class SchedulingUI extends UI {

	private SchedulingEventBus schedulingEventBus;
	
	// For logging!
	private final static Logger logger = Logger.getLogger(SchedulingUI.class.getName());

	@Override
	protected void init(VaadinRequest request) {

		setLocale(Locale.US);
		setupEventBus();
		Responsive.makeResponsive(this);
		addStyleName(MyTheme.UI_WITH_MENU);

		if (CurrentUser.isLoggedIn()) {
			removeStyleName("loginview");
			setContent(new MainView());
		} else {
			setContent(new LoginBox());
			addStyleName("loginview");
		}

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(event -> SchedulingEventBus.post(new BrowserResizeEvent()));
	
	}

	private void setupEventBus() {
		
		schedulingEventBus = new SchedulingEventBus();
		
		SchedulingEventBus.register(this);
	}
	
	@Subscribe
	public void userLoggedIn(LoginEvent event) {
		
		CurrentUser.set(event.getUser());
		ConnectionManager.getInstance().close();
		setContent(new MainView());
	}
	
	@Subscribe
	public void navigateTo(NavigationEvent view) {
		
		getNavigator().navigateTo(view.getViewName());
	}
	
	@Subscribe
	public void logout(LogoutEvent logoutEvent) {
		// Don't invalidate the underlying HTTP session in case we might use it for something else.
		VaadinSession.getCurrent().getSession().invalidate();
		VaadinSession.getCurrent().close();
		Page.getCurrent().reload();
	}
	
	public static SchedulingUI getCurrent() {
		return (SchedulingUI) UI.getCurrent();
	}
	
	public static SchedulingEventBus getSchedulingEventBus() {
		return ((SchedulingUI) getCurrent()).schedulingEventBus;
	}


	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SchedulingUI.class)
	public static class Servlet extends VaadinServlet {
	}
	
}