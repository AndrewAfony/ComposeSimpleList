package com.example.composesimplelist

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val listOfItems: MutableList<Person> = MutableList(10) { Person("Andrew", 18, "") }



}

