package com.pangidoannsh.mystories.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pangidoannsh.mystories.view.auth.LoginFragment
import com.pangidoannsh.mystories.view.auth.RegisterFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        if (position == 0) LoginFragment() else RegisterFragment()

}