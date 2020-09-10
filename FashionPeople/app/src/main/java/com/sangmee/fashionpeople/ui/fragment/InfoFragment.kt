package com.sangmee.fashionpeople.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.RetrofitClient
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import com.sangmee.fashionpeople.ui.FeedImageAdapter
import kotlinx.android.synthetic.main.fragment_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoFragment : Fragment() {

    lateinit var customId: String
    private val feedImageAdapter by lazy {
        FeedImageAdapter(customId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customId = GlobalApplication.prefs.getString("custom_id", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //리사이클러뷰에 adpater 세팅 후 레트로핏으로 저장한 이미지 적용
        rv_feed_image.apply {
            adapter = feedImageAdapter
        }
        getFeedImages()
    }

    private fun getFeedImages() {
        if (customId != "") {
            RetrofitClient().getFeedImageService().getFeedImages(customId)
                .enqueue(object : Callback<List<FeedImage>> {
                    override fun onResponse(
                        call: Call<List<FeedImage>>,
                        response: Response<List<FeedImage>>
                    ) {
                        response.body()?.let { feedImages ->
                            feedImageAdapter.setFeedImages(feedImages)
                        }
                        Log.d("feedUrls", response.body()!!.size.toString())
                        response.body()?.forEach {

                        }

                    }

                    override fun onFailure(call: Call<List<FeedImage>>, t: Throwable) {
                        Log.d("fail", t.message)
                    }
                })
        }
    }
}
