package com.example.shoppinglist.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShoppingListViewModel() : ViewModel() {

    private var shoppingProjectsList = MutableLiveData<MutableList<ShoppingProject>>()
    private var archivedShoppingProjectsList = MutableLiveData<MutableList<ShoppingProject>>()
    private var dbHandler = MutableLiveData<DBHandler>()

    init {
        shoppingProjectsList.value = mutableListOf<ShoppingProject>()
        archivedShoppingProjectsList.value = mutableListOf<ShoppingProject>()
    }

    fun initializeDBHandler(context: Context){
        dbHandler.value = DBHandler(context)
    }

    fun getDBHandler() : MutableLiveData<DBHandler> = dbHandler

    fun openDatabase(){
        dbHandler.value?.openDatabase()
    }

    fun initializeData(){
        dbHandler.value?.openDatabase()
        shoppingProjectsList.value = dbHandler.value?.getShoppingProjectsList("0")
        archivedShoppingProjectsList.value = dbHandler.value?.getShoppingProjectsList("1")
    }

    fun closeDatabase(){
        dbHandler.value?.close()
    }

    fun setShoppingProjectsList(spList : MutableList<ShoppingProject>) {
        shoppingProjectsList.value = spList
    }

    fun setArchivedShoppingProjectsList(archSpList : MutableList<ShoppingProject>) {
        archivedShoppingProjectsList.value = archSpList
    }

    fun getShoppingProjectsList() : MutableList<ShoppingProject> =
        shoppingProjectsList.value!!

    fun getArchivedShoppingProjectsList() : MutableList<ShoppingProject> =
        archivedShoppingProjectsList.value!!

    fun getShoppingProjectAtIndex(index : Int) : ShoppingProject =
        shoppingProjectsList.value!![index]

    fun addNewShoppingProject(sp : ShoppingProject ){
        val projectId = dbHandler.value?.insertProject(sp)
        if (projectId != null) {
            sp.id = projectId
        }
        addShoppingProject(sp)
    }

    fun addShoppingProject(sp : ShoppingProject ){
        shoppingProjectsList.value?.add(sp)
    }

    fun archiveShoppingProject(sp : ShoppingProject ){
        archivedShoppingProjectsList.value?.add(sp)
    }

    fun archiveShoppingProjectAtIndex(index : Int){
        val sp = shoppingProjectsList.value?.removeAt(index)
        if (sp != null) {
            sp.archived = 1
            dbHandler.value?.updateProject(sp)
            archiveShoppingProject(sp)
        }
    }

    fun unarchiveShoppingProjectAtIndex(index : Int){
        val sp = archivedShoppingProjectsList.value?.removeAt(index)
        if (sp != null) {
            sp.archived = 0
            dbHandler.value?.updateProject(sp)
            addShoppingProject(sp)
        }
    }

    fun removeShoppingProjectAtIndex(index : Int ){
        val sp = shoppingProjectsList.value?.removeAt(index)
        if (sp != null) {
            dbHandler.value?.deleteProject(sp)
        }
    }

    fun removeArchivedShoppingProjectAtIndex(index : Int ){
        val sp = archivedShoppingProjectsList.value?.removeAt(index)
        if (sp != null) {
            dbHandler.value?.deleteProject(sp)
        }
    }

}