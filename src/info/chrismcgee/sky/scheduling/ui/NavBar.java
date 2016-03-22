package info.chrismcgee.sky.scheduling.ui;

import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import info.chrismcgee.sky.event.SchedulingEvent.PostViewChangeEvent;
import info.chrismcgee.sky.event.SchedulingEvent.ProfileUpdatedEvent;
import info.chrismcgee.sky.event.SchedulingEventBus;
import info.chrismcgee.sky.scheduling.beans.User;
import info.chrismcgee.sky.scheduling.ui.views.ViewType;
import info.chrismcgee.sky.scheduling.util.CurrentUser;
import info.chrismcgee.sky.scheduling.util.event.LogoutEvent;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 * 
 * @author Chris McGee
 *
 */
public class NavBar extends CustomComponent {

	public static final String ID = "scheduling-menu";
	public static final String REPORTS_BADGE_ID = "scheduling-menu-reports-badge";
	public static final String NOTIFICATIONS_BADGE_ID = "scheduling-menu-notifications-badge";
	private static final String STYLE_VISIBLE = "valo-menu-visible";
	private MenuItem settingsItem;

	
	public NavBar() {
		
		setPrimaryStyleName("valo-menu");
		setId(ID);
		setSizeUndefined();
		
		// There's only one NavBar per UI so this doesn't need to be
		// unregistered from the UI-scoped SchedulingEventBus.
		SchedulingEventBus.register(this);
		
		setCompositionRoot(buildContent());
		
	}
	
	private Component buildContent() {
		
		final CssLayout menuContent = new CssLayout();
		menuContent.addStyleName("sidebar");
		menuContent.addStyleName(ValoTheme.MENU_PART);
		menuContent.addStyleName("no-vertical-drag-hints");
		menuContent.addStyleName("no-horizontal-drag-hints");
		menuContent.setWidth(null);
		menuContent.setHeight("100%");
		
		menuContent.addComponent(buildTitle());
		menuContent.addComponent(buildUserMenu());
		menuContent.addComponent(buildToggleButton());
		menuContent.addComponent(buildMenuItems());
		
		return menuContent;
	}
	
	private Component buildTitle() {
		Label logo = new Label("Sky <strong>Scheduling</strong>",
				ContentMode.HTML);
		logo.setSizeUndefined();
		HorizontalLayout logoWrapper = new HorizontalLayout(logo);
		logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
		logoWrapper.addStyleName("valo-menu-title");
		
		return logoWrapper;
	}
	
	private User getCurrentUser() {
		return CurrentUser.get();
	}
	
	private Component buildUserMenu() {
		
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");
		settingsItem = settings.addItem("",
				new ThemeResource("img/profile-pic-300px.jpg"),
				null);
		updateUserName(null);
		settingsItem.addItem("Sign Out", new Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				SchedulingEventBus.post(new LogoutEvent());
			}
		});
		
		return settings;
	}
	
	private Component buildToggleButton() {
		
		Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
					getCompositionRoot().removeStyleName(STYLE_VISIBLE);
				} else {
					getCompositionRoot().addStyleName(STYLE_VISIBLE);
				}
			}
		});
		
		valoMenuToggleButton.setIcon(FontAwesome.LIST);
		valoMenuToggleButton.addStyleName("valo-menu-toggle");
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
		
		return valoMenuToggleButton;
	}
	
	private Component buildMenuItems() {
		
		CssLayout menuItemsLayout = new CssLayout();
		menuItemsLayout.addStyleName("valo-menuitems");
		menuItemsLayout.setHeight(100.0f, Unit.PERCENTAGE);
		
		for (final ViewType view : ViewType.values()) {
			Component menuItemComponent = new ValoMenuItemButton(view);
			
			menuItemsLayout.addComponent(menuItemComponent);
		}
		return menuItemsLayout;
	}
	
	
	@Override
	public void attach() {
		super.attach();
	}
	
	@Subscribe
	public void postViewChange(final PostViewChangeEvent event) {
		// After a successful view change the menu can be hidden in mobile view.
		getCompositionRoot().removeStyleName(STYLE_VISIBLE);
	}
	
	
	@Subscribe
	public void updateUserName(final ProfileUpdatedEvent event) {
		
		User user = getCurrentUser();
		settingsItem.setText("Test");
		settingsItem.setText(user.getUserName());
	}
	
	
	public final class ValoMenuItemButton extends Button {
		
		private static final String STYLE_SELECTED = "selected";
		
		private final ViewType view;
		
		public ValoMenuItemButton(final ViewType view) {
			
			this.view = view;
			setPrimaryStyleName("valo-menu-item");
			setIcon(view.getIcon());
			setCaption(view.getViewName().substring(0, 1).toUpperCase()
					+ view.getViewName().substring(1));
			SchedulingEventBus.register(this);
			addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().getNavigator().navigateTo(view.getViewName());
				}
			});
		}
		
		@Subscribe
		public void postViewChange(final PostViewChangeEvent event) {
			
			removeStyleName(STYLE_SELECTED);
			if (event.getView() == view) {
				addStyleName(STYLE_SELECTED);
			}
		}
	}

}
