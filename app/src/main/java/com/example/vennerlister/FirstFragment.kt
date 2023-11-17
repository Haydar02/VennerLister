package com.example.vennerlister

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vennerlister.databinding.FragmentFirstBinding
import com.example.vennerlister.models.PersonViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val personViewModel : PersonViewModel by activityViewModels()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    // private val handler = Handler(Looper.getMainLooper())
    //private val sessionTimeoutInSeconds = 60L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val currentUser = auth.currentUser
        if(currentUser != null){
           findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.editTextEmail.text.trim().toString()
            if (email.isEmpty()) {
                binding.editTextEmail.error = "No e_mail"
                return@setOnClickListener
            }

            val password = binding.editTextPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editTextPassword.error = "No Password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("APPLE", "createUserWithEmail : success")
                        val user = auth.currentUser
                        val userEmail = user?.email
                        if(userEmail != null){
                        personViewModel.setUserEmail(userEmail)
                            val savedEmail = userEmail
                        personViewModel.getPersonByUserId(savedEmail)
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                            //extendSession()
                            //sessionTimeoutInSeconds
                        }
                        else{
                            Log.e("APPLE","User Email is empty or null")
                        }

                    } else {
                        Log.w("APPLE", "signInWithEmailAndPassword: failure", task.exception)
                        binding.textviewMessage.text = "Authentication failed: ${task.exception?.message}"
                    }
                }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.editTextEmail.text.trim().toString()
            if (email.isEmpty()) {
                binding.editTextEmail.error = "No Email"
                return@setOnClickListener
            }
            val password = binding.editTextPassword.text.trim().toString()
            if (password.isEmpty()) {
                binding.editTextPassword.error = "No Password"
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("APPEL", "createEmailAndPassword :success")
                        //val user = auth.currentUser
                        binding.registerButton.setOnClickListener {
                            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                        }
                    } else {
                        Log.w("APPEL", "createEmailAndPassword:failure", task.exception)
                        binding.textviewMessage.text =
                            "Registration failed: " + task.exception?.message
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}