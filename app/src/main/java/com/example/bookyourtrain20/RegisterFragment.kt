package com.example.bookyourtrain20

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bookyourtrain20.MainActivity.Companion.viewPagers
import com.example.bookyourtrain20.databinding.FragmentRegisterBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val userCollectionRef = firestore.collection("users")

    private lateinit var binding: FragmentRegisterBinding

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
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding) {
            btnSignUp.setOnClickListener {
                val usernameInput = editTxtUsername.text.toString()
                val nimInput = editTxtNim.text.toString()
                val passwordInput = editTxtPassword.text.toString()

                val createUser = User(
                    username = usernameInput,
                    nim = nimInput,
                    password = passwordInput
                )

                addUser(createUser)
                setEmptyField()
                viewPagers.currentItem = 1

            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun addUser(user: User) {
        userCollectionRef.add(user).addOnSuccessListener {
                documentReference ->
            val createdUserId = documentReference.id
            user.id = createdUserId
            documentReference.set(user).addOnFailureListener {
                Log.d("MainActivity", "Error updating budget id : ", it)
            }
        }.addOnFailureListener {
            Log.d("MainActivity", "Error updating budget id : ", it)
        }
    }

    private fun setEmptyField() {
        with(binding) {
            editTxtUsername.setText("")
            editTxtNim.setText("")
            editTxtPassword.setText("")
        }
    }
}