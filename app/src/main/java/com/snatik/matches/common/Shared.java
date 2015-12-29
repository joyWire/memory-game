package com.snatik.matches.common;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.snatik.matches.engine.Engine;
import com.snatik.matches.events.EventBus;
import com.snatik.matches.model.Model;

public class Shared {

	public static Context context;
	public static FragmentActivity activity;
	public static Engine engine;
	public static EventBus eventBus;
	public static Model model;
	
}