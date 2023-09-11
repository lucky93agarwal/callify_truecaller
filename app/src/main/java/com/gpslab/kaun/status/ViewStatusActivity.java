package com.gpslab.kaun.status;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codekidlabs.storagechooser.StorageChooser;
import com.devlomi.record_view.DpUtil;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.OTP.OTPActivity;
import com.gpslab.kaun.Phone.PhoneActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.Service.GetContactsService;
import com.gpslab.kaun.Webapi.ChatWebAPI;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.chat.MyFCMService;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.download_notification.BackgroundNotificationAudioSendService;
import com.gpslab.kaun.download_notification.BackgroundNotificationImageGalleryService;
import com.gpslab.kaun.download_notification.BackgroundNotificationService;
import com.gpslab.kaun.download_notification.BackgroundNotificationVideoSendService;
import com.gpslab.kaun.model.GetSeeStatusVideo;
import com.gpslab.kaun.model.GetUserStatusListVideo;
import com.gpslab.kaun.retrofit.Log;
import com.gpslab.kaun.view.AnimButton;
import com.gpslab.kaun.view.AttachmentView;
import com.gpslab.kaun.view.BitmapUtils;
import com.gpslab.kaun.view.CameraActivity;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.ChatEditText;
import com.gpslab.kaun.view.ContactUtils;
import com.gpslab.kaun.view.DirManager;
import com.gpslab.kaun.view.ExpandableContact;
import com.gpslab.kaun.view.FileFilter;
import com.gpslab.kaun.view.FileUtils;
import com.gpslab.kaun.view.FireManager;
import com.gpslab.kaun.view.IntentUtils;
import com.gpslab.kaun.view.KeyboardHelper;
import com.gpslab.kaun.view.Message;
import com.gpslab.kaun.view.MessageCreator;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.MyApp;
import com.gpslab.kaun.view.PhoneNumber;
import com.gpslab.kaun.view.Place;
import com.gpslab.kaun.view.PlacesPickerActivity;
import com.gpslab.kaun.view.QuotedMessage;
import com.gpslab.kaun.view.RealPathUtil;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.RecorderSettings;
import com.gpslab.kaun.view.ResultCodes;
import com.gpslab.kaun.view.SelectContactNumbersActivity;
import com.gpslab.kaun.view.SharedPreferencesManager;
import com.gpslab.kaun.view.Status;
import com.gpslab.kaun.view.StatusHelper;
import com.gpslab.kaun.view.StatusSeenBy;
import com.gpslab.kaun.view.StatusType;
import com.gpslab.kaun.view.TextStatus;
import com.gpslab.kaun.view.TimeHelper;
import com.gpslab.kaun.view.User;
import com.gpslab.kaun.view.Util;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.realm.RealmResults;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import me.zhanghai.android.systemuihelper.SystemUiHelper;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;
import ooo.oxo.library.widget.PullBackLayout;

import static com.gpslab.kaun.view.ChatActivity.CAMERA_REQUEST;
import static com.gpslab.kaun.view.ChatActivity.MAX_FILE_SIZE;
import static com.gpslab.kaun.view.ChatActivity.PICK_CONTACT_REQUEST;
import static com.gpslab.kaun.view.ChatActivity.PICK_GALLERY_REQUEST;
import static com.gpslab.kaun.view.ChatActivity.PICK_LOCATION_REQUEST;
import static com.gpslab.kaun.view.ChatActivity.PICK_MUSIC_REQUEST;
import static com.gpslab.kaun.view.ChatActivity.PICK_NUMBERS_FOR_CONTACT_REQUEST;
import static com.gpslab.kaun.view.ChatActivity.RECORD_START_AUDIO_LENGTH;

public class ViewStatusActivity extends AppCompatActivity  implements StoriesProgressView.StoriesListener, PullBackLayout.Callback, StatusSeenByCallback {
    String userId;
    StoriesProgressView storiesProgressView;
    private ImageView image;

    private VideoView videoView;
    private CircleImageView profileImage;
    private TextView tvUsername;
    private TextView tvStatusTime;
    private ImageButton backButton;
    private ConstraintLayout root;
    private TextView tvStatus;
    private RecyclerView rvSeenBy;
    private AttachmentView attachmentView;
    MediaPlayer.OnPreparedListener onPreparedListener;
    MediaPlayer.OnErrorListener onErrorListener;
    private ProgressBar progressBar;
    private StatusSeenByAdapter adapter;
    private int counter = 0;
    private TextView tvSeenCount;
    private ImageView arrowUp, replyArrowUp;


    private LinearLayout typingLayout;
    private ImageView emojiBtn;
    private ChatEditText etMessage;
//    private EmojiPopup emojiPopup;
    private ImageView imgAttachment;
    private ImageView cameraBtn;

    private RecordView recordView;
    private AnimButton recordButton;
    String timerStr = "";


    private LinearLayout bottomSheetSeen, bottomSheetReply;
    SystemUiHelper systemUiHelper;
    //image story duration 7 seconds
    public static final long IMAGE_STORY_DURATION = 7000L;
    //text story duration 6 seconds
    private static final long TEXT_STORY_DURATION = 6000L;

    long pressTime = 0L;
    long limit = 500L;
    RealmResults<Status> statuses;

    private FrameLayout quotedMessageFrame;
    private View quotedColor;
    private EmojiconTextView tvQuotedName;
    private EmojiconTextView tvQuotedText;
    private ImageView quotedThumb;
    private ImageView btnCancelImage;
    private View replyDimView;


    User user;

    Recorder recorder;
    File recordFile;
    BottomSheetBehavior seenByBottomsheetBehavior, replyBehavior;
    RealmResults<StatusSeenBy> statusSeenBy;
//    private StatusManager statusManager = new StatusManager();

    //on touch listener, when user holds his thumb it will pause the story,and when he release it will resume
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isSeenByExpanded() || isReplyExpanded()) {
                        return false;
                    }

                    pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    return resume();
            }

            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemUiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_HIDE_STATUS_BAR, SystemUiHelper.FLAG_IMMERSIVE_STICKY);
        systemUiHelper.hide();
        setContentView(R.layout.activity_view_status);



//        SendDataToServerssNew("Mobile");


        userId = getIntent().getStringExtra(IntentUtils.UID);
//        user = new User();
//        user.setUid(userId);
//        RealmHelper.getInstance().saveEmptyChat(user);
        Log.d("UseridUseridUseridUserid","Userid    ====       "+userId);
        statuses = RealmHelper.getInstance().getUserStatuses(userId).getFilteredStatuses();

        if (userId.equals(FireManager.getUid())){


            user = SharedPreferencesManager.getCurrentUser();
        }
        else{
            user = RealmHelper.getInstance().getUser(userId);
        }


        initViews();
        quotedMessageFrame.setVisibility(View.VISIBLE);
        setQuotedMessageStyle();
        //set stories durations
        storiesProgressView.setStoriesCountWithDurations(getDurations());
        storiesProgressView.setStoriesListener(this);


        //onVideo prepared listener
        onPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                image.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
