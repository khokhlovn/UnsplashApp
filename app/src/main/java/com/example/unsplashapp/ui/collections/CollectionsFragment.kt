package com.example.unsplashapp.ui.collections

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
import com.example.unsplashapp.databinding.FragmentCollectionsBinding
import com.example.unsplashapp.ui.collection.CollectionPhotosFragmentDirections
import com.example.unsplashapp.ui.collections.adapter.CollectionsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionsFragment : Fragment() {

    private lateinit var binding: FragmentCollectionsBinding
    private val collectionsViewModel by viewModels<CollectionsViewModel>()

    @Inject
    lateinit var adapter: CollectionsPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCollectionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoadStates()
        adapter.listener = { id, title, dsc, count, author ->
            findNavController().navigate(
                CollectionPhotosFragmentDirections.openCollectionPhotos(
                    id,
                    title,
                    dsc,
                    count,
                    author
                )
            )
        }
        binding.rvCollections.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCollections.adapter = adapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchCollections()
        }
        fetchCollections()
    }

    private fun setLoadStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.pb.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    private fun fetchCollections() {
        viewLifecycleOwner.lifecycleScope.launch {
            collectionsViewModel.fetchCollections().collect {
                binding.pb.visibility = View.GONE
                adapter.submitData(it)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
