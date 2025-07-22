package com.example.myfamily

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.databinding.ItemMemberBinding

class MemberAdapter(private val listMembers: List<MemberModel>) : RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    lateinit var binding: ItemMemberBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapter.ViewHolder {

        binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberAdapter.ViewHolder, position: Int) {
        val item = listMembers[position]
        holder.name.text = item.name
        holder.address.text = item.address
        holder.battery.text = item.battery
        holder.distance.text = item.distance
    }

    override fun getItemCount(): Int {
        return listMembers.size
    }

    class ViewHolder(private val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageUser = binding.imgUser
        val name = binding.name
        val address = binding.address
        val battery = binding.batteryPercent
        val distance = binding.distanceValue
    }

}