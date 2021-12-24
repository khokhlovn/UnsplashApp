package com.example.unsplashapp.ui.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unsplashapp.ui.collections.CollectionsFragment
import com.example.unsplashapp.ui.my_photos.MyPhotosFragment
import com.example.unsplashapp.ui.photos.PhotosFragment

class CustomFragmentStateAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PhotosFragment()
            1 -> CollectionsFragment()
            2 -> MyPhotosFragment()
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}
