package com.sangmee.fashionpeople.ui.add

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding4.widget.textChanges
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.data.GlobalApplication
import com.sangmee.fashionpeople.databinding.ActivityCategoryBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import java.util.concurrent.TimeUnit

class CategoryActivity : AppCompatActivity(), OnListItemSelectedInterface {

    //assets 폴더에서 브랜드 파일 읽기용 프로퍼티
    private var categoryList = arrayListOf<String>()
    private var postNumList = arrayListOf<Int>()
    private val categoryAdapter = CategoryAdapter(this)

    private val subject by lazy {
        intent.getStringExtra("subject")
    }

    private val vm by viewModels<CategoryViewModel>()
    private val compositeDisposable = CompositeDisposable()

    private lateinit var binding: ActivityCategoryBinding

    override fun onResume() {
        super.onResume()
        initViewModel()
        initEditTextListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.activity = this
        binding.lifecycleOwner = this
        var sub: String? = null
        var hintSub: String? = null
        when (this.subject) {
            "style" -> {
                sub = "스타일"
                binding.hint = "스타일 입력"
            }
            "top" -> {
                sub = "상의"
                binding.hint = "상의 브랜드명 입력"
            }
            "pants" -> {
                sub = "하의"
                binding.hint = "하의 브랜드명 입력"
            }
            "shoes" -> {
                sub = "신발"
                binding.hint = "신발 브랜드명 입력"
            }
        }
        binding.subject = "$sub 선택"

        initView()

    }

    private fun initView() {
        categoryAdapter.setTitleList(categoryList, postNumList)

        //리사이클러뷰 세팅
        binding.rvBrand.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
        }

        binding.ivCancel.setOnClickListener {
            finish()
        }
    }

    private fun initViewModel() {
        vm.brandList.observe(this, androidx.lifecycle.Observer {
            val brandList = ArrayList<String>()
            val numList = ArrayList<Int>()
            for (brand in it) {
                brandList.add(brand.brandName)
                numList.add(brand.postNum)
            }
            categoryAdapter.setTitleList(brandList, numList)
        })

        vm.styleList.observe(this, androidx.lifecycle.Observer {
            val styleList = ArrayList<String>()
            val numList = ArrayList<Int>()
            for (style in it) {
                styleList.add(style.styleName)
                numList.add(style.postNum)
            }
            categoryAdapter.setTitleList(styleList, numList)
        })

        vm.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.pbLoading.isVisible = it
            }
            .addTo(compositeDisposable)

    }

    private fun initEditTextListener() {

        binding.etBrand.textChanges()
            .debounce(600, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!it.isNullOrEmpty()) {
                    binding.isFill = true
                    if (subject == "style") {
                        vm.callStyle(it.toString())
                    } else {
                        vm.callBrand(it.toString())
                    }
                } else {
                    binding.isFill = false
                    categoryAdapter.setTitleList(categoryList, postNumList)
                }
            }.addTo(compositeDisposable)
    }

    fun clickCompleteBtn() {
        val title = binding.etBrand.text.toString()
        GlobalApplication.prefs.setString(subject, title)
        val intent = intent
        intent.putExtra("subject", subject)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onItemSelected(title: String) {
        GlobalApplication.prefs.setString(subject, title)
        val intent = intent
        intent.putExtra("subject", subject)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onPause() {
        vm.unbindViewModel()
        compositeDisposable.clear()
        super.onPause()
    }
}