//                storiesProgressView.start(counter);
                videoView.start();
            }
        };

        onErrorListener = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(ViewStatusActivity.this, R.string.error_playing_this, Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        if (statuses.size() >= 0) {

            Log.d("UseridUseridUseridUserid","storiesProgressView  counter    ====       "+counter);
            loadStatus(statuses.get(0));
            storiesProgressView.startStories(counter);
        }

        // bind reverse view
        //play the previous story (onClick)
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });




        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        //play the next story (onClick)
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);


        //back button in toolbar onClick
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Log.d("onBackPressedCheck","finish   1");
            }
        });


        PullBackLayout pullBackLayout = findViewById(R.id.pull);
        pullBackLayout.setCallback(this);

        setUserInfo(user);


        seenByBottomsheetBehavior = BottomSheetBehavior.from(bottomSheetSeen);

        seenByBottomsheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:


                        rvSeenBy.animate().translationY(-DpUtil.toPixel(50, MyApp.context())).start();
                        arrowUp.animate().rotation(180).setDuration(200).start();
                        pause();
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        arrowUp.animate().rotation(0).setDuration(200).start();
                        rvSeenBy.animate().translationY(0).start();
                        resume();

                        break;


                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        replyBehavior = BottomSheetBehavior.from(bottomSheetReply);

        replyBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    KeyboardHelper.hideSoftKeyboard(ViewStatusActivity.this, bottomSheet);
                    replyArrowUp.animate().rotation(0).setDuration(200).start();
                    replyDimView.setVisibility(View.GONE);

                    resume();
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    replyArrowUp.animate().rotation(180).setDuration(200).start();
                    replyDimView.setVisibility(View.VISIBLE);
                    pause();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        showReplyLayout();

        imgAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attachmentView.reveal(view);
