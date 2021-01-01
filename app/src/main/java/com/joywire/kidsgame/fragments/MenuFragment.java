package com.joywire.kidsgame.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.joywire.kidsgame.R;
import com.joywire.kidsgame.common.Music;
import com.joywire.kidsgame.common.Shared;
import com.joywire.kidsgame.events.ui.StartEvent;
import com.joywire.kidsgame.ui.PopupManager;
import com.joywire.kidsgame.utils.Utils;

public class MenuFragment extends Fragment {

	private ImageView mTitle;
	private ImageView mStartGameButton;
	private ImageView mStartButtonLights;
	private ImageView mSettingsGameButton;
	AdView adView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_fragment, container, false);
		mTitle = (ImageView) view.findViewById(R.id.title);
		mStartGameButton = (ImageView) view.findViewById(R.id.start_game_button);
		mSettingsGameButton = (ImageView) view.findViewById(R.id.settings_game_button);
		mSettingsGameButton.setSoundEffectsEnabled(false);
		mSettingsGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupManager.showPopupSettings();
			}
		});

		mStartButtonLights = (ImageView) view.findViewById(R.id.start_game_button_lights);
		mStartGameButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// animate title from place and navigation buttons from place
				animateAllAssetsOff(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						Shared.eventBus.notify(new StartEvent());
					}
				});
			}
		});

		startLightsAnimation();
		startTootipAnimation();

		// play background music
		Music.playBackgroundMusic();

		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.adView = new AdView(Shared.context);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getResources().getString(R.string.add_unit_banner));
		adView = view.findViewById(R.id.adView);
		mListener.onComplete(adView);
	}

	protected void animateAllAssetsOff(AnimatorListenerAdapter adapter) {
		// title
		// 120dp + 50dp + buffer(30dp)
		ObjectAnimator titleAnimator = ObjectAnimator.ofFloat(mTitle, "translationY", Utils.px(-200));
		titleAnimator.setInterpolator(new AccelerateInterpolator(2));
		titleAnimator.setDuration(300);

		// lights
		ObjectAnimator lightsAnimatorX = ObjectAnimator.ofFloat(mStartButtonLights, "scaleX", 0f);
		ObjectAnimator lightsAnimatorY = ObjectAnimator.ofFloat(mStartButtonLights, "scaleY", 0f);

		// settings button
		ObjectAnimator settingsAnimator = ObjectAnimator.ofFloat(mSettingsGameButton, "translationY", Utils.px(120));
		settingsAnimator.setInterpolator(new AccelerateInterpolator(2));
		settingsAnimator.setDuration(300);

		// start button
		ObjectAnimator startButtonAnimator = ObjectAnimator.ofFloat(mStartGameButton, "translationY", Utils.px(130));
		startButtonAnimator.setInterpolator(new AccelerateInterpolator(2));
		startButtonAnimator.setDuration(300);

		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.addListener(adapter);
		animatorSet.start();
	}

	private void startTootipAnimation() {
		final AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setStartDelay(1000);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animatorSet.setStartDelay(2000);
				animatorSet.start();
			}
		});
		animatorSet.start();
	}

	private void startLightsAnimation() {
		ObjectAnimator animator = ObjectAnimator.ofFloat(mStartButtonLights, "rotation", 0f, 360f);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.setDuration(6000);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		mStartButtonLights.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		animator.start();
	}
	private MenuFragment.OnCompleteListener mListener;
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			this.mListener = (MenuFragment.OnCompleteListener)context;
		}
		catch (final ClassCastException e) {
			throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
		}
	}


	public static interface OnCompleteListener {
		public abstract void onComplete(AdView adView);
	}

}
