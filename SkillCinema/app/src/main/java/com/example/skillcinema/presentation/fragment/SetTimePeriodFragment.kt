package com.example.skillcinema.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.skillcinema.App
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentSetTimePeriodBinding
import com.example.skillcinema.presentation.fragment.VPFragment.YearSelectorFragment
import com.example.skillcinema.presentation.viewmodel.adapter.viewPager.VPAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class SetTimePeriodFragment : Fragment() {

    private var _binding: FragmentSetTimePeriodBinding? = null
    private val binding get() = _binding!!
    private val searchPageVM = App.appComponent.searchPageVM()
    private val yearFromFragmentList = mutableListOf<YearSelectorFragment>()
    private val yearToFragmentList = mutableListOf<YearSelectorFragment>()
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    private fun onBackPressYearFromContainer() {
        val position = binding.yearFromContainer.currentItem
        Log.d("Selector", "Position: $position")
        binding.yearFromContainer.setCurrentItem(position - 1, true)
    }

    private fun onForwardPressYearFromContainer() {
        val position = binding.yearFromContainer.currentItem
        if (position < 2) {
            binding.yearFromContainer.setCurrentItem(position + 1, true)
        }
    }

    private fun onBackPressYearToContainer() {
        val position = binding.yearToContainer.currentItem
        binding.yearToContainer.setCurrentItem(position - 1, true)
    }

    private fun onForwardPressYearToContainer() {
        val position = binding.yearToContainer.currentItem
        if (position < 2) {
            binding.yearToContainer.setCurrentItem(position + 1, true)
        }
    }

    private fun fragmentListInit() {
        yearFromFragmentList.add(
            YearSelectorFragment.newInstance(
                currentYear - 24,
                { onBackPressYearFromContainer() },
                { onForwardPressYearFromContainer() }
            )
        )
        yearFromFragmentList.add(
            YearSelectorFragment.newInstance(
                currentYear - 12,
                { onBackPressYearFromContainer() },
                { onForwardPressYearFromContainer() }
            )
        )
        yearFromFragmentList.add(
            YearSelectorFragment.newInstance(
                currentYear,
                { onBackPressYearFromContainer() },
                { onForwardPressYearFromContainer() }
            )
        )

        yearToFragmentList.add(
            YearSelectorFragment.newInstance(
                currentYear - 24,
                { onBackPressYearToContainer() },
                { onForwardPressYearToContainer() }
            )
        )
        yearToFragmentList.add(
            YearSelectorFragment.newInstance(
                currentYear - 12,
                { onBackPressYearToContainer() },
                { onForwardPressYearToContainer() }
            )
        )
        yearToFragmentList.add(
            YearSelectorFragment.newInstance(
                currentYear,
                { onBackPressYearToContainer() },
                { onForwardPressYearToContainer() }
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetTimePeriodBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentListInit()
        binding.pageName.init {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        val activity = activity
        if (activity != null && yearFromFragmentList.isNotEmpty()) {
            val yearFromAdapter = VPAdapter(activity, yearFromFragmentList)
            binding.yearFromContainer.adapter = yearFromAdapter
            binding.yearFromContainer.setCurrentItem(yearFromFragmentList.size, false)
            binding.yearFromContainer.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        val position = binding.yearFromContainer.currentItem
                        yearFromFragmentList.forEach { it.clearSelectedYear() }
                        val fragmentYear = yearFromFragmentList[position].fragmentYear.value
                        when (position) {
                            0 -> {
                                if (fragmentYear >= 1000 + 12) {
                                    yearFromFragmentList[0].setYear(fragmentYear - 12)
                                    yearFromFragmentList[1].setYear(fragmentYear)
                                    yearFromFragmentList[2].setYear(fragmentYear + 12)
                                    binding.yearFromContainer.setCurrentItem(1, false)
                                }
                            }
                            yearFromFragmentList.lastIndex -> {
                                if (fragmentYear <= currentYear - 12) {
                                    yearFromFragmentList[0].setYear(fragmentYear - 12)
                                    yearFromFragmentList[1].setYear(fragmentYear)
                                    yearFromFragmentList[2].setYear(fragmentYear + 12)
                                    binding.yearFromContainer.setCurrentItem(1, false)
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
            )

            val yearToAdapter = VPAdapter(activity, yearToFragmentList)
            binding.yearToContainer.adapter = yearToAdapter
            binding.yearToContainer.setCurrentItem(yearToFragmentList.size, false)
            binding.yearToContainer.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        val position = binding.yearToContainer.currentItem
                        yearToFragmentList.forEach { it.clearSelectedYear() }
                        val fragmentYear = yearToFragmentList[position].fragmentYear.value
                        when (position) {
                            0 -> {
                                if (fragmentYear >= 1000 + 12) {
                                    yearToFragmentList[0].setYear(fragmentYear - 12)
                                    yearToFragmentList[1].setYear(fragmentYear)
                                    yearToFragmentList[2].setYear(fragmentYear + 12)
                                    binding.yearToContainer.setCurrentItem(1, false)
                                }
                            }
                            yearToFragmentList.lastIndex -> {
                                if (fragmentYear <= currentYear - 12) {
                                    yearToFragmentList[0].setYear(fragmentYear - 12)
                                    yearToFragmentList[1].setYear(fragmentYear)
                                    yearToFragmentList[2].setYear(fragmentYear + 12)
                                    binding.yearToContainer.setCurrentItem(1, false)
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
            )
        }

        binding.selectButton.setOnClickListener {
            val yearFrom = yearFromFragmentList[binding.yearFromContainer.currentItem].getSelectedYear()
            val yearTo = yearToFragmentList[binding.yearToContainer.currentItem].getSelectedYear()
            if (yearFrom != null && yearTo != null && yearFrom <= yearTo) {
                searchPageVM.setYearFrom(yearFrom)
                searchPageVM.setYearTo(yearTo)
                findNavController().navigate(R.id.action_setTimePeriodFragment_to_searchSettingsFragment)
            } else {
                Snackbar.make(
                    requireView(),
                    "Период выбран не корректно",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}