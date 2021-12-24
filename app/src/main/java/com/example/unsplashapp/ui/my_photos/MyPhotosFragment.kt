package com.example.unsplashapp.ui.my_photos

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplashapp.Constants
import com.example.unsplashapp.databinding.FragmentPhotosBinding
import com.example.unsplashapp.ui.photo_details.PhotoDetailsFragmentDirections
import com.example.unsplashapp.ui.photos.adapter.PhotosPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyPhotosFragment : Fragment() {

    private lateinit var binding: FragmentPhotosBinding
    private val myPhotosViewModel by viewModels<MyPhotosViewModel>()

    @Inject
    @MyPhotosQualifier
    lateinit var adapter: PhotosPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotosBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoadStates()
        adapter.listener = { link, id, author, likes, liked, authorUrl,
                             contentUrl, contentUrlRegular, created, width, height ->
            findNavController().navigate(
                PhotoDetailsFragmentDirections.openPhoto(
                    link, id, author, likes, liked, authorUrl,
                    contentUrl, contentUrlRegular, created, width, height
                )
            )
        }
        binding.searchView.visibility = View.GONE
        binding.filter.visibility = View.GONE
        binding.floatingActionButton.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(Constants.UPLOAD_IMAGE_URL))
        }
        binding.rvPhotos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPhotos.adapter = adapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchPhotos()
        }
        fetchPhotos()
    }

    private fun setLoadStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.pb.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    private fun fetchPhotos() {
        viewLifecycleOwner.lifecycleScope.launch {
            myPhotosViewModel.fetchMyPhotos().collect {
                binding.pb.visibility = View.GONE
                adapter.submitData(it)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
