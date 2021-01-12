package com.sangmee.fashionpeople.ui.add

import android.os.Bundle
import android.text.Editable
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
import kotlinx.android.synthetic.main.activity_category.*
import java.util.concurrent.TimeUnit

class CategoryActivity : AppCompatActivity(), OnListItemSelectedInterface {

    //assets 폴더에서 브랜드 파일 읽기용 프로퍼티
    private var categoryList = arrayListOf<String>()
    private val categoryAdapter = CategoryAdapter(this)

    private val subject by lazy {
        intent.getStringExtra("subject")
    }

    private val vm by viewModels<CategoryViewModel>()
    private val compositeDisposable = CompositeDisposable()

    private lateinit var binding: ActivityCategoryBinding

    override fun onResume() {
        initViewModel()
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        binding.activity = this
        binding.lifecycleOwner = this
        var sub: String? = null
        when (this.subject) {
            "style" -> sub = "스타일"
            "top" -> sub = "상의"
            "pants" -> sub = "하의"
            "shoes" -> sub = "신발"
        }
        binding.subject = "$sub 선택"

        initView()
        initEditTextListener()

    }

    private fun initView(){
        categoryAdapter.setTitleList(categoryList)

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
            for (brand in it) {
                brandList.add(brand.brandName)
            }
            categoryAdapter.setTitleList(brandList)
        })

        vm.styleList.observe(this, androidx.lifecycle.Observer {
            val styleList = ArrayList<String>()
            for (style in it) {
                styleList.add(style.styleName)
            }
            categoryAdapter.setTitleList(styleList)
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
            .debounce(800, TimeUnit.MILLISECONDS)
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
                    categoryAdapter.setTitleList(categoryList)
                }
            }.addTo(compositeDisposable)
    }

    fun clickCompleteBtn(){
        val title = binding.etBrand.text.toString()
        GlobalApplication.prefs.setString(subject, title)
        val intent = intent
        intent.putExtra("subject", subject)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onItemSelected(title: String) {
        binding.etBrand.apply {
            setText(title)
            setSelection(this.text.length)
        }
    }

    override fun onPause() {
        vm.unbindViewModel()
        compositeDisposable.clear()
        super.onPause()
    }
}
