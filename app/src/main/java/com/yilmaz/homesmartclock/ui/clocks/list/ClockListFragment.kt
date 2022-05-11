package com.yilmaz.homesmartclock.ui.clocks.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yilmaz.homesmartclock.R
import com.yilmaz.homesmartclock.application.MyApplication
import com.yilmaz.homesmartclock.constants.MqttConstants
import com.yilmaz.homesmartclock.databinding.FragmentClockListBinding
import com.yilmaz.homesmartclock.enum.MqttConnectionState
import com.yilmaz.homesmartclock.model.Clock
import com.yilmaz.homesmartclock.ui.new_clock.NewClockFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ClockListFragment : Fragment() {
    private val TAG:String = ClockListFragment::class.java.name
    private var _binding: FragmentClockListBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private lateinit var clockAdapter : ClockListAdapter
    private lateinit var newClockFragment: NewClockFragment
    private val viewModel: ClockListFragmentViewModel by viewModels {
        MainViewModelFactory((activity?.applicationContext as MyApplication).mqttRepository,
            (activity?.applicationContext as MyApplication).clockRepository)
    }

    private var clockList : List<Clock>? = null
    private var mqttConnectionState = MqttConnectionState.DISCONNECTED.id()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockListBinding.inflate(inflater, container, false)
        clockAdapter = ClockListAdapter {clock ->
            adapterOnClick(clock)
        }
        recyclerView = binding.recyclerViewClockList
        recyclerView!!.layoutManager =
            StaggeredGridLayoutManager(1,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = clockAdapter
        clockAdapter.updateClockList(arrayListOf())
        /*
        viewModel.mqttConnectStatus.observe(viewLifecycleOwner) { connectionStatus->
            when(connectionStatus)
            {
                MqttConnectionState.CONNECTED.id->{
                    Log.d(TAG, "mqttConnectStatus: CONNECTED")
                }
                MqttConnectionState.DISCONNECTED.id->{
                    Log.d(TAG, "mqttConnectStatus: DISCONNECTED")
                }
                MqttConnectionState.ERROR.id->{
                    Log.d(TAG, "mqttConnectStatus: ERROR")
                }else->{

                }
            }
        }*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        binding.buttonSendMessage.setOnClickListener{

        }

        binding.butonClockAdd.setOnClickListener{
            newClockFragment = NewClockFragment.newInstance{ newClock->
                Log.d(TAG, "resultNewClock: butonClockAdd")
                saveNewClock(newClock)
            }
            newClockFragment.show(
                parentFragmentManager,
                "bottomsheet_newClock"
            )
        }

        binding.txtClockAdd.setOnClickListener{
            newClockFragment =
                NewClockFragment.newInstance{newClock->
                    Log.d(TAG, "resultNewClock: txtClockAdd")
                    saveNewClock(newClock)
            }
            newClockFragment.show(
                parentFragmentManager,
                "bottomsheet_newClock"
            )
        }

        viewModel.allClocks.observe(viewLifecycleOwner){ clocks->
            clockAdapter.updateClockList(clocks)
            clockList = clocks

            if(clocks.isEmpty()){
                binding.txtClockAdd.visibility = View.VISIBLE
            }else{
                binding.txtClockAdd.visibility = View.INVISIBLE
                if(mqttConnectionState == MqttConnectionState.DISCONNECTED.id){
                    connectMqttBroker()
                }
            }
        }
    }

    private fun initViewModel(){
        viewModel.mqttConnectStatus.observe(viewLifecycleOwner) { connectionStatus ->
            when (connectionStatus) {
                MqttConnectionState.CONNECTED.id->{
                    mqttConnectionState =  MqttConnectionState.CONNECTED.id
                    Log.d(TAG, "mqttConnectStatus: CONNECTED")
                    clockList?.forEach { clock->
                        viewModel.subscribe("${MqttConstants.topicTitle}/${clock.id}/${MqttConstants.publishAlive}")
                        Log.d(TAG, "initViewModel: ${MqttConstants.topicTitle}/${clock.id}/${MqttConstants.publishAlive}")
                    }
                }
                MqttConnectionState.DISCONNECTED.id->{
                    Log.d(TAG, "mqttConnectStatus: DISCONNECTED")
                    MqttConnectionState.DISCONNECTED.id
                }
                MqttConnectionState.ERROR.id->{
                    Log.d(TAG, "mqttConnectStatus: ERROR")
                    MqttConnectionState.ERROR.id
                }
                else->{
                    Log.d(TAG, "mqttConnectStatus: UNKNOWN")
                }
            }
        }
    }

    private fun connectMqttBroker(){
        viewModel.initBroker(requireContext())
    }



    private fun adapterOnClick(clock: Clock) {
        Log.d(TAG, "adapterOnClick: $clock")
        val action:NavDirections = ClockListFragmentDirections.actionFirstFragmentToSecondFragment(clock)
        findNavController().navigate(action)

    }

    private fun saveNewClock(newClock: Clock) {
        Log.d(TAG, "resultNewClock: $newClock")
        //daha önce veri tabanına aynı id eklemiş ise kullanıcıyı uyar
        clockList?.forEach {
            if(it.id == newClock.id){
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.already_registered),
                    Toast.LENGTH_LONG).show()
                return
            }
        }
        viewModel.insert(newClock)
    }

    private fun parseMqttMessage(message:String){
        if (isJsonValid(message)) {
            var data: JSONObject? = null
            data = JSONObject(message)
            // [begin command respond code]
            // [end command respond code]
        }else{
            updateUI(message)
        }
    }

    fun isJsonValid(data: String?): Boolean {
        try {
            JSONObject(data)
        } catch (ex: JSONException) {
            try {
                JSONArray(data)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }

    private fun updateUI(message: String){
    }

    override fun onResume() {
        Log.d(TAG, "onResume:")
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause:")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop:")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroyView:")
    }
}
