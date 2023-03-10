package com.example.skillcinema.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.App
import com.example.skillcinema.databinding.FragmentImageScrollBinding
import com.example.skillcinema.presentation.adapter.movieImage.ScrollablePagingImageAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ImageScrollFragment : Fragment() {

    private var _binding: FragmentImageScrollBinding? = null
    private val binding get() = _binding!!
    private val galleryVM = App.appComponent.galleryVM()
    var imageUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageScrollBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pageName.init(galleryVM.currentListName.value){ activity?.onBackPressedDispatcher?.onBackPressed() }
        val pagedImages = galleryVM.currentPagedImages.value
        val adapter = ScrollablePagingImageAdapter()
        binding.viewPager.adapter = adapter
        pagedImages?.onEach {
            adapter.submitData(it)
        }?.launchIn(viewLifecycleOwner.lifecycleScope)

        imageUrl = galleryVM.image.value?.imageUrl
        var position = 0
        while (adapter.snapshot()[position]?.imageUrl != imageUrl) {
            position++
        }
        binding.viewPager.setCurrentItem(position, false)
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        _binding = null
        super.onDestroyView()
    }
}