//                KeyboardHelper.hideSoftKeyboard(ViewStatusActivity.this, etMessage);


            }
        });

        etMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                emojiPopup.dismiss();
                if (attachmentView.isShowing())
                    attachmentView.hide(imgAttachment);

            }
        });


        attachmentView.setOnAttachmentClick(new AttachmentView.AttachmentClickListener() {
            @Override
            public void OnClick(int id) {
                switch (id) {
                    case R.id.attachment_gallery:
                        pickImages();
                        break;

                    case R.id.attachment_camera:
                        startCamera();
                        break;

                    case R.id.attachment_document:
                        pickFile();
                        break;

                    case R.id.attachment_audio:
                        pickMusic();
                        break;

                    case R.id.attachment_contact:
                        pickContact();
                        break;

                    case R.id.attachment_location:
                        pickLocation();
                        break;
                }
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });

        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                emojiPopup.toggle();
            }
        });

        recordView.setCancelBounds(0);
        recordView.setSlideToCancelArrowColor(ContextCompat.getColor(this,R.color.iconTintColor));
        recordView.setCounterTimeColor(ContextCompat.getColor(this,R.color.colorText));
        recordView.setSlideToCancelTextColor(ContextCompat.getColor(this,R.color.colorText));
        recordButton.setRecordView(recordView);

        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                sendMessage(text);
            }
        });
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                hideOrShowRecord(false);
//                getDisposables().add(getFireManager().setTypingStat(user.getUid(), TypingStat.RECORDING, false, false).subscribe());
                handleRecord();
            }

            @Override
            public void onCancel() {
                stopRecord(true, -1);
//                getDisposables().add(getFireManager().setTypingStat(userId, TypingStat.NOT_TYPING, false, false).subscribe());
            }

            @Override
            public void onFinish(long recordTime) {
                hideOrShowRecord(true);

//                getDisposables().add(getFireManager().setTypingStat(userId, TypingStat.NOT_TYPING, false, false).subscribe());
                stopRecord(false, recordTime);
                requestEditTextFocus();
            }

            @Override
            public void onLessThanSecond() {
                Toast.makeText(ViewStatusActivity.this, R.string.voice_message_is_short_toast, Toast.LENGTH_SHORT).show();
                hideOrShowRecord(true);
//                getDisposables().add(getFireManager().setTypingStat(userId, TypingStat.NOT_TYPING, false, false).subscribe());
                stopRecord(true, -1);
                requestEditTextFocus();
            }
        });

        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                hideOrShowRecord(true);
                requestEditTextFocus();
            }
        });

        imgAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attachmentView.reveal(view);
                KeyboardHelper.hideSoftKeyboard(ViewStatusActivity.this, etMessage);


            }
        });


        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                if (text.trim().length() > 0) {
                    changeSendButtonState(true);


                } else if (text.trim().length() == 0) {
                    changeSendButtonState(false);
                }
            }
        });
      SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

      String APPUserid = sharedPreferences.getString("mobile","");
        if (userId.equalsIgnoreCase(APPUserid)){
            bottomSheetSeen.setVisibility(View.VISIBLE);
        }else {
            bottomSheetReply.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
//        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {
//            systemUiHelper.hide();
//        });
    }


    @Override
    protected void onStop() {
        super.onStop();
//        KeyboardUtils.removeAllKeyboardToggleListeners();
    }




    //start recording voice message
    private void handleRecord() {
        recordFile = DirManager.generateFile(MessageType.SENT_VOICE_MESSAGE);
        recorder = OmRecorder.wav(
                new PullTransport.Default(RecorderSettings.getMic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {

                    }
                }), recordFile);


        //start record when the record sound "BEEP" finishes
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recorder.startRecording();
            }
        }, RECORD_START_AUDIO_LENGTH);


    }






    //stop record
    private void stopRecord(boolean isCancelled, long recordTime) {
        try {
            if (recorder != null)
                recorder.stopRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if it's cancelled (the user swiped to cancel) then delete the recordFile
        if (isCancelled) {
            recordFile.delete();
        } else {

            //otherwise get the recordTime and convert it to Readable String and send the message
            timerStr = Util.milliSecondsToTimer(recordTime);
            String filePath = recordFile.getPath();
            sendVoiceMessage(filePath, timerStr);
        }

    }

    // hide/show typingLayout or recordLayout
    private void hideOrShowRecord(boolean hideRecord) {
        if (hideRecord) {
            recordView.setVisibility(View.GONE);
            typingLayout.setVisibility(View.VISIBLE);
        } else {
            recordView.setVisibility(View.VISIBLE);
            typingLayout.setVisibility(View.GONE);
        }
    }

    //set the cursor on the EditText after finish recording
    private void requestEditTextFocus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                etMessage.requestFocus();
            }
        }, 100);

    }





    public ByteArrayOutputStream byteArrayOutputStream;
    byte[] CDRIVES;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_GALLERY_REQUEST && resultCode == RESULT_OK) {
            List<String> mPaths = Matisse.obtainPathResult(data);
            for (String mPath : mPaths) {
                if (!FileUtils.isFileExists(mPath)) {
                    Toast.makeText(ViewStatusActivity.this, R.string.image_video_not_found, Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            //Check if it's a video
            if (FileUtils.isPickedVideo(mPaths.get(0))) {

                sendTheVideo(mPaths);

            } else {
                sendImage(mPaths);
            }
        } else if (requestCode == PICK_MUSIC_REQUEST && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            String[] audioArray = RealPathUtil.getAudioPath(this, uri);
            if (audioArray == null)
                Toast.makeText(this, R.string.could_not_get_audio_file, Toast.LENGTH_SHORT).show();
            else
                sendAudio(audioArray[0], audioArray[1]);

        } else if (requestCode == CAMERA_REQUEST && resultCode != ResultCodes.CAMERA_ERROR_STATE) {

            if (resultCode == ResultCodes.IMAGE_CAPTURE_SUCCESS) {
                String path = data.getStringExtra(IntentUtils.EXTRA_PATH_RESULT);

                Bitmap bm = BitmapFactory.decodeFile(path);


                byteArrayOutputStream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                CDRIVES = byteArrayOutputStream.toByteArray();
                long lengthbmp = CDRIVES.length;


                sendImage(path, true, CDRIVES, String.valueOf(lengthbmp));

            } else if (resultCode == ResultCodes.VIDEO_RECORD_SUCCESS) {
                String path = data.getStringExtra(IntentUtils.EXTRA_PATH_RESULT);
                sendTheVideo(path);

            }

            //if user choose to forward image to other users
        } else if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            //get selected contacts from Phonebook
            List<ContactResult> results = MultiContactPicker.obtainResult(data);
            //convert results to expandableList so the user can choose which numbers he wants to send
            List<ExpandableContact> contactNameList = ContactUtils.getContactsFromContactResult(results);

            Intent intent = new Intent(this, SelectContactNumbersActivity.class);
            intent.putParcelableArrayListExtra(IntentUtils.EXTRA_CONTACT_LIST, (ArrayList<? extends Parcelable>) contactNameList);
            startActivityForResult(intent, PICK_NUMBERS_FOR_CONTACT_REQUEST);


        } else if (requestCode == PICK_NUMBERS_FOR_CONTACT_REQUEST && resultCode == RESULT_OK) {
            //get contacts after the user selects the numbers he wants to send
            List<ExpandableContact> selectedContacts = data.getParcelableArrayListExtra(IntentUtils.EXTRA_CONTACT_LIST);
            sendContacts(selectedContacts);
        } else if (requestCode == PICK_LOCATION_REQUEST && resultCode == RESULT_OK) {
            Place place = data.getParcelableExtra(Place.EXTRA_PLACE);
            try {
                sendLocation(place);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendLocation(Place place)  throws IOException {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();
        Message message = new MessageCreator.Builder(user, MessageType.SENT_LOCATION,image_name).quotedMessage(getQuotedMessage()).place(place).build();
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
        double latitude = place.getLatLng().latitude;
        double longitude = place.getLatLng().longitude;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0);

        android.util.Log.d("MainActivityLL", "Contact latitude ==  ==   " + String.valueOf(latitude));
        android.util.Log.d("MainActivityLL", "Contact longitude ==  ==   " + String.valueOf(longitude));
        android.util.Log.d("MainActivityLL", "Contact address ==  ==   " + address);

        ChatWebAPI addpopup = new ChatWebAPI(ViewStatusActivity.this);
        if (getQuotedMessage() != null) {
            Message quotedMessage = getQuotedMessage();
//            hideReplyLayout();
            String replay_id = quotedMessage.getMessageId();

            android.util.Log.d("replyItemClicked_nwe", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
            android.util.Log.d("replyItemClicked_nwe", "message id =   " + image_name);
            addpopup.sendlocationReplay(getIntent().getStringExtra("id"), String.valueOf(latitude) + ";" + String.valueOf(longitude) + ";" + address, MessageType.SENT_LOCATION, image_name,replay_id);

        } else {
            addpopup.sendlocation(getIntent().getStringExtra("id"), String.valueOf(latitude) + ";" + String.valueOf(longitude) + ";" + address, MessageType.SENT_LOCATION, image_name);
        }





        //calling updateChat because the 'RealmChangeListener' may not be alive since the user launched another activity
        sendFinished();

    }

    private void sendContacts(List<ExpandableContact> selectedContacts) {

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();
        List<Message> messages = new MessageCreator.Builder(user, MessageType.SENT_CONTACT,image_name).quotedMessage(getQuotedMessage()).contacts(selectedContacts).buildContacts();



        for (MultiCheckExpandableGroup selectedContact : selectedContacts) {

            ArrayList<PhoneNumber> numbers = (ArrayList) selectedContact.getItems();
            android.util.Log.d("MainActivityLL", "Contact numbers  ==  ==   " + numbers.get(0).getNumber());
            android.util.Log.d("MainActivityLL", "Contact Title ==  ==   " + selectedContact.getTitle());





            ChatWebAPI addpopup = new ChatWebAPI(ViewStatusActivity.this);

            if (getQuotedMessage() != null) {
                Message quotedMessage = getQuotedMessage();

                String replay_id = quotedMessage.getMessageId();

                android.util.Log.d("ContactClicked_nwe", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
                android.util.Log.d("ContactClicked_nwe", "message id =   " + image_name);
//                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
                addpopup.sendcontactReplay(getIntent().getStringExtra("id"), numbers.get(0).getNumber(), selectedContact.getTitle(), MessageType.SENT_CONTACT, image_name,replay_id);
            } else {
                android.util.Log.d("ContactClicked_nwe", "else =   " + image_name);
                addpopup.sendcontact(getIntent().getStringExtra("id"), numbers.get(0).getNumber(), selectedContact.getTitle(), MessageType.SENT_CONTACT, image_name);
            }




        }
        for (Message message : messages) {
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());

            //calling updateChat because the 'RealmChangeListener' may not be alive since the user launched another activity
            sendFinished();

        }

    }


    private void sendVoiceMessage(String path, String duration) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();
        Message message = new MessageCreator.Builder(user, MessageType.SENT_VOICE_MESSAGE,image_name).quotedMessage(getQuotedMessage()).path(path).duration(duration).build();
        //addVoiceMessageStatListener to indicates when the recipient listened to this VoiceMessage
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());

        sendFinished();
    }



    private void sendTheVideo(String path) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();
        Message message = new MessageCreator.Builder(user, MessageType.SENT_VIDEO,image_name).quotedMessage(getQuotedMessage()).path(path).context(this).build();
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());

        //calling updateChat because the 'RealmChangeListener' may not be alive since the user launched another activity
        sendFinished();


    }
    public ArrayList<String> video_name_array = new ArrayList<>();
    public ArrayList<File> filePath_array = new ArrayList<>();

    public ArrayList<String> video_size_array = new ArrayList<>();

    public ArrayList<String> video_extension_array = new ArrayList<>();

    public ArrayList<File> video_thum_path_array = new ArrayList<>();

    public int sizesize_video = 0;
    public ArrayList<String> file_path_path = new ArrayList<>();
    private void sendTheVideo(List<String> pathList) {
        sizesize_video = pathList.size() - 1;

        if (file_path_path.size() > 0) {
            file_path_path.clear();
        }

        if (video_name_array.size() > 0) {
            video_extension_array.clear();
        }
        if (filePath_array.size() > 0) {
            filePath_array.clear();
        }

        if (video_size_array.size() > 0) {
            video_size_array.clear();
        }

        if (video_extension_array.size() > 0) {
            video_extension_array.clear();
        }
        if (video_thum_path_array.size() > 0) {
            video_thum_path_array.clear();
        }
        for (String path : pathList) {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String image_name = ts+"_"+randomString.nextString();
            Message message = new MessageCreator.Builder(user, MessageType.SENT_VIDEO,image_name).quotedMessage(getQuotedMessage()).path(path).context(this).build();
            Message quotedMessage = getQuotedMessage();
            replay_id = quotedMessage.getMessageId();
            Bitmap videoThumbBitmap = BitmapUtils.getThumbnailFromVideo(path);
            //generate blurred thumb to send it to other user
            String thumb = BitmapUtils.decodeImage(videoThumbBitmap);
            //generate normal video thumb without blur to show it in recyclerView
            String videoThumb = BitmapUtils.generateVideoThumbAsBase64(videoThumbBitmap);
            File file = new File(path);
            file_path_path.add(path);
            filePath_array.add(file);

            long length = file.length();


            File Imagefile = storeImage(StringToBitMap(thumb), video_name);
            video_thum_path_array.add(Imagefile);
            String lengthsize = size(length);

            video_size_array.add(lengthsize);
            String extension = path.substring(path.lastIndexOf("."));
            video_extension_array.add(extension);
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());

            //calling updateChat because the 'RealmChangeListener' may not be alive since the user launched another activity
            sendFinished();

        }
        Intent intent = new Intent(ViewStatusActivity.this, BackgroundNotificationVideoSendService.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("filePath_array", file_path_path);
        bundle.putStringArrayList("video_name_array", video_name_array);
        bundle.putStringArrayList("video_size_array", video_size_array);
        bundle.putStringArrayList("video_extension_array", video_extension_array);

        intent.putExtra("bundle", bundle);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("sizesize_video", sizesize_video);
        intent.putExtra("replay_id", replay_id);
        startService(intent);

    }


    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(String video_name) {
        android.util.Log.d("WalletScrollCheck", "Scroll Check 49");
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = video_name + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


    private File storeImage(Bitmap image, String video_name) {
        android.util.Log.d("WalletScrollCheck", "Scroll Check 48");
        File pictureFile = getOutputMediaFile(video_name);
        if (pictureFile == null) {
            android.util.Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());

        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            android.util.Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            android.util.Log.d("TAG", "Error accessing file: " + e.getMessage());
        }


        return pictureFile;
    }

    public Bitmap StringToBitMap(String encodedString) {
        android.util.Log.d("WalletScrollCheck", "Scroll Check 47");
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    private void sendMessage(Message message) {
        Message quotedMessage = getQuotedMessage();
        if (quotedMessage != null)
            message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
        RealmHelper.getInstance().saveObjectToRealm(message);
        RealmHelper.getInstance().saveChatIfNotExists(message, user);
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());


    }

    private Message getQuotedMessage() {
        if (!isCurrentIndexValid(counter)) return null;

        Status status = statuses.get(counter);
        return Status.statusToMessage(status, userId);
    }




    //send text message
    private void sendMessage(String text) {

        if (text.trim().isEmpty())
            return;

        int length = text.getBytes().length;
        if (length > FireConstants.MAX_SIZE_STRING) {
            Toast.makeText(ViewStatusActivity.this, R.string.message_is_too_long, Toast.LENGTH_SHORT).show();
            return;
        }

//        emojiPopup.dismiss();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();
        ChatWebAPI addpopup = new ChatWebAPI(ViewStatusActivity.this);

        Message message = new MessageCreator.Builder(user, MessageType.SENT_TEXT,image_name).quotedMessage(getQuotedMessage()).text(text).build();
        Message quotedMessage = getQuotedMessage();
        String replay_id = quotedMessage.getMessageId();
        addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
        etMessage.setText("");

        sendFinished();
    }
    public static String size(long size) {
        android.util.Log.d("WalletScrollCheck", "Scroll Check 52");
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    public String replay_id;
    //"isFromCamera" is when taking a picture ,because taking a picture from camera will save it directly in the app folder
    //send only one image
    private void sendImage(String filePath, boolean isFromCamera, byte[] encodedtwo, String Size) {

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();
        String lengthsize = size(Long.parseLong(Size));
        Message message = new MessageCreator.Builder(user, MessageType.SENT_IMAGE,image_name).quotedMessage(getQuotedMessage()).path(filePath).fromCamera(isFromCamera).build();
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
        Intent intent = new Intent(ViewStatusActivity.this, BackgroundNotificationService.class);
        Message quotedMessage = getQuotedMessage();

        replay_id = quotedMessage.getMessageId();
        intent.putExtra("encodedtwo", encodedtwo);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("image_name", image_name);
        intent.putExtra("lengthsize", lengthsize);
        intent.putExtra("replay_id",replay_id);
        startService(intent);
        sendFinished();

    }

    public List<String> ImageName = new ArrayList();
    public List<byte[]> ImageCDRIVES = new ArrayList();
    public List<String> lengthsizeArry = new ArrayList<>();
    public List<String> filePath = new ArrayList<>();
    //send multiple images
    private void sendImage(List<String> pathList) {
        if (filePath.size() > 0) {
            filePath.clear();
        }
        if (lengthsizeArry.size() > 0) {
            lengthsizeArry.clear();
        }
        if (ImageCDRIVES.size() > 0) {
            ImageCDRIVES.clear();
        }
        if (ImageName.size() > 0) {
            ImageName.clear();
        }
        for (String imagePath : pathList) {

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String image_name = ts+"_"+randomString.nextString();

            Message message = new MessageCreator.Builder(user, MessageType.SENT_IMAGE,image_name).quotedMessage(getQuotedMessage()).path(imagePath).fromCamera(false).build();
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
            Message quotedMessage = getQuotedMessage();
            replay_id = quotedMessage.getMessageId();




            String pathpath = imagePath;
            Bitmap bm = BitmapFactory.decodeFile(pathpath);


            byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            CDRIVES = byteArrayOutputStream.toByteArray();
//            encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);
            long lengthbmp = CDRIVES.length;
            String lengthsize = size(lengthbmp);
            android.util.Log.d("SendImageLucky", "Image Path  =   1 " + imagePath);
//            updateChat(message);


            filePath.add(imagePath);
            lengthsizeArry.add(lengthsize);
            ImageCDRIVES.add(CDRIVES);
            ImageName.add(image_name);
        }
        sizesize = pathList.size() - 1;
        Intent intent = new Intent(ViewStatusActivity.this, BackgroundNotificationImageGalleryService.class);

        intent.putExtra("encodedtwo", ImageCDRIVES.get(0));
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("image_name", ImageName.get(0));
        intent.putExtra("lengthsize", lengthsizeArry.get(0));
        intent.putExtra("sizesize", sizesize);
        intent.putExtra("position", 0);
        intent.putExtra("replay_id",replay_id);
        startService(intent);
        sendFinished();

    }

    public int sizesize = 0;
    private void sendFile(final String filePath) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();

        Message message = new MessageCreator.Builder(user, MessageType.SENT_FILE,image_name).quotedMessage(getQuotedMessage()).path(filePath).build();
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());

        sendFinished();

    }




    private void sendAudio(final String filePath, String audioDuration) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts+"_"+randomString.nextString();

        Message message = new MessageCreator.Builder(user, MessageType.SENT_AUDIO,image_name).quotedMessage(getQuotedMessage()).path(filePath).duration(audioDuration).build();
        Message quotedMessage = getQuotedMessage();

        replay_id = quotedMessage.getMessageId();
        if (message == null) {
            Toast.makeText(this, R.string.space_or_permissions_error_toast, Toast.LENGTH_SHORT).show();
        } else {
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
            sendFinished();



            String extension = filePath.substring(filePath.lastIndexOf("."));

            File file = new File(filePath);

            android.util.Log.d("WalletCheckLucky", "uploadExampleFile extension  " + extension);
            android.util.Log.d("WalletCheckLucky", "uploadExampleFile audioDuration  " + audioDuration);
            android.util.Log.d("WalletCheckLucky", "uploadExampleFile image_name  " + image_name);
//            spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//
//
//            spacesFileRepository.uploadExampleAudioFile(file, ChatActivity.this, getIntent().getStringExtra("id"), image_name, audioDuration, extension);

            android.util.Log.d("WalletAudiouckyreplay_id", "uploadExampleFile replay_id  " + replay_id);
            Intent intent = new Intent(ViewStatusActivity.this, BackgroundNotificationAudioSendService.class);

            intent.putExtra("filePath", filePath);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            intent.putExtra("image_name", image_name);
            intent.putExtra("audioDuration", audioDuration);
            intent.putExtra("extension", extension);
            intent.putExtra("replay_id",replay_id);

            startService(intent);

        }


    }

    private void sendFinished() {
        Toast.makeText(ViewStatusActivity.this, R.string.sending_reply, Toast.LENGTH_SHORT).show();
        KeyboardHelper.hideSoftKeyboard(this, etMessage);
        replyBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void startCamera() {
        startActivityForResult(new Intent(ViewStatusActivity.this, CameraActivity.class), CAMERA_REQUEST);
    }

    private void pickImages() {
        Matisse.from(ViewStatusActivity.this)
                .choose(MimeType.of(MimeType.MP4, MimeType.THREEGPP, MimeType.THREEGPP2
                        , MimeType.JPEG, MimeType.BMP, MimeType.PNG, MimeType.GIF))
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(PICK_GALLERY_REQUEST);
    }


    private void pickMusic() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_MUSIC_REQUEST);
    }

    private void pickLocation() {
        startActivityForResult(new Intent(this, PlacesPickerActivity.class), PICK_LOCATION_REQUEST);
    }

    private void pickContact() {
        new MultiContactPicker.Builder(ViewStatusActivity.this)
                .handleColor(ContextCompat.getColor(ViewStatusActivity.this, R.color.colorPrimary))
                .bubbleColor(ContextCompat.getColor(ViewStatusActivity.this, R.color.colorPrimary))
                .showPickerForResult(PICK_CONTACT_REQUEST);
    }


    private void pickFile() {
        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(ViewStatusActivity.this)
                .withFragmentManager(getFragmentManager())
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .disableMultiSelect()
                .build();


        chooser.show();


        chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
            @Override
            public void onSelect(String path) {
                File file = new File(path);
                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                String fileExtension = Util.getFileExtensionFromPath(path);

                if (file_size > MAX_FILE_SIZE) {
                    Toast.makeText(ViewStatusActivity.this, R.string.file_is_too_big, Toast.LENGTH_SHORT).show();

                } else if (!FileFilter.isOkExtension(fileExtension)) {
                    Toast.makeText(ViewStatusActivity.this, R.string.type_not_supported, Toast.LENGTH_SHORT).show();
                } else {
                    sendFile(path);
                }

            }
        });


    }

    private void setupAdapter(Status status) {
        adapter = new StatusSeenByAdapter(statusSeenBy, this);
        rvSeenBy.setLayoutManager(new LinearLayoutManager(this));
        rvSeenBy.setAdapter(adapter);


//        getDisposables().add(statusManager.getStatusSeenByList(status.getStatusId()).subscribe(pair -> {
//            Status currentStatus = statuses.get(counter);
//            String statusId = pair.component1();
//            List<StatusSeenBy> statusSeenBy = pair.component2();
//            if (statusId.equals(currentStatus.getStatusId()))
//                tvSeenCount.setText(statusSeenBy.size() + "");
//        }, throwable -> {
//
//        }));
    }


    private void showReplyLayout() {
        if (!isCurrentIndexValid(counter)) return;

        btnCancelImage.setVisibility(View.VISIBLE);

        Status status = statuses.get(counter);
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String APPUserid = sharedPreferences.getString("mobile","");
        Log.d("CheckUserID","CheckUserID   userId ==     "+userId);
        Log.d("CheckUserID","CheckUserID   APPUserid ==     "+APPUserid);
        if (userId.equalsIgnoreCase(APPUserid)){
            tvQuotedName.setText("You");
        }else {
            tvQuotedName.setText(sharedPreferences.getString("other_user_name",""));
        }


        tvQuotedText.setText(StatusHelper.INSTANCE.getStatusContent(status));
        if (status.getThumbImg() != null) {
            quotedThumb.setVisibility(View.VISIBLE);
            Glide.with(this).load(status.getThumbImg()).into(quotedThumb);
        } else
            quotedThumb.setVisibility(View.GONE);

        if (status.getType() != StatusType.TEXT && StatusHelper.INSTANCE.getStatusTypeDrawable(status.getType()) != -1) {
            int messageTypeResource = StatusHelper.INSTANCE.getStatusTypeDrawable(status.getType());
            if (messageTypeResource != -1) {
                Drawable drawable = getResources()
                        .getDrawable(messageTypeResource);
                drawable.mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);
                tvQuotedText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        } else
            tvQuotedText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    private void setQuotedMessageStyle() {
        quotedMessageFrame.setBackgroundColor(ContextCompat.getColor(this, R.color.quoted_sent_background_color));
        tvQuotedName.setTextColor(ContextCompat.getColor(this, R.color.quoted_sent_text_color));
        quotedColor.setBackgroundColor(ContextCompat.getColor(this, R.color.quoted_sent_quoted_color));
        btnCancelImage.setColorFilter(ContextCompat.getColor(this, R.color.quoted_cancel_color), PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        videoView.setOnPreparedListener(null);
        videoView.setOnErrorListener(null);
        storiesProgressView.destroy();
        super.onDestroy();
    }

    private boolean resume() {
        long now = System.currentTimeMillis();

        //resume video if needed
        if (statuses.get(counter).getType() == StatusType.VIDEO && !videoView.isPlaying()) {
            videoView.start();
        }
        storiesProgressView.resume();
        return limit < now - pressTime;
    }


    private void pause() {
        pressTime = System.currentTimeMillis();
        storiesProgressView.pause();
        //pause video if needed
        if (statuses.get(counter).getType() == StatusType.VIDEO && videoView.isPlaying()) {
            videoView.pause();
        }
    }


    private void initViews() {
        storiesProgressView = findViewById(R.id.stories);
        image = findViewById(R.id.image);
        videoView = findViewById(R.id.video_view);
        profileImage = findViewById(R.id.profile_image);
        tvUsername = findViewById(R.id.tv_username);
        tvStatusTime = findViewById(R.id.tv_status_time);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progress_bar);
        tvStatus = findViewById(R.id.tv_status);
        tvSeenCount = findViewById(R.id.tv_seen_count);
        arrowUp = findViewById(R.id.arrow_up);
        rvSeenBy = findViewById(R.id.rv_seen_by);
        bottomSheetSeen = findViewById(R.id.bottom_sheet_seen_by);
        bottomSheetReply = findViewById(R.id.bottom_sheet_reply_status);
        quotedMessageFrame = findViewById(R.id.quoted_message_frame);
        quotedColor = findViewById(R.id.quoted_color);
        tvQuotedName = findViewById(R.id.tv_quoted_name);
        tvQuotedText = findViewById(R.id.tv_quoted_text);
        quotedThumb = findViewById(R.id.quoted_thumb);
        btnCancelImage = findViewById(R.id.btn_cancel_image);
        replyDimView = findViewById(R.id.reply_dim_view);

        typingLayout = findViewById(R.id.typing_layout);
        emojiBtn = findViewById(R.id.emoji_btn);
        etMessage = findViewById(R.id.et_message);
        imgAttachment = findViewById(R.id.img_attachment);
        cameraBtn = findViewById(R.id.camera_btn);

        root = findViewById(R.id.root);
        recordView = findViewById(R.id.record_view);
        recordButton = findViewById(R.id.record_button);

        attachmentView = findViewById(R.id.attachment_view);
        Log.d("UseridUseridUseridUserid","storiesProgressView  statuses    ====       "+statuses.size());
        storiesProgressView.setStoriesCount(statuses.size());

        replyArrowUp = findViewById(R.id.reply_arrow_up);

//        emojiPopup = EmojiPopup.Builder.fromRootView(root)
//                .setOnEmojiPopupShownListener(() -> emojiBtn.setImageResource(R.drawable.ic_baseline_keyboard_24))
//                .setOnEmojiPopupDismissListener(() -> emojiBtn.setImageResource(R.drawable.ic_insert_emoticon_black))
//                .build(etMessage);


    }

    @Override
    public void onNext() {
        videoView.stopPlayback();
        videoView.setOnPreparedListener(null);
        videoView.setOnErrorListener(null);
        int newCounter = counter + 1;

        if (newCounter >= 0 && newCounter < statuses.size()) {
            counter = newCounter;
            loadStatus(statuses.get(counter));
        } else {
            return;
        }
    }

    private boolean isCurrentIndexValid(int index) {

        if (index >= 0 && index < statuses.size()) {
            return true;
        }
        return false;
    }

    @Override
    public void onPrev() {

        videoView.stopPlayback();
        videoView.setOnPreparedListener(null);
        videoView.setOnErrorListener(null);


        int newCounter = counter - 1;

        if (newCounter >= 0 && newCounter < statuses.size()) {
            counter = newCounter;
            loadStatus(statuses.get(counter));
        } else {
            return;
        }

    }


    @Override
    public void onComplete() {
        Log.d("onBackPressedCheck","finish   2");
        finish();
    }


    public String Status_image_id;

    private void loadStatus(final Status status) {

        setStatusTime(status.getTimestamp());
//        storiesProgressView.setCurrent(counter);
        videoView.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
        tvStatus.setVisibility(View.GONE);
        root.setBackgroundColor(Color.BLACK);

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        String APPUserid = sharedPreferences.getString("mobile","");
        Log.d("CheckUserID","CheckUserID   userId ==     "+userId);
        Log.d("CheckUserID","CheckUserID   APPUserid ==     "+APPUserid);
        if (userId.equalsIgnoreCase(APPUserid)){

            if (status.getType() == StatusType.IMAGE || status.getType() == StatusType.VIDEO)
                image.setImageBitmap(BitmapUtils.simpleBlur(this, BitmapUtils.encodeImage(status.getThumbImg())));
        }else {
            if (status.getType() == StatusType.IMAGE || status.getType() == StatusType.VIDEO){
                Glide.with(this).load(status.getThumbImg()).into(image);

            }



//        Status_image_id = status.getStatusId();
//        SendDataToServerss("Mobile");

        }
        //load thumb blurred image while loading original image or video







//        Status_image_id = status.getStatusId();
//        SendDataToServerss("Mobile");
        Log.d("CheckUserIDCheckUserID","status ==     "+status.getStatusId());

        if (status.getType() == StatusType.IMAGE) {
            loadImage(status);
        } else if (status.getType() == StatusType.VIDEO) {
            loadVideo(status);
        } else if (status.getType() == StatusType.TEXT) {
            progressBar.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            loadTextStatus(status.getTextStatus());
        }

        statusSeenBy = RealmHelper.getInstance().getSeenByList(status.getSeenBy());
        tvSeenCount.setText(statusSeenBy.size() + "");
        setupAdapter(status);

        //set status as seen
        if (!status.isSeen()) {
            RealmHelper.getInstance().setStatusAsSeen(status.getStatusId());
            //check if all statuses are seen and save it
            if (status.getStatusId().equals(statuses.last().getStatusId()))
                RealmHelper.getInstance().setAllStatusesAsSeen(userId);
        }
        //Schedule a job to update status count on Firebase
        if (!status.getUserId().equals(FireManager.getUid()) && !status.isSeenCountSent()) {
//            getDisposables().add(statusManager.setStatusSeen(userId, status.getStatusId()).subscribe(() -> {
//
//            }, throwable -> {
//
//            }));
        }


    }

    private void loadTextStatus(TextStatus textStatus) {
        try {
            String color = textStatus.getBackgroundColor();
            root.setBackgroundColor(Color.parseColor(color));
            String fontName = textStatus.getFontName();

//            if (isFontExists(fontName)) {
//                tvStatus.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + fontName));
//            }

            tvStatus.setText(textStatus.getText());
//            storiesProgressView.start(counter);
        } catch (Exception e) {
            root.setBackgroundColor(Color.BLACK);

        }

    }


    private void loadImage(Status status) {

        //if this status by this user load it locally ,otherwise load it from server and cache it
        String url = status.getLocalPath() == null ? status.getContent() : status.getLocalPath();

        Glide.with(this).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
//                storiesProgressView.start(counter);
                image.setImageDrawable(resource);
                return false;
            }
        }).into(image);
    }

    private void loadVideo(Status status) {
        //if the video is not exists download it
        Log.d("onBackPressedCheck","loadVideo   2");
        if (status.getLocalPath() == null) {
            Log.d("onBackPressedCheck","downloadStatusVideo   1");
//            downloadStatusVideo(status);
            playVideoNew(status.getLocalPath());
        } else {
            //if the video is exists in device play it
            if (FileUtils.isFileExists(status.getLocalPath())) {
                playVideo(status.getLocalPath());
            } else {
                Log.d("onBackPressedCheck","downloadStatusVideo   2");
                //otherwise download it
                playVideoNew(status.getLocalPath());
//                downloadStatusVideo(status);
            }
        }
    }


    public String video_name;
    private void downloadStatusVideo(Status status) {
        Log.d("onBackPressedCheck","getLocalPath   2   =      "+status.getLocalPath());
        File statusFile = DirManager.getReceivedStatusFile(status.getStatusId(), status.getType());
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        video_name = ts + "_" + randomString.nextString() + "downloaded_audio.mp4";
        DownloadTaskVideo downloadaudio = new DownloadTaskVideo();
        downloadaudio.execute(status.getLocalPath());

//        getDisposables().add(statusManager.downloadVideoStatus(status.getStatusId(), status.getContent(), statusFile).subscribe(filePath -> {
//            Status currentStatus = statuses.get(counter);
//
//            if (isCurrentIndexValid(counter) && currentStatus != null &&
//                    status.getStatusId().equals(currentStatus.getStatusId())
//                    && filePath != null) {
//
//                playVideo(filePath);
//
//            }
//        }, throwable -> {
//
//        }));
    }
    private void playVideoNew(String path) {
        videoView.requestFocus();
        videoView.setVideoPath(path);
        videoView.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(onPreparedListener);
        videoView.setOnErrorListener(onErrorListener);








//        final MediaPlayer.OnInfoListener onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {
//
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                switch (what) {
//                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
//                        storiesProgressView.resume();
////                        mProgressBar.setVisibility(View.GONE);
//                        Log.d("onBackPressedCheckCheck","MEDIA_INFO_VIDEO_RENDERING_START");
//                        return true;
//                    }
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
//                        storiesProgressView.pause();
//                        Log.d("onBackPressedCheckCheck","MEDIA_INFO_BUFFERING_START");
////                        mProgressBar.setVisibility(View.VISIBLE);
//                        return true;
//                    }
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
//                        storiesProgressView.resume();
////                        mProgressBar.setVisibility(View.GONE);
//                        Log.d("onBackPressedCheckCheck","MEDIA_INFO_BUFFERING_END");
//                        return true;
//                    }
//                }
//                return false;
//            }
//
//        };
//
//        videoView.setOnInfoListener(onInfoToPlayStateListener);


    }
    private void playVideo(String path) {
        videoView.requestFocus();
        videoView.setVideoURI(Uri.parse(path));
        videoView.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(onPreparedListener);
        videoView.setOnErrorListener(onErrorListener);
    }

    class DownloadTaskVideo extends AsyncTask<String, Integer, String> {

//        ProgressDialog progressDialog;

        /**
         * Set up a ProgressDialog
         */
        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(TestingActivity.this);
//            progressDialog.setTitle("Download in progress...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setMax(100);
//            progressDialog.setProgress(0);
//            progressDialog.show();

        }

        /**
         * Background task
         */
        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length;

            android.util.Log.d("onundeliveredMessage: path", path);
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();

                /**
                 * Create a folder
                 */
                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
                        android.util.Log.d("InfoInfoInfo", "Folder succesfully created");
                    } else {
                        android.util.Log.d("InfoInfoInfo", "Failed to create folder");
                    }
                } else {
                    android.util.Log.d("InfoInfoInfo", "Folder already exists" + new_folder);
                }

                /**
                 * Create an output file to store the image for download
                 */


                File output_file = new File(new_folder, video_name);
                OutputStream outputStream = new FileOutputStream(output_file);
                android.util.Log.d("InfoInfoInfo", "output_file: " + output_file);
                android.util.Log.d("InfoInfoInfo", "outputStream: " + outputStream);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                android.util.Log.d("InfoInfoInfo", "inputStream: " + inputStream);
                byte[] data = new byte[1024];
                int total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;

                    outputStream.write(data, 0, count);
                    int progress = 100 * total / file_length;
                    publishProgress(progress);

                    android.util.Log.d("InfoInfoInfo", "Progress: " + Integer.toString(progress));
                }
                inputStream.close();
                outputStream.close();

                android.util.Log.d("InfoInfoInfo", "file_length: " + Integer.toString(file_length));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download complete.";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressDialog.hide();

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
            File output_file = new File(folder, video_name);
            String path = output_file.toString();

            Log.d("onBackPressedCheck", "path: " + path);

            playVideo(path);
