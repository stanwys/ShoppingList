package com.example.shoppinglist

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.shoppinglist.model.DBHandler
import com.example.shoppinglist.model.Item
import com.example.shoppinglist.ui.main.ShoppingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var mItemsList : MutableList<Item>
    private var mProjectId: String? = null
    private lateinit var mProjectTitle: String
    private var mListAdapter: ShoppingAdapter<Item>? = null
    private var mListView: ListView? = null
    private var mDBHandler: DBHandler? = null
    private lateinit var mListener: ShoppingAdapter.ListAdapterListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        val extras = intent.extras?:return
        mProjectId = extras.getString("projectId").toString()
        mProjectTitle = extras.getString("title").toString()

        val toolbarLayout = findViewById<Toolbar>(R.id.toolbar)
        toolbarLayout.title = mProjectTitle

        mListView = findViewById(R.id.contentListView)
        mItemsList = mutableListOf()
        mDBHandler = DBHandler(this)
        mDBHandler!!.openDatabase()
        initializeItemsList()

        mListener = object : ShoppingAdapter.ListAdapterListener {
            override fun onClickRemoveButton(position: Int) {
                val im = mItemsList.removeAt(position)
                mDBHandler?.deleteItem(im)
            }
            override fun onClickArchiveButton(position: Int) {
            }
            override fun onClickNewActivityButton(position: Int) {
            }
            override fun onClickCompleteButton(position: Int) {
                mItemsList[position].changeCompletedState()
                mDBHandler?.updateItem(mItemsList[position])
            }
        }

        val addItemButton = findViewById<FloatingActionButton>(R.id.addNewItem)
        addItemButton.setOnClickListener {
            showCustomDialog()
        }
        updateListView()
    }

    override fun onResume() {
        super.onResume()
        mDBHandler?.openDatabase()
    }

    override fun onStop() {
        super.onStop()
        mDBHandler?.close()
    }

    private fun initializeItemsList(){
        mItemsList = mDBHandler?.getShoppingProjectItemsList(mProjectId!!)!!
    }

    private fun updateListView(){
        mListAdapter = ShoppingAdapter(this,
            R.layout.shopping_project_item,
            ArrayList(mItemsList), mListener)
        mListView!!.adapter = mListAdapter
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.shopping_list_dialog)

        val header = dialog.findViewById<TextView>(R.id.titleView)
        header.setText("Enter shopping item name:")
        val title = dialog.findViewById<EditText>(R.id.editTitleName)
        val confirmButton = dialog.findViewById<Button>(R.id.ConfirmButton)
        val cancelButton = dialog.findViewById<Button>(R.id.CancelButton)

        confirmButton.setOnClickListener {
            val name = title.text.toString()
            val im = Item( Integer.parseInt(mProjectId),name)
            val itemId = mDBHandler?.insertItem(im)
            if (itemId != null) {
                im.id = itemId
            }
            mItemsList.add(im)
            updateListView()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}