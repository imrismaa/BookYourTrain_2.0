package com.example.bookyourtrain20

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.example.bookyourtrain20.databinding.FragmentEditTravelBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditTravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditTravelFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentEditTravelBinding

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
        binding = FragmentEditTravelBinding.inflate(inflater, container, false)
        val view = binding.root

        if(ListTravelAdminFragment.travelId.isNotEmpty()) {
            getTravelDataFromFirestore(ListTravelAdminFragment.travelId)
        }

        with(binding) {
            val train = resources.getStringArray(R.array.train)
            val adapterTrain = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, train)
            spinnerTrain.adapter = adapterTrain

            var selectedTrain = ""
            spinnerTrain.onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedTrain = train[position]
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
        }

        return view
    }

    private fun getTravelDataFromFirestore(travelId: String) {
        val travelCollectionRef = FirebaseFirestore.getInstance().collection("travel")

        travelCollectionRef
            .whereEqualTo("id", travelId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val travelDocument = documents.documents[0]
                    val departure = travelDocument.getString("departure")
                    val destination = travelDocument.getString("destination")
                    val price = travelDocument.getLong("price")
                    val train = travelDocument.getString("train")

                    Log.d("TAG", "Departure: $departure, Destination: $destination, Price: $price, Train: $train")

                    with(binding){
                        editTxtDeparture.setText(departure)
                        editTxtDestination.setText(destination)
                        editTxtPrice.setText(price.toString())
                        spinnerTrain.setSelection(getIndex(spinnerTrain, train.toString()))
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.d("TAG", "get failed with ", exception)
            }
    }

    private fun getIndex(spinnerTrain: AppCompatSpinner, toString: String): Int {
        for (i in 0 until spinnerTrain.count) {
            if (spinnerTrain.getItemAtPosition(i).toString() == toString) {
                return i
            }
        }
        return 0
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditTravelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditTravelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}