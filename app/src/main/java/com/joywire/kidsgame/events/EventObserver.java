package com.joywire.kidsgame.events;

import com.joywire.kidsgame.events.engine.FlipDownCardsEvent;
import com.joywire.kidsgame.events.engine.GameWonEvent;
import com.joywire.kidsgame.events.engine.HidePairCardsEvent;
import com.joywire.kidsgame.events.ui.BackGameEvent;
import com.joywire.kidsgame.events.ui.DifficultySelectedEvent;
import com.joywire.kidsgame.events.ui.FlipCardEvent;
import com.joywire.kidsgame.events.ui.NextGameEvent;
import com.joywire.kidsgame.events.ui.ResetBackgroundEvent;
import com.joywire.kidsgame.events.ui.StartEvent;
import com.joywire.kidsgame.events.ui.ThemeSelectedEvent;


public interface EventObserver {

	void onEvent(FlipCardEvent event);

	void onEvent(DifficultySelectedEvent event);

	void onEvent(HidePairCardsEvent event);

	void onEvent(FlipDownCardsEvent event);

	void onEvent(StartEvent event);

	void onEvent(ThemeSelectedEvent event);

	void onEvent(GameWonEvent event);

	void onEvent(BackGameEvent event);

	void onEvent(NextGameEvent event);

	void onEvent(ResetBackgroundEvent event);

}
