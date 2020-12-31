package com.joywire.kidsgame.events;

import com.joywire.kidsgame.events.engine.FlipDownCardsEvent;
import com.joywire.kidsgame.events.engine.GameWonEvent;
import com.joywire.kidsgame.events.engine.HidePairCardsEvent;
import com.joywire.kidsgame.events.ui.BackGameEvent;
import com.joywire.kidsgame.events.ui.FlipCardEvent;
import com.joywire.kidsgame.events.ui.NextGameEvent;
import com.joywire.kidsgame.events.ui.ResetBackgroundEvent;
import com.joywire.kidsgame.events.ui.ThemeSelectedEvent;
import com.joywire.kidsgame.events.ui.DifficultySelectedEvent;
import com.joywire.kidsgame.events.ui.StartEvent;


public class EventObserverAdapter implements EventObserver {

	public void onEvent(FlipCardEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(DifficultySelectedEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(HidePairCardsEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(FlipDownCardsEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(StartEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(ThemeSelectedEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(GameWonEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(BackGameEvent event) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void onEvent(NextGameEvent event) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public void onEvent(ResetBackgroundEvent event) {
		throw new UnsupportedOperationException();		
	}

}
