package com.example.vennerlister.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vennerlister.repository.PersonRepository
import com.google.firebase.auth.FirebaseAuth

class PersonViewModel : ViewModel() {
    private val repository = PersonRepository()
    val personsLiveData: LiveData<List<Person>> = repository.personLiveData
    val errorMassegeLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        val email = FirebaseAuth.getInstance().currentUser?.email
        email?.let { reload(it) }
    }

    fun reload(user_id: String) {
        Log.d("APPLE", "Reloading data for user:$user_id")
        repository.initRepository(user_id)
    }

    operator fun get(index: Int): Person? {
        return personsLiveData.value?.get(index)
    }

    fun getPersonByUserId(user_id: String) {
        repository.getPersonByUserId(user_id)
    }

    fun add(person: Person) {
        repository.add(person)
    }

    fun delete(id: Int) {
        repository.Delete(id)
        Log.d("APPLE", "Deleted person with id: $id")
    }

    fun update(person: Person) {
        repository.update(person)
        Log.d("APPLE", "Updated person: $person")
    }

    fun sortByName() {
        repository.sortByName()
    }

    fun sortByAge() {
        repository.sortByAge()
    }

    fun sortByBirthday() {
        repository.sortByBirthdayDay()
    }

    fun filterByName(name: String) {
        repository.filterByName(name)
    }
    private var userEmail: String = ""

    fun getUserEmail(): String {
        return userEmail
    }
    fun setUserEmail(email : String) {
        userEmail = email
    }


}