package com.sangmee.fashionpeople.ui.fragment.content

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
import kotlinx.android.synthetic.main.fragment_post_image.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostImageFragment : Fragment() {

    private lateinit var customId : String
    private val feedImageAdapter by lazy {
        FeedImageAdapter(customId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customId = GlobalApplication.prefs.getString("custom_id", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFeedImages()

        rv_user_image.apply {
            adapter = feedImageAdapter
        }
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
                    }

                    override fun onFailure(call: Call<List<FeedImage>>, t: Throwable) {
                        Log.d("fail", t.message)
                    }
                })
        }
    }
}
