package com.example.bookyourtrain20

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bookyourtrain20.databinding.FragmentAddTravelBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTravelFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAddTravelBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val travelCollectionRef = firestore.collection("travel")

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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTravelBinding.inflate(inflater, container, false)
        val view = binding.root

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

            val departureInput = editTxtDeparture.text.toString()
            val destinationInput = editTxtDestination.text.toString()
            val priceInput = editTxtPrice.text.toString()

            btnAddTravel.setOnClickListener {
                val travel = Travel(
                    departure = editTxtDeparture.text.toString(),
                    destination = editTxtDestination.text.toString(),
                    price = editTxtPrice.text.toString().toInt(),
                    train = selectedTrain
                )
                addTravel(travel)
                Toast.makeText(requireContext(), "Travel added successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        return view
    }

    private fun addTravel(travel: Travel) {
        travelCollectionRef.add(travel).addOnSuccessListener {
                documentReference ->
            val createdTravelId = documentReference.id
            travel.id = createdTravelId
            documentReference.set(travel).addOnFailureListener {
                Log.d("MainActivity", "Error updating travel id : ", it)
            }
        }.addOnFailureListener {
            Log.d("MainActivity", "Error updating travel id : ", it)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddTravelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTravelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}