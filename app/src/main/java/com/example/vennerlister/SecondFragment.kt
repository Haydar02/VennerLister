package com.example.vennerlister


import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vennerlister.databinding.FragmentSecoundBinding
import com.example.vennerlister.models.MyAdapter
import com.example.vennerlister.models.PersonViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class SecondFragment : Fragment() {

    private var _binding: FragmentSecoundBinding? = null
    private val binding get() = _binding!!
    private val personViewModel: PersonViewModel by activityViewModels()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userInteractionDetected = false
    private val LOGOUT_DELAY = 5 * 60 * 1000
    private var logoutHandler: Handler? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        _binding = FragmentSecoundBinding.inflate(inflater, container, false)

        return binding.root

    }

    @SuppressLint("SuspiciousIndentation", "ClickableViewAccessibility")
    private val touchListener = View.OnTouchListener { _, _ ->
        Log.d("APPLE", "Touch event detected")
        userInteractionDetected = true
        resetLogoutTimer()
        false
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personViewModel.personsLiveData.observe(viewLifecycleOwner) { persons ->
            Log.d("APPLE", persons.toString())

            binding.progressbar.visibility = View.GONE
            binding.recyclerView.visibility = if (persons == null) View.GONE else View.VISIBLE
            binding.swiperefresh.setOnTouchListener(touchListener)
            binding.swiperefresh.isClickable = true

            startLogoutTimer()
            if (persons != null) {
                val adapter = MyAdapter(persons) { position ->
                    val action =
                        SecondFragmentDirections.actionSecondFragmentToModifyFragment(position)
                    findNavController().navigate(action)
                }
                var columns = 2
                val currentOrientation = this.resources.configuration.orientation
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    columns = 4

                } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    columns = 2
                }
                binding.recyclerView.layoutManager = GridLayoutManager(this.context, columns)
                binding.recyclerView.adapter = adapter
            }
        }

        binding.swiperefresh.setOnRefreshListener {
            val user_id = FirebaseAuth.getInstance().currentUser?.email
            if (user_id == null) {
                binding.textviewMessage.text = "Nobody is signed in "
            } else
                personViewModel.reload(user_id)
            binding.swiperefresh.isRefreshing = false
        }

        binding.buttonSort.setOnClickListener {
            when (binding.SpinnerSorting.selectedItemPosition) {
                0 -> personViewModel.sortByName()
                1 -> personViewModel.sortByAge()
                2 -> personViewModel.sortByBirthday()
            }
        }
        binding.buttonFilterName.setOnClickListener {
            val filter = binding.edittextFilterName.text.toString().trim()
            if (filter.isBlank()) {
                binding.edittextFilterName.error = " No Title"
                return@setOnClickListener
            }
            personViewModel.filterByName(filter)
        }
        binding.fabButton.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            findNavController().navigate(R.id.action_SecondFragment_to_newFriend)
        }
    }

    private fun startLogoutTimer() {
        Log.d("APPLE", "startLogoutTimer")
        logoutHandler = Handler(Looper.getMainLooper())
        logoutHandler?.postDelayed({
            if (!userInteractionDetected) {
                Log.d("APPLE", "Auto Logout")
                Snackbar.make(
                    binding.secoundfragment,
                    "Auto Logout..", Snackbar.LENGTH_LONG
                ).show()
                FirebaseAuth.getInstance().signOut()
                Log.d("APPLE", "Before navigating to FirstFragment")
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                Log.d("APPLE", "After navigating to FirstFragment")
            }
        }, LOGOUT_DELAY.toLong())
    }



    private fun resetLogoutTimer() {
        logoutHandler?.removeCallbacksAndMessages(null)
        startLogoutTimer()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Snackbar.make(binding.secoundfragment, "Settings..", Snackbar.LENGTH_LONG).show()
                return true
            }

            R.id.action_logout -> {
                Snackbar.make(binding.secoundfragment, "Logout..", Snackbar.LENGTH_LONG).show()
                auth.signOut()
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}



