package info.chrismcgee.sky.scheduling;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

import info.chrismcgee.sky.scheduling.ui.NavBar;
import info.chrismcgee.sky.scheduling.util.MyTheme;

public class MainView extends HorizontalLayout {
	
	public MainView() {
		setSizeFull();
		addStyleName("mainview");
		
		addComponent(new NavBar());
		
//		ComponentContainer content = new CssLayout();
		Panel content = new Panel();
		content.setSizeFull();
//		content.addStyleName("view-content");
		content.addStyleName(MyTheme.PANEL_BORDERLESS);
		
		addComponent(content);
		setExpandRatio(content, 1.0f);
		
		new SchedulingNavigator(content);
	}

}
