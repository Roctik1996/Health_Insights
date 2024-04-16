package roctik.app.health.ui.main.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import roctik.app.health.databinding.FragmentMainBinding
import roctik.app.health.domain.model.AggregateDataItem
import roctik.app.health.ui.main.MainViewModel
import roctik.app.health.utils.ItemClickListener

class MainFragment : Fragment(), ItemClickListener<AggregateDataItem> {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    private val mainViewModel: MainViewModel by sharedViewModel()
    private lateinit var adapter: ActivityDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)

        initRecycler()
        setupObserver()

        return binding?.root
    }

    private fun setupObserver() {
        mainViewModel.aggregateDataItems.observe(viewLifecycleOwner) { activityDataList ->
            adapter.differ.submitList(activityDataList)
        }
    }

    private fun initRecycler() {
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = ActivityDataAdapter(this)

        binding?.recyclerView?.setHasFixedSize(false)
        binding?.recyclerView?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onItemClick(item: AggregateDataItem) {
        //---
    }
}