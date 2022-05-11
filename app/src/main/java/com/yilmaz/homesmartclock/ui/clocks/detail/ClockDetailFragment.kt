package com.yilmaz.homesmartclock.ui.clocks.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yilmaz.homesmartclock.R
import com.yilmaz.homesmartclock.application.MyApplication
import com.yilmaz.homesmartclock.databinding.FragmentClockDetailBinding
import com.yilmaz.homesmartclock.enum.MqttConnectionState
import com.yilmaz.homesmartclock.ui.clocks.list.ClockListFragmentDirections
import com.yilmaz.homesmartclock.ui.clocks.setting.SettingFragmentDirections

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ClockDetailFragment : Fragment() {

    private var _binding: FragmentClockDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ClockDetailFragmentArgs>()

    private val viewModel by viewModels<ClockDetailViewModel> {
        Factory((activity?.applicationContext as MyApplication).mqttRepository)
    }
    private var mqttConnectionState = MqttConnectionState.DISCONNECTED.id()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewClockName.text = "${args.clock?.name}"
        binding.textViewClockIdentity.text = "${resources.getString(R.string.clock_id)} ${args.clock?.id}"

        clickEvent()
        initViewModel()

    }

    private fun clickEvent(){
        binding.imageViewSetting.setOnClickListener {
            val action: NavDirections =
                ClockDetailFragmentDirections.actionClockDetailFragmentToSettingFragment(null)
            findNavController().navigate(action)
        }

        binding.imageButtonSendMessage.setOnClickListener {

            if(mqttConnectionState == MqttConnectionState.CONNECTED.id){
                val message = binding.editTextTextMessage.text.toString()
                if (message.isNotEmpty()){
                    viewModel.publishMessage(message, args.clock?.id.toString())
                }
            }else{
                Toast.makeText(requireContext(),resources.getString(R.string.clock_failed_connect), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViewModel(){
        viewModel.mqttConnectStatus.observe(viewLifecycleOwner){
            mqttConnectionState = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}