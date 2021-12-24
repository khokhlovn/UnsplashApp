package com.example.unsplashapp.ui.photo_details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.unsplashapp.R
import com.example.unsplashapp.databinding.FragmentPhotoDetailsBinding
import com.example.unsplashapp.ui.collection.CollectionPhotosQualifier
import com.example.unsplashapp.ui.photos.adapter.PhotosPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private lateinit var binding: FragmentPhotoDetailsBinding
    private val photoDetailsViewModel by viewModels<PhotoDetailsViewModel>()

    @Inject
    @CollectionPhotosQualifier
    lateinit var adapter: PhotosPagingAdapter

    private var isLiked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments ?: return
        binding.authorName.text =
            PhotoDetailsFragmentArgs.fromBundle(bundle).author
        binding.likesCount.text =
            PhotoDetailsFragmentArgs.fromBundle(bundle).likes.toString()
        isLiked = PhotoDetailsFragmentArgs.fromBundle(bundle).liked
        binding.toggleLike.isChecked = isLiked
        binding.toggleLike.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isPressed) {
                if (isLiked)
                    unlikePhoto(PhotoDetailsFragmentArgs.fromBundle(bundle).id)
                else
                    likePhoto(PhotoDetailsFragmentArgs.fromBundle(bundle).id)
            }
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.putExtra(
                Intent.EXTRA_TEXT, PhotoDetailsFragmentArgs.fromBundle(
                    bundle
                ).link
            )
            startActivity(Intent.createChooser(intent, "Share"))
        }
        binding.download.setOnClickListener {
            Toast.makeText(requireContext(), "Download started", Toast.LENGTH_SHORT).show()
            photoDetailsViewModel.download(
                PhotoDetailsFragmentArgs.fromBundle(bundle).id,
                PhotoDetailsFragmentArgs.fromBundle(bundle).contentUrl
            )
        }
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date =
            format.parse(PhotoDetailsFragmentArgs.fromBundle(bundle).created)
        date?.let {
            val spf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val presentationDate = spf.format(date)
            binding.created.text = requireContext().getString(
                R.string.publication_date,
                presentationDate
            )
        }
        binding.size.text = requireContext().getString(
            R.string.size,
            PhotoDetailsFragmentArgs.fromBundle(bundle).width,
            PhotoDetailsFragmentArgs.fromBundle(bundle).height
        )
        Glide
            .with(requireContext())
            .load(PhotoDetailsFragmentArgs.fromBundle(bundle).authorUrl)
            .placeholder(R.drawable.placeholder)
            .circleCrop()
            .into(binding.authorPhoto)

        Glide
            .with(requireContext())
            .load(PhotoDetailsFragmentArgs.fromBundle(bundle).contentUrlRegular)
            .placeholder(R.drawable.placeholder)
            .into(binding.mediaImage)
    }

    private fun likePhoto(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            photoDetailsViewModel.likePhoto(id).collect {
                updateData(true)
            }
        }
    }

    private fun unlikePhoto(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            photoDetailsViewModel.unlikePhoto(id).collect {
                updateData(false)
            }
        }
    }

    private fun updateData(liked: Boolean) {
        if (liked)
            binding.likesCount.text = (binding.likesCount.text.toString().toInt() + 1).toString()
        else
            binding.likesCount.text = (binding.likesCount.text.toString().toInt() - 1).toString()
        isLiked = liked
        binding.toggleLike.isChecked = liked
    }
}
