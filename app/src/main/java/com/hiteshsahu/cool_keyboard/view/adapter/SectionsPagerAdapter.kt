package com.hiteshsahu.cool_keyboard.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.hiteshsahu.cool_keyboard.view.fragments.MagicKeyboardFragment

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        //Create new Demo Fragments
        return MagicKeyboardFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "TypeWriter"
            1 -> return "Star War"
            2 -> return "Chat Bubble"
        }
        return null
    }
}