//            imageView.setImageDrawable(Drawable.createFromPath(path));
//            Log.i("Info", "Path: " + path);
        }
    }
    //get statuses durations
    private long[] getDurations() {
        long[] array = new long[statuses.size()];
        for (int i = 0; i < statuses.size(); i++) {
            Status status = statuses.get(i);
            //if it's an image set its duration to IMAGE_STORY_DURATION
            if (status.getType() == StatusType.IMAGE) {
                array[i] = IMAGE_STORY_DURATION;
            } else if (status.getType() == StatusType.TEXT) {
                array[i] = TEXT_STORY_DURATION;
            } else {
                //if it's a video set its duration to the video duration
                array[i] = status.getDuration();
            }
        }
        return array;
    }

    @Override
    public void onPullStart() {
        isFinishing = false;
    }

    float currentVelocity = 0;

    private boolean isReplyExpanded() {
        return replyBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    private boolean isSeenByExpanded() {
        return seenByBottomsheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    @Override
    public void onPull(float v) {
        currentVelocity = v;
        if (isFinishing
                || isSeenByExpanded()
                || isReplyExpanded())
            return;
        int height = root.getHeight();
        float newAlpha = 1 - v;
        float trans = height * v;
        if (v >= 0.2) {
            root.animate().translationY(height).setDuration(200).start();
            root.animate().alpha(0).setDuration(200).start();
            isFinishing = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("onBackPressedCheck","finish   3");
                    finish();
                }
            }, 200);
        } else {
            root.animate().alpha(newAlpha).setDuration(0).start();
            root.animate().translationY(trans).setDuration(0).start();
        }
    }

    boolean isFinishing = false;

    @Override
    public void onPullCancel() {
        if (currentVelocity <= 0.2) {

            root.animate().translationY(0).setDuration(200).start();
            root.animate().alpha(1).setDuration(200).start();
        }
        resume();
    }

    //when the user swipes vertically exit the activity
    @Override
    public void onPullComplete() {
//        finish();
    }
    private void setStatusTime(long timestamp) {
        tvStatusTime.setText(TimeHelper.getStatusTime(timestamp));
    }

    //set user image and user info
    private void setUserInfo(User user) {

        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        String APPUserid = sharedPreferences.getString("mobile","");
        Log.d("CheckUserID","CheckUserID   userId ==     "+userId);
        Log.d("CheckUserID","CheckUserID   APPUserid ==     "+APPUserid);
        if (userId.equalsIgnoreCase(APPUserid)){
            Glide.with(this).load(sharedPreferences.getString("img","")).into(profileImage);
            tvUsername.setText(getResources().getString(R.string.you));
        }

        else{
            Glide.with(this).load(sharedPreferences.getString("img_img","")).into(profileImage);
            tvUsername.setText(sharedPreferences.getString("other_user_name",""));
        }

    }

    @Override
    public void onClick(@NotNull User user, @NotNull View itemView) {

    }

    @Override
    public void onBackPressed() {
        Log.d("onBackPressedCheck","onBackPressed");

        if (attachmentView.isShowing()) {
            attachmentView.hide(imgAttachment);
        } else
            super.onBackPressed();
    }

    private void changeSendButtonState(boolean setTyping) {
        if (setTyping) {
            recordButton.goToState(AnimButton.TYPING_STATE);
            recordButton.setListenForRecord(false);

        } else {
            recordButton.goToState(AnimButton.RECORDING_STATE);
            recordButton.setListenForRecord(true);
        }

    }
