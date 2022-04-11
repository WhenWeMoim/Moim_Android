package com.legends.moim.src.viewMoim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.legends.moim.databinding.ItemMoimBinding
import com.legends.moim.src.viewMoim.model.Moim
import com.legends.moim.src.viewMoim.model.Moims
import com.legends.moim.src.viewMoim.model.currentMoimPosition

class RVMoimsAdapter : RecyclerView.Adapter<RVMoimsAdapter.GroupViewHolder>() {

    private lateinit var moimClickListener : MoimClickListener

    interface MoimClickListener{
        fun onItemClick(moim: Moim) //실행해줄 함수(데이터 랜더링을 위해 앨범 정보를 받아왔음!)
    }

    inner class GroupViewHolder(val binding: ItemMoimBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(moim : Moim) {
            binding.itemMoimTitleTv.text= moim.title
            binding.itemMoimExplainTv.text= moim.explain
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVMoimsAdapter.GroupViewHolder {
        val binding: ItemMoimBinding = ItemMoimBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RVMoimsAdapter.GroupViewHolder, position: Int) {
        holder.bind(Moims[position])
        holder.itemView.setOnClickListener {
            currentMoimPosition = position
            moimClickListener.onItemClick(Moims[position])
        }
    }

    override fun getItemCount(): Int = Moims.size

    fun setMoimClickListener(itemClickListener: MoimClickListener) {
        moimClickListener = itemClickListener
    }
}