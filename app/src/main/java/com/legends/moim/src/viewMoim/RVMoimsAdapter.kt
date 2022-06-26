package com.legends.moim.src.viewMoim

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.legends.moim.databinding.ItemMoimBinding
import com.legends.moim.src.viewMoim.model.ListMoimInfo
import com.legends.moim.src.viewMoim.model.currentMoimPosition

class RVMoimsAdapter(private val moims: Array<ListMoimInfo>) : RecyclerView.Adapter<RVMoimsAdapter.GroupViewHolder>() {

    private lateinit var moimClickListener : MoimClickListener

    interface MoimClickListener{
        fun onItemClick(listMoimInfo: ListMoimInfo) //실행해줄 함수(데이터 랜더링을 위해 앨범 정보를 받아왔음!)
    }

    inner class GroupViewHolder(val binding: ItemMoimBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(moim: ListMoimInfo) {
            binding.itemMoimTitleTv.text= moim.moimTitle
            binding.itemMoimExplainTv.text= moim.moimDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVMoimsAdapter.GroupViewHolder {
        val binding: ItemMoimBinding = ItemMoimBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RVMoimsAdapter.GroupViewHolder, position: Int) {
        holder.bind(moims[position])
        holder.itemView.setOnClickListener {
            currentMoimPosition = position
            moimClickListener.onItemClick(moims[position])
        }
    }

    override fun getItemCount(): Int = moims.size

    fun setMoimClickListener(itemClickListener: MoimClickListener) {
        moimClickListener = itemClickListener
    }
}