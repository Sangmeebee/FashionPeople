package com.sangmee.fashionpeople.data.dataSource.remote

import android.content.Context
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ListObjectsRequest
import java.io.File


class S3RemoteDataSourceImpl(private val context: Context, private val customId: String) :
    S3RemoteDataSource {

    private val credentialsProvider = CognitoCachingCredentialsProvider(
        context,
        "ap-northeast-2:04a21c16-627a-49a9-8229-f1c412ddebfa",  // 자격 증명 풀 ID
        Regions.AP_NORTHEAST_2 // 리전
    )

    //aws s3에 이미지 업로드
    override fun uploadWithTransferUtility(fileName: String, file: File?, location: String) {


        TransferNetworkLossHandler.getInstance(context)

        val transferUtility = TransferUtility.builder()
            .context(context)
            .defaultBucket("fashionprofile-images")
            .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2)))
            .build()

        /* Store the new created Image file path */

        val uploadObserver = transferUtility.upload(
            "users/${customId}/${location}/${fileName}",
            file,
            CannedAccessControlList.PublicRead
        )

        //CannedAccessControlList.PublicRead 읽기 권한 추가

        // Attach a listener to the observer
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    Log.d("MYTAG", "fileupload")
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })

        // If you prefer to long-poll for updates
        if (uploadObserver.state == TransferState.COMPLETED) {
            /* Handle completion */

        }
    }

    override fun deleteFileInS3(location: String) {
        AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2)).deleteObject(
            DeleteObjectRequest("fashionprofile-images", location)
        )
    }

    override fun deleteFolderInS3(location: String) {
        val s3Client = AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2))
        if(s3Client.doesBucketExist("fashionprofile-images")){
            val listObjectsRequest = ListObjectsRequest()
                .withBucketName("fashionprofile-images")
                .withPrefix(location)

            var objectListing = s3Client.listObjects(listObjectsRequest)

            while (true) {
                for (objectSummary in objectListing.objectSummaries) {
                    s3Client.deleteObject("fashionprofile-images", objectSummary.key)
                }
                objectListing = if (objectListing.isTruncated) {
                    s3Client.listNextBatchOfObjects(objectListing)
                } else {
                    break
                }
            }
        }
    }
}
