package com.example.myfamily

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private val listContacts: ArrayList<ContactModel> = ArrayList()

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
                "Srishti Tomar",
                "3rd Floor, 5/579, Vaishali Sector 5, Ghaziabad, Uttar Pradesh, 201010",
                "80%",
                "1180",
            ),
            MemberModel(
                "Riya Gupta",
                "Plot 268, Street no. 16, Gachibowli Rd, Sri Shayam Nagar, Telecom Nagar, Gachibowli, Hyderabad, 500032",
                "98%",
                "590",
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


        Log.d("FetchContact89", "fetchContact: Start krne wale hai ")

        Log.d("FetchContact89", "fetchContact: start hogya hai ${listContacts.size}")

        val inviteAdapter = InviteAdapter(listContacts)
        Log.d("FetchContact89", "fetchContact: end hogya hai ")

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("FetchContact89", "fetchContact: coroutine start")
            listContacts.addAll(fetchContacts())
            insertDataBaseContacts(listContacts)
            withContext(Dispatchers.Main){
                inviteAdapter.notifyDataSetChanged()
            }
            Log.d("FetchContact89", "fetchContact: coroutine end ${listContacts.size}")
        }


        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        inviteRecycler.adapter = inviteAdapter


    }

    private suspend fun insertDataBaseContacts(listContacts: ArrayList<ContactModel>) {
        val database = MyFamilyDatabase.getDatabse(requireContext())

        database.contactDao().insertAll(this.listContacts)
    }

    private fun fetchContacts(): ArrayList<ContactModel> {

        Log.d("FetchContact89", "fetchContacts: start ")
        val cr = requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        val listContacts:ArrayList<ContactModel> = ArrayList()

        if(cursor != null && cursor.count > 0){
            while(cursor != null && cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber > 0){
                    val pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
                        arrayOf(id),
                        ""
                    )
                    if(pCur != null && pCur.count > 0){
                        while(pCur != null && pCur.moveToNext()){
                            val phoneNum = pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            listContacts.add(ContactModel(name, phoneNum))
                        }
                        pCur.close()
                    }
                }
            }
            if(cursor != null){
                cursor.close()
            }
        }
        Log.d("FetchContact89", "fetchContacts: end ")
        return listContacts
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}