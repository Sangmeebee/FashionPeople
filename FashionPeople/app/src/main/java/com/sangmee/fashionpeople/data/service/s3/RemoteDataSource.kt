package com.sangmee.fashionpeople.data.service.s3

import java.io.File

interface RemoteDataSource {

    fun uploadWithTransferUtility(fileName: String, file: File?)
}