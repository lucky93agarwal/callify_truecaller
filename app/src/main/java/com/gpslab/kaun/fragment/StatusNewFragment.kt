package com.gpslab.kaun.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.devlomi.circularstatusview.CircularStatusView
import com.google.android.gms.ads.AdView
import com.gpslab.kaun.ImageEditor
import com.gpslab.kaun.R
import com.gpslab.kaun.adapter.StatusNewAdapter
import com.gpslab.kaun.chat.NetworkHelper
import com.gpslab.kaun.digitaloceanspaces.RandomString
import com.gpslab.kaun.digitaloceanspaces.SpacesFileRepository
import com.gpslab.kaun.retrofit.Log
import com.gpslab.kaun.status.*
import com.gpslab.kaun.view.*
import com.zhihu.matisse.Matisse
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_new_status.*
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.concurrent.TimeUnit

class StatusNewFragment : BaseFragment(), StatusNewAdapter.OnClickListener  {

    private lateinit var adapter: StatusNewAdapter
    var statusesList: RealmResults<UserStatuses>? = null
    private var myStatuses: UserStatuses? = null
    private var decor: HeaderViewDecoration? = null
    private var header1pos = 0
    private var header2pos = 0
    private var header1Title: String? = null
    private var header2Title = ""
    override var adView: AdView? = null
    private var callbacks: StatusFragmentCallbacks? = null
    private val statusManager = StatusManager()
    val CAMERA_REQUEST = 4659
    val REQUEST_CODE_TEXT_STATUS = 9145
    private var MAX_STATUS_VIDEO_TIME = 0

    private lateinit var btnViewMyStatuses: ImageButton
    private lateinit var tvLastStatusTime: TextView
    private lateinit var tvTextStatus:TextViewWithShapeBackground
    private lateinit var circularStatusView: CircularStatusView
    private lateinit var profileImage: ImageView
    private lateinit var rowStatusContainer: ConstraintLayout
    private lateinit var sharedPreferences: SharedPreferences


