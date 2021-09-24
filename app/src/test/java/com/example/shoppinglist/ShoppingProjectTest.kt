package com.example.shoppinglist

import org.junit.Test

import org.junit.Assert.*
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.shoppinglist.model.ShoppingProject
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`


class ShoppingProjectTest {

    @Test
    fun defaultConstructorTest() {
        val spTest = ShoppingProject("default")
        assertEquals(-1, spTest.id)
    }

    @Test
    fun sortedListTest(){
        val list = mutableListOf<ShoppingProject>()
        list.add(ShoppingProject(5,"a",54567,0))
        list.add(ShoppingProject(1,"b",567,0))
        list.add(ShoppingProject(3,"c",2367,0))
        list.add(ShoppingProject(4,"c",4367,0))

        val sortedList = list.sortedBy { it -> it.date.time }

        assertEquals( 567, sortedList[0].date.time)
        assertEquals( 54567, sortedList[3].date.time)
    }
}