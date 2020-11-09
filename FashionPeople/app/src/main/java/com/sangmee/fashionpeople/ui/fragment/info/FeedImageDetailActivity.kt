package com.sangmee.fashionpeople.ui.fragment.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sangmee.fashionpeople.R
import com.sangmee.fashionpeople.kakaologin.GlobalApplication
import com.sangmee.fashionpeople.retrofit.model.FeedImage
import kotlinx.android.synthetic.main.activity_feed_image_detail.*

class FeedImageDetailActivity : AppCompatActivity() {

    private lateinit var customId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_image_detail)

        customId = GlobalApplication.prefs.getString("custom_id", "")

        intent?.getParcelableExtra<FeedImage>(KEY_FEED_IMAGE)?.let { feedImage ->
            Glide.with(this)
                .load("https://fashionprofile-images.s3.ap-northeast-2.amazonaws.com/users/$customId/feed/${feedImage.imageName}")
                .into(iv_feed_image)

        }

    }

    companion object {
        const val KEY_FEED_IMAGE = "key_feed_image"
    }
}
