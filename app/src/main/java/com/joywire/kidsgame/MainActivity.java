package com.joywire.kidsgame;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.joywire.kidsgame.common.Shared;
import com.joywire.kidsgame.engine.Engine;
import com.joywire.kidsgame.engine.ScreenController;
import com.joywire.kidsgame.engine.ScreenController.Screen;
import com.joywire.kidsgame.events.EventBus;
import com.joywire.kidsgame.events.EventObserver;
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
import com.joywire.kidsgame.fragments.MenuFragment;
import com.joywire.kidsgame.ui.PopupManager;
import com.joywire.kidsgame.utils.Utils;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity  implements EventObserver, MenuFragment.OnCompleteListener  {

	private ImageView mBackgroundImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Shared.context = getApplicationContext();
		Shared.engine = Engine.getInstance();
		Shared.eventBus = EventBus.getInstance();

		setContentView(R.layout.activity_main);
		mBackgroundImage = (ImageView) findViewById(R.id.background_image);

		Shared.activity = this;
		Shared.engine.start();
		Shared.engine.setBackgroundImageView(mBackgroundImage);

		// set background
		setBackgroundImage();

		// set menu
		ScreenController.getInstance().openScreen(Screen.MENU);
		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {


			}
		});
		RequestConfiguration configuration = new RequestConfiguration.Builder()
				.setMaxAdContentRating("MAX_AD_CONTENT_RATING_G").build();
		MobileAds.setRequestConfiguration(configuration);
		Shared.eventBus.listen(GameWonEvent.TYPE, this);
	}
	AdView adView;
	void setupAds(){
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
			}

			@Override
			public void onAdFailedToLoad(LoadAdError adError) {
				// Code to be executed when an ad request fails.
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when an ad opens an overlay that
				// covers the screen.
			}

			@Override
			public void onAdClicked() {
				// Code to be executed when the user clicks on an ad.
			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when the user is about to return
				// to the app after tapping on an ad.

			}
		});
	}
	@Override
	protected void onStart() {
		super.onStart();
		this.adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getResources().getString(R.string.add_unit_banner));
		adView = findViewById(R.id.adView);
		setupAds();
		this.rewardedAd = createAndLoadRewardedAd();
	}

	@Override
	protected void onDestroy() {
		Shared.engine.stop();
		Shared.eventBus.unlisten(GameWonEvent.TYPE, this);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (PopupManager.isShown()) {
			PopupManager.closePopup();
			if (ScreenController.getLastScreen() == Screen.GAME) {
				Shared.eventBus.notify(new BackGameEvent());
			}
		} else if (ScreenController.getInstance().onBack()) {
			super.onBackPressed();
		}
	}

	private void setBackgroundImage() {
		Bitmap bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight());
		bitmap = Utils.crop(bitmap, Utils.screenHeight(), Utils.screenWidth());
		bitmap = Utils.downscaleBitmap(bitmap, 2);
		mBackgroundImage.setImageBitmap(bitmap);
	}

	RewardedAd rewardedAd;

	public RewardedAd createAndLoadRewardedAd() {
		RewardedAd rewardedAd = new RewardedAd(this,
				"ca-app-pub-3940256099942544/5224354917");
		RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
			@Override
			public void onRewardedAdLoaded() {
				// Ad successfully loaded.
			}

			@Override
			public void onRewardedAdFailedToLoad(LoadAdError adError) {
				// Ad failed to load.
			}
		};
		rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
		return rewardedAd;
	}


	@Override
	public void onEvent(FlipCardEvent event) {

	}

	@Override
	public void onEvent(DifficultySelectedEvent event) {

	}

	@Override
	public void onEvent(HidePairCardsEvent event) {

	}

	@Override
	public void onEvent(FlipDownCardsEvent event) {

	}

	@Override
	public void onEvent(StartEvent event) {

	}

	@Override
	public void onEvent(ThemeSelectedEvent event) {

	}

	public void onEvent(GameWonEvent event) {
		if (rewardedAd.isLoaded()) {
			Activity activityContext = MainActivity.this;
			RewardedAdCallback adCallback = new RewardedAdCallback() {
				@Override
				public void onRewardedAdOpened() {
					// Ad opened.
				}

				@Override
				public void onRewardedAdClosed() {
					rewardedAd = createAndLoadRewardedAd();
				}

				@Override
				public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

				}
			};
			rewardedAd.show(activityContext, adCallback);
		} else {
			Log.d("TAG", "The rewarded ad wasn't loaded yet.");
		}

	}

	@Override
	public void onEvent(BackGameEvent event) {

	}

	@Override
	public void onEvent(NextGameEvent event) {

	}

	@Override
	public void onEvent(ResetBackgroundEvent event) {
	}

	@Override
	public void onComplete(AdView adView) {
		if (adView != null){
			this.adView = adView;
			setupAds();
		}
	}
}
