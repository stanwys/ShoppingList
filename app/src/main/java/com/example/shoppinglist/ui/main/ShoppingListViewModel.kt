package com.example.shoppinglist.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.model.ShoppingProject

class ShoppingListViewModel : ViewModel() {

    private lateinit var shoppingProjectsList : MutableLiveData<MutableList<ShoppingProject>>
    private lateinit var archivedShoppingProjectsList : MutableLiveData<MutableList<ShoppingProject>>

    init {
        shoppingProjectsList.value = mutableListOf<ShoppingProject>()
        archivedShoppingProjectsList.value = mutableListOf<ShoppingProject>()
    }

    fun addShoppingProject(sp : ShoppingProject ){
        shoppingProjectsList.value?.add(sp)
    }

    fun addArchivedShoppingProject(sp : ShoppingProject ){
        archivedShoppingProjectsList.value?.add(sp)
    }

    fun removeShoppingProject(sp : ShoppingProject ){
        shoppingProjectsList.value?.remove(sp)
    }

    fun removeArchivedShoppingProject(sp : ShoppingProject ){
        archivedShoppingProjectsList.value?.remove(sp)
    }
}