package com.example.unsplashapp.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplashapp.R
import com.example.unsplashapp.databinding.FragmentCollectionPhotosBinding
import com.example.unsplashapp.ui.photo_details.PhotoDetailsFragmentDirections
import com.example.unsplashapp.ui.photos.adapter.PhotosPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionPhotosFragment : Fragment() {

    private lateinit var binding: FragmentCollectionPhotosBinding
    private val collectionPhotosViewModel by viewModels<CollectionPhotosViewModel>()

    @Inject
    @CollectionPhotosQualifier
    lateinit var adapter: PhotosPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollectionPhotosBinding.inflate(layoutInflater)
        return binding.root
    }

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
        binding.rvPhotos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPhotos.adapter = adapter
        val bundle = arguments ?: return
        val id = CollectionPhotosFragmentArgs.fromBundle(bundle).id
        binding.tvTitle.text = CollectionPhotosFragmentArgs.fromBundle(
            bundle
        ).title
        binding.tvDsc.visibility = View.GONE
        CollectionPhotosFragmentArgs.fromBundle(bundle).dsc?.let {
            binding.tvDsc.text = it
            binding.tvDsc.visibility = View.VISIBLE
        }
        binding.tvMeta.text = requireContext().getString(
            R.string.collection_meta,
            CollectionPhotosFragmentArgs.fromBundle(bundle).count,
            CollectionPhotosFragmentArgs.fromBundle(bundle).author
        )
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        fetchPhotos(id)
    }

    private fun setLoadStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.pb.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    private fun fetchPhotos(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            collectionPhotosViewModel.fetchCollectionPhotos(id).collect {
                binding.pb.visibility = View.GONE
                adapter.submitData(it)
            }
        }
    }
}
