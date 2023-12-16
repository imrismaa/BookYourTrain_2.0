package com.example.bookyourtrain20

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookyourtrain20.databinding.FragmentListTravelAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListTravelAdminFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListTravelAdminFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentListTravelAdminBinding
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
        binding = FragmentListTravelAdminBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {
            btnAddTravel.setOnClickListener {
                val action = ListTravelAdminFragmentDirections.actionListTravelAdminFragment2ToAddTravelFragment()
                findNavController().navigate(action)
            }
        }

        observeTravel()
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
                    "Error Listening for travel changes:", error)
            }
            val travels = snapshots?.toObjects(Travel::class.java)
            if (travels != null) {
                travelListLiveData.postValue(travels)
            }
        }
    }

    //update adapter tiap livedata berubah
    private fun observeTravel() {
        travelListLiveData.observe(viewLifecycleOwner) { travels->
            val adapterTravel = TravelAdapter(travels) { travel ->
                val action = ListTravelAdminFragmentDirections.actionListTravelAdminFragment2ToEditTravelFragment()
                travelId = travel.id
                findNavController().navigate(action)
            }

            with(binding) {
                rvListTravel.apply{
                    adapter = adapterTravel
                    layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }


    companion object {
        var travelId: String = ""
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListTravelAdminFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListTravelAdminFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}