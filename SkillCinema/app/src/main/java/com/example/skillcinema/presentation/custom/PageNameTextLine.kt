package com.example.skillcinema.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.skillcinema.databinding.PageNameTextLineBinding
import com.example.skillcinema.R

class PageNameTextLine @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: PageNameTextLineBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.page_name_text_line, this, true)
        binding = PageNameTextLineBinding.bind(this)
        attributesInit(attrs, defStyleAttr, defStyleRes)
    }

    private fun attributesInit(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.PageNameTextLine,
            defStyleAttr,
            defStyleRes
        )
        binding.pageName.text = typedArray.getString(R.styleable.PageNameTextLine_pageName)
        typedArray.recycle()
    }

    fun init(
        pageName: String? = null,
        onBackButtonClick: () -> Unit
    ) {
        if (pageName != null) binding.pageName.text = pageName
        binding.backButton.setOnClickListener { onBackButtonClick() }
    }
}