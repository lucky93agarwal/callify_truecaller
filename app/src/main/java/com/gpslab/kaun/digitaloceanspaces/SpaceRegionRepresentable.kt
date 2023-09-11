package com.gpslab.kaun.digitaloceanspaces

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.gpslab.kaun.Webapi.ChatWebAPI
import com.gpslab.kaun.download_notification.BackgroundNotificationService
import com.gpslab.kaun.view.DownloadUploadStat
import com.gpslab.kaun.view.MessageType
import com.gpslab.kaun.view.RealmHelper
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


interface SpaceRegionRepresentable {
    fun endpoint(): String
}

/**
 * Represents a region in which a Digital Ocean Space can be created
 */
enum class SpaceRegion: SpaceRegionRepresentable {
    SFO {
        override fun endpoint(): String {
            return "https://sfo2.digitaloceanspaces.com"
        }
    }, AMS {
        override fun endpoint(): String {
            return "https://ams3.digitaloceanspaces.com"
        }
    }, SGP {
        override fun endpoint(): String {
            return "https://nyc3.digitaloceanspaces.com"
        }
    }
}

class SpacesFileRepository(context: Context) {
    private val accesskey = "4SYUKBCFA4KASIHESCTP"
    private val secretkey = "cWCsWzfqX4h1Y6ULx+8OE0hrPjf5CCiDPFOSIdkxOSA"
    private val spacename = "gpslabindia"
    private val spaceregion = SpaceRegion.SGP

    private val filename = "example_image"
    private val filePermission = CannedAccessControlList.PublicRead
    private val filetype = "jpeg"

    private var transferUtility: TransferUtility

    private var appContext: Context

    init {
        val credentials = StaticCredentialsProvider(BasicAWSCredentials(accesskey, secretkey))
        val client = AmazonS3Client(credentials, Region.getRegion("us-east-1"))

        client.endpoint = spaceregion.endpoint()

        transferUtility = TransferUtility.builder().s3Client(client).context(context).build()

        appContext = context
    }

    /**
     * Converts a APK resource to a file for uploading with the S3 SDK
     */
    private fun convertResourceToFile(): File {
        val exampleIdentifier = appContext.resources.getIdentifier(
                filename,
                "drawable",
                appContext.packageName
        )
        val exampleBitmap = BitmapFactory.decodeResource(appContext.resources, exampleIdentifier)

        val exampleFile = File(appContext.filesDir, Date().toString())
        exampleFile.createNewFile()

        val outputStream = ByteArrayOutputStream()
        exampleBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val exampleBitmapData = outputStream.toByteArray()

        val fileOutputStream = FileOutputStream(exampleFile)
        fileOutputStream.write(exampleBitmapData)
        fileOutputStream.flush()
        fileOutputStream.close()

        return exampleFile
    }





    private fun convertKanuImageResourceToFile(path: String, encoded: ByteArray): File {
//        val exampleIdentifier = appContext.resources.getIdentifier(
//                filename,
//                "drawable",
//                appContext.packageName
//        )
        val exampleBitmap = BitmapFactory.decodeFile(path)

        val exampleFile = File(appContext.filesDir, Date().toString())
        exampleFile.createNewFile()

//        val outputStream = ByteArrayOutputStream()
//        exampleBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        val exampleBitmapData = outputStream.toByteArray()

        val fileOutputStream = FileOutputStream(exampleFile)
        fileOutputStream.write(encoded)
        fileOutputStream.flush()
        fileOutputStream.close()

        return exampleFile
    }

    /**
     * Uploads the example file to a DO Space
     */

