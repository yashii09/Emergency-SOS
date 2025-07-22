package com.example.myfamily

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.databinding.ItemInviteBinding

class InviteAdapter(private val listContacts: List<ContactModel>) :

    RecyclerView.Adapter<InviteAdapter.ViewHolder>(){

        lateinit var binding: ItemInviteBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ItemInviteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listContacts[position]
        holder.binding.name.text = item.name
    }

    class ViewHolder (val binding: ItemInviteBinding) : RecyclerView.ViewHolder(binding.root)

}


