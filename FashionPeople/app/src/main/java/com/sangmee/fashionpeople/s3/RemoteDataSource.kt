package com.sangmee.fashionpeople.s3

import java.io.File

interface RemoteDataSource {

    fun uploadWithTransferUtility(fileName: String, file: File?)
}
