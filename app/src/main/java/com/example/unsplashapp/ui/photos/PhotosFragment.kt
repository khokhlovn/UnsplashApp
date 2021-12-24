package com.example.unsplashapp.ui.photos

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplashapp.Constants
import com.example.unsplashapp.R
import com.example.unsplashapp.databinding.FragmentPhotosBinding
import com.example.unsplashapp.ui.photo_details.PhotoDetailsFragmentDirections
import com.example.unsplashapp.ui.photos.adapter.PhotosPagingAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private lateinit var binding: FragmentPhotosBinding
    private val photosViewModel by viewModels<PhotosViewModel>()

    @Inject
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
        setupViews()
        adapter.listener = { link, id, author, likes, liked, authorUrl,
                             contentUrl, contentUrlRegular, created, width, height ->
            findNavController().navigate(
                PhotoDetailsFragmentDirections.openPhoto(
                    link, id, author, likes, liked, authorUrl,
                    contentUrl, contentUrlRegular, created, width, height
                )
            )
        }
        binding.rvPhotos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPhotos.adapter = adapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            photosViewModel.order = "Latest"
            fetchPhotos()
        }
        if (binding.searchView.query.toString() == "")
            fetchPhotos()
        else
            fetchPhotos(binding.searchView.query.toString())
    }

    private fun setupViews() {
        binding.searchView.isIconified = false
        binding.searchView.setOnQueryTextListener(object
            : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchPhotos(query)
                }
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (newText == "") {
                        searchView.clearFocus()
                        fetchPhotos()
                    }
                }
                return false
            }
        })
        val clearButton = binding.searchView.findViewById<ImageView>(
            androidx.appcompat.R.id.search_close_btn
        )
        clearButton.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
            fetchPhotos()
        }
        binding.floatingActionButton.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(Constants.UPLOAD_IMAGE_URL))
        }
        val orderOptions = resources.getStringArray(R.array.orders)
        binding.filter.setOnClickListener {
            val currentSelection = orderOptions.indexOf(photosViewModel.order)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.sort_by)
                .setSingleChoiceItems(orderOptions, currentSelection) { dialog, which ->
                    if (which != currentSelection) {
                        photosViewModel.order = orderOptions[which]
                        fetchPhotos()
                    }
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun setLoadStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.pb.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    private fun fetchPhotos(query: String = "") {
        viewLifecycleOwner.lifecycleScope.launch {
            photosViewModel.fetchPhotos(query).collect {
                binding.pb.visibility = View.GONE
                adapter.submitData(it)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
