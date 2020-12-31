package com.joywire.kidsgame;

import android.app.Application;

import com.joywire.kidsgame.utils.FontLoader;

public class GameApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		FontLoader.loadFonts(this);
	}
}
