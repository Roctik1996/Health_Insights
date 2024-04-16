package roctik.app.health.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import roctik.app.health.databinding.FragmentPermissionBinding
import roctik.app.health.ui.action.RequestPermissionAction
import roctik.app.health.ui.main.MainViewModel

class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionBinding.inflate(layoutInflater)

        binding?.btnRequestPermission?.setOnClickListener {
            mainViewModel.sendAction(RequestPermissionAction)
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(): PermissionFragment {
            return PermissionFragment()
        }
    }
}