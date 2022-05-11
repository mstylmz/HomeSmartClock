package com.yilmaz.homesmartclock.ui.clocks.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yilmaz.homesmartclock.R
import com.yilmaz.homesmartclock.model.Clock

class ClockListAdapter(private val onClick: (Clock) -> Unit) : RecyclerView.Adapter<ClockListAdapter.ViewHolder>() {
    private val TAG = ClockListAdapter::class.java.simpleName

    private lateinit var clockList:List<Clock>

    class ViewHolder(view: View, onClick: (Clock) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val textView: TextView
        private val imageView: ImageView
        private var currentClock: Clock? = null

        init {
            textView = view.findViewById(R.id.txtClocknName)
            imageView = view.findViewById(R.id.imageViewClock)
            itemView.setOnClickListener {
                currentClock?.let {
                    onClick(it)
                }
            }
        }

        fun bind(clock: Clock) {
            currentClock = clock
            textView.text = clock.name
            /*
            if (clock.image != null)
                imageView.setImageResource(clock.image)
            else
                imageView.setImageResource(R.drawable.clock_outline)*/
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.clock_list_item, viewGroup, false)
        return ViewHolder(view, onClick)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateClockList(list:List<Clock>?){
        if (list != null) {
            this.clockList = list
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val clock = clockList[position]
        holder.bind(clock)
    }

    override fun getItemCount(): Int {
        return if(clockList.isEmpty()) 0 else clockList.size
    }
}