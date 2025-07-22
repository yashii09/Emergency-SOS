package com.example.myfamily

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfamily.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var inviteAdapter: InviteAdapter
    private val listContacts: ArrayList<ContactModel> = ArrayList()

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 1001)
        } else {
            setupUI()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupUI()
        } else {
            Toast.makeText(requireContext(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI() {
        val listMembers = listOf(
            MemberModel("Srishti", "3rd Floor, 5/579, Vaishali Sector 5, Ghaziabad, Uttar Pradesh, 201010", "80%", "1180"),
            MemberModel("Vaibhav", "Plot 268, Street no. 16, Gachibowli Rd, Sri Shayam Nagar, Telecom Nagar, Gachibowli, Hyderabad, 500032", "98%", "590"),
            MemberModel("Yachana", "HN 188 Baheda Khurdh 1, Bahera Sandal Singh, Saharanpur, Uttar Pradesh, 247120", "88%", "1260"),
            MemberModel("Shradha    ", "Flat no.-602, Block C, PMO Apartment, Sector 62, Noida, Uttar Pradesh, 201301", "83%", "1220")
        )

        val adapter = MemberAdapter(listMembers)

        binding.recyclerMember.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMember.adapter = adapter

        inviteAdapter = InviteAdapter(listContacts)

        binding.recyclerInvite.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerInvite.adapter = inviteAdapter

        fetchDatabaseContacts()

        CoroutineScope(Dispatchers.IO).launch {
            val fetched = fetchContacts()
            insertDataBaseContacts(fetched)
        }


        binding.threeDotsSignout.setOnClickListener {

            SharedPref.putBoolean(PrefConstants.IS_USER_LOGGED_IN, false)

            FirebaseAuth.getInstance().signOut()

            Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()

            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun fetchDatabaseContacts() {
        val database = MyFamilyDatabase.getDatabse(requireContext())
        database.contactDao().getAllContacts().observe(viewLifecycleOwner) {
            listContacts.clear()
            listContacts.addAll(it)
            inviteAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun insertDataBaseContacts(newContacts: ArrayList<ContactModel>) {
        val database = MyFamilyDatabase.getDatabse(requireContext())
        database.contactDao().insertAll(newContacts)
    }

    private fun fetchContacts(): ArrayList<ContactModel> {
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        val list: ArrayList<ContactModel> = ArrayList()

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhone = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhone > 0) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf(id),
                        null
                    )

                    pCur?.use { phoneCursor ->
                        while (phoneCursor.moveToNext()) {
                            val number = phoneCursor.getString(
                                phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            list.add(ContactModel(name, number))
                        }
                    }
                }
            }
        }

        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
