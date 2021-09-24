package com.example.shoppinglist.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentArchivedShoppingListBinding
import com.example.shoppinglist.model.ShoppingListViewModel
import com.example.shoppinglist.model.ShoppingProject

class ArchivedShoppingListFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentArchivedShoppingListBinding? = null
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
                viewModel.removeArchivedShoppingProjectAtIndex(position)
            }
            override fun onClickArchiveButton(position: Int) {
                viewModel.unarchiveShoppingProjectAtIndex(position)
            }
            override fun onClickNewActivityButton(position: Int) {
            }
            override fun onClickCompleteButton(position: Int) {
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
        mListAdapter = ShoppingAdapter(requireActivity(),
            R.layout.archived_shopping_project,
            ArrayList(viewModel.getArchivedShoppingProjectsList()), mListener)
        mListView!!.adapter = mListAdapter
    }
}