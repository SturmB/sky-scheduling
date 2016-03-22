package info.chrismcgee.sky.event;

import com.google.gwt.thirdparty.guava.common.eventbus.EventBus;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionContext;
import com.google.gwt.thirdparty.guava.common.eventbus.SubscriberExceptionHandler;

import info.chrismcgee.sky.scheduling.SchedulingUI;

/**
 * A simple wrapper for Guava event bus.
 * It is a convenience class for accessing the _UI Scoped_ SchedulingEventBus.
 * Defines static convenience methods for relevant actions.
 * We are not using a CDI event bus or anything similar, so we must have this.
 * 
 * @author Marketing
 *
 */
public class SchedulingEventBus implements SubscriberExceptionHandler {

	private final transient EventBus eventBus = new EventBus(this);
	
	public static void register(final Object listener) {
		SchedulingUI.getSchedulingEventBus().eventBus.register(listener);
	}
	
	public static void unregister(final Object listener) {
		SchedulingUI.getSchedulingEventBus().eventBus.unregister(listener);
	}

	public static void post(final Object event) {
		SchedulingUI.getSchedulingEventBus().eventBus.post(event);
	}
	
	@Override
	public void handleException(final Throwable exception,
			final SubscriberExceptionContext context) {
		
		exception.printStackTrace();
	}

}
