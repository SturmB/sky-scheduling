package info.chrismcgee.sky.scheduling.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;

import info.chrismcgee.sky.scheduling.ui.components.VerticalSpacedLayout;
import info.chrismcgee.sky.scheduling.util.CurrentUser;
import info.chrismcgee.sky.scheduling.util.MyTheme;
import info.chrismcgee.sky.scheduling.util.ViewConfig;

@ViewConfig(uri = "", displayName = "Home")
public class HomeView extends VerticalSpacedLayout implements View {

	/**
	 * Serialization!
	 */
	private static final long serialVersionUID = -8674912001958197865L;
	public static final String NAME = "homeView"; // Defines this view.

	public HomeView() {
		
		Label caption = new Label("Welcome, " + CurrentUser.get().getUserName());
		Label description = new Label("This is the home view and should be the first one a user sees after logging in.");
		
		addComponents(caption, description);
		
		caption.addStyleName(MyTheme.LABEL_HUGE);
		description.addStyleName(MyTheme.LABEL_LARGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
