package com.example.myfamily

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.R

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMembers = listOf<MemberModel>(
            MemberModel(
                "Vaibhav Srivastava",
                "Plot 268, Street no. 16, Gachibowli Rd, Sri Shayam Nagar, Telecom Nagar, Gachibowli, Hyderabad, 500032",
                "98%",
                "590",
            ),
            MemberModel(
                "Srishti Tomar",
                "3rd Floor, 5/579, Vaishali Sector 5, Ghaziabad, Uttar Pradesh, 201010",
                "80%",
                "1180",
            ),
            MemberModel(
                "Yachana Singh",
                "HN 188 Baheda Khurdh 1, Bahera Sandal Singh, Saharanpur, Uttar Pradesh, 247120",
                "88%",
                "1260",
            ),
            MemberModel(
                "Shradha Singh",
                "Flat no.-602, Block C, PMO Apartment, Sector 62, Noida, Uttar Pradesh, 201301",
                "83%",
                "1220"
            )
        )

        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        val listContacts = listOf<ContactModel>(
            ContactModel(
              "Vaibhav",
                7786873783
            ),
            ContactModel(
                "Srishti",
                7668925117
            ),
            ContactModel(
                "Riya ",
                7726959188
            ),
            ContactModel(
                "Yachana",
                9410130633
            )
        )

        val inviteAdapter = InviteAdapter(listContacts)

        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager = LinearLayoutManager(requireContext())
        inviteRecycler.adapter = inviteAdapter


    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}