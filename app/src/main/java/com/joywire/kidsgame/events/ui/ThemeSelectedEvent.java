package com.joywire.kidsgame.events.ui;

import com.joywire.kidsgame.events.AbstractEvent;
import com.joywire.kidsgame.events.EventObserver;
import com.joywire.kidsgame.themes.Theme;

public class ThemeSelectedEvent extends AbstractEvent {

	public static final String TYPE = ThemeSelectedEvent.class.getName();
	public final Theme theme;

	public ThemeSelectedEvent(Theme theme) {
		this.theme = theme;
	}

	@Override
	protected void fire(EventObserver eventObserver) {
		eventObserver.onEvent(this);
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
