package com.example.bookyourtrain20

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.bookyourtrain20.databinding.FragmentListTravelBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListTravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListTravelFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentListTravelBinding

    private val firestore = FirebaseFirestore.getInstance()
    private val travelsCollectionRef = firestore.collection("travel")
    private val travelListLiveData: MutableLiveData<List<Travel>> by lazy {
        MutableLiveData<List<Travel>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListTravelBinding.inflate(inflater, container, false)
        val view = binding.root

        observeNotes()
        getAllTravels()
        return view
    }

    private fun getAllTravels() {
        observeTravelsChange()
    }

    override fun onResume() {
        super.onResume()
        getAllTravels()
    }

    private fun observeTravelsChange() {
        travelsCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("MainActivity",
                    "Error Listening for budget changes:", error)
            }
            val budgets = snapshots?.toObjects(Travel::class.java)
            if (budgets != null) {
                travelListLiveData.postValue(budgets)
            }
        }
    }

    //update adapter tiap livedata berubah
    private fun observeNotes() {
        travelListLiveData.observe(this) { travels->
            val adapterNote = TravelAdapter(travels) { travel ->
                val action = ListTravelFragmentDirections.actionListTravelFragment2ToBuyTicketFragment()
                findNavController().navigate(action)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListTravelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListTravelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}