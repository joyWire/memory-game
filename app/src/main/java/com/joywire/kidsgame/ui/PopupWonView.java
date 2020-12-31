package com.joywire.kidsgame.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.joywire.kidsgame.R;
import com.joywire.kidsgame.common.Music;
import com.joywire.kidsgame.common.Shared;
import com.joywire.kidsgame.events.ui.BackGameEvent;
import com.joywire.kidsgame.events.ui.NextGameEvent;
import com.joywire.kidsgame.model.GameState;
import com.joywire.kidsgame.utils.Clock;
import com.joywire.kidsgame.utils.Clock.OnTimerCount;
import com.joywire.kidsgame.utils.FontLoader;
import com.joywire.kidsgame.utils.FontLoader.Font;

import androidx.annotation.NonNull;

public class PopupWonView extends RelativeLayout {

	private TextView mTime;
	private TextView mScore;
	private ImageView mStar1;
	private ImageView mStar2;
	private ImageView mStar3;
	private ImageView mNextButton;
	private ImageView mBackButton;
	private Handler mHandler;
	RewardedAd rewardedAd;
	public PopupWonView(Context context) {
		this(context, null);
	}

	public PopupWonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.popup_won_view, this, true);
		mTime = (TextView) findViewById(R.id.time_bar_text);
		mScore = (TextView) findViewById(R.id.score_bar_text);
		mStar1 = (ImageView) findViewById(R.id.star_1);
		mStar2 = (ImageView) findViewById(R.id.star_2);
		mStar3 = (ImageView) findViewById(R.id.star_3);
		mBackButton = (ImageView) findViewById(R.id.button_back);
		mNextButton = (ImageView) findViewById(R.id.button_next);
		FontLoader.setTypeface(context, new TextView[] { mTime, mScore }, Font.GROBOLD);
		setBackgroundResource(R.drawable.level_complete);
		mHandler = new Handler();
		
		mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared.eventBus.notify(new BackGameEvent());
			}
		});
		
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared.eventBus.notify(new NextGameEvent());
			}
		});
		setupVideoAds(Shared.context, Shared.activity);
	}

	public void setGameState(final GameState gameState) {
		int min = gameState.remainedSeconds / 60;
		int sec = gameState.remainedSeconds - min * 60;
		mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
		mScore.setText("" + 0);
		
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				animateScoreAndTime(gameState.remainedSeconds, gameState.achievedScore);
				animateStars(gameState.achievedStars);
			}
		}, 500);
	}

	private void animateStars(int start) {
		switch (start) {
		case 0:
			mStar1.setVisibility(View.GONE);
			mStar2.setVisibility(View.GONE);
			mStar3.setVisibility(View.GONE);
			break;
		case 1:
			mStar2.setVisibility(View.GONE);
			mStar3.setVisibility(View.GONE);
			mStar1.setAlpha(0f);
			animateStar(mStar1, 0);
			break;
		case 2:
			mStar3.setVisibility(View.GONE);
			mStar1.setVisibility(View.VISIBLE);
			mStar1.setAlpha(0f);
			animateStar(mStar1, 0);
			mStar2.setVisibility(View.VISIBLE);
			mStar2.setAlpha(0f);
			animateStar(mStar2, 600);
			break;
		case 3:
			mStar1.setVisibility(View.VISIBLE);
			mStar1.setAlpha(0f);
			animateStar(mStar1, 0);
			mStar2.setVisibility(View.VISIBLE);
			mStar2.setAlpha(0f);
			animateStar(mStar2, 600);
			mStar3.setVisibility(View.VISIBLE);
			mStar3.setAlpha(0f);
			animateStar(mStar3, 1200);
			break;
		default:
			break;
		}
	}
	
	private void animateStar(final View view, int delay) {
		ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
		alpha.setDuration(100);
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1f);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(alpha, scaleX, scaleY);
		animatorSet.setInterpolator(new BounceInterpolator());
		animatorSet.setStartDelay(delay);
		animatorSet.setDuration(600);
		view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		animatorSet.start();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Music.showStar();
			}
		}, delay);
	}

	private void animateScoreAndTime(final int remainedSeconds, final int achievedScore) {
		final int totalAnimation = 1200;

		Clock.getInstance().startTimer(totalAnimation, 35, new OnTimerCount() {

			@Override
			public void onTick(long millisUntilFinished) {
				float factor = millisUntilFinished / (totalAnimation * 1f); // 0.1
				int scoreToShow = achievedScore - (int) (achievedScore * factor);
				int timeToShow = (int) (remainedSeconds * factor);
				int min = timeToShow / 60;
				int sec = timeToShow - min * 60;
				mTime.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec));
				mScore.setText("" + scoreToShow);
			}

			@Override
			public void onFinish() {
				mTime.setText(" " + String.format("%02d", 0) + ":" + String.format("%02d", 0));
				mScore.setText("" + achievedScore);
			}
		});

	}
	public void setupVideoAds(Context context, Activity activityContext){
		RewardedAd rewardedAd = new RewardedAd(context,
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
		if (rewardedAd.isLoaded()) {
			RewardedAdCallback adCallback = new RewardedAdCallback() {
				@Override
				public void onRewardedAdOpened() {
					// Ad opened.
				}

				@Override
				public void onRewardedAdClosed() {
					// Ad closed.
				}

				@Override
				public void onUserEarnedReward(@NonNull RewardItem reward) {
					// User earned reward.
				}

				@Override
				public void onRewardedAdFailedToShow(AdError adError) {
					// Ad failed to display.
				}
			};
			rewardedAd.show(activityContext, adCallback);
		} else {
			Log.d("TAG", "The rewarded ad wasn't loaded yet.");
		}

	}
}
