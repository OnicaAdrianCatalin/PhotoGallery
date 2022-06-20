package com.example.photogallery.presentation.photoGallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photogallery.databinding.FragmentPhotoGalleryBinding

class PhotoGalleryFragment : Fragment() {

    private lateinit var binding: FragmentPhotoGalleryBinding
    private val viewModel: PhotoGalleryViewModel by lazy {
        ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        binding.photoRecyclerView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel.galleryItemLiveData.observe(
            viewLifecycleOwner
        ) { galleryItems ->
            binding.photoRecyclerView.adapter =
                PhotoAdapter(galleryItems, requireContext())
        }
    }

    companion object {
        private const val SPAN_COUNT = 3

        fun newInstance() = PhotoGalleryFragment()
    }
}
