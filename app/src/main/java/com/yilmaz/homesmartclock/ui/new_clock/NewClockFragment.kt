package com.yilmaz.homesmartclock.ui.new_clock

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yilmaz.homesmartclock.R
import com.yilmaz.homesmartclock.base.BaseBottomSheetFragment
import com.yilmaz.homesmartclock.databinding.FragmentNewClockDialogBinding
import com.yilmaz.homesmartclock.model.Clock

const val ARG_ITEM_COUNT = "item_count"

class NewClockFragment() : BaseBottomSheetFragment() {

    private var _binding: FragmentNewClockDialogBinding? = null
    private val binding get() = _binding!!

    private var WARNING_CLOCK_NAME = ""
    private var WARNING_CLOCK_ID = ""

    companion object {
        private lateinit var newClockResult: (Clock) -> Unit
        fun newInstance(result: (Clock) -> Unit): NewClockFragment
        {
            newClockResult = result
            return NewClockFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewClockDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        WARNING_CLOCK_NAME = resources.getString(R.string.clock_name_empty_warning)
        WARNING_CLOCK_ID = resources.getString(R.string.clock_id_empty_warning)
        clickEvent()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun clickEvent(){
        binding.buttonSaveClock.setOnClickListener{
            saveClock()
        }
    }

    private fun saveClock(){
        val clockName = binding.editTextTextClockName.text.toString()
        val clockID = binding.editTextClockID.text.toString()
        if(TextUtils.isEmpty(clockName)){
            Toast.makeText(requireContext(), WARNING_CLOCK_NAME, Toast.LENGTH_SHORT).show()
            return
        }
        if(TextUtils.isEmpty(clockID)){
            Toast.makeText(requireContext(), WARNING_CLOCK_ID, Toast.LENGTH_SHORT).show()
            return
        }
        val clock = Clock(clockID.toLong(), clockName.capitalize())
        newClockResult(clock)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}