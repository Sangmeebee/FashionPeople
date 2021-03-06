package com.sangmee.fashionpeople.data.dataSource.remote

import java.io.File

interface S3RemoteDataSource {

    fun uploadWithTransferUtility(fileName: String, file: File?, location: String)

    fun deleteFileInS3(location: String)

    fun deleteFolderInS3(location: String)
}
