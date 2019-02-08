package com.hiteshsahu.cool_keyboard.view.activitis

import android.animation.ArgbEvaluator
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.ImageView
import android.widget.SeekBar
import com.hiteshsahu.cool_keyboard.R
import com.hiteshsahu.cool_keyboard.view.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.demo_activity.*


class HomeActivity : BaseMaterialActivity() {
    private var indicators: Array<ImageView>? = null
    private var colorList: IntArray? = null
    private var evaluator: ArgbEvaluator? = null


    override val activityLayout: Int
        get() {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            return R.layout.demo_activity
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doCircularReveal(appRoot)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        animateExitScreen(appRoot,toolbar)
    }

    override val revealBgColor: Int
        get() = R.color.primary_500

    override fun setUpToolBar() {
        //Set Up Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    private fun updateIndicators(position: Int) {
        for (i in indicators!!.indices) {
            indicators!![i]
                    .setBackgroundResource(if (i == position)
                        R.drawable.indicator_selected
                    else
                        R.drawable.indicator_unselected)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home_navigator, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_info -> {
                startActivity(Intent(this@HomeActivity, AboutActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun setUpLayout() {
        evaluator = ArgbEvaluator()
        indicators = arrayOf(findViewById<View>(R.id.intro_indicator_0) as ImageView, findViewById<View>(R.id.intro_indicator_1) as ImageView, findViewById<View>(R.id.intro_indicator_2) as ImageView)
        colorList = intArrayOf(ContextCompat.getColor(this, R.color.cyan), ContextCompat.getColor(this, R.color.orange), ContextCompat.getColor(this, R.color.green))

        rotationBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                ROTATION_SPEED = progress
                rotationValue!!.text = getString(R.string.rpm) + ROTATION_SPEED

            }
        })

        accelarationBar!!
                .setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                    override fun onStopTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {

                    }

                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int, fromUser: Boolean) {
                        ACCELERATION = progress.toFloat() / -10000
                        accValue!!.text = getString(R.string.gravity) + progress

                    }
                })

        accValue!!.text = getString(R.string.gravity) + accelarationBar!!.progress
        rotationValue!!.text = getString(R.string.rpm) + rotationBar!!.progress
        demoPager!!.adapter = SectionsPagerAdapter(
                supportFragmentManager)
        tabLayout!!.setupWithViewPager(demoPager)
        demoPager!!
                .addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(position: Int,
                                                positionOffset: Float, positionOffsetPixels: Int) {
                        //update Page Color
                        val colorUpdate = evaluator!!.evaluate(
                                positionOffset, colorList!![position],
                                colorList!![if (position == 2)
                                    position
                                else
                                    position + 1]) as Int
                        appRoot.setBackgroundColor(colorUpdate)
                    }

                    override fun onPageSelected(position: Int) {
                        //update Indicator
                        updateIndicators(position)
                        demoPager!!.setBackgroundColor(colorList!![position])
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })

        demoPager!!.currentItem = 0
        updateIndicators(0)
    }

    companion object {
        var ACCELERATION = -0.00013f
        var ROTATION_SPEED = 144
    }
}
