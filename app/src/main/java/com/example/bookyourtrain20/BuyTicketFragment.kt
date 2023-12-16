package com.example.bookyourtrain20

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.bookyourtrain20.databinding.FragmentBuyTicketBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BuyTicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuyTicketFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentBuyTicketBinding

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
        binding = FragmentBuyTicketBinding.inflate(inflater, container, false)
        val view = binding.root

        if(ListTravelFragment.travelId.isNotEmpty()) {
            getTravelDataFromFirestore(ListTravelFragment.travelId)
        }

        with(binding) {
            val classes = resources.getStringArray(R.array.classes)
            val adapterClass = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classes)
            spinnerClass.adapter = adapterClass

            var selectedClass = ""
            spinnerClass.onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedClass = classes[position]
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
        }

        return view
    }

    @SuppressLint("SetTextI18n")
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
                    with(binding){
                        val formattedPrice = formatPrice(price.toString().toInt())
                        txtDeparture.text = "Departure \n$departure"
                        txtDestination.text = "Destination \n$destination"
                        txtPrice.text = formattedPrice
                        txtTrain.text = train
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.d("TAG", "get failed with ", exception)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BuyTicketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BuyTicketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun formatPrice(price: Int): String {
        // Use NumberFormat to format the price in IDR
        val localeID = Locale("id", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(price.toLong())
    }
}