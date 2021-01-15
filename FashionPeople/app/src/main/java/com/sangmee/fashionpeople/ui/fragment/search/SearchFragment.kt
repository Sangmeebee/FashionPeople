package com.sangmee.fashionpeople.ui.fragment.search

import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.databinding.FragmentSearchBinding
import com.sangmee.fashionpeople.ui.fragment.search.brand.SearchBrandViewModel
import com.sangmee.fashionpeople.ui.fragment.search.style.SearchStyleViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.fragment_info.tl_container
import kotlinx.android.synthetic.main.fragment_info.viewPager
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val compositeDisposable = CompositeDisposable()
    private val brandVm by activityViewModels<SearchBrandViewModel>()
    private val styleVm by activityViewModels<SearchStyleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)?.apply {
            binding = DataBindingUtil.bind(this)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyBoard(et_name)
        setTabLayout()
        initView()
    }

    private fun initView() {

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.hint = "스타일 검색..."
                    1 -> binding.hint = "브랜드 검색..."
                    2 -> binding.hint = "계정 검색..."
                }
            }
        })

        binding.etName.textChanges()
            .debounce(500L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    when (binding.viewPager.currentItem) {
                        0 -> styleVm.callStyle(it.toString())
                        1 -> brandVm.callBrand(it.toString())
                    }
                } else {
                    when (binding.viewPager.currentItem) {
                        0 -> styleVm.isEmpty.call()
                        1 -> brandVm.isEmpty.call()
                    }
                }
            }.addTo(compositeDisposable)
    }

    private fun setTabLayout() {
        viewPager.adapter = SearchViewPagerAdapter(this)

        TabLayoutMediator(tl_container, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "스타일"
                }
                1 -> {
                    tab.text = "브랜드"
                }
                2 -> {
                    tab.text = "계정"
                }
            }
        }.attach()
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    private fun showKeyBoard(view: View) {
        view.requestFocus()
        val imm =
            requireContext().getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        view.postDelayed({
            imm.showSoftInput(view, 0)
        }, 30)
    }
}
