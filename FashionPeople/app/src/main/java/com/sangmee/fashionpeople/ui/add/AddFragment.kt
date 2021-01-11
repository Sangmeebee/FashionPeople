package com.sangmee.fashionpeople.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.model.Evaluation
import com.sangmee.fashionpeople.databinding.FragmentAddBinding
import com.sangmee.fashionpeople.observer.AddViewModel

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private var evaluationList = listOf<Evaluation>()

    private val vm: AddViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddViewModel() as T
            }
        }).get(AddViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        vm.callEvaluatedFeedImage()
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerCallback()
    }

    private fun observerCallback() {
        vm.evaluatedFeedImage.observe(viewLifecycleOwner, Observer {
            binding.feedImage = it
            it.evaluations?.let { evals ->
                evaluationList = evals
            }
            setProgressView()
        })

        vm.isComplete.observe(viewLifecycleOwner, Observer {
            crossfade()
        })
    }

    private fun crossfade() {
        binding.clContainer.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(500L)
                .setListener(null)
        }
    }

    private fun setProgressView() {
        val total = evaluationList.size

        var progressValue1 = 0
        var progressValue2 = 0
        var progressValue3 = 0
        var progressValue4 = 0
        var progressValue5 = 0

        if (evaluationList.isNotEmpty()) {
            for (i in evaluationList.indices) {
                when (evaluationList[i].score) {
                    1f -> {
                        progressValue1++
                    }
                    2f -> {
                        progressValue2++
                    }
                    3f -> {
                        progressValue3++
                    }
                    4f -> {
                        progressValue4++
                    }
                    5f -> {
                        progressValue5++
                    }
                }
            }
            binding.progress1.progress = ((progressValue1 * 100) / total).toFloat() + 5
            binding.progress1.labelText = "${progressValue1}명"
            binding.progress2.progress = ((progressValue2 * 100) / total).toFloat() + 5
            binding.progress2.labelText = "${progressValue2}명"
            binding.progress3.progress = ((progressValue3 * 100) / total).toFloat() + 5
            binding.progress3.labelText = "${progressValue3}명"
            binding.progress4.progress = ((progressValue4 * 100) / total).toFloat() + 5
            binding.progress4.labelText = "${progressValue4}명"
            binding.progress5.progress = ((progressValue5 * 100) / total).toFloat() + 5
            binding.progress5.labelText = "${progressValue5}명"
        } else {
            binding.progress1.progress = 5f
            binding.progress1.labelText = "0명"
            binding.progress2.progress = 5f
            binding.progress2.labelText = "0명"
            binding.progress3.progress = 5f
            binding.progress3.labelText = "0명"
            binding.progress4.progress = 5f
            binding.progress4.labelText = "0명"
            binding.progress5.progress = 5f
            binding.progress5.labelText = "0명"
        }


    }
}
