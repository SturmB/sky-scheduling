package info.chrismcgee.sky.scheduling;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

import info.chrismcgee.sky.event.SchedulingEvent.BrowserResizeEvent;
import info.chrismcgee.sky.event.SchedulingEvent.CloseOpenWindowsEvent;
import info.chrismcgee.sky.event.SchedulingEvent.PostViewChangeEvent;
import info.chrismcgee.sky.event.SchedulingEventBus;
import info.chrismcgee.sky.scheduling.ui.views.ViewType;

@SuppressWarnings("serial")
public class SchedulingNavigator extends Navigator {
	
	private static final ViewType ERROR_VIEW = ViewType.HOMEVIEW;
	private ViewProvider errorViewProvider;

	public SchedulingNavigator(final ComponentContainer container) {
		
		super(UI.getCurrent(), container);

		initViewChangeListener();
		initViewProviders();
	}

	public SchedulingNavigator(final Panel content) {
		// TODO Auto-generated constructor stub
		super(UI.getCurrent(), content);
		
		initViewChangeListener();
		initViewProviders();
	}

	private void initViewChangeListener() {
		
		addViewChangeListener(new ViewChangeListener() {
			
			@Override
			public boolean beforeViewChange(final ViewChangeEvent event) {
				// Since there's no conditions in switching between the views
				// we can always return true.
				return true; // false blocks navigation, always return true here.
			}
			
			@Override
			public void afterViewChange(final ViewChangeEvent event) {

				ViewType view = ViewType.getByViewName(event.getViewName());
				// Appropriate events get fired after the view is changed.
				SchedulingEventBus.post(new PostViewChangeEvent(view));
				SchedulingEventBus.post(new BrowserResizeEvent());
				SchedulingEventBus.post(new CloseOpenWindowsEvent());
			}
		});
	}
	
	private void initViewProviders() {
		// A dedicated view provider is added for each separate view type
		for (final ViewType viewType : ViewType.values()) {
			ViewProvider viewProvider = new ClassBasedViewProvider(
					viewType.getViewName(), viewType.getViewClass()) {
				
				// This field caches an already initialized view instance if the
				// view should be cached (stateful views).
				private View cachedInstance;
				
				@Override
				public View getView(final String viewName) {
					View result = null;
					if (viewType.getViewName().equals(viewName)) {
						if (viewType.isStateful()) {
							// Stateful views get lazily instantiated
							if (cachedInstance == null) {
								cachedInstance = super.getView(viewType.getViewName());
							}
							result = cachedInstance;
						} else {
							// Non-stateful views get instantiated every time
							// they're navigated to
							result = super.getView(viewType.getViewName());
						}
					}
					return result;
				}
			};
			
			if (viewType == ERROR_VIEW) {
				errorViewProvider = viewProvider;
			}
			
			addProvider(viewProvider);
		}
		
		setErrorProvider(new ViewProvider() {
			
			@Override
			public String getViewName(final String viewAndParameters) {
				return ERROR_VIEW.getViewName();
			}
			
			@Override
			public View getView(final String viewName) {
				return errorViewProvider.getView(ERROR_VIEW.getViewName());
			}
		});
	}

}
