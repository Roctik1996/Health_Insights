package roctik.app.health.ui.main.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import roctik.app.health.databinding.FragmentImportDataBinding
import roctik.app.health.ui.action.NavigateToMainScreen
import roctik.app.health.ui.main.MainViewModel

class ImportDataFragment : Fragment() {

    private val TAG = ImportDataFragment::class.simpleName
    private var _binding: FragmentImportDataBinding? = null
    private val binding get() = _binding

    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImportDataBinding.inflate(layoutInflater)

        initProgress()
        mainViewModel.getAggregateData()

        binding?.btnContinue?.setOnClickListener {
            mainViewModel.sendAction(NavigateToMainScreen)
        }

        return binding?.root
    }

    private fun initProgress() {
        val valueAnimator = ValueAnimator.ofInt(0, 100)
        valueAnimator.duration = 2000

        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            binding?.progressCircular?.progress = animatedValue.toFloat()
        }

        valueAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                binding?.btnContinue?.isEnabled = true
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        valueAnimator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(): ImportDataFragment {
            return ImportDataFragment()
        }
    }
}