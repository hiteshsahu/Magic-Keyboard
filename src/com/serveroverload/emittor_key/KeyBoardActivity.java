package com.serveroverload.emittor_key;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.plattysoft.leonids.ParticleSystem;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

public class KeyBoardActivity extends Fragment {

	public String input;
	private boolean fontToggle;

	private static final String ARG_SECTION_NUMBER = "section_number";

	private float ACCELERATION = AppConstants.ACCELERATION_DEFAULT;
	private int ROTATION_SPEED = AppConstants.ROTATION_SPEED_DEFAULT;
	HashMap<Character, Integer> keyParticleMap;
	HashMap<Character, Integer> bubbleParticleMap;
	HashMap<Character, Integer> starWarParticleMap;
	MediaPlayer mp;
	private View rootView;
	private int position;
	private TextView outEditText;

	public static KeyBoardActivity newInstance(int sectionNumber) {
		KeyBoardActivity fragment = new KeyBoardActivity();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	

	public KeyBoardActivity() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		position = getArguments().getInt(ARG_SECTION_NUMBER);

		if (position == 0) {

			initKeyParticleMap();

			rootView = inflater.inflate(R.layout.activity_keyboard_physics,
					container, false);

			mp = MediaPlayer.create(getActivity(), R.raw.type_sound);

			outEditText = (EditText) rootView.findViewById(R.id.edit);

			Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
					"Adler.ttf");
			outEditText.setTypeface(font);
		} else if (position == 1) {

			initStarWarParticleMap();

			rootView = inflater.inflate(R.layout.activity_keyboard_physics_sw,
					container, false);

			outEditText = (EditText) rootView.findViewById(R.id.edit);

			mp = MediaPlayer.create(getActivity(), R.raw.laser);

			Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
					"Starjout.ttf");

