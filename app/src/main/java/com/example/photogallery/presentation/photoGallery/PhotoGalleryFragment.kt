package com.example.photogallery.presentation.photoGallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.photogallery.R
import com.example.photogallery.data.PollWorker
import com.example.photogallery.data.QueryPreferences
import com.example.photogallery.databinding.FragmentPhotoGalleryBinding
import com.example.photogallery.presentation.PollNotificationHandlerFragment
import java.util.concurrent.TimeUnit

class PhotoGalleryFragment : PollNotificationHandlerFragment() {

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
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_photo_gallery, menu)
        setUpSearchView(menu)

        val toggleItem = menu.findItem(R.id.menu_item_toggle_polling)
        val isPolling = QueryPreferences.isPolling(requireContext())
        val toggleItemTitle = if (isPolling) {
            R.string.stop_polling
        } else {
            R.string.start_polling
        }
        toggleItem.setTitle(toggleItemTitle)
    }

    private fun setUpSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.d(TAG, "onQueryTextSubmit: $query")
                    viewModel.fetchPhotos(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "onQueryTextChange: $newText")
                    return false
                }
            })
            setOnSearchClickListener {
                searchView.setQuery(viewModel.searchTerm, false)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                viewModel.fetchPhotos("")
                true
            }
            R.id.menu_item_toggle_polling -> {
                onPollingItemClick()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onPollingItemClick() {
        val isPolling = QueryPreferences.isPolling(requireContext())
        if (isPolling) {
            WorkManager.getInstance(requireContext()).cancelUniqueWork(POLL_WORK)
            QueryPreferences.setPolling(requireContext(), false)
        } else {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
            val periodicRequest = PeriodicWorkRequest
                .Builder(PollWorker::class.java, REPEAT_INTERVAL, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                POLL_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
            QueryPreferences.setPolling(requireContext(), true)
        }
        activity?.invalidateOptionsMenu()
    }

    companion object {
        private const val SPAN_COUNT = 3
        private const val REPEAT_INTERVAL = 15L
        private const val TAG = "PhotoGalleryFragment"
        private const val POLL_WORK = "POLL_WORK"

        fun newInstance() = PhotoGalleryFragment()
    }
}
