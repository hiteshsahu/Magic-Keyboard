package com.hiteshsahu.cool_keyboard.view.fragments

import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import com.bumptech.glide.Glide
import com.hiteshsahu.cool_keyboard.R
import com.hiteshsahu.cool_keyboard.view.activitis.HomeActivity
import com.plattysoft.leonids.ParticleSystem
import kotlinx.android.synthetic.main.demo_fragment.*
import java.util.*

class MagicKeyboardFragment : Fragment(), TextWatcher {
    private var inputStringText: String? = null
    //Media player for tap sound
    private var typeSoundPlayer: MediaPlayer? = null
    private var rootView: View? = null
    private var fragmentPosition: Int = 0

    //input field

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.demo_fragment,
                container, false)

        fragmentPosition = arguments!!.getInt(ARG_SECTION_NUMBER)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initParticleMap()
        when (fragmentPosition) {
            0 -> {
                //change typing sound
                typeSoundPlayer = MediaPlayer.create(activity, R.raw.type_sound)
                //change Font
                inputEditText!!.typeface = Typeface.createFromAsset(activity!!.assets,
                        "Adler.ttf")
                //change BG
                Glide.with(activity).load(R.drawable.typewriter).into(bgCover!!)
            }
            1 -> {
                //change typing sound
                typeSoundPlayer = MediaPlayer.create(activity, R.raw.laser)
                //change font
                inputEditText!!.typeface = Typeface.createFromAsset(activity!!.assets,
                        "Starjout.ttf")
                //change BG
                Glide.with(activity).load(R.drawable.vader).into(bgCover!!)
            }
            2 -> {
                //change typing sound
                typeSoundPlayer = MediaPlayer.create(activity, R.raw.type_sound)
                //change BG
                Glide.with(activity).load(R.drawable.kitten).into(bgCover!!)
            }
        }

        inputEditText!!.addTextChangedListener(this@MagicKeyboardFragment)
        inputEditText!!.requestFocus()
    }

    private fun makeTapSound() {
        if (typeSoundPlayer!!.isPlaying) {
            typeSoundPlayer!!.stop()
            typeSoundPlayer!!.release()
            if (fragmentPosition == 0) {
                typeSoundPlayer = MediaPlayer.create(activity, R.raw.type_sound)
            } else if (fragmentPosition == 1) {
                typeSoundPlayer = MediaPlayer.create(activity, R.raw.laser)
            } else {
                typeSoundPlayer = MediaPlayer.create(activity, R.raw.type_sound)
            }
        }
        typeSoundPlayer!!.start()
    }

    private fun initParticleMap() {
        //Map for Keyboard Key drawables
        keyParticleMap = HashMap()
        //Map for Bubble Key drawables
        bubbleParticleMap = HashMap()
        //Map for StarWar Key drawables
        starWarParticleMap = HashMap()

        var charValue: Char

        for (i in INT_VALUE_OF_A until INT_VALUE_OF_Z) {
            //Char to Emit
            charValue = i.toChar()

            //Map drawable id with Char Value
            keyParticleMap[charValue] = resources.getIdentifier(charValue.toString(),
                    "drawable", //Drawable ID
                    activity!!.packageName)

            bubbleParticleMap[charValue] = resources.getIdentifier(
                    charValue.toString() + "256", //Drawable ID
                    "drawable",
                    activity!!.packageName)

            starWarParticleMap[charValue] = resources.getIdentifier(
                    charValue.toString() + "_sw", //Drawable ID
                    "drawable",
                    activity!!.packageName)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(inputCharSequence: CharSequence, start: Int, before: Int, count: Int) {

        makeTapSound()

        var inputCharacter = ""

        if (count > before) {
            // text added
            inputCharacter = inputCharSequence.toString()
                    .subSequence(before, count)
                    .toString()
        } else {
            // text Removed
            if (count > 0) {
                inputCharacter = inputStringText!!
                        .subSequence(count, before)
                        .toString()
            }
        }

        //Store for future refrence
        inputStringText = inputCharSequence.toString()

        if (fragmentPosition == 0) {
            if (!inputCharacter.isEmpty())
                emitKeyBoardParticle(inputCharacter.toLowerCase()[0])
        } else if (fragmentPosition == 1) {
            if (!inputCharacter.isEmpty())
                emitStarWarParticle(inputCharacter.toLowerCase()[0])
        } else {
            if (!inputCharacter.isEmpty())
                emitChatBubbleParticle(inputCharacter.toLowerCase()[0])
        }
    }

    override fun afterTextChanged(s: Editable) {

    }


    /**
     * @param charToEmit
     */
    private fun emitKeyBoardParticle(charToEmit: Char) {
        val drawableRedId = keyParticleMap[charToEmit]
        if (null != drawableRedId) {

            val viewID = resources
                    .getIdentifier(
                            charToEmit.toString(), "id",
                            activity!!.packageName)

            ParticleSystem(activity!!, 1, drawableRedId, 5000)
                    .setAcceleration(HomeActivity.ACCELERATION, 270)
                    .setSpeedModuleAndAngleRange(0f, 1.0f, 180, 360)
                    .setRotationSpeed(HomeActivity.ROTATION_SPEED.toFloat())
                    .setFadeOut(200, AccelerateInterpolator())
                    .emitWithGravity(rootView!!.findViewById(viewID),
                            Gravity.TOP,
                            1,
                            1000)

        }
    }

    /**
     * @param charToEmit
     */
    private fun emitChatBubbleParticle(charToEmit: Char) {

        if (null != bubbleParticleMap[charToEmit]) {

            val viewID = resources.getIdentifier(
                    charToEmit.toString(), "id",
                    activity!!.packageName)

            bubbleParticleMap[charToEmit]?.let {
                ParticleSystem(activity!!, 1,
                        it, 5000)
                        .setAcceleration(HomeActivity.ACCELERATION, 270)
                        .setSpeedModuleAndAngleRange(0f, 1.0f, 180, 360)
                        .setRotationSpeed(HomeActivity.ROTATION_SPEED.toFloat())
                        .setFadeOut(200, AccelerateInterpolator())
                        .emitWithGravity(rootView!!.findViewById(viewID),
                                Gravity.TOP,
                                1,
                                1000)
            }

        }
    }

    /**
     * @param charToEmit
     */
    private fun emitStarWarParticle(charToEmit: Char) {

        if (null != starWarParticleMap[charToEmit]) {

            val viewID = resources
                    .getIdentifier(
                            charToEmit.toString(),
                            "id",
                            activity!!.packageName)

            starWarParticleMap!![charToEmit]?.let {
                ParticleSystem(activity!!, 1,
                        it, 5000)
                        .setAcceleration(HomeActivity.ACCELERATION, 270)
                        .setSpeedModuleAndAngleRange(0f, 1.0f, 180, 360)
                        .setRotationSpeed(HomeActivity.ROTATION_SPEED.toFloat())
                        .setFadeOut(200, AccelerateInterpolator())
                        .emitWithGravity(rootView!!.findViewById(viewID),
                                Gravity.TOP,
                                1,
                                1000)
            }

        }
    }

    companion object {
        private val INT_VALUE_OF_A = 97 //int value of A
        private val INT_VALUE_OF_Z = INT_VALUE_OF_A + 26 //int value of Z
        private val ARG_SECTION_NUMBER = "FragNumber"
        //Map of Key board Character and associated Drawable ID to emit on tap of that char
        //for keyboard drawables
        lateinit var keyParticleMap: HashMap<Char, Int>
        //for  chat bubble drawables
        lateinit var bubbleParticleMap: HashMap<Char, Int>
        //for star war drawables
        lateinit var starWarParticleMap: HashMap<Char, Int>

        fun newInstance(sectionNumber: Int): MagicKeyboardFragment {
            val fragment = MagicKeyboardFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}
