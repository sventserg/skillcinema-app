package com.example.skillcinema.presentation.fragment.view_pager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.databinding.ViewPagerFragmentYearSelectorBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class YearSelectorFragment(
    private val year: Int,
    private val onBackPress: () -> Unit,
    private val onForwardPress: () -> Unit
) : Fragment() {

    private var _binding: ViewPagerFragmentYearSelectorBinding? = null
    private val binding get() = _binding!!
    private val _year = MutableStateFlow(year)
    val fragmentYear = _year.asStateFlow()
    private var selectedYear: Int? = null
    fun getSelectedYear(): Int? {
        return selectedYear
    }

    fun setYear(newYear: Int) {
        _year.value = newYear
        binding.yearSelector.initYearSelector(
            newYear,
            onBackPress = { onBackPress() },
            onForwardPress = { onForwardPress() })
    }

    fun clearSelectedYear() {
        if (_binding != null) {
            binding.yearSelector.clearSelectedYear()
            selectedYear = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ViewPagerFragmentYearSelectorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.yearSelector.initYearSelector(year,
            onBackPress = { onBackPress() },
            onForwardPress = { onForwardPress() })
        viewLifecycleOwner.lifecycleScope.launch {
            binding.yearSelector.selectedYear.collect {
                selectedYear = it
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(
            year: Int, onBackPress: () -> Unit, onForwardPress: () -> Unit
        ) = YearSelectorFragment(
            year,
            onBackPress = { onBackPress() },
            onForwardPress = { onForwardPress() })
    }
}