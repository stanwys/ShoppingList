package com.example.shoppinglist.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.model.ShoppingProject

class ShoppingListViewModel : ViewModel() {

    private var shoppingProjectsList = MutableLiveData<MutableList<ShoppingProject>>()
    private var archivedShoppingProjectsList = MutableLiveData<MutableList<ShoppingProject>>()

    init {
        shoppingProjectsList.value = mutableListOf<ShoppingProject>()
        archivedShoppingProjectsList.value = mutableListOf<ShoppingProject>()
    }

    fun getShoppingProjectsList() : MutableList<ShoppingProject> =
        shoppingProjectsList.value!!

    fun getArchivedShoppingProjectsList() : MutableList<ShoppingProject> =
        archivedShoppingProjectsList.value!!

    fun addShoppingProject(sp : ShoppingProject ){
        shoppingProjectsList.value?.add(sp)
    }

    fun addArchivedShoppingProject(sp : ShoppingProject ){
        archivedShoppingProjectsList.value?.add(sp)
    }

    fun archiveShoppingProjectAtIndex(index : Int){
        val sp = shoppingProjectsList.value?.removeAt(index)
        if (sp != null) {
            addArchivedShoppingProject(sp)
        }
    }

    fun unarchiveShoppingProjectAtIndex(index : Int){
        val sp = archivedShoppingProjectsList.value?.removeAt(index)
        if (sp != null) {
            addShoppingProject(sp)
        }
    }

    fun removeShoppingProjectAtIndex(index : Int ){
        shoppingProjectsList.value?.removeAt(index)
    }

    fun removeShoppingProject(sp : ShoppingProject ){
        shoppingProjectsList.value?.remove(sp)
    }

    fun removeArchivedShoppingProjectAtIndex(index : Int ){
        archivedShoppingProjectsList.value?.removeAt(index)
    }

    fun removeArchivedShoppingProject(sp : ShoppingProject ){
        archivedShoppingProjectsList.value?.remove(sp)
    }
}