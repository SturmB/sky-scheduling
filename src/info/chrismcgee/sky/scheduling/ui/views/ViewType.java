package info.chrismcgee.sky.scheduling.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum ViewType {

	HOMEVIEW(HomeView.NAME, HomeView.class, FontAwesome.HOME, false),
	FIRSTVIEW(FirstView.NAME, FirstView.class, FontAwesome.BAR_CHART_O, false),
	SECONDVIEW(SecondView.NAME, SecondView.class, FontAwesome.TREE, false),
	TREETABLEVIEW(TreeTableView.NAME, TreeTableView.class, FontAwesome.TABLE, false);
	
	private final String viewName;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final boolean stateful;
	
	/**
	 * @param viewName
	 * @param viewClass
	 * @param icon
	 * @param stateful
	 */
	private ViewType(final String viewName,
			final Class<? extends View> viewClass,
			final Resource icon,
			final boolean stateful) {
		
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.stateful = stateful;
	}
	
	public boolean isStateful() {
		return stateful;
	}

	public String getViewName() {
		return viewName;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}
	
	public static ViewType getByViewName(final String viewName) {
		
		ViewType result = null;
		for (ViewType viewType : values()) {
			if (viewType.getViewName().equals(viewName)) {
				result = viewType;
				break;
			}
		}
		return result;
	}

}