			outEditText.setTypeface(font);

		} else if (position == 2) {

			initBubbleParticleMap();

			rootView = inflater.inflate(R.layout.activity_keyboard_physics,
					container, false);

			mp = MediaPlayer.create(getActivity(), R.raw.type_sound);

			outEditText = (EditText) rootView.findViewById(R.id.edit);

			Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
					"Adler.ttf");

			outEditText.setTypeface(font);

		}
		//
		// ImageView im = (ImageView) findViewById(R.id.background);
		//
		// Glide.with(KeyBoardActivity.this).load("")
		// .placeholder(R.drawable.typing_keyboard).into(im);

		FABRevealLayout fabRevealLayout = (FABRevealLayout) rootView
				.findViewById(R.id.fab_reveal_layout);
		configureFABReveal(fabRevealLayout);

		setUpLayout();

		return rootView;
	}

	private void setUpLayout() {
		SeekBar rotationBar = (SeekBar) rootView.findViewById(R.id.rotn);
		SeekBar accelarationBar = (SeekBar) rootView.findViewById(R.id.acc);

		// rootView.findViewById(R.id.toggle_font_root).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (fontToggle) {
		//
		// // Lazy initilization
		// initKeyParticleMap();
		//
		// // Free up memory
		// bubbleParticleMap.clear();
		//
		// ((TextView) findViewById(R.id.font_now))
		// .setText("Keyboard Key");
		//
		// } else {
		//
		// // Lazy initilization
		// initBubbleParticleMap();
		//
		// // Free up memory
		// keyParticleMap.clear();
		//
		// ((TextView) findViewById(R.id.font_now))
		// .setText("Speech Bubble");
		// }
		//
		// fontToggle = !fontToggle;
		//
		// }
		// });

		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(outEditText, InputMethodManager.SHOW_IMPLICIT);

		outEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// do your stuff here
				}
				return false;
			}

		});

		rotationBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				ROTATION_SPEED = progress;

			}
		});

		accelarationBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						ACCELERATION = ((float) progress) / (-10000);

					}
				});

		outEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				makeNoice();

				String temp = "";

				if (count > before) {

					// text added
					temp = s.toString().subSequence(before, count).toString();

					input = s.toString();
				} else {

					// backpress
					if (count > 0) {
						temp = input.subSequence(count, before).toString();
						input = s.toString();
					}
				}

				if (position == 0) {

					if (null != temp && !temp.isEmpty())
						generateKeyBoardPerticles(temp);

				} else if (position == 1) {
					if (null != temp && !temp.isEmpty())
						generateStarWarPerticles(temp);
				} else {
					if (null != temp && !temp.isEmpty())
						generateBubbleParticles(temp);
				}

			}

			private void makeNoice() {
				if (mp.isPlaying()) {
					mp.stop();
					mp.release();
					if (position == 0) {
						mp = MediaPlayer
								.create(getActivity(), R.raw.type_sound);
					} else if (position == 1) {
						mp = MediaPlayer.create(getActivity(), R.raw.laser);
					}
				}
				mp.start();
			}

		});
	}

	private void generateBubbleParticles(String temp) {

		if (null != bubbleParticleMap.get(temp.toLowerCase().charAt(0))) {

			final int envId = getResources().getIdentifier(
					String.valueOf(temp.toLowerCase().charAt(0)), "id",
					getActivity().getPackageName());

			new ParticleSystem(getActivity(), 1, bubbleParticleMap.get(temp
					.toLowerCase().charAt(0)), 5000)
					.setAcceleration(ACCELERATION, 270)
					.setSpeedModuleAndAngleRange(0f, 1.0f, 180, 360)
					.setRotationSpeed(ROTATION_SPEED)
					.setFadeOut(200, new AccelerateInterpolator())
					.emitWithGravity(rootView.findViewById(envId), Gravity.TOP,
							1, 1000);

		}
	}

	private void generateKeyBoardPerticles(String temp) {

		if (null != keyParticleMap.get(temp.toLowerCase().charAt(0))) {

			final int envId = getResources().getIdentifier(
					String.valueOf(temp.toLowerCase().charAt(0)), "id",
					getActivity().getPackageName());

			new ParticleSystem(getActivity(), 1, keyParticleMap.get(temp
					.toLowerCase().charAt(0)), 5000)
					.setAcceleration(ACCELERATION, 270)
					.setSpeedModuleAndAngleRange(0f, 1.0f, 180, 360)
					.setRotationSpeed(ROTATION_SPEED)
					.setFadeOut(200, new AccelerateInterpolator())
					.emitWithGravity(rootView.findViewById(envId), Gravity.TOP,
							1, 1000);

		}
	}

	private void initKeyParticleMap() {

		keyParticleMap = new HashMap<Character, Integer>();

		char charValue;

		for (int i = AppConstants.INT_VALUE_OF_A; i < AppConstants.INT_VALUE_OF_Z; i++) {
			charValue = (char) i;

			keyParticleMap.put(
					charValue,
					getResources().getIdentifier(String.valueOf(charValue),
							"drawable", getActivity().getPackageName()));

		}
	}

	private void initBubbleParticleMap() {

		bubbleParticleMap = new HashMap<Character, Integer>();

		char charValue;

		for (int i = AppConstants.INT_VALUE_OF_A; i < AppConstants.INT_VALUE_OF_Z; i++) {

			charValue = (char) i;

			bubbleParticleMap.put(
					charValue,
					getResources().getIdentifier(
							String.valueOf(charValue) + "256", "drawable",
							getActivity().getPackageName()));

		}
	}

	private void configureFABReveal(FABRevealLayout fabRevealLayout) {
		fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
			@Override
			public void onMainViewAppeared(FABRevealLayout fabRevealLayout,
					View mainView) {
			}

			@Override
			public void onSecondaryViewAppeared(
					final FABRevealLayout fabRevealLayout, View secondaryView) {
				prepareBackTransition(fabRevealLayout);
			}
		});
	}

	private void prepareBackTransition(final FABRevealLayout fabRevealLayout) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				fabRevealLayout.revealMainView();
			}
		}, 2000);
	}

	private void initStarWarParticleMap() {

		starWarParticleMap = new HashMap<Character, Integer>();

		char charValue;

		for (int i = AppConstants.INT_VALUE_OF_A; i < AppConstants.INT_VALUE_OF_Z; i++) {

			charValue = (char) i;

			starWarParticleMap.put(
					charValue,
					getResources().getIdentifier(
							String.valueOf(charValue) + "_sw", "drawable",
							getActivity().getPackageName()));

		}
	}

	private void generateStarWarPerticles(String temp) {

		if (null != starWarParticleMap.get(temp.toLowerCase().charAt(0))) {

			final int envId = getResources().getIdentifier(
					String.valueOf(temp.toLowerCase().charAt(0)), "id",
					getActivity().getPackageName());

			new ParticleSystem(getActivity(), 1, starWarParticleMap.get(temp
					.toLowerCase().charAt(0)), 5000)
					.setAcceleration(ACCELERATION, 270)
					.setSpeedModuleAndAngleRange(0f, 1.0f, 180, 360)
					.setRotationSpeed(ROTATION_SPEED)
					.setFadeOut(200, new AccelerateInterpolator())
					.emitWithGravity(rootView.findViewById(envId), Gravity.TOP,
							1, 1000);

		}
	}
}
