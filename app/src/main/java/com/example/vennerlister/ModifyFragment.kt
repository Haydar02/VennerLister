package com.example.vennerlister

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.vennerlister.databinding.FragmentModifyBinding
import com.example.vennerlister.models.Person
import com.example.vennerlister.models.PersonViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class ModifyFragment : Fragment() {

    private var _binding: FragmentModifyBinding? = null
    private val binding get() = _binding!!
    private val personViewModel: PersonViewModel by activityViewModels()
    private var selectedDate = Calendar.getInstance()
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

            selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = requireArguments()
        val modifyFragmentArgs: ModifyFragmentArgs = ModifyFragmentArgs.fromBundle(bundle)
        val position = modifyFragmentArgs.position
        val person = personViewModel[position]
        if (person == null) {
            binding.textviewMessage.text = "No such Friend!"
            return
        }

        binding.editTextName.setText(person.name)
        binding.buttonBirthdate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentYear = calendar[Calendar.YEAR]
            val currentMonth = calendar[Calendar.MONTH]
            val currentDayOfMonth = calendar[Calendar.DAY_OF_MONTH]

            val datePicker = DatePickerDialog(
                requireContext(),
                dateSetListener,
                currentYear,
                currentMonth,
                currentDayOfMonth
            )
            datePicker.show()
        }


        binding.buttonDelete.setOnClickListener {
            val user_id = FirebaseAuth.getInstance().currentUser?.email
            if(user_id != null) {
                personViewModel.delete(person.id)
                personViewModel.reload(user_id)
            }
            findNavController().navigate(R.id.action_modifyFragment_to_SecondFragment)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_modifyFragment_to_SecondFragment)
        }


        binding.buttonUpdate.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val birthYear = selectedDate.get(Calendar.YEAR)
            val birthMonth = selectedDate.get(Calendar.MONTH)
            val birthDayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH)
//            val birthYear = binding.editTextBirthYear.text.toString().trim().toInt()
//            val birthMounth = binding.editTextBirthMonth.text.toString().trim().toInt()
//            val birthDayOfMounth = binding.editTextBirthDayOfMounth.text.toString().trim().toInt()
            val E_mail = person.userId
            val updatePerson =
                Person(
                    person.id,
                    name,
                    person.age,
                    birthYear,
                    birthMonth,
                    birthDayOfMonth,
                    E_mail
                )
            personViewModel.update(updatePerson)
            val user_id = FirebaseAuth.getInstance().currentUser?.email
            if(user_id != null) {
                //personViewModel.update(person)
                personViewModel.reload(user_id)
            }
            findNavController().navigate(R.id.action_modifyFragment_to_SecondFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}