//
//    @Override
//    public boolean enablePresence() {
//        return false;
//    }





    String resTxt = null;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;

    private void SendDataToServerss(final String mobile) {

//        GetContactsIntoArrayList();
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                edit = sharedPreferences.edit();

                String QuickMobile = sharedPreferences.getString("mobile","");


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                nameValuePairs.add(new BasicNameValuePair("user_id", QuickMobile));
                nameValuePairs.add(new BasicNameValuePair("image_or_video_id", Status_image_id));



                android.util.Log.d("dfsdfsdfsdfdfsd", "QuickMobile = = " + QuickMobile);
                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "verify_user/");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    resTxt = EntityUtils.toString(entity);

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return resTxt;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
//                mprogress.setVisibility(View.GONE);
                android.util.Log.d("dfsdfsdfsdfdfsd", "result = = " + result);
                if (result.isEmpty()) {

                } else {
                    String res_result;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Log.d("dfsdfsdfsdfdfsd", "jsonObject2 = = " + jsonObject.getString("response"));



                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mobile);
    }



    public ArrayList<GetSeeStatusVideo> productlist = new ArrayList<>();
    public GetSeeStatusVideo getSeeStatusVideo = new GetSeeStatusVideo();



    public ArrayList<GetUserStatusListVideo> productlist_userlist = new ArrayList<>();
    public GetUserStatusListVideo getUserStatusListVideo = new GetUserStatusListVideo();

    public List<StatusSeenBy> seenByList = new ArrayList<>();
    public StatusSeenBy statusSeenBy_new = new StatusSeenBy();
    private void SendDataToServerssNew(final String mobile) {

//        GetContactsIntoArrayList();
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                edit = sharedPreferences.edit();

                String QuickMobile = sharedPreferences.getString("mobile","");


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                nameValuePairs.add(new BasicNameValuePair("user_id", QuickMobile));



                android.util.Log.d("dfsdfsdfsdfdfsd", "QuickMobile = = " + QuickMobile);
                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "verify_user/");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    resTxt = EntityUtils.toString(entity);

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return resTxt;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
//                mprogress.setVisibility(View.GONE);
                android.util.Log.d("dfsdfsdfsdfdfsd", "result = = " + result);
                if (result.isEmpty()) {

                } else {
                    String res_result;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray response = jsonObject.getJSONArray("response");

                        if (response.length() == 1) {
                            res_result = response.getJSONObject(0).getString("result");
                            android.util.Log.d("OTPPage", "res_result = = " + res_result);
                        } else {
                            res_result = response.getJSONObject(1).getString("result");
                        }

                        if (res_result.equalsIgnoreCase("0")) {

                            for(int i=0;i<response.length();i++){
                                String story_id = response.getJSONObject(i).getString("fname");
                                JSONArray userlist = response.getJSONObject(i).getJSONArray("userlist");
//                                getSeeStatusVideo.setStatusid(story_id);

                                for(int j=0;j<userlist.length();j++){
                                    String username = userlist.getJSONObject(i).getString("username");
                                    String userimage = userlist.getJSONObject(i).getString("userimage");
                                    String sectime = userlist.getJSONObject(i).getString("sectime");

//
//                                    getUserStatusListVideo.setUsername(username);
//                                    getUserStatusListVideo.setUserimage(userimage);
//                                    getUserStatusListVideo.setSectime(sectime);

                                    User user = new User();
                                    user.setUserName(username);
                                    user.setPhoto(userimage);
                                    statusSeenBy_new.setUser(user);
                                    statusSeenBy_new.setSeenAt(Long.parseLong(sectime));
                                    seenByList.add(statusSeenBy_new);

//
//
//                                    productlist_userlist.add(getUserStatusListVideo);
                                }
//                                getSeeStatusVideo.setUserlist(productlist_userlist);
//
//
//
//                                productlist.add(getSeeStatusVideo);




                                RealmHelper.getInstance().saveSeenByList(story_id,seenByList);
                            }

                        }
                        Log.d("dfsdfsdfsdfdfsd", "jsonObject2 = = " + jsonObject.getString("response"));



                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(mobile);
    }
}