    private val viewModel: MainViewModel by activityViewModels()
    override fun showAds(): Boolean {
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        Log.d("checkStatusinstatusFragment", "context ==    " + context)
//        callbacks = context as StatusFragmentCallbacks

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_status, container, false)
    }


    val checkdata: Boolean=false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //since we are using <include> the app sometimes crashes, to solve that we are instantiate it using findViewById
        btnViewMyStatuses = view.findViewById(R.id.btn_view_my_statuses)
        circularStatusView = view.findViewById(R.id.circular_status_view)
        tvLastStatusTime = view.findViewById(R.id.tv_last_status_time)
        tvTextStatus = view.findViewById(R.id.tv_text_status)
        rowStatusContainer = view.findViewById(R.id.row_status_container)
        profileImage = view.findViewById(R.id.profile_image)

        adView = ad_view
        adViewInitialized(adView)

        MAX_STATUS_VIDEO_TIME = 300
        btnViewMyStatuses.setOnClickListener(View.OnClickListener {
            if (myStatuses == null) return@OnClickListener
            startActivity(Intent(activity, MyStatusActivity::class.java))
        })
        statusesList = RealmHelper.getInstance().allStatuses
        initMyStatuses()
        circularStatusView.visibility = View.GONE
        initAdapter()

        rowStatusContainer.setOnClickListener {
            Log.d("checkStatusinstatusFragment", "onclick")
//            Log.d("checkStatusinstatusFragment", "Check ==    " + myStatuses?.filteredStatuses?.isNotEmpty());
            if (checkdata) {
                val intent = Intent(activity, ViewStatusActivity::class.java)
                intent.putExtra(IntentUtils.UID, myStatuses?.userId)
                startActivity(intent)
            } else {
                Log.d("checkStatusinstatusFragment", "checkdata  ==   " + checkdata)
                val intent = Intent(activity, CameraActivity::class.java)
                intent.putExtra(IntentUtils.CAMERA_VIEW_SHOW_PICK_IMAGE_BUTTON, true)
                intent.putExtra(IntentUtils.IS_STATUS, true)
                startActivityForResult(intent, CAMERA_REQUEST)
            }
        }

        viewModel.statusLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { statusFragmentEvent ->
            when (statusFragmentEvent) {

                is StatusFragmentEvent.StatusInsertedEvent -> statusInserted()
                is StatusFragmentEvent.OnActivityResultEvent -> {
                    val requestCode = statusFragmentEvent.requestCode
                    val resultCode = statusFragmentEvent.resultCode
                    val data = statusFragmentEvent.data

                    if (requestCode == CAMERA_REQUEST) {
                        onCameraActivityResult(resultCode, data)
                        Log.d("checkStatusinstatusFragment", "status    data  ==   " + CAMERA_REQUEST)

                    } else if (requestCode == ImageEditor.RC_IMAGE_EDITOR && resultCode == Activity.RESULT_OK) {
                        data?.getStringExtra(ImageEditor.EXTRA_EDITED_PATH)?.let { imagePath ->
                            onImageEditSuccess(imagePath)
                        }

                    } else if (requestCode == REQUEST_CODE_TEXT_STATUS && resultCode == Activity.RESULT_OK) {
                        data.getParcelableExtra<TextStatus>(IntentUtils.EXTRA_TEXT_STATUS)?.let { textStatus ->
                            onTextStatusResult(textStatus)
                        }

                    }
                }
            }
        })

        viewModel.queryTextChange.observe(viewLifecycleOwner, androidx.lifecycle.Observer { newText ->
            onQueryTextChange(newText)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST || requestCode == ImageEditor.RC_IMAGE_EDITOR || requestCode == REQUEST_CODE_TEXT_STATUS) {
            Log.d("checkStatusinstatusFragment", "requestCode  ==   " + requestCode)
            Log.d("checkStatusinstatusFragment", "resultCode  ==   " + resultCode)
            Log.d("checkStatusinstatusFragment", "data  ==   " + data)
            viewModel.onActivityResult(requestCode, resultCode, data)

        }

    }
    private fun initMyStatuses() {
        myStatuses = RealmHelper.getInstance().getUserStatuses(FireManager.uid)
    }

    fun setMyStatus() {
        Log.d("checuploadVideoStatusment", "start");
        if (myStatuses == null) initMyStatuses()
        if (myStatuses != null
                && myStatuses?.filteredStatuses?.isNotEmpty() == true) {
            Log.d("checuploadVideoStatusment", "start not else");
            val lastStatus = myStatuses?.statuses?.last()
            val statusTime = TimeHelper.getStatusTime(lastStatus?.timestamp ?: Date().time)
            tvLastStatusTime.text = statusTime
            btnViewMyStatuses.visibility = View.VISIBLE
            circularStatusView.visibility = View.VISIBLE
            if (lastStatus?.type == StatusType.IMAGE || lastStatus?.type == StatusType.VIDEO) {
                tvTextStatus.visibility = View.GONE
                profileImage.visibility = View.VISIBLE
                Glide.with(requireActivity()).load(lastStatus.thumbImg).into(profileImage)
            } else if (lastStatus?.type == StatusType.TEXT) {
                tvTextStatus.visibility = View.VISIBLE
                profileImage.visibility = View.GONE
                val textStatus = lastStatus.textStatus
                tvTextStatus.text = textStatus.text
                tvTextStatus.setShapeColor(Color.parseColor(textStatus?.backgroundColor
                        ?: "#000000"))
            }
        } else {
            Log.d("checuploadVideoStatusment", "start else");
            Log.d("checuploadVideoStatusment", "sharedPreferences   ===     " + sharedPreferences.getString("img", "xyz"));
            circularStatusView.visibility = View.GONE
            tvTextStatus.visibility = View.GONE
            profileImage.visibility = View.VISIBLE

            Glide.with(requireActivity()).load(sharedPreferences.getString("img", "xyz")).placeholder(R.drawable.profile).into(profileImage)
            btnViewMyStatuses.visibility = View.GONE
            tvLastStatusTime.text = getString(R.string.tap_to_add_status)
        }
    }


    fun onCameraActivityResult(resultCode: Int, data: Intent) {
        Log.d("checkStatusinstatusFragment", "onCameraActivityResult resultCode  ==   " + resultCode)
        Log.d("checkStatusinstatusFragment", "onCameraActivityResult data  ==   " + data)
        if (resultCode != ResultCodes.CAMERA_ERROR_STATE) {

            Log.d("checkStatusinstatusFragment", "onCameraActivityResult  ==   " + resultCode)
            if (resultCode == ResultCodes.IMAGE_CAPTURE_SUCCESS) {
                Log.d("checkStatusinstatusFragment", "onCameraActivityResult  ==   1 $")
                val path = data.getStringExtra(IntentUtils.EXTRA_PATH_RESULT)
                Log.d("checkStatusinstatusFragment", "onCameraActivityResult  path  ==   " + path)
                Log.d("checkStatusinstatusFragment", "onCameraActivityResult  activity  ==   " + activity)
                ImageEditorRequest.open(activity, path)
            } else if (resultCode == ResultCodes.VIDEO_RECORD_SUCCESS) {
                Log.d("checkStatusinstatusFragment", "onCameraActivityResult  ==   2 $")
                data.getStringExtra(IntentUtils.EXTRA_PATH_RESULT)?.let { path ->
                    uploadVideoStatus(path)
                }
            } else if (resultCode == ResultCodes.PICK_IMAGE_FROM_CAMERA) {
                Log.d("checkStatusinstatusFragment", "onCameraActivityResult  ==   3 $")
                val mPaths = Matisse.obtainPathResult(data)
                for (mPath in mPaths) {
                    if (!FileUtils.isFileExists(mPath)) {
                        Toast.makeText(activity, MyApp.context().resources.getString(R.string.image_video_not_found), Toast.LENGTH_SHORT).show()
                        return
                    }
                }


                //Check if it's a video
                if (FileUtils.isPickedVideo(mPaths[0])) {

                    //check if video is longer than 30sec
                    val mediaLengthInMillis = Util.getMediaLengthInMillis(context, mPaths[0])
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(mediaLengthInMillis)
                    if (seconds <= MAX_STATUS_VIDEO_TIME) {
                        for (mPath in mPaths) {
                            uploadVideoStatus(mPath)
                        }
                    } else {
                        Toast.makeText(activity, MyApp.context().resources.getString(R.string.video_length_is_too_long), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //if it's only one image open image editor
                    if (mPaths.size == 1) ImageEditorRequest.open(activity, mPaths[0]) else for (path in mPaths) {
                        uploadImageStatus(path)
                    }

//                    val spacesFileRepository = activity?.let { SpacesFileRepository(it) }
//                    spacesFileRepository.uploadExampleFile(mmPaths[0],ImageCDRIVES[0],activity,mmPathsid[0],sizesize,0)

                }
            }
        }
    }
    private fun replydataimage(i: Int){
//        val spacesFileRepository = activity?.let { SpacesFileRepository(it) }
//        spacesFileRepository.uploadExampleFile(mmPaths[i],ImageCDRIVES[i],activity,mmPathsid[i],sizesize,0)
    }

    private fun initAdapter() {
        adapter = StatusNewAdapter(statusesList, true, context, this@StatusNewFragment)
        rv_status.layoutManager = LinearLayoutManager(context)
        rv_status.adapter = adapter
        decor = HeaderViewDecoration(context)
        decor?.let {
            rv_status.addItemDecoration(it)
        }

    }

    private fun setupHeaders() {
        header1pos = -1
        header2pos = -1
        statusesList?.let {


            for (userStatuses in it) {
                if (!userStatuses.isAreAllSeen) {
                    if (header1pos == -1) {
                        header1pos = it.indexOf(userStatuses)
                    }
                } else {
                    if (header2pos == -1) {
                        header2pos = it.indexOf(userStatuses)
                        break
                    }
                }
            }
        }
        //if the statuses are all seen,then set the header title as Viewed updates
        if (header1pos == -1) {
            header1Title = MyApp.context().resources.getString(R.string.viewed_statuses)
            header2Title = MyApp.context().resources.getString(R.string.viewed_statuses)
        } else {
            header1Title = MyApp.context().resources.getString(R.string.recent_updates)
            header2Title = MyApp.context().resources.getString(R.string.viewed_statuses)
        }
    }

    private fun uploadVideoStatus(path: String) {

        Log.d("checuploadVideoStatusment", "uploadVideoStatus path  ==   " + path)
        if (!NetworkHelper.isConnected(MyApp.context())) {
            Toast.makeText(activity, MyApp.context().resources.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(activity, R.string.uploading_status, Toast.LENGTH_SHORT).show()
//        disposables.add(statusManager.uploadStatus(path, StatusType.VIDEO, true).subscribe { status, throwable ->
//            if (throwable != null) {
//                Toast.makeText(activity, MyApp.context().resources.getString(R.string.error_uploading_status), Toast.LENGTH_SHORT).show()
//            } else {
//                setMyStatus()
//                Toast.makeText(activity, MyApp.context().resources.getString(R.string.status_uploaded), Toast.LENGTH_SHORT).show()
//            }
//        })


    }

    //    public void replydatadoc(int i) {
    //        Log.d("WalletScrollCheck","Scroll Check 39");
    //        Log.d("CheckdocSize", "replydataimage position  =   " + i);
    //        edit.putInt("Docs_check",0);
    //        edit.apply();
    //        Intent intent = new Intent(ChatActivity.this, BackgroundNotificationDocSendService.class);
    //
    //        intent.putExtra("doc_path_path",doc_path_path.get(i));
    //        intent.putExtra("id",getIntent().getStringExtra("id"));
    //        intent.putExtra("doc_name",doc_name.get(i));
    //        intent.putExtra("doc_file_size",doc_file_size.get(i));
    //        intent.putExtra("doc_extension",doc_extension.get(i));
    //        intent.putExtra("sizesize_doc",sizesize_doc);
    //        intent.putExtra("position",i);
    //        startService(intent);
    //
    //
    ////        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
    ////        spacesFileRepository.uploadExampledocFile(doc_path.get(i), ChatActivity.this, getIntent().getStringExtra("id"), doc_name.get(i), doc_file_size.get(i), doc_extension.get(i), sizesize_doc, i);
    //    }
    var mmPaths: ArrayList<String> = arrayListOf<String>()
    var mmPathsid: ArrayList<String> = arrayListOf<String>()
    var byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
    var ImageCDRIVES: ArrayList<ByteArray> = arrayListOf<ByteArray>()
    var sizesize: Int = 0
    var CDRIVES: ByteArray = byteArrayOf()
    private fun uploadImageStatus(path: String) {
        Log.d("checuploadVideoStatusment", "uploadImageStatus path  ==   " + path)
        if (!NetworkHelper.isConnected(MyApp.context())) {
            Toast.makeText(MyApp.context(), MyApp.context().resources.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(MyApp.context(), MyApp.context().resources.getString(R.string.uploading_status), Toast.LENGTH_SHORT).show()
        val mPath = compressImage(path)
        mmPaths.add(mPath)
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val randomString = RandomString()
        val video_name = ts + "_" + randomString.nextString()

        mmPathsid.add(video_name)


        val bm = BitmapFactory.decodeFile(mPath)


        byteArrayOutputStream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        CDRIVES = byteArrayOutputStream.toByteArray()
        ImageCDRIVES.add(CDRIVES)
        sizesize = ImageCDRIVES.size -1


        Log.d("checuploadVideoStatusmentmmPathsid", "uploadImageStatus mmPaths  ==   " + mmPaths)


        Log.d("checuploadVideoStatusmentmmPathsid", "uploadImageStatus mmPathsid  ==   " + mmPathsid)
//        statusManager.uploadStatus(mPath, StatusType.IMAGE, false).subscribe { status, throwable ->
//            if (throwable != null) {
//                Toast.makeText(activity, MyApp.context().resources.getString(R.string.error_uploading_status), Toast.LENGTH_SHORT).show()
//            } else {
//                setMyStatus()
//                Toast.makeText(activity, MyApp.context().resources.getString(R.string.status_uploaded), Toast.LENGTH_SHORT).show()
//            }
//        }.addTo(disposables)
    }

    override fun onResume() {
        super.onResume()
        updateHeaders()
        setMyStatus()
        //fetch status when user swipes to this page
        callbacks?.fetchStatuses()
    }

    private fun updateHeaders() {
        if (decor != null) {
            setupHeaders()
            decor?.updateHeaders(header1pos, header2pos, header1Title, header2Title)
            adapter.notifyDataSetChanged()
        }
    }

    fun statusInserted() {
        try {
            //Fix for crash 'fragment not attached to context'
            updateHeaders()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onQueryTextChange(newText: String?) {
        super.onQueryTextChange(newText)
        if (adapter != null) {

            adapter.filter(newText)
        }
    }

    override fun onSearchClose() {
        super.onSearchClose()
        adapter = StatusNewAdapter(statusesList, true, activity, this@StatusNewFragment)

        rv_status?.let {
            it.adapter = adapter
        }

    }

    override fun onStatusClick(view: View, userStatuses: UserStatuses) {
        val intent = Intent(activity, ViewStatusActivity::class.java)
        intent.putExtra(IntentUtils.UID, userStatuses.userId)
        startActivity(intent)
    }

    //compress image when user chooses an image from gallery
    private fun compressImage(imagePath: String): String {
        //generate file in sent images folder
        val file = DirManager.generateFile(MessageType.SENT_IMAGE)
        //compress image and copy it to the given file
        BitmapUtils.compressImage(imagePath, file)
        return file.path
    }

    fun onImageEditSuccess(imagePath: String) {
        uploadImageStatus(imagePath)
    }

    fun onTextStatusResult(textStatus: TextStatus) {
        if (!NetworkHelper.isConnected(MyApp.context())) {
            Toast.makeText(MyApp.context(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(MyApp.context(), R.string.uploading_status, Toast.LENGTH_SHORT).show()
//            statusManager.uploadTextStatus(textStatus).subscribe({
//                setMyStatus()
//            }, { throwable ->
//
//            }).addTo(disposables)

        }
    }

}