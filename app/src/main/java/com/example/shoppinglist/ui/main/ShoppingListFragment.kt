package com.example.shoppinglist.ui.main

import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShoppingListBinding
import com.example.shoppinglist.model.ShoppingProject
import android.widget.*
import com.example.shoppinglist.ShoppingListActivity
import com.example.shoppinglist.model.ShoppingListViewModel


class ShoppingListFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    private var mListAdapter: ShoppingAdapter<ShoppingProject>? = null
    private var mListView: ListView? = null
    private lateinit var mListener: ShoppingAdapter.ListAdapterListener
    private lateinit var viewModel: ShoppingListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingListViewModel::class.java)

        mListener = object : ShoppingAdapter.ListAdapterListener {
            override fun onClickRemoveButton(position: Int) {
                viewModel.removeShoppingProjectAtIndex(position)
            }
            override fun onClickArchiveButton(position: Int) {
                viewModel.archiveShoppingProjectAtIndex(position)
            }
            override fun onClickNewActivityButton(position: Int) {
                val inT = Intent(context, ShoppingListActivity::class.java)
                val sp = viewModel.getShoppingProjectAtIndex(position)
                inT.putExtra("title", sp.name)
                inT.putExtra("projectId", sp.id.toString())
                context?.startActivity(inT)
            }
            override fun onClickCompleteButton(position: Int) {
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val root = binding.root

        val openDialog = binding.addNewList
        openDialog.setOnClickListener {
            showCustomDialog()
        }

        mListView = binding.shopProjectsList
        updateListView()

        return root
    }

    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        if (visible && isResumed) {
            updateListView()
        }
    }

    private fun updateListView(){
        viewModel.sortShoppingProjectsList()
        mListAdapter = ShoppingAdapter(requireActivity(),
            R.layout.shopping_project,
            ArrayList(viewModel.getShoppingProjectsList()), mListener)
        mListView!!.adapter = mListAdapter
    }

    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.shopping_list_dialog)

        val header = dialog.findViewById<TextView>(R.id.titleView)
        header.setText("Enter shopping list name:")
        val title = dialog.findViewById<EditText>(R.id.editTitleName)
        val confirmButton = dialog.findViewById<Button>(R.id.ConfirmButton)
        val cancelButton = dialog.findViewById<Button>(R.id.CancelButton)

        confirmButton.setOnClickListener {
            val name = title.text.toString()
            val sp = ShoppingProject(name)

            viewModel.addNewShoppingProject(sp)

            updateListView()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}