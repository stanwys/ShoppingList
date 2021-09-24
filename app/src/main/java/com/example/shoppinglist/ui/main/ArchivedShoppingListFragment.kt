package com.example.shoppinglist.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentArchivedShoppingListBinding
import com.example.shoppinglist.databinding.FragmentShoppingListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArchivedShoppingListFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentArchivedShoppingListBinding? = null
    private val binding get() = _binding!!

    private var mListAdapter: ShoppingProjectAdapter? = null
    private var mListView: ListView? = null
    private lateinit var mListener: ShoppingProjectAdapter.ListAdapterListener
    private lateinit var viewModel: ShoppingListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingListViewModel::class.java)
        mListener = object : ShoppingProjectAdapter.ListAdapterListener {
            override fun onClickRemoveButton(position: Int) {
                viewModel.removeArchivedShoppingProjectAtIndex(position)
                Toast.makeText(
                    getActivity(),
                    "open ${viewModel.getShoppingProjectsList().size} " +
                            ", arch: ${viewModel.getArchivedShoppingProjectsList().size}",
                    Toast.LENGTH_SHORT
                ).show();
            }
            override fun onClickArchiveButton(position: Int) {
                viewModel.unarchiveShoppingProjectAtIndex(position)
                Toast.makeText(
                    getActivity(),
                    "open ${viewModel.getShoppingProjectsList().size} " +
                            ", arch: ${viewModel.getArchivedShoppingProjectsList().size}",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentArchivedShoppingListBinding.inflate(inflater, container, false)
        val root = binding.root
        mListView = binding.archivedShopProjectsList
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
        mListAdapter = ShoppingProjectAdapter(requireActivity(),
            R.layout.archived_shopping_project,
            ArrayList(viewModel.getArchivedShoppingProjectsList()), mListener)
        mListView!!.adapter = mListAdapter
    }
}