package com.example.vennerlister.models

import java.io.Serializable

data class Person(
    var id: Int,
    var name: String,
    var age: Int,
    var birthYear: Int,
    var birthMonth: Int,
    var birthDayOfMonth: Int,
    var userId: String
) :
    Serializable {
    constructor(
        name: String,
        age: Int,
        birthYear: Int,
        birthMonth: Int,
        birthDayOfMounth: Int,
        E_mail: String
    ) :
            this(-1, name, age, birthYear, birthMonth, birthDayOfMounth, E_mail)

    override fun toString(): String {
        return "$id $name $age $birthYear $birthMonth $birthDayOfMonth $userId"
    }
}