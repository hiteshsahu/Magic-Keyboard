package com.hiteshsahu.cool_keyboard.view.activitis

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver


/**
 * Created by Hitesh.Sahu on 5/26/2017.
 *
 *
 * Base Activity  class which Enter and Exit with Circular Reveal animation
 * and bind views with ButterKnife
 */

abstract class BaseMaterialActivity : AppCompatActivity() {

    protected abstract val activityLayout: Int

    protected abstract val revealBgColor: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLayout)

        setUpToolBar()

        setUpLayout()

    }

     fun doCircularReveal(appRoot: View) {

        appRoot!!.visibility = View.INVISIBLE

        window.decorView.findViewById<View>(android.R.id.content)
                .setBackgroundColor(ContextCompat.getColor(applicationContext, revealBgColor))

        val viewTreeObserver = appRoot!!.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    circularRevealView(appRoot!!)
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        appRoot!!.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    } else {
                        appRoot!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })
        }
    }

    protected abstract fun setUpToolBar()

    protected abstract fun setUpLayout()

    /*
     Animate Circular Exit activity
     */
    fun animateExitScreen(appRoot: View, toolbar:Toolbar) {

        //Slide out toolbar to Distract user
            toolbar!!.animate().y(-500f).duration = 500


        //Circular exit Animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val anim = exitReveal(appRoot!!)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            anim!!.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    supportFinishAfterTransition()
                }
            })
            anim.start()
        } else {
            finish()
        }
    }


      private  fun circularRevealView(revealLayout: View) {

            val cx = revealLayout.width / 2
            val cy = revealLayout.height / 2

            val finalRadius = Math.max(revealLayout.width, revealLayout.height).toFloat()

            // create the animator for this view (the start radius is zero)
            var circularReveal: Animator? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                circularReveal = ViewAnimationUtils.createCircularReveal(revealLayout, cx, cy, 0f, finalRadius)

                circularReveal!!.duration = 1000

                // make the view visible and start the animation
                revealLayout.visibility = View.VISIBLE

                circularReveal.start()
            } else {
                revealLayout.visibility = View.VISIBLE
            }
        }

      private fun exitReveal(myView: View): Animator? {
            // previously visible view

            // get the center for the clipping circle
            val cx = myView.measuredWidth / 2
            val cy = myView.measuredHeight / 2


            // get the initial radius for the clipping circle
            val initialRadius = myView.width / 2

            // create the animation (the final radius is zero)
            var anim: Animator? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius.toFloat(), 0f)

                // make the view invisible when the animation is done
                anim!!.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        myView.visibility = View.INVISIBLE
                    }
                })
            }

            //  anim.setDuration(800);

            // start the animation
            return anim

    }
}