    // upload multiple image in server
    fun uploadExampleFile(path: String, encoded: ByteArray, context: Context,  image_name: String, arraysize: Int, position: Int){
        //Starts the upload of our file
        var listener = transferUtility.upload(
                spacename + "/callify/upload/story",
                image_name + ".$filetype",
                convertKanuImageResourceToFile(path, encoded), filePermission
        )


        //we are getting all messages because it's may be a broadcast ,if so we want to update the state of all of them
        Log.d("updateDowloadUpload", "message id  1 =   $image_name    ")
//        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING)


        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object : TransferListener {
            override fun onError(id: Int, ex: Exception?) {
                Log.e("S3 Upload", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.i("S3 Upload", "Progress ${((bytesCurrent / bytesTotal) * 100)}")
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Log.i("S3 Upload", "Completed")
                    Log.d("WalletCheckLucky", "uploadExampleFile key 2  =   " + "Completed")


                    val addpopup = ChatWebAPI(context)
//                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS)


//                    addpopup.sendmessageImage(U_ids, image_name + ".$filetype," + size, MessageType.SENT_IMAGE, arraysize, position, idd)


                }
            }
        })
    }





    /////// video send
    fun uploadExamplevideoFile(path: File, context: Context, U_ids: String, video_name: String, size: String, extension: String, arraysize: Int, position: Int,imagepath: File){
        //Starts the upload of our file
        var listener = transferUtility.upload(
                spacename + "/callify/upload/videos/video_data",
                video_name + extension,
                path, filePermission
        )


        //we are getting all messages because it's may be a broadcast ,if so we want to update the state of all of them
//        Log.d("updateDowloadUpload", "message id  1 =   $image_name    ")
        RealmHelper.getInstance().updateDownloadUploadStat(video_name, DownloadUploadStat.LOADING)


        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object : TransferListener {
            override fun onError(id: Int, ex: Exception?) {
//                Log.e("WalletCheckLucky", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
//                Log.i("WalletCheckLucky", "video  Progress ${((bytesCurrent / bytesTotal) * 100)}")
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {

                    Log.i("sendVideocheck", "Video  =   " + "Completed")



                    Log.i("sendVideocheck", "video_name  =   " + video_name)



                    RealmHelper.getInstance().updateDownloadUploadStat(video_name, DownloadUploadStat.SUCCESS)
//
//
                    uploadExamplevideothumFile(path, context, U_ids, video_name, size, extension, arraysize, position,imagepath)



                }
            }
        })
    }


    fun uploadExampledocFile(path: File, context: Context, U_ids: String, doc_name: String, doc_size: String, extension: String, arraysize: Int, position: Int){
        //Starts the upload of our file
        var listener = transferUtility.upload(
                spacename + "/callify/upload/docs",
                doc_name + extension,
                path, filePermission
        )


        //we are getting all messages because it's may be a broadcast ,if so we want to update the state of all of them
//        Log.d("updateDowloadUpload", "message id  1 =   $image_name    ")
        RealmHelper.getInstance().updateDownloadUploadStat(doc_name, DownloadUploadStat.LOADING)


        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object : TransferListener {
            override fun onError(id: Int, ex: Exception?) {
//                Log.e("WalletCheckLucky", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
//                Log.i("WalletCheckLucky", "video  Progress ${((bytesCurrent / bytesTotal) * 100)}")
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {

                    Log.d("CheckdocSize", "Video  =   " + "Completed")

                    Log.d("CheckdocSize", "replydataimage doc_name   =   $doc_name")

                    Log.d("WalletCheckLucky", "video_name  =   " + doc_name)


                    val addpopup = ChatWebAPI(context)
                    RealmHelper.getInstance().updateDownloadUploadStat(doc_name, DownloadUploadStat.SUCCESS)
//
//
                    addpopup.sendmessageDoc(U_ids, doc_name + extension + "," + doc_size, MessageType.SENT_FILE, doc_name, arraysize, position)


                }
            }
        })
    }


    /////// video thum send
    fun uploadExamplevideothumFile(path: File, context: Context, U_ids: String, video_name: String, size: String, extension: String, arraysize: Int, position: Int,imagepath: File){
        //Starts the upload of our file
        var listener = transferUtility.upload(
                spacename + "/callify/upload/videos/thumb",
                video_name + ".jpeg" ,
                imagepath, filePermission
        )


        //we are getting all messages because it's may be a broadcast ,if so we want to update the state of all of them
//        Log.d("updateDowloadUpload", "message id  1 =   $image_name    ")



        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object : TransferListener {
            override fun onError(id: Int, ex: Exception?) {
                Log.e("sendVideocheck", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.i("sendVideocheck", "Progress ${((bytesCurrent / bytesTotal) * 100)}")
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Log.i("sendVideocheck", "Completed")
                    Log.i("sendVideocheck", "Thumb   =   " + "Completed")
                    val addpopup = ChatWebAPI(context)
                    RealmHelper.getInstance().updateDownloadUploadStat(video_name, DownloadUploadStat.SUCCESS)
//
//

                    addpopup.sendmessageVideo(U_ids, video_name + extension + "," + size, MessageType.SENT_VIDEO, video_name, arraysize, position)

                }
            }
        })
    }






    //// upload image in server
    fun uploadExampleImageFile(path: String, encoded: ByteArray, context: Context, U_ids: String, image_name: String, size: String){
        //Starts the upload of our file
        val intent = Intent(context, BackgroundNotificationService::class.java)
        context.startService(intent)

        var listener = transferUtility.upload(
                spacename + "/callify/upload/images",
                image_name + ".$filetype",
                convertKanuImageResourceToFile(path, encoded), filePermission
        )

        Log.d("uploadExampleImageFile", "message id  2 =   $image_name    ")
        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING)


        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object : TransferListener {
            override fun onError(id: Int, ex: Exception?) {
                Log.e("S3 Upload", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.d("S3 Upload", "Progress ${((bytesCurrent / bytesTotal) * 100)}")

            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Log.i("S3 Upload", "Completed")
                    Log.d("WalletCheckLucky", "uploadExampleFile key 2  =   " + "Completed")


                    val addpopup = ChatWebAPI(context)

                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS)

//                    addpopup.sendmessage(U_ids, image_name + ".$filetype," + size, MessageType.SENT_IMAGE, image_name)
                }
            }
        })
    }






    fun uploadExampleAudioFile(path: File, context: Context, U_ids: String, image_name: String, audioDuration: String, extension: String){
        //Starts the upload of our file
        var listener = transferUtility.upload(
                spacename + "/callify/upload/audio",
                image_name + extension,
                path, filePermission
        )
        Log.d("updateDowloadUpload", "message id  3 =   $image_name    ")
        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING)




        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object : TransferListener {
            override fun onError(id: Int, ex: Exception?) {
                Log.d("WalletCheckLucky", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.d("WalletCheckLucky", "Progress ${((bytesCurrent / bytesTotal) * 100)}")
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Log.i("WalletCheckLucky", "Completed")
                    Log.d("WalletCheckLucky", "uploadExampleFile key 2  =   " + "Completed")
                    Log.d("WalletCheckLucky", "uploadExampleFile extension 2  =   " + extension)
                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS)

                    val addpopup = ChatWebAPI(context)



//                    addpopup.sendmessage(U_ids, image_name + extension + "," + audioDuration, MessageType.SENT_AUDIO, image_name)
                }
            }
        })
    }

    /**
     * Downloads example file from a DO Space
     */
    fun downloadExampleFile(callback: (File?, Exception?) -> Unit) {
        //Create a local File object to save the remote file to
        val file = File("${appContext.cacheDir}/$filename.$filetype")
        Log.d("WalletCheckLucky", "downloadExampleFile file 2  =   " + file);
        //Download the file from DO Space
        var listener = transferUtility.download(spacename, "$filename.$filetype", file)

        //Listen to the progress of the download, and call the callback when the download is complete
        listener.setTransferListener(object : TransferListener {
            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                Log.i("S3 Download", "Progress ${((bytesCurrent / bytesTotal) * 100)}")

            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Log.d(
                            "WalletCheckLucky",
                            "downloadExampleFile Completed 2  =   " + "Completed"
                    );
                    Log.i("S3 Download", "Completed")
                    callback(file, null)
                }
            }

            override fun onError(id: Int, ex: Exception?) {
                Log.e("S3 Download", ex.toString())
                Log.d("WalletCheckLucky", "ex 2  =   " + ex.toString());
                callback(null, ex)
            }
        })
    }
}