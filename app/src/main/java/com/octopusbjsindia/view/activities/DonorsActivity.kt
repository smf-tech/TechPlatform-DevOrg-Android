package com.octopusbjsindia.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.octopusbjsindia.R
import com.octopusbjsindia.databinding.ActivityDonorsBinding
import com.octopusbjsindia.view.fragments.PotentialDonorFragment
import com.octopusbjsindia.view.fragments.ProspectDonorFragment

private const val NUM_TABS = 2

class DonorsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonorsBinding

    private val tabTitleArray = arrayOf(
        "Prospective",
        "Potential",
    )

    companion object{
        const val DONOR_TYPE_PROSPECT = "prospect"
        const val DONOR_TYPE_POTENTIAL = "potential"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return NUM_TABS
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return ProspectDonorFragment()
                1 -> return PotentialDonorFragment()
            }
            return ProspectDonorFragment()
        }
    }


}