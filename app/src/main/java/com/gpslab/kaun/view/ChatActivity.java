package com.gpslab.kaun.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codekidlabs.storagechooser.StorageChooser;
import com.devlomi.hidely.hidelyviews.HidelyImageButton;
import com.devlomi.record_view.DpUtil;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordView;
import com.fxn.pix.Pix;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gpslab.kaun.Forward.ForwardActivity;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.MyCustomDialog;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Webapi.ChatWebAPI;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.calling.CallingActivity;
import com.gpslab.kaun.calling.CameraNewActivity;
import com.gpslab.kaun.chat.MyFCMService;
import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.digitaloceanspaces.SpacesFileRepository;
import com.gpslab.kaun.download_notification.BackgroundNotificationAudioSendService;
import com.gpslab.kaun.download_notification.BackgroundNotificationDocSendService;
import com.gpslab.kaun.download_notification.BackgroundNotificationImageGalleryService;
import com.gpslab.kaun.download_notification.BackgroundNotificationService;
import com.gpslab.kaun.download_notification.BackgroundNotificationVideoSendService;
import com.gpslab.kaun.fullscreen.DeleteDialog;
import com.gpslab.kaun.fullscreen.FullscreenActivity;
import com.gpslab.kaun.language.LanguageActivity;
import com.gpslab.kaun.mssagedetail.MessageDetailsActivity;
import com.gpslab.kaun.popup.defaultpoup;
import com.gpslab.kaun.status.FireConstants;
import com.gpslab.kaun.status.TypingStat;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.nekoloop.base64image.Base64Image;
import com.nekoloop.base64image.RequestEncode;
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;

import static com.gpslab.kaun.view.DownloadUploadStat.CANCELLED;
import static com.gpslab.kaun.view.DownloadUploadStat.FAILED;
import static com.gpslab.kaun.view.DownloadUploadStat.LOADING;

public class ChatActivity extends AppCompatActivity implements Interaction, ContactHolderInteraction, AudibleInteraction {
    /////////////////
    private MessageSwipeController messageSwipeController;
    MessagingAdapter adapter;
    User user;
    /////////////
    public Bitmap selectedImage = null;
    public ByteArrayOutputStream byteArrayOutputStream;
    byte[] CDRIVES;
    public String encoded = "null";


    public static Boolean checkclick = false;
    public static MediaPlayer mediaPlayers = new MediaPlayer();


    public SpacesFileRepository spacesFileRepository;

    private RecyclerView recyclerView;
    public static final int RECORD_START_AUDIO_LENGTH = 575;
    String timerStr = "";
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    private ImageView imgAttachment, cameraBtn, emojiBtn;
    public ChatEditText etMessage;
    File recordFile;
    String previousMessageIdForScroll = "";
    private AttachmentView attachmentView;
    private FrameLayout quotedMessageFrame;
    View rootView;
    RealmResults<Message> messageList;
    private Message currentQuotedMessage = null;
    private View quotedColor;
    private ConstraintLayout typingLayoutContainer, mainContainer;
    int unreadCount = 0;
    private CircleImageView userImgToolbarChatAct;
    private boolean mIsDetailsActivityStarted = false;
    private HidelyImageButton btnScroll;
    private ImageView btnCancelImage;
    private EmojiconTextView tvQuotedName;
    LinearLayoutManager linearLayoutManager;
    private EmojiconTextView tvQuotedText;
    public EmojIconActions actions;
    boolean wasInTypingMode = false;
    private RecordView recordView;
    private AnimButton recordButton;
    private LinearLayout typingLayout;
    private ImageView quotedThumb;
    public static int MAX_FILE_SIZE = 40000;
    public static final int MAX_SELECTABLE = 9;
    public static final int PICK_MUSIC_REQUEST = 159;
    private TextView tvCantSendMessages;
    public TextView countUnreadBadge;
    public static final int CAMERA_REQUEST = 4659;
    public static final int PICK_GALLERY_REQUEST = 4815;
    public static final int PICK_DOCUMENT = 1024;
    public static final int FORWARD_MESSAGE_REQUEST = 4981;
    public static final int PICK_CONTACT_REQUEST = 5491;
    public static final int PICK_NUMBERS_FOR_CONTACT_REQUEST = 5517;
    public static final int PICK_LOCATION_REQUEST = 7125;
    boolean typingStarted = false;
    Recorder recorder;
    boolean isGroup = false;
    boolean isBroadcast = false;
    OrderedRealmCollectionChangeListener<RealmResults<Message>> changeListener;

    String[] appPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.USE_FULL_SCREEN_INTENT};




    String[] appAudioPermission =  {Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};



    String[] appLocationPermission =  {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};




    private static final int PERMISSION_REQUEST_CODE = 1240;
    private StickyHeaderDecoration decor;
    public boolean isInActionMode = false;
    private ChatViewModel viewModel;

    private Window window;
    private int brightness = 200;
    private SearchView searchViewToolbar;

    private void ScreenViewControl() {
        Log.d("WalletScrollCheck", "Scroll Check 1");
        //       Get the current system brightness
        window = ChatActivity.this.getWindow();
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }

    private void setQuotedMessageStyle() {
        Log.d("WalletScrollCheck", "Scroll Check 2");
        quotedMessageFrame.setBackgroundColor(ContextCompat.getColor(this, R.color.quoted_sent_background_color));
        tvQuotedName.setTextColor(ContextCompat.getColor(this, R.color.quoted_sent_text_color));
        quotedColor.setBackgroundColor(ContextCompat.getColor(this, R.color.quoted_sent_quoted_color));
        btnCancelImage.setColorFilter(ContextCompat.getColor(this, R.color.quoted_cancel_color), PorterDuff.Mode.SRC_IN);
    }

    public TextView tvname, tvtyping;
    public ImageView ivuserimg;
    public String UserChat_id;


    public void onTyping(int in) {
        Log.d("WalletScrollCheck", "Scroll Check 3");
        if (in == 0) {
            tvtyping.setText("isTyping");
        } else {
            tvtyping.setText("");
        }

    }

    public int counter = 0;

    public String Scroll_value = "0";


    public ImageView ivback;
    Chat chat;

    @Override
    public void onBackPressed() {
        if (isInActionMode)
            exitActionMode();
        else if (isInSearchMode)
            exitSearchMode();
        else if (attachmentView.isShowing()) {
            attachmentView.hide(imgAttachment);
        } else
            super.onBackPressed();
    }

    private Toolbar toolbar;

    private void updateMuteItemTitle() {
        if (toolbar.getMenu().findItem(R.id.mute_item) != null) {
            toolbar.getMenu().findItem(R.id.mute_item).setTitle(chat != null && chat.isMuted() ? getString(R.string.unmute_notifications) : getString(R.string.mute_notifications));
        }
    }

    Menu currentMenu;

    private void updateBlockMenuItemTitle(Menu menu) {
        if (menu == null) return;
        MenuItem item = menu.findItem(R.id.block_contact);
        if (item != null) {
            item.setTitle(user.isBlocked() ? getString(R.string.unblock_contact) : getString(R.string.block_contact));
        }
    }

    private void updateGroupCallButtonsVisibility(Menu menu) {

        boolean showCallsButtons = isGroupActive() && user.getGroup() != null && user.getGroup().getUsers().size() <= 20;

        MenuItem voiceCallItem = menu.findItem(R.id.voice_call_item);
        if (voiceCallItem != null) {
            voiceCallItem.setVisible(showCallsButtons);
        }

        MenuItem videoCallItem = menu.findItem(R.id.video_call_item);

        if (videoCallItem != null) {
            videoCallItem.setVisible(showCallsButtons);
        }
    }

    private void setGroupMenuItems(Menu menu) {
        if (isGroup || isBroadcast) {
            menu.findItem(R.id.block_contact).setVisible(false);
            menu.findItem(R.id.add_to_contacts).setVisible(false);
            if (isGroup)
                menu.findItem(R.id.view_contact_menu_item).setTitle(R.string.group_info);
            else menu.findItem(R.id.view_contact_menu_item).setTitle(R.string.broadcast_list_info);

        }
    }

    int searchIndex = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.currentMenu = menu;

        getMenuInflater().inflate(R.menu.menu_chat, menu);

        updateBlockMenuItemTitle(menu);

        if (!isGroup && !isBroadcast)
            menu.findItem(R.id.add_to_contacts).setVisible(!ContactUtils.contactExists(this, user.getPhone()));
        else {
            if (user.isBroadcastBool()) {
                menu.findItem(R.id.voice_call_item).setVisible(false);
                menu.findItem(R.id.video_call_item).setVisible(false);
            }

            if (user.isGroupBool()) {
                updateGroupCallButtonsVisibility(menu);
            }
        }


        if (chat == null) {
            MenuItem item = menu.findItem(R.id.mute_item);
            if (item != null) {
                item.setVisible(false);
            }
        }

        updateMuteItemTitle();

        setGroupMenuItems(menu);


        return super.onCreateOptionsMenu(menu);
    }

    //get index from list using the id
    private int getPosFromId(String messageId) {
        Message message = new Message();
        message.setMessageId(messageId);
        return messageList.indexOf(message);
    }

    private void scrollAndHighlightSearch(final int index) {
        recyclerView.scrollToPosition(index);
        View view = this.getCurrentFocus();
        //hide keyboard
        if (view != null)
            KeyboardHelper.hideSoftKeyboard(this, view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //get view holder of this textView
                RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(index);
                //get textView
                TextView tv = viewHolderForAdapterPosition.itemView.findViewById(R.id.tv_message_content);
                //highlight text
                tv.setText(Util.highlightText(tv.getText().toString()));
            }
        }, 100);

    }

    String receiverUid;
    private ImageButton btnToolbarBack, upArrowSearchToolbar, downArrowSearchToolbar;
    private androidx.constraintlayout.widget.Group searchGroup;
    public int isTypingNow = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.d("WalletScrollCheck", "Scroll Check 4");
        SharedPreferences sharedPre = getSharedPreferences("ChatData", 0);
        SharedPreferences.Editor editor = sharedPre.edit();
        editor.putBoolean("check_progress", false);

        editor.putInt("pause_and_resume", 0);
        editor.putBoolean("check_pro", false);


        editor.putBoolean("check_progress_doc", false);

        editor.putInt("pause_and_resume_doc", 0);
        editor.putBoolean("check_pro_doc", false);


        editor.putBoolean("check_progress_video", false);

        editor.putInt("pause_and_resume_video", 0);
        editor.putBoolean("check_pro_video", false);
        editor.apply();

        searchGroup = findViewById(R.id.search_layout);
        ivback = (ImageView) findViewById(R.id.btn_toolbar_back);

        toolbar = findViewById(R.id.toolbar);
        tvCounterAction = findViewById(R.id.tv_counter_action);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInActionMode || isInSearchMode) return;
//                viewContact();
            }
        });

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        userNameToolbarChatActivity = findViewById(R.id.user_name_toolbar_chat_activity);
        availableStatToolbar = findViewById(R.id.available_stat_toolbar);
        tvTypingStatToolbar = findViewById(R.id.tv_typing_stat_toolbar);
        userImgToolbarChatAct = findViewById(R.id.user_img_toolbar_chat_act);
        UserChat_id = sharedPre.getString("chat_id", "");
        user = new User();
        user.setUid(UserChat_id);
        user.setUserName(getIntent().getStringExtra("name"));
        user.setPhone(UserChat_id);
        user.setStatus("");
        user.setPhoto(getIntent().getStringExtra("image"));
        user.setThumbImg(getIntent().getStringExtra("image"));
        RealmHelper.getInstance().saveEmptyChat(user);


        tvtyping = (TextView) findViewById(R.id.tv_typing_stat_toolbar);
        tvtyping.setText("");
        tvname = (TextView) findViewById(R.id.user_name_toolbar_chat_activity);
        tvname.setText(getIntent().getStringExtra("name"));
        ivuserimg = (ImageView) findViewById(R.id.user_img_toolbar_chat_act);


        ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
        addpopup.Readmessage(UserChat_id);

        Glide.with(ChatActivity.this)
                .load(getIntent().getStringExtra("image"))
                .apply(new RequestOptions().placeholder(R.drawable.profile))
                .into(ivuserimg);


//        ScreenViewControl();
//        if (checkAndRequestPermissions()) {
//
//        }

        Realm.init(ChatActivity.this);
        init();
        setBackgroundImage();
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        SharedPreferencesManager.init(this);
        SharedPreferencesManager.saveMyThumbImg("thumbImg");
        loadMessagesList();
        setAdapter();
        observeMessagesChanges();

        imgAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachmentView.reveal(view);
                KeyboardHelper.hideSoftKeyboard(ChatActivity.this, etMessage);
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
                        if(locationExternalPermission()){
                            pickLocation();
                        }else {
                            checkLocationRequestPermissions();
                        }

                        break;
                }
            }
        });

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        if (messageList.size() > 0) {
            RealmHelper.getInstance().saveChatIfNotExists(messageList.get(messageList.size() - 1), user);
        }
        upArrowSearchToolbar = findViewById(R.id.up_arrow_search_toolbar);
        downArrowSearchToolbar = findViewById(R.id.down_arrow_search_toolbar);
        searchViewToolbar = findViewById(R.id.search_view_toolbar);
        searchViewToolbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                receiverUid = user.getUid();
                final RealmResults<Message> results = RealmHelper.getInstance().searchForMessage(receiverUid, query);

                if (!results.isEmpty()) {

                    searchIndex = results.size() - 1;
                    String foundMessageId = results.get(searchIndex).getMessageId();
                    int mIndex = getPosFromId(foundMessageId);


                    scrollAndHighlightSearch(mIndex);


                    downArrowSearchToolbar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (results.isEmpty() || searchIndex + 2 > results.size()) {
                                Toast.makeText(ChatActivity.this, R.string.not_found, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            searchIndex++;

                            String foundMessageId = results.get(searchIndex).getMessageId();
                            int mIndex = getPosFromId(foundMessageId);

                            scrollAndHighlightSearch(mIndex);


                        }
                    });

                    upArrowSearchToolbar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (results.isEmpty() || searchIndex - 1 < 0) {
                                Toast.makeText(ChatActivity.this, R.string.not_found, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            searchIndex -= 1;

                            String foundMessageId = results.get(searchIndex).getMessageId();
                            int mIndex = getPosFromId(foundMessageId);


                            scrollAndHighlightSearch(mIndex);


                        }
                    });
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchViewToolbar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    KeyboardHelper.openSoftKeyboard(ChatActivity.this, view.findFocus());
            }
        });

        searchViewToolbar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                isInSearchMode = false;
                return true;
            }
        });


        new CountDownTimer(5000000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ChatWebAPI addpopups = new ChatWebAPI(ChatActivity.this);
                addpopups.islive(getIntent().getStringExtra("id"));
                Scroll_value = sharedPreferences.getString("scroll", "0");

                try {
                    check_read_message();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(etMessage.isFocused()){
                    Log.i("echecknow2","checkKnow");
                    //EditText1 is focused
                }else {
//                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
//                    addpopup.stopsendTypeing(getIntent().getStringExtra("id"));
                    Log.i("echecknow2","not checkKnow");
                }

                Image_check = sharedPreferences.getInt("Image_check", 0);
                Image_position = sharedPreferences.getInt("Image_position", 0);
                liveknow = sharedPreferences.getInt("live", 0);
                isTypingNow  = sharedPreferences.getInt("istyping", 0);



                if (isTypingNow == 1){
                    tvtyping.setText("isTyping");
                    edit.putInt("istyping",0);
                    edit.apply();
                }else {
                    if(liveknow == 1){
                        tvtyping.setText("online");
                    }else {
                        tvtyping.setText("");
                    }
                }

                if (Scroll_value.equalsIgnoreCase("1")) {
                    scrollToLast();

                }
                if (Image_check == 1) {
                    replydataimage(Image_position);

                }
                counter++;
            }

            @Override
            public void onFinish() {

            }
        }.start();



        viewModel.getItemSelectedLiveData().observe(this, selectedMessages -> {
            if (selectedMessages.isEmpty())
                exitActionMode();

            else {
                updateToolbarButtons(selectedMessages);
            }


            updateActionModeItemsCount(selectedMessages.size());

        });



    }

    private boolean locationExternalPermission()
    {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private boolean audiorecordingExternalPermission()
    {
        String permission = android.Manifest.permission.RECORD_AUDIO;
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public int liveknow;
    public void hideShareItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_share);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_share).setVisible(false);
    }
    public void showShareItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_share);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_share).setVisible(true);
    }
    public void hideCopyItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_copy);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_copy).setVisible(false);
    }
    public void hideForwardItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_forward);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_forward).setVisible(false);
    }
    public void hideReplyMenuItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_reply);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_reply).setVisible(false);
    }

    public void showCopyItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_copy);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_copy).setVisible(true);
    }

    public void showForwardItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_forward);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_forward).setVisible(true);
    }


    public void showReplyItem() {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_item_reply);
        if (menuItem != null)
            toolbar.getMenu().findItem(R.id.menu_item_reply).setVisible(true);
    }
    //hide or show toolbar button in activity depending on conditions
    private void updateToolbarButtons(List<Message> selectedMessages) {
        if (AdapterHelper.shouldHideAllItems(selectedMessages)) {
            hideShareItem();
            hideCopyItem();
            hideForwardItem();
            hideReplyMenuItem();
        } else {
            if (AdapterHelper.shouldEnableCopyItem(selectedMessages))
                showCopyItem();
            else
                hideCopyItem();

            if (AdapterHelper.shouldEnableForwardButton(selectedMessages))
                showForwardItem();
            else
                hideForwardItem();

            if (AdapterHelper.shouldEnableShareButton(selectedMessages))
                showShareItem();
            else
                hideShareItem();

            boolean isGroupActive = isGroupActive();
            if (AdapterHelper.shouldEnableReplyItem(selectedMessages, user.isGroupBool(), isGroupActive))
                showReplyItem();
            else
                hideReplyMenuItem();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.video_call_item:
                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();
                RandomString randomString = new RandomString();
                String video_name = ts + "_" + randomString.nextString();
                Intent intent = new Intent(ChatActivity.this, CallingActivity.class);
                intent.putExtra(IntentUtils.CALL_TYPE,2);
                intent.putExtra(IntentUtils.CALL_DIRECTION,FireCallDirection.OUTGOING);
                intent.putExtra(IntentUtils.UID,getIntent().getStringExtra("id"));
                intent.putExtra(IntentUtils.CALL_ID,video_name);
                intent.putExtra(IntentUtils.CALL_ACTION_TYPE, IntentUtils.ACTION_START_NEW_CALL);
                intent.putExtra(IntentUtils.PHONE, getIntent().getStringExtra("id"));
                startActivity(intent);
//                if (user.isGroupBool()) {
//                    if (user.getGroup() != null && user.getGroup().isActive()) {
//                        performCall.performConferenceCall(true, user.getUid());
//                    }
//                } else
//                    performCall.performCall(true, receiverUid);
                break;

            case R.id.voice_call_item:
                Long tsLongx = System.currentTimeMillis() / 1000;
                String tsx = tsLongx.toString();
                RandomString randomStringx = new RandomString();
                String id_name = tsx + "_" + randomStringx.nextString();
                Intent intentx = new Intent(ChatActivity.this, CallingActivity.class);
                intentx.putExtra(IntentUtils.CALL_TYPE,1);
                intentx.putExtra(IntentUtils.CALL_DIRECTION,FireCallDirection.OUTGOING);
                intentx.putExtra(IntentUtils.UID,getIntent().getStringExtra("id"));
                intentx.putExtra(IntentUtils.CALL_ID,id_name);
                intentx.putExtra(IntentUtils.CALL_ACTION_TYPE, IntentUtils.ACTION_START_NEW_CALL);
                intentx.putExtra(IntentUtils.PHONE, getIntent().getStringExtra("id"));
                startActivity(intentx);
//                if (user.isGroupBool()) {
//                    if (user.getGroup() != null && user.getGroup().isActive()) {
//                        performCall.performConferenceCall(false, user.getUid());
//                    }
//                } else
//                    performCall.performCall(false, receiverUid);
                break;

            case R.id.view_contact_menu_item:
                Intent inten = new Intent(ChatActivity.this, UserProfile.class);
                inten.putExtra("name", getIntent().getStringExtra("name"));
                inten.putExtra("number", getIntent().getStringExtra("id"));
                inten.putExtra("img", getIntent().getStringExtra("image"));
                inten.putExtra("duration", "10");
                startActivity(inten);

                break;

            case R.id.menu_item_copy:
                copyItemClicked();
                break;

            case R.id.menu_item_delete:
                deleteItemClicked();
                break;

            case R.id.menu_item_share:
                shareClicked();
                break;


            case R.id.menu_item_forward:
                forwardClicked();
                break;

            case R.id.search_item:
                searchItemClicked();
                break;

            case R.id.block_contact:
//                blockUserClicked();
                break;

            case R.id.add_to_contacts:
//                addToContacts();
                break;

            case R.id.mute_item:
//                setMuted();
                break;

            case R.id.clear_chat_item:
                clearChat();
                break;

            case R.id.menu_item_reply:
                replyItemClicked_nwe();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void deleteItemClicked() {

        final List<Message> selectedItemsForActionMode = viewModel.getSelectedItems();
        boolean canDeleteForEveryOne = AdapterHelper.canDeleteForEveryOne(selectedItemsForActionMode);

        boolean containMedia = viewModel.isSelectedItemsContainMedia();
        Log.d("deleteItemClickedlucky"," ==         canDeleteForEveryOne    ===="+canDeleteForEveryOne);
        if (canDeleteForEveryOne) {
            Log.d("deleteItemClickedlucky"," ==         canDeleteForEveryOne    ====");
            DeleteDialog deleteDialog = new DeleteDialog(this, containMedia, true);
            deleteDialog.setOnItemClick(new DeleteDialog.OnItemClick() {
                @Override
                public void onClick(int pos, boolean isDeleteChecked) {
                    switch (pos) {
                        //delete for me clicked
                        case 0:
                            Log.d("onActivityResultLucky"," ==         0    ====");
                            for (Message message : selectedItemsForActionMode) {
//                                if (message.getDownloadUploadStat() == LOADING) {
//                                    if (MessageType.isSentType(message.getType())) {
//                                        DownloadManager.cancelUpload(message.getMessageId());
//                                    } else
//                                        DownloadManager.cancelDownload(message.getMessageId());
//                                }

                                ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                                addpopup.senddelete_me(getIntent().getStringExtra("id"), message.getMessageId());
                                Log.i("onundeliveredMessage", "message =  "+message.getChatId() );
                                RealmHelper.getInstance().deleteMessageFromRealm(message.getChatId(), message.getMessageId(), isDeleteChecked);

                            }
                            viewModel.clearSelectedItems();
                            exitActionMode();
                            break;


                        //delete for every one
                        case 2:
                            Log.d("deleteItemClickedlucky"," ==         2    ====");
                            for (final Message message : selectedItemsForActionMode) {

                                ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                                addpopup.senddelete_foreveryone(getIntent().getStringExtra("id"), message.getMessageId());
                                RealmHelper.getInstance().setMessageDeleted(message.getMessageId());

                            }
                            exitActionMode();

                            break;
                    }
                }
            });

            deleteDialog.show();

        } else {
            Log.d("deleteItemClickedlucky"," ==         else    ====");
            DeleteDialog deleteDialog = new DeleteDialog(this, containMedia);
            deleteDialog.setmListener(new DeleteDialog.OnFragmentInteractionListener() {
                @Override
                public void onPositiveClick(boolean isDeleteChecked) {

                    for (Message message : selectedItemsForActionMode) {
//                        if (message.getDownloadUploadStat() == LOADING) {
//                            if (MessageType.isSentType(message.getType())) {
//                                DownloadManager.cancelUpload(message.getMessageId());
//                            } else
//                                DownloadManager.cancelDownload(message.getMessageId());
//                        }


                        ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                        addpopup.senddelete_me(getIntent().getStringExtra("id"), message.getMessageId());


                        Log.d("deleteItemClickedlucky"," ==         ChatId    ===="+message.getChatId());
                        Log.d("deleteItemClickedlucky"," ==         isDeleteChecked    ===="+isDeleteChecked);
                        Log.d("deleteItemClickedlucky"," ==         else    ====");

                        RealmHelper.getInstance().deleteMessageFromRealm(message.getChatId(), message.getMessageId(), isDeleteChecked);
                    }


                    exitActionMode();

                }
            });
            deleteDialog.show();
        }


    }

    private void forwardClicked() {
        Intent intent = new Intent(this, ForwardActivity.class);
        startActivityForResult(intent, FORWARD_MESSAGE_REQUEST);
    }
    private void shareClicked() {
        Message message = viewModel.getSelectedItems().get(0);
        if (message.getLocalPath() == null) return;
        Intent shareImageIntent = IntentUtils.getShareImageIntent(message.getLocalPath());
        shareImageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareImageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(shareImageIntent);
    }

    private void replyItemClicked_nwe() {
        Message selectedMessage = viewModel.getSelectedItems().get(0);
        Log.d("replyItemClicked_nwe","selectedMessage   =   =    "+selectedMessage);
        if (selectedMessage == null) return;
        String author;
        if (selectedMessage.getFromId().equals(FireManager.getUid())) {
            author = getResources().getString(R.string.you);
        } else {
            User user = RealmHelper.getInstance().getUser(selectedMessage.getFromId());
            author = user == null ? selectedMessage.getFromPhone() : user.getProperUserName();
        }

        showReplyLayout(author, selectedMessage);
        exitActionMode();
        KeyboardHelper.openSoftKeyboard(this, etMessage.findFocus());
        etMessage.requestFocus();
        Log.d("replyItemClicked_nwe","selectedMessage 2    =   =    "+selectedMessage);
        currentQuotedMessage = selectedMessage;
    }

    private void copyItemClicked() {
        List<Message> selectedItemsForActionMode = viewModel.getSelectedItems();

        //sorting messages by timestamp
        //if the user selected the messages in a Random way
        Collections.sort(selectedItemsForActionMode);

        StringBuilder builder = new StringBuilder();
        for (Message message : selectedItemsForActionMode) {
            builder.append(message.getContent() + "\n");
        }

        String copiedString = builder.toString();
        ClipboardUtil.copyTextToClipboard(this, copiedString);
        Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
        exitActionMode();
    }
    private void clearChat() {
        DeleteDialog deleteDialog = new DeleteDialog(this, true);
        deleteDialog.setMTitle(getResources().getString(R.string.confirmation));
        deleteDialog.setMessage(R.string.clear_chat_message);
        deleteDialog.setmListener(new DeleteDialog.OnFragmentInteractionListener() {
            @Override
            public void onPositiveClick(boolean isDeleteChecked) {
                ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.deleting));
                RealmHelper.getInstance().clearChat(user.getUid(), isDeleteChecked);
            }
        });


        deleteDialog.show();
    }
    private boolean isInSearchMode = false;

    private void searchItemClicked() {
        if (isInActionMode)
            exitActionMode();

        isInSearchMode = true;
        toolbar.getMenu().clear();
        searchGroup.setVisibility(View.VISIBLE);
        hideOrShowUserInfo(true);
        if (searchViewToolbar.isIconified())
            searchViewToolbar.onActionViewExpanded();

        searchViewToolbar.requestFocus();
    }

    private TextView userNameToolbarChatActivity, availableStatToolbar, tvCounterAction, tvTypingStatToolbar;

    //hide or show the views in toolbar, userImg,userName,typing and available
    private void hideOrShowUserInfo(boolean hide) {
        int visibility = hide ? View.GONE : View.VISIBLE;
        userImgToolbarChatAct.setVisibility(visibility);
        userNameToolbarChatActivity.setVisibility(visibility);
        tvTypingStatToolbar.setVisibility(visibility);
        availableStatToolbar.setVisibility(visibility);
    }

    public int Image_position = 0;
    public int Image_check = 0;


    public int doc_doc_position = 0;
    public int doc_doc_check = 0;

    //    public void exitActionMode() {
//        adapter.notifyDataSetChanged();
//        isInActionMode = false;
//        tvCounterAction.setVisibility(View.GONE);
//        toolbar.getMenu().clear();
//        //re inflate default menu
//        toolbar.inflateMenu(R.menu.menu_chat);
//        invalidateOptionsMenu();
//        hideOrShowUserInfo(false);
//        //update online and typing tvs visibility after exiting action mode
//        updateToolbarTvsVisibility(currentTypingState != TypingStat.NOT_TYPING);
//        viewModel.clearSelectedItems();
//    }
    private void loadMessagesList() {
        Log.d("WalletScrollCheck", "Scroll Check 5");
        messageList = RealmHelper.getInstance().getMessagesInChat(UserChat_id);


    }

    private void observeMessagesChanges() {
        Log.d("WalletScrollCheck", "Scroll Check 5");
        changeListener = new OrderedRealmCollectionChangeListener<RealmResults<Message>>() {
            @Override
            public void onChange(RealmResults<Message> messages, OrderedCollectionChangeSet changeSet) {

                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                OrderedCollectionChangeSet.Range[] insertion = changeSet.getInsertionRanges();


                if (insertion.length != 0) {
                    updateChat(messages.get(insertion[0].startIndex));
                } else if (modifications.length != 0) {
                    updateChat(messages.get(modifications[0].startIndex));
                }


                for (OrderedCollectionChangeSet.Range range : modifications) {
                    //get the new Message
                    final Message message = messages.get(range.startIndex);


                    //if this message was sent by user then we want to add a Listener to it to observe newChanges to its state whether it's RECEIVED OR READ
                    if (!isBroadcast && message.getType() != MessageType.GROUP_EVENT && message.getFromId().equals(FireManager.getUid()))
                        //if it's a broadcast message then get the ACTUAL message id
                        //since we are ONLY copying the message locally
//                        addListener(message.getMessageId());


                        //update date header if it's a new day
                        adapter.messageInserted();


                    //update incoming messages
                    // if this message is from the recipient and its' not read before then update the message currentTypingState to READ
                    if (!isGroup && message.getType() != MessageType.GROUP_EVENT && !message.getFromId().equals(FireManager.getUid()) && message.getChatId().equals(UserChat_id) && message.getMessageStat() != MessageStat.READ) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //          ServiceHelper.startUpdateMessageStatRequest(ChatActivity.this, message.getMessageId(), fireManager.getUid(), message.getChatId(), MessageStat.READ);
                            }
                        }, 100);

                    }

                }
            }
        };
    }

    //set background image if user chooses another image than the default one
    private void setBackgroundImage() {
        Log.d("WalletScrollCheck", "Scroll Check 6");
        if (SharedPreferencesManager.getWallpaperPath().equals(""))
            return;

        Bitmap bitmap;
        try {
            bitmap = BitmapUtils.convertFileImageToBitmap(SharedPreferencesManager.getWallpaperPath());
            if (bitmap != null)
                mainContainer.setBackground(new BitmapDrawable(null, bitmap));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.failed_to_load_wallpaper, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        Log.d("WalletScrollCheck", "Scroll Check 7");
        super.onResume();
//        updateProgressData();
    }


    //update the network process progress
    @Subscribe
    public void onNetworkProgress(UpdateNetworkProgress event) {

//        viewModel.networkProgressChanged(messageId, progress);
    }

    //on finish network job
    @Subscribe
    public void onNetworkJobComplete(OnNetworkComplete data) {

//        viewModel.removeNetworkProgress(messageId);


    }

    public void check_read_message() throws IOException {
        Log.d("sendmessagemsgRead", "size  = " + messageList.size());
        for (int i = 0; i < messageList.size(); i++) {
            int stat = messageList.get(i).getMessageStat();


            Log.d("sendmessagemsgRead", "stat  = " + messageList.get(i).getContent());
            if (stat == MessageStat.RECEIVED) {
                Log.d("sendmessagemsgRead", "stat  = " + "Lucky Agarwal");
                if (MessageType.RECEIVED_TEXT == messageList.get(i).getType()) {
//                    Log.d("msg_read_report_sender", "Content  = " + messageList.get(i).getContent());
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendmessagemsgRead(getIntent().getStringExtra("id"), messageList.get(i).getMessageId());
                    RealmHelper.getInstance().updateDownloadReadStat(messageList.get(i).getMessageId(), DownloadUploadStat.SUCCESS);
                } else if (MessageType.RECEIVED_AUDIO == messageList.get(i).getType()) {
//                    Log.d("msg_read_report_sender", "Content  = " + messageList.get(i).getContent());
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendmessagemsgRead(getIntent().getStringExtra("id"), messageList.get(i).getMessageId());
                    RealmHelper.getInstance().updateDownloadReadStat(messageList.get(i).getMessageId(), DownloadUploadStat.SUCCESS);
                } else if (MessageType.RECEIVED_CONTACT == messageList.get(i).getType()) {
//                    Log.d("msg_read_report_sender", "Content  = " + messageList.get(i).getContent());
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendmessagemsgRead(getIntent().getStringExtra("id"), messageList.get(i).getMessageId());
                    RealmHelper.getInstance().updateDownloadReadStat(messageList.get(i).getMessageId(), DownloadUploadStat.SUCCESS);
                } else if (MessageType.RECEIVED_FILE == messageList.get(i).getType()) {
//                    Log.d("msg_read_report_sender", "Content  = " + messageList.get(i).getContent());
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendmessagemsgRead(getIntent().getStringExtra("id"), messageList.get(i).getMessageId());
                    RealmHelper.getInstance().updateDownloadReadStat(messageList.get(i).getMessageId(), DownloadUploadStat.SUCCESS);
                } else if (MessageType.RECEIVED_IMAGE == messageList.get(i).getType()) {
//                    Log.d("msg_read_report_sender", "Content  = " + messageList.get(i).getContent());
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendmessagemsgRead(getIntent().getStringExtra("id"), messageList.get(i).getMessageId());
                    RealmHelper.getInstance().updateDownloadReadStat(messageList.get(i).getMessageId(), DownloadUploadStat.SUCCESS);
                } else if (MessageType.RECEIVED_LOCATION == messageList.get(i).getType()) {
//                    Log.d("msg_read_report_sender", "Content  = " + messageList.get(i).getContent());
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendmessagemsgRead(getIntent().getStringExtra("id"), messageList.get(i).getMessageId());
                    RealmHelper.getInstance().updateDownloadReadStat(messageList.get(i).getMessageId(), DownloadUploadStat.SUCCESS);
                } else if (MessageType.RECEIVED_VIDEO == messageList.get(i).getType()) {
//                    Log.d("msg_read_report_sender", "Content  = " + messageList.get(i).getContent());
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendmessagemsgRead(getIntent().getStringExtra("id"), messageList.get(i).getMessageId());
                    RealmHelper.getInstance().updateDownloadReadStat(messageList.get(i).getMessageId(), DownloadUploadStat.SUCCESS);
                }

            }

        }
    }

    private void setAdapter() {
        Log.d("WalletScrollCheck", "Scroll Check 8");
        LiveData<List<Message>> data = new MutableLiveData<List<Message>>();

        LiveData<Map<String, Integer>> data2 = new MutableLiveData<Map<String, Integer>>();


        User user1 = new User();
        MediaPlayer mediaPlayer = new MediaPlayer();

        adapter = new MessagingAdapter(messageList, true, viewModel.getItemSelectedLiveData(), viewModel.getProgressMapLiveData(), this,
                user1);
        Log.d("sendmessagedmsgRead", "size  = " + messageList.size());


        decor = new StickyHeaderDecoration(adapter);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        //add Date Header to the Adapter
        recyclerView.addItemDecoration(decor, 0);

        //if there are messages in this chat
        if (messageList.size() > 0) {

            chat = RealmHelper.getInstance().getChat(UserChat_id);

            recyclerView.scrollToPosition(messageList.size() - 1);
//            }

            enableSwipeToDeleteAndUndo();
        }


    }

    private void enableSwipeToDeleteAndUndo() {
        Log.d("WalletScrollCheck", "Scroll Check 9");
        messageSwipeController = new MessageSwipeController(this, isGroupActive(), new SwipeControllerActions() {
            @Override
            public void showReplyUI(int position) {
                replyItemClicked(messageList.get(position));
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(messageSwipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    private void replyItemClicked(Message selectedMessage) {
        Log.d("WalletScrollCheck", "Scroll Check 10");
        Log.d("selectedMessage", "id  =   " + selectedMessage.getFromId());
        if (selectedMessage == null) return;
        String author;
        if (selectedMessage.getFromId().equals(FireManager.getUid())) {
            author = getResources().getString(R.string.you);
        } else {
            Log.d("selectedMessage", "id  =   " + selectedMessage.getFromId());
            User user = RealmHelper.getInstance().getUser(selectedMessage.getFromId());
            author = user == null ? selectedMessage.getFromPhone() : user.getProperUserName();
        }

        showReplyLayout(author, selectedMessage);
        exitActionMode();
        KeyboardHelper.openSoftKeyboard(this, etMessage.findFocus());
        etMessage.requestFocus();

        currentQuotedMessage = selectedMessage;
    }

    public void exitActionMode() {
        Log.d("WalletScrollCheck", "Scroll Check 11");
        adapter.notifyDataSetChanged();
        isInActionMode = false;
        tvCounterAction.setVisibility(View.GONE);
        toolbar.getMenu().clear();
        //re inflate default menu
        toolbar.inflateMenu(R.menu.menu_chat);
        invalidateOptionsMenu();
        hideOrShowUserInfo(false);
        //update online and typing tvs visibility after exiting action mode
        updateToolbarTvsVisibility(currentTypingState != TypingStat.NOT_TYPING);
        viewModel.clearSelectedItems();
    }

    private void showReplyLayout(String messageAuthor, Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 12");
        btnCancelImage.setVisibility(View.VISIBLE);
        animateReplyLayout(true);


        tvQuotedName.setText(messageAuthor);
        tvQuotedText.setText(MessageTypeHelper.getMessageContent(message, false));
        if (message.getThumb() != null) {
            quotedThumb.setVisibility(View.VISIBLE);
            Glide.with(this).load(message.getThumb()).into(quotedThumb);
        } else
            quotedThumb.setVisibility(View.GONE);

        if (!message.isTextMessage() && MessageTypeHelper.getMessageTypeDrawable(message.getType()) != -1) {
            int messageTypeResource = MessageTypeHelper.getMessageTypeDrawable(message.getType());
            if (messageTypeResource != -1) {
                Drawable drawable = getResources()
                        .getDrawable(messageTypeResource);
                drawable.mutate().setColorFilter(ContextCompat.getColor(this, R.color.grey), PorterDuff.Mode.SRC_IN);
                tvQuotedText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
        } else
            tvQuotedText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        tvQuotedText.setTextColor(ContextCompat.getColor(this, R.color.colorText));

    }

    private void animateReplyLayout(final boolean showLayout) {
        Log.d("WalletScrollCheck", "Scroll Check 12");
        if (showLayout)
            quotedMessageFrame.setVisibility(View.VISIBLE);
        else {
            if (quotedMessageFrame.getVisibility() == View.GONE)//don't animate if it's already hidden
                return;
        }


        float fromY = showLayout ? typingLayout.getBottom() : typingLayout.getTop();
        float toY = showLayout ? typingLayout.getTop() : typingLayout.getBottom();


        Animation
                animation = new TranslateAnimation(0, 0, fromY, toY);
        animation.setDuration(250);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) quotedMessageFrame.getLayoutParams();
                quotedMessageFrame.setLayoutParams(params);
                if (!showLayout)
                    quotedMessageFrame.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        quotedMessageFrame.startAnimation(animation);
    }

    private boolean isGroupActive() {
        return false;
    }
    private boolean checkAudioRecordRequestPermissions() {
        Log.d("WalletScrollCheck", "Scroll Check 13");
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appAudioPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        }
        ;
        return true;
    }

    private boolean checkLocationRequestPermissions() {
        Log.d("WalletScrollCheck", "Scroll Check 13");
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appLocationPermission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        }
        ;
        return true;
    }


    private boolean checkAndRequestPermissions() {
        Log.d("WalletScrollCheck", "Scroll Check 13");
        // Check which Permissions are granted (Check करें कि कौन सी Permissions Granted है।)
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);

//                Log.d("CheckPermissionLucky","Yes");
            }
        }
        // Ask for non-granted permissions
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSION_REQUEST_CODE);
//            Log.d("CheckPermissionLucky","No");
            return false;
        }
        ;
        return true;
    }

    public void scrollToLast() {
        Log.d("WalletScrollCheck", "Scroll Check 14");
        if (messageList == null) return;
        if (messageList.size() - 1 <= 0) return;


        recyclerView.scrollToPosition(messageList.size() - 1);
        hideUnreadCount();
        btnScroll.hide();

        edit.putString("scroll", "0");
        edit.apply();
    }


    public void scrolltolaastlucky() {
        Log.d("WalletLUckyScroll", "Scroll Function running");
    }

    // hide/show typingLayout or recordLayout
    private void hideOrShowRecord(boolean hideRecord) {
        Log.d("WalletScrollCheck", "Scroll Check 15");
        if (hideRecord) {
            recordView.setVisibility(View.GONE);
            typingLayout.setVisibility(View.VISIBLE);
        } else {
            recordView.setVisibility(View.VISIBLE);
            typingLayout.setVisibility(View.GONE);
        }
    }

    private void hideUnreadCount() {
        Log.d("WalletScrollCheck", "Scroll Check 16");
        unreadCount = 0;
        countUnreadBadge.setText("");
        countUnreadBadge.setVisibility(View.GONE);
    }

    private void requestEditTextFocus() {
        Log.d("WalletScrollCheck", "Scroll Check 17");
        if (wasInTypingMode) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    etMessage.requestFocus();
                }
            }, 100);

        }
    }

    private void init() {
        Log.d("WalletScrollCheck", "Scroll Check 18");
        recyclerView = findViewById(R.id.recycler_chat);
        rootView = findViewById(R.id.root_view);
        emojiBtn = findViewById(R.id.emoji_btn);
        etMessage = findViewById(R.id.et_message);
        imgAttachment = findViewById(R.id.img_attachment);
        cameraBtn = findViewById(R.id.camera_btn);
        recordView = findViewById(R.id.record_view);
        recordButton = findViewById(R.id.record_button);
        typingLayout = findViewById(R.id.typing_layout);
        attachmentView = findViewById(R.id.attachment_view);
        actions = new EmojIconActions(this, rootView, etMessage, emojiBtn);
        actions.ShowEmojIcon();
        countUnreadBadge = findViewById(R.id.count_unread_badge);
        tvQuotedName = findViewById(R.id.tv_quoted_name);
        tvQuotedText = findViewById(R.id.tv_quoted_text);


        etMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (attachmentView.isShowing())
                    attachmentView.hide(imgAttachment);

            }
        });
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int cont = etMessage.getText().length();
                Log.d("onStopTypingonStopTyping", "number 1 = ");
                if(cont == 1 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 8 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 16 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 24 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 32 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 40 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 48 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 56 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 64 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 72 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }else if(cont == 80 ){
                    ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
                    addpopup.sendTypeing(getIntent().getStringExtra("id"));
                }






            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
//                ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
//                addpopup.sendStopTypeing(getIntent().getStringExtra("id"));
                if (text.trim().length() > 0) {
                    changeSendButtonState(true);
                    //              getDisposables().add(fireManager.setTypingStat(receiverUid, TypingStat.TYPING, isGroup, isBroadcast).subscribe());


                } else if (text.trim().length() == 0 && typingStarted) {
                    changeSendButtonState(false);
//                    if (!isBroadcast)
                    //                   getDisposables().add(fireManager.setTypingStat(receiverUid, TypingStat.NOT_TYPING, isGroup, isBroadcast).subscribe());
                }
            }
        });
        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                hideOrShowRecord(true);
                requestEditTextFocus();
            }
        });

        recordView.setCancelBounds(0);

        recordView.setSlideToCancelArrowColor(ContextCompat.getColor(this, R.color.iconTintColor));
        recordView.setCounterTimeColor(ContextCompat.getColor(this, R.color.colorText));
        recordView.setSlideToCancelTextColor(ContextCompat.getColor(this, R.color.colorText));

        recordButton.setRecordView(recordView);

        recordButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
//                if (user.isBlocked()) {
//                    showBlockedDialog();
//                    return;
//                }
                if(audiorecordingExternalPermission()){
                    String text = etMessage.getText().toString();
                    sendMessage(text);
                }else {
                    checkAudioRecordRequestPermissions();
                }

            }
        });
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                hideOrShowRecord(false);
                //       getDisposables().add(fireManager.setTypingStat(receiverUid, TypingStat.RECORDING, isGroup, isBroadcast).subscribe());
                handleRecord();
            }

            @Override
            public void onCancel() {
                stopRecord(true, -1);
//                getDisposables().add(fireManager.setTypingStat(receiverUid, TypingStat.NOT_TYPING, isGroup, isBroadcast).subscribe());
            }

            @Override
            public void onFinish(long recordTime) {
                hideOrShowRecord(true);

//                getDisposables().add(fireManager.setTypingStat(receiverUid, TypingStat.NOT_TYPING, isGroup, isBroadcast).subscribe());
                stopRecord(false, recordTime);
                requestEditTextFocus();
            }

            @Override
            public void onLessThanSecond() {
                Toast.makeText(ChatActivity.this, R.string.voice_message_is_short_toast, Toast.LENGTH_SHORT).show();
                hideOrShowRecord(true);
//                getDisposables().add(fireManager.setTypingStat(receiverUid, TypingStat.NOT_TYPING, isGroup, isBroadcast).subscribe());
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
        btnScroll = findViewById(R.id.btn_scroll);
        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToLast();
            }
        });


        btnScroll = findViewById(R.id.btn_scroll);
        countUnreadBadge = findViewById(R.id.count_unread_badge);
        tvCantSendMessages = findViewById(R.id.tv_cant_send_messages);
        typingLayoutContainer = findViewById(R.id.typing_layout_container);
        mainContainer = findViewById(R.id.content_chat);
        quotedMessageFrame = findViewById(R.id.quoted_message_frame);
        quotedColor = findViewById(R.id.quoted_color);
        tvQuotedName = findViewById(R.id.tv_quoted_name);
        tvQuotedText = findViewById(R.id.tv_quoted_text);
        quotedThumb = findViewById(R.id.quoted_thumb);
        btnCancelImage = findViewById(R.id.btn_cancel_image);
        setQuotedMessageStyle();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.d("CheckChatAPI", "Check Scorll View     addOnScrollListener");

                //detect when user stops scrolling
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int lastVisibleItemPosition = getLastVisibileItem();

                    if (lastVisibleItemPosition != messageList.size() - 1) {
                        //only show it when it is hidden
                        if (!btnScroll.isShowing())
                            btnScroll.show();

                    } else {
                        btnScroll.hide();
                        hideUnreadCount();

                    }
                }
            }

        });


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startCamera();
            }
        });


        btnCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideReplyLayout();
            }
        });
    }


    public String SelectChatId;

    public void id_genrated() {
        Log.d("WalletScrollCheck", "Scroll Check 19");
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();
    }

    //scroll to last OR update the unread count
    private static MyFCMService defaultpopupnew;


    public void datagetnewapi(String chatid) {
        Toast.makeText(this, "Data Get in Socket = " + chatid, Toast.LENGTH_SHORT).show();
    }

    private void sendMessage(Message message) {
        Log.d("sendMessagesendMessage", "Scroll Check 77");
        Message quotedMessage = getQuotedMessage();
        if (quotedMessage != null)
            message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
        RealmHelper.getInstance().saveObjectToRealm(message);
        RealmHelper.getInstance().saveChatIfNotExists(message, user);
        hideReplyLayout();

    }

    //send text message
    private void sendMessage(String text) {

        Log.d("replyItemClicked_nwe", "Scroll Check 55");
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();


        Log.d("CheckChatAPI", "id 1 = " + getIntent().getStringExtra("id"));
        Log.d("CheckChatAPI", "text 1 = " + text);
        ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);

        if (text.trim().isEmpty())
            return;

        int length = text.getBytes().length;
        if (length > 3800) {
            Toast.makeText(ChatActivity.this, R.string.message_is_too_long, Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = new MessageCreator.Builder(user, MessageType.SENT_TEXT, image_name).quotedMessage(getQuotedMessage()).text(text).build();
        Log.d("replyItemClicked_nwe", "getQuotedMessage  " + getQuotedMessage());
        if (getQuotedMessage() != null) {
            Message quotedMessage = getQuotedMessage();
            hideReplyLayout();
            String replay_id = quotedMessage.getMessageId();

            Log.d("replyItemClicked_nwe", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
            Log.d("replyItemClicked_nwe", "message id =   " + image_name);
            addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
        } else {
            addpopup.sendmessage(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name,"NA");
        }



        Log.d("replyItemClicked_nwe", "getQuotedMessage getQuotedMessage  " + getQuotedMessage());

        etMessage.setText("");

        scrollToLast();
    }

    private int getLastVisibileItem() {
        return linearLayoutManager.findLastVisibleItemPosition();
    }

    //stop record
    private void stopRecord(boolean isCancelled, long recordTime) {
        Log.d("WalletScrollCheck", "Scroll Check 21");
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

    //    private void addVoiceMessageStatListener(final String messageId) {
//        if (isBroadcast) return;
//        DatabaseReference ref = FireConstants.voiceMessageStat.child(receiverUid).child(messageId);
//        fireListener.addVoiceMessageListener(ref, voiceMessageStatListener);
//    }
    private void sendVoiceMessage(String path, String duration) {
        Log.d("WalletScrollCheck", "Scroll Check 22");
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();
        Message message = new MessageCreator.Builder(user, MessageType.SENT_VOICE_MESSAGE, image_name).quotedMessage(getQuotedMessage()).path(path).duration(duration).build();


        File file = new File(path);


        String extension = path.substring(path.lastIndexOf("."));


        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);


        spacesFileRepository.uploadExampleAudioFile(file, ChatActivity.this, getIntent().getStringExtra("id"), image_name, duration, extension);


        hideReplyLayout();



        Intent intent = new Intent(ChatActivity.this, BackgroundNotificationAudioSendService.class);



        Log.i("Checkrcoarding","filePath = "+path);
        Log.i("Checkrcoarding","id = "+getIntent().getStringExtra("id"));
        Log.i("Checkrcoarding","image_name = "+image_name);
        Log.i("Checkrcoarding","audioDuration = "+duration);
        Log.i("Checkrcoarding","extension = "+extension);
        Log.i("Checkrcoarding","replay_id = "+replay_id);
        intent.putExtra("filePath", path);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("image_name", image_name);
        intent.putExtra("audioDuration", duration);
        intent.putExtra("extension", extension);
        intent.putExtra("replay_id",replay_id);

        startService(intent);
        scrollToLast();

    }

    ///
    private static final String EXTENSION_WAV = ".wav";
    File file;

    public static String generateNewName(int type) {
        Log.d("WalletScrollCheck", "Scroll Check 23");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddSSSS", Locale.US); //the Locale us is to use english numbers
        return getFileTypeString(type) + "-" + sdf.format(date);
    }

    private static String getFileTypeString(int type) {
        Log.d("WalletScrollCheck", "Scroll Check 24");
        switch (type) {
            case MessageType.SENT_IMAGE:
            case MessageType.RECEIVED_IMAGE:
                return "IMG";


            case MessageType.SENT_VIDEO:
            case MessageType.RECEIVED_VIDEO:
                return "VID";


            case MessageType.SENT_AUDIO:
            case MessageType.RECEIVED_AUDIO:
                return "AUD";


            case MessageType.SENT_VOICE_MESSAGE:
            case MessageType.RECEIVED_VOICE_MESSAGE:
                //push to talk (voice message)
                return "PTT";


            default:
                return "FILE";


        }

    }

    public static String voiceMessagesReceived() {
        Log.d("WalletScrollCheck", "Scroll Check 25");
        File file = new File(mainAppFolder() + "/" + APP_FOLDER_NAME + " " + "VoiceMessage");
        if (!file.exists())
            file.mkdirs();


        createNoMediaFile(file);

        return file.getPath();
    }

    //Main App Folder: /sdcard/FireApp/
    public static String mainAppFolder() {
        Log.d("WalletScrollCheck", "Scroll Check 26");
        File file;
        if (Build.VERSION.SDK_INT >= 30) {
            file = new File(MyApp.context().getExternalFilesDir(null) + "/" + APP_FOLDER_NAME + "/");
        } else {
            file = new File(Environment.getExternalStorageDirectory() + "/" + APP_FOLDER_NAME + "/");
        }
        //if the directory is not exists create it
        if (!file.exists())
            file.mkdir();


        return file.getAbsolutePath();
    }

    //basically just hide them
    public static void createNoMediaFile(File folderPath) {
        Log.d("WalletScrollCheck", "Scroll Check 27");
        File file = new File(folderPath + "/" + ".nomedia");
        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String APP_FOLDER_NAME = "LuckyStorage";
    ////


    //start recording voice message
    private void handleRecord() {
        Log.d("WalletScrollCheck", "Scroll Check 28");
        Log.d("recordingactivity", "dirmanager =  " + RECORD_START_AUDIO_LENGTH);

        Log.d("recordingactivity", "recordFile =  " + recordFile);

        String filePath = sentVoiceMessageDir() + "/" + generateNewName(11) + EXTENSION_WAV;
        file = new File(filePath);
        recordFile = file;


//        String extension = recordFile.substring(recordFile.lastIndexOf("."));
        recorder = OmRecorder.wav(
                new PullTransport.Default(RecorderSettings.getMic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {

                    }
                }), recordFile);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recorder.startRecording();
            }
        }, RECORD_START_AUDIO_LENGTH);


    }

    @NonNull
    private File file() {
        return new File(Environment.getExternalStorageDirectory(), "demo.wav");
    }

    public static String sentVoiceMessageDir() {
        Log.d("WalletScrollCheck", "Scroll Check 29");
        File file = new File(mainAppFolder() + "/" + APP_FOLDER_NAME + " " + "VoiceMessage/Sent/");
        if (!file.exists()) {
            file.mkdirs();
        }
        createNoMediaFile(file);

        Log.d("CheckViceSent", "VICE Message   ===    " + file.getAbsolutePath());

        return file.getAbsolutePath();
    }

    private void changeSendButtonState(boolean setTyping) {
        Log.d("WalletScrollCheck", "Scroll Check 30");
        Log.d("MainActivitRecording", "Recording   =   =  " + setTyping);
        if (setTyping) {
            recordButton.goToState(AnimButton.TYPING_STATE);
            recordButton.setListenForRecord(false);
            typingStarted = true;

        } else {
            recordButton.goToState(AnimButton.RECORDING_STATE);
            recordButton.setListenForRecord(true);
            typingStarted = false;


        }

    }

    private void pickImages() {
        Log.d("WalletScrollCheck", "Scroll Check 31");
        Matisse.from(ChatActivity.this)
                .choose(MimeType.of(MimeType.MP4, MimeType.THREEGPP, MimeType.THREEGPP2
                        , MimeType.JPEG, MimeType.BMP, MimeType.PNG, MimeType.GIF))
                .countable(true)
                .maxSelectable(MAX_SELECTABLE)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(PICK_GALLERY_REQUEST);
    }

    private void pickMusic() {
        Log.d("WalletScrollCheck", "Scroll Check 32");
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_MUSIC_REQUEST);
    }

    private void pickLocation() {
        Log.d("WalletScrollCheck", "Scroll Check 34");
        startActivityForResult(new Intent(this, PlacesPickerActivity.class), PICK_LOCATION_REQUEST);
    }

    private void startCamera() {
        Log.d("WalletScrollCheck", "Scroll Check 35");
        Intent intent = new Intent(ChatActivity.this, CameraNewActivity.class);
        intent.putExtra("openPage","chatActivity");
        startActivityForResult(intent, CAMERA_REQUEST);
//        startActivityForResult(new Intent(ChatActivity.this, CameraActivity.class), CAMERA_REQUEST);
    }

    private void pickContact() {
        Log.d("WalletScrollCheck", "Scroll Check 36");
        new MultiContactPicker.Builder(ChatActivity.this)
                .handleColor(ContextCompat.getColor(ChatActivity.this, R.color.colorPrimary))
                .bubbleColor(ContextCompat.getColor(ChatActivity.this, R.color.colorPrimary))
                .showPickerForResult(PICK_CONTACT_REQUEST);
    }

    private void pickFile() {
        Log.d("WalletScrollCheck", "Scroll Check 37");
        Intent intent4 = new Intent(this, NormalFilePickActivity.class);
        intent4.putExtra(Constant.MAX_NUMBER, 9);
        intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[]{"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf"});
        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);


    }

    public ArrayList<String> doc_name = new ArrayList<>();
    public ArrayList<String> doc_path_path = new ArrayList<>();
    public ArrayList<File> doc_path = new ArrayList<>();
    public ArrayList<String> doc_file_size = new ArrayList<>();
    public ArrayList<String> doc_extension = new ArrayList<>();
    public int sizesize_doc = 0;

    public String replay_id= "NA";
    private void sendFile(final ArrayList<NormalFile> list) {
        Log.d("WalletScrollCheck", "Scroll Check 38");
        Log.d("MainActivityLL", "Image Second ==  ==   " + list);
        if (doc_name.size() > 0) {
            doc_name.clear();
        }
        if (doc_file_size.size() > 0) {
            doc_file_size.clear();
        }
        if (doc_path.size() > 0) {
            doc_path.clear();
        }
        if (doc_extension.size() > 0) {
            doc_extension.clear();
        }
        if (doc_path_path.size() > 0) {
            doc_path_path.clear();
        }
        for (int i = 0; i < list.size(); i++) {
            String filePath = list.get(i).getPath();
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String image_name = ts + "_" + randomString.nextString();

            File file = new File(filePath);

            long length = file.length();
            String lengthsize = size(length);
            String extension = filePath.substring(filePath.lastIndexOf("."));
            doc_path_path.add(filePath);
            doc_name.add(image_name);
            doc_file_size.add(lengthsize);
            doc_path.add(file);
            sizesize_doc = list.size() - 1;
            doc_extension.add(extension);

            Message message = new MessageCreator.Builder(user, MessageType.SENT_FILE, image_name).quotedMessage(getQuotedMessage()).path(filePath).build();
            if (getQuotedMessage() != null) {
                Message quotedMessage = getQuotedMessage();
                hideReplyLayout();
                replay_id = quotedMessage.getMessageId();

                Log.d("replyItemClicked_nwe", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
                Log.d("replyItemClicked_nwe", "message id =   " + image_name);
//                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
            } else {
                replay_id = "NA";
//                addpopup.sendmessage(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name);
            }
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
//            hideReplyLayout();
            scrollToLast();
        }

        Log.d("CheckdocSize", "replydataimage doc_path  =   " + doc_path.get(0));
        Log.d("CheckdocSize", "replydataimage doc_name  =   " + doc_name.get(0));
        Log.d("CheckdocSize", "replydataimage doc_file_size  =   " + doc_file_size.get(0));
        Log.d("CheckdocSize", "replydataimage doc_extension  =   " + doc_extension.get(0));
        Log.d("CheckdocSize", "replydataimage doc_array size  =   " + sizesize_doc);
//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//
//        spacesFileRepository.uploadExampledocFile(doc_path.get(0), ChatActivity.this, getIntent().getStringExtra("id"), doc_name.get(0), doc_file_size.get(0), doc_extension.get(0), sizesize_doc, 0);

        Log.d("CheckdocSizeDocFile", "replay_id replay_id size  =   " + replay_id);
        Intent intent = new Intent(ChatActivity.this, BackgroundNotificationDocSendService.class);


        Bundle bundle = new Bundle();
        bundle.putStringArrayList("doc_path_path", doc_path_path);
        bundle.putStringArrayList("doc_name", doc_name);
        bundle.putStringArrayList("doc_file_size", doc_file_size);
        bundle.putStringArrayList("doc_extension", doc_extension);

//        intent.putExtra("doc_path_path",doc_path_path.get(0));
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("bundle", bundle);
//        intent.putExtra("doc_name",doc_name.get(0));
        intent.putExtra("replay_id",replay_id);
        intent.putExtra("doc_extension",doc_extension.get(0));
        intent.putExtra("sizesize_doc", sizesize_doc);
        intent.putExtra("position", 0);
        startService(intent);
    }



    public List<String> mmPaths = new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResultLucky", "333");
        Log.d("onActivityResult", "requestCode mPaths ==  ==   " + requestCode);
        Log.d("MainActivityLL", "resultCode mPaths ==  ==   " + resultCode);
        Log.d("MainActivityLL", "data mPaths ==  ==   " + data);
        if (requestCode == PICK_DOCUMENT && resultCode == RESULT_OK) {
            Log.d("onActivityResultLucky", "PICK_DOCUMENT     334");
            Log.d("MainActivityLL", "Image mPaths ==  ==   ");
            ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);


            for (int i = 0; i < list.size(); i++) {
                String path = list.get(i).getPath();
                File file = new File(path);

                long length = file.length();
                String lengthsize = size(length);
                String lastFourDigits = lengthsize.substring(lengthsize.length() - 2);
                String[] separated = lengthsize.split("\\ ");
                String first = separated[0]; // this will contain "Fruit: they taste good"
                String Second = separated[1];
                double d = Double.valueOf(first);
                if (Second.equalsIgnoreCase("MB")) {
                    if (d > 100.00) {
//                        Toast.makeText(ChatActivity.this, "Can't upload file more then 100 MB  "+list.get(i).getName(), Toast.LENGTH_SHORT).show();
                        Toast toast = Toast.makeText(this, "Can't upload file more then 100 MB  " + list.get(i).getName(), Toast.LENGTH_SHORT);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        v.setTextColor(Color.WHITE);
                        toast.show();
                        list.remove(i);
                    }
                }
                Log.d("MainActivityLL", "Image mPaths ==  ==   " + lastFourDigits);
                Log.d("MainActivityLL", "Image size ==  ==   " + lengthsize);


                Log.d("MainActivityLL", "Image first ==  ==   " + first);
                Log.d("MainActivityLL", "Image Second ==  ==   " + Second);
//                Log.d("MainActivityLL", "Image mPaths ==  ==   "+list.get(i).getPath() );
            }
            if (list.size() > 0) {
                Log.d("CheckdocSize", "size  =   " + list.size());
                sendFile(list);
            }

        } else
            if (requestCode == PICK_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Log.d("onActivityResultLucky", "PICK_GALLERY_REQUEST     334");
            List<String> mPaths = Matisse.obtainPathResult(data);
            for (String mPath : mPaths) {
                if (!FileUtils.isFileExists(mPath)) {
                    Toast.makeText(ChatActivity.this, R.string.image_video_not_found, Toast.LENGTH_SHORT).show();
                    return;
                }

            }
            Log.d("MainActivityLL", "Image mPaths ==  ==   " + mPaths.toString());

            //Check if it's a video
            if (FileUtils.isPickedVideo(mPaths.get(0))) {
                Log.d("WalletCheckPathSize", "Video Path ==  ==   ");

//                File file = new File(mPaths.get(0));
//                long length = file.length();
//                length = length / 1024;
//                Toast.makeText(ChatActivity.this, "Video size:" + length + "KB", Toast.LENGTH_LONG).show();


//                Log.d("MainActivityLL", "Video uri ==  ==   " + length);

                File new_folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                if (!new_folder.exists()) {
                    if (new_folder.mkdir()) {
                        Log.d("WalletCheckPathSize", "Folder succesfully created");
                    } else {
                        Log.d("WalletCheckPathSize", "Failed to create folder");
                    }
                } else {
                    Log.d("WalletCheckPathSize", "Folder already exists" + new_folder);
                }//Getting a file within the dir.
//                FileOutputStream out = new FileOutputStream(fileWithinMyDir);
                Log.i("VideoCheckknow","check 2");
                Recursion(mPaths, count);


            } else {
                Log.i("ImageClickSignble", "Galary ==  ==   ");

                for(int i=0;i<mPaths.size();i++){
                    original_image_path.add(mPaths.get(i).toString());
                }
                RecursionImage(mPaths, count);
//                sendImage(mPaths);
            }
        } else
            if (requestCode == PICK_MUSIC_REQUEST && resultCode == RESULT_OK) {
            Log.d("onActivityResultLucky", "PICK_MUSIC_REQUEST     334");
            Uri uri = data.getData();

            String[] audioArray = RealPathUtil.getAudioPath(this, uri);
            if (audioArray == null)
                Toast.makeText(this, R.string.could_not_get_audio_file, Toast.LENGTH_SHORT).show();
            else {

            }
            Log.d("MainActivityLLSendAudio", "Music uri ==  ==   " + uri.toString());
            Log.d("MainActivityLLSendAudio", "Music 0 ==  ==   " + audioArray[0]);
            Log.d("MainActivityLLSendAudio", "Music 1 ==  ==   " + audioArray[1]);
            sendAudio(audioArray[0], audioArray[1]);

        } else
        if (requestCode == CAMERA_REQUEST && resultCode != ResultCodes.CAMERA_ERROR_STATE) {
            Log.d("valonActivityResult", "CAMERA_REQUEST     334");
            if (resultCode == ResultCodes.PICK_IMAGE_FROM_CAMERA) {
                Log.d("valonActivityResult", "IMAGE_CAPTURE_SUCCESS     334");
                List<String> mPaths = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                String path = mPaths.get(0);
                Log.d("valonActivityResult", "IMAGE_CAPTURE_SUCCESS  path   334    ====      "+path);

                Log.d("valonActivityResult", "CAMERA Image uri ==  ==   " + path);
//                Bitmap bm = BitmapFactory.decodeFile(path);
                File imgFile = new  File(path);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap orientedBitmap = ExifUtil.rotateBitmap(path, myBitmap);
                byteArrayOutputStream = new ByteArrayOutputStream();
                orientedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                CDRIVES = byteArrayOutputStream.toByteArray();
                long lengthbmp = CDRIVES.length;



                File filesDir = getFilesDir();
                File imageFile = new File(filesDir, "temp" + ".jpg");

                OutputStream os;
                try {
                    os = new FileOutputStream(imageFile);
                    orientedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }



                Log.i("ImageClickSignble", "CAMERA Image path path ==  ==   " + path);
                sendImage(path, true, CDRIVES, String.valueOf(lengthbmp),imageFile);

            } else if (resultCode == ResultCodes.VIDEO_RECORD_SUCCESS) {
                List<String> mPaths = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                String path = mPaths.get(0);
                Log.i("sendVideocheck","VIDEO_RECORD_SUCCESS");
                sendTheVideo(path);
                Log.d("valonActivityResult", "CAMERA VIDEO_RECORD uri ==  ==   " + path);
            }

            //if user choose to forward image to other users
        } else if (requestCode == FORWARD_MESSAGE_REQUEST && resultCode == RESULT_OK) {
            Log.d("onActivityResultLucky", "FORWARD_MESSAGE_REQUEST     334");
            List<User> selectedList = data.getParcelableArrayListExtra(IntentUtils.EXTRA_DATA_RESULT);
            Toast.makeText(this, R.string.sending_messages, Toast.LENGTH_SHORT).show();
            sendForwardedMessages(viewModel.getSelectedItems(), selectedList);
            exitActionMode();
            scrollToLast();

        } else if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            //get selected contacts from Phonebook
            Log.d("onActivityResultLucky", "PICK_CONTACT_REQUEST     334");
            List<ContactResult> results = MultiContactPicker.obtainResult(data);
            Log.d("MainActivityLL", "Contact data ==  ==   " + data);
            Log.d("MainActivityLL", "Contact results ==  ==   " + results);
            //convert results to expandableList so the user can choose which numbers he wants to send
            List<ExpandableContact> contactNameList = ContactUtils.getContactsFromContactResult(results);

            Intent intent = new Intent(this, SelectContactNumbersActivity.class);
            intent.putParcelableArrayListExtra(IntentUtils.EXTRA_CONTACT_LIST, (ArrayList<? extends Parcelable>) contactNameList);
            startActivityForResult(intent, PICK_NUMBERS_FOR_CONTACT_REQUEST);


        } else if (requestCode == PICK_NUMBERS_FOR_CONTACT_REQUEST && resultCode == RESULT_OK) {
            //get contacts after the user selects the numbers he wants to send
            Log.d("onActivityResultLucky", "PICK_NUMBERS_FOR_CONTACT_REQUEST     334");
            List<ExpandableContact> selectedContacts = data.getParcelableArrayListExtra(IntentUtils.EXTRA_CONTACT_LIST);
            Log.d("MainActivityLL", "Contact selectedContacts ==  ==   " + selectedContacts.size());
            Log.d("MainActivityLL", "Contact selectedContacts ==  ==   " + selectedContacts.get(0));


//            Log.d("MainActivityLL", "Contact Title ==  ==   " + selectedContacts.get(0).getTitle());
//            Log.d("MainActivityLL", "Contact Number ==  ==   " + selectedContacts.get(0).getItems());


            sendContacts(selectedContacts);
        } else if (requestCode == PICK_LOCATION_REQUEST && resultCode == RESULT_OK) {
            Log.d("onActivityResultLucky", "PICK_LOCATION_REQUEST     334");
            Place place = data.getParcelableExtra(Place.EXTRA_PLACE);
            Log.d("MainActivityLL", "Location place ==  ==   " + place);
            try {
                sendLocation(place);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    String imagePath = null;
    public String uploadBitmap(Context mContext, Bitmap bitmap) {

        String imagePath = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Uri uri;
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                imagePath = uri.toString();
                Log.d("pathatka", uri.toString());
                break;
            } while (cursor.moveToNext());
            cursor.close();
        }

        return imagePath;
    }

    public int count = 0;
    public ArrayList<String> video_path = new ArrayList<>();

    private void Recursion(List<String> pathList, int number) {
        Log.i("VideoCheckknow", "Scroll Check 40");
        Log.i("VideoCheckknow", "Number 1 ===   " + number);
        Log.i("VideoCheckknow", "Size 1 ===   " + pathList.size());


        Log.i("VideoCheckknow", "size 786 ===   " + video_path.size());
        if (pathList.size() > number) {


            File file = new File(pathList.get(number));
            long length = file.length();
            String lengthsize = size(length);
            Log.i("VideoCheckknow", "File know Size " + number + " ===   " + lengthsize);

            String[] separated = lengthsize.split("\\ ");
            String first = separated[0]; // this will contain "Fruit: they taste good"
            String Second = separated[1];
            double d = Double.valueOf(first);
            Log.i("VideoCheckknow", "double d ===   " + String.valueOf(d));
            Log.i("VideoCheckknow", "first d ===   " + first);
            Log.i("VideoCheckknow", "Second d ===   " + Second);
            if (Second.equalsIgnoreCase("MB")) {
                if (d > 100.00) {

                    Toast.makeText(this, "This video is over 100 MB", Toast.LENGTH_SHORT).show();
                    pathList.remove(number);
//                    Long tsLong_new = System.currentTimeMillis() / 1000;
//                    String ts_new = tsLong_new.toString();
//                    RandomString randomString_new = new RandomString();
//                    String video_name_new = ts_new + "_" + randomString_new.nextString();
//
//                    File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
//                    File output_file = new File(folder, video_name_new + ".mp4");
//                    String pathss = output_file.toString();
//                    Log.i("VideoCheckknow", "pathss = " + pathss);
//
//
//                    VideoCompressor.start(pathList.get(number), pathss, new CompressionListener() {
//                        @Override
//                        public void onStart() {
//                            // Compression start
//                            Log.i("VideoCheckknow", "onStart");
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            Log.i("VideoCheckknow", "onSuccess");
//                            // On Compression success
//
//                            File file_new = new File(pathss);
//
//
//                            long length_new = file_new.length();
//
//
//                            String lengthsize_new = size(length_new);
//
//                            Log.i("VideoCheckknow", "onSuccess = " + lengthsize_new);
//
//                            Log.i("VideoCheckknow", "onSuccess paths = " + pathss);
//                            video_path.add(pathList.get(number));
//                            Recursion(pathList, number + 1);
//
//
//                        }
//
//                        @Override
//                        public void onFailure(String failureMessage) {
//                            // On Failure
//                            Log.i("VideoCheckknow", "onFailure = "+failureMessage);
//                        }
//
//                        @Override
//                        public void onProgress(float progressPercent) {
//                            // Update UI with progress value
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//
////                        progress.setText(progressPercent + "%");
////                        progressBar.setProgress((int) progressPercent);
//                                    Log.i("VideoCheckknow", "progressPercent = " + progressPercent);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled() {
//                            // On Cancelled
//                        }
//                    }, VideoQuality.MEDIUM, false, false);
//
//
////                    Toast.makeText(this, "Grater then 50 Path ==   " + pathList.get(number), Toast.LENGTH_SHORT).show();
//                    Log.i("VideoCheckknow", "path 1 ===   " + pathList.get(number));

                } else {
                    Log.i("VideoCheckknow", "path 2 ===   " + pathList.get(number));
//                    Toast.makeText(this, "Less then 50 ==   " + pathList.get(number), Toast.LENGTH_SHORT).show();
                    video_path.add(pathList.get(number));
                    Recursion(pathList, number + 1);

                }
            } else if (Second.equalsIgnoreCase("KB")) {
                video_path.add(pathList.get(number));
                Log.i("VideoCheckknow", "path 2 ===   " + pathList.get(number));
                Recursion(pathList, number + 1);
            }


        } else {
            if(pathList.size() != 0){
                sendTheVideo(video_path,pathList);
            }

        }
    }

    public ArrayList<String> image_path = new ArrayList<>();
    public ArrayList<String> original_image_path = new ArrayList<>();


    public String compressImage(String imagePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Log.i("imagecompression","image path = "+imagePath);

        Bitmap bmp = BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath(), options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        Log.i("imagecompression","image Height = "+String.valueOf(actualHeight));
        Log.i("imagecompression","image Width = "+String.valueOf(actualWidth));
        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        if(bmp!=null)
        {
            bmp.recycle();
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = getFilename();
        try {
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files/Compressed");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }

        String mImageName="IMG_"+ String.valueOf(System.currentTimeMillis()) +".jpg";
        String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ mImageName);;
        return uriString;

    }
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;


    class ImageCompression extends AsyncTask<String, Void, String> {
        private Context context;
        public ImageCompression(Context context){
            this.context=context;
        }


        @Override
        protected String doInBackground(String... strings) {
            if(strings.length == 0 || strings[0] == null)
                return null;

            return compressImage(strings[0]);
        }

        protected void onPostExecute(String imagePath){
            // imagePath is path of new compressed image.

            File file = new File(imagePath);
            long length = file.length();
            String lengthsize = size(length);
            Log.i("imagecompression",  " === after compressed image size  " + lengthsize);

            image_path.add(imagePath);
            RecursionImage(original_image_path, number_image + 1);
            Log.i("imagecompression","compressed image path = "+String.valueOf(imagePath));
        }
    }

    public int number_image = 0;
    private void RecursionImage(List<String> pathList, int number) {
        Log.i("imagecompression", "Scroll Check 40");
        Log.i("imagecompression", "Number 1 ===   " + number);
        Log.i("imagecompression", "Size 1 ===   " + pathList.size());


//        Log.d("WalletCheckPathSize", "size 786 ===   " + video_path.size());
        if (pathList.size() > number) {


            File file = new File(pathList.get(number));
            long length = file.length();
            String lengthsize = size(length);
            Log.i("imagecompression", "original image no " + number + " === original image size  " + lengthsize);

            String[] separated = lengthsize.split("\\ ");
            String first = separated[0]; // this will contain "Fruit: they taste good"
            String Second = separated[1];
            double d = Double.valueOf(first);
            Log.i("imagecompression", " original image first size  " + first + " === original image second size  " + Second);
            if (Second.equalsIgnoreCase("MB")) {
                if (d > 1.00) {


                    Long tsLong_new = System.currentTimeMillis() / 1000;
                    String ts_new = tsLong_new.toString();
                    RandomString randomString_new = new RandomString();
                    String video_name_new = ts_new + "_" + randomString_new.nextString();

                    File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
                    File output_file = new File(folder, video_name_new + ".mp4");
                    String paths = output_file.toString();
                    Log.i("imagecompression", "paths = " + paths);




                    number_image = number;
                    ImageCompression imageCompression = new ImageCompression(this);
                    imageCompression.execute(pathList.get(number).toString());

                    Log.i("imagecompression", "path 1 ===   " + pathList.get(number));

                } else {
                    Log.i("imagecompression", "path 2 ===   " + pathList.get(number));
//                    Toast.makeText(this, "Less then 50 ==   " + pathList.get(number), Toast.LENGTH_SHORT).show();
                    image_path.add(pathList.get(number));
                    RecursionImage(pathList, number + 1);

                }
            } else if (Second.equalsIgnoreCase("KB")) {
                image_path.add(pathList.get(number));
                Log.i("imagecompression", "path 2 ===   " + pathList.get(number));
                RecursionImage(pathList, number + 1);
            }


        } else {
//            sendTheVideo(video_path);

            Log.i("imagecompression", "Finish ===   " + image_path.toString());
            sendImage(image_path);
        }
    }








    private void sendLocation(Place place) throws IOException {
        Log.d("WalletScrollCheck", "Scroll Check 41");
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();
        Message message = new MessageCreator.Builder(user, MessageType.SENT_LOCATION, image_name).quotedMessage(getQuotedMessage()).place(place).build();
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());

        //calling updateChat because the 'RealmChangeListener' may not be alive since the user launched another activity
        double latitude = place.getLatLng().latitude;
        double longitude = place.getLatLng().longitude;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0);

        Log.d("MainActivityLL", "Contact latitude ==  ==   " + String.valueOf(latitude));
        Log.d("MainActivityLL", "Contact longitude ==  ==   " + String.valueOf(longitude));
        Log.d("MainActivityLL", "Contact address ==  ==   " + address);

        ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
        if (getQuotedMessage() != null) {
            Message quotedMessage = getQuotedMessage();
            hideReplyLayout();
            String replay_id = quotedMessage.getMessageId();

            Log.d("replyItemClicked_nwe", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
            Log.d("replyItemClicked_nwe", "message id =   " + image_name);
            addpopup.sendlocationReplay(getIntent().getStringExtra("id"), String.valueOf(latitude) + ";" + String.valueOf(longitude) + ";" + address, MessageType.SENT_LOCATION, image_name,replay_id);

        } else {
            addpopup.sendlocation(getIntent().getStringExtra("id"), String.valueOf(latitude) + ";" + String.valueOf(longitude) + ";" + address, MessageType.SENT_LOCATION, image_name);
        }





        updateChat(message);
        hideReplyLayout();
    }

    private void sendForwardedMessages(List<Message> selectedMessages, List<User> selectedUsers) {
        Log.d("WalletScrollCheck", "Scroll Check 43");
        for (User selectedUser : selectedUsers) {
            for (Message selectedMessage : selectedMessages) {
                Message message = MessageCreator.createForwardedMessage(selectedMessage, selectedUser, FireManager.getUid());
//                ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
            }
        }

        Toast.makeText(this, R.string.sending_messages, Toast.LENGTH_SHORT).show();

    }

    private void sendAudio(final String filePath, String audioDuration) {
        Log.d("WalletScrollCheck", "Scroll Check 44");


        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();
        Message message = new MessageCreator.Builder(user, MessageType.SENT_AUDIO, image_name).quotedMessage(getQuotedMessage()).path(filePath).duration(audioDuration).build();
        if (getQuotedMessage() != null) {
            Message quotedMessage = getQuotedMessage();
            hideReplyLayout();
            replay_id = quotedMessage.getMessageId();

            Log.d("SendImageLucky", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
            Log.d("SendImageLucky", "message id =   " + image_name);
//                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
        } else {
            replay_id = "NA";
//                addpopup.sendmessage(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name);
        }
        if (message == null) {
            Toast.makeText(this, R.string.space_or_permissions_error_toast, Toast.LENGTH_SHORT).show();
        } else {
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
            updateChat(message);

            String extension = filePath.substring(filePath.lastIndexOf("."));

            File file = new File(filePath);

            Log.d("WalletCheckLucky", "uploadExampleFile extension  " + extension);
            Log.d("WalletCheckLucky", "uploadExampleFile audioDuration  " + audioDuration);
            Log.d("WalletCheckLucky", "uploadExampleFile image_name  " + image_name);
//            spacesFileRepository = new SpacesFileRepository(ChatActivity.this);


//            spacesFileRepository.uploadExampleAudioFile(file, ChatActivity.this, getIntent().getStringExtra("id"), image_name, audioDuration, extension);

            Log.d("WalletAudiouckyreplay_id", "uploadExampleFile replay_id  " + replay_id);
            Intent intent = new Intent(ChatActivity.this, BackgroundNotificationAudioSendService.class);

            intent.putExtra("filePath", filePath);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            intent.putExtra("image_name", image_name);
            intent.putExtra("audioDuration", audioDuration);
            intent.putExtra("extension", extension);
            intent.putExtra("replay_id",replay_id);

            startService(intent);
        }
        hideReplyLayout();

    }

    private void sendTheVideo(String path) {
        Log.i("sendVideocheck", "Scroll Check 45");
        Long tsLong_new = System.currentTimeMillis() / 1000;
        String ts_new = tsLong_new.toString();
        RandomString randomString_new = new RandomString();
        String video_name_new = ts_new + "_" + randomString_new.nextString();

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Callify");
        File output_file = new File(folder, video_name_new + ".mp4");
        String paths = output_file.toString();


        VideoCompressor.start(path, paths, new CompressionListener() {
            @Override
            public void onStart() {
                // Compression start
                Log.i("sendVideocheck", "onStart");
            }

            @Override
            public void onSuccess() {
                Log.i("sendVideocheck", "onSuccess video path = "+paths);
                // On Compression success
            }

            @Override
            public void onFailure(String failureMessage) {
                // On Failure
                Log.i("sendVideocheck", "onFailure");
            }

            @Override
            public void onProgress(float v) {
                // Update UI with progress value
                runOnUiThread(new Runnable() {
                    public void run() {
//                        progress.setText(progressPercent + "%");
//                        progressBar.setProgress((int) progressPercent);
                    }
                });
            }

            @Override
            public void onCancelled() {
                // On Cancelled
            }
        }, VideoQuality.MEDIUM, false, false);


        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String video_name = ts + "_" + randomString.nextString();


        Message message = new MessageCreator.Builder(user, MessageType.SENT_VIDEO, video_name).quotedMessage(getQuotedMessage()).path(path).context(this).build();
//        ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
        //calling updateChat because the 'RealmChangeListener' may not be alive since the user launched another activity
        updateChat(message);
        hideReplyLayout();


        Bitmap videoThumbBitmap = BitmapUtils.getThumbnailFromVideo(path);
        //generate blurred thumb to send it to other user
        String thumb = BitmapUtils.decodeImage(videoThumbBitmap);
        //generate normal video thumb without blur to show it in recyclerView
        String videoThumb = BitmapUtils.generateVideoThumbAsBase64(videoThumbBitmap);
        File file = new File(path);
        long length = file.length();


        File Imagefile = storeImage(StringToBitMap(thumb), video_name);
        String lengthsize = size(length);
        String extension = path.substring(path.lastIndexOf("."));


//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExamplevideothumFile(Imagefile, video_name, "jpeg");
        Log.i("sendVideocheck", "Video  =   " );
        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
        spacesFileRepository.uploadExamplevideoFile(file, ChatActivity.this, getIntent().getStringExtra("id"), video_name, lengthsize, extension, 0, 0,Imagefile);

    }

    public Bitmap StringToBitMap(String encodedString) {
        Log.d("WalletScrollCheck", "Scroll Check 47");
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private File storeImage(Bitmap image, String video_name) {
        Log.d("WalletScrollCheck", "Scroll Check 48");
        File pictureFile = getOutputMediaFile(video_name);
        if (pictureFile == null) {
            Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());

        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }


        return pictureFile;
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(String video_name) {
        Log.d("WalletScrollCheck", "Scroll Check 49");
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

    private void sendContacts(List<ExpandableContact> selectedContacts) {
        Log.d("WalletScrollCheck", "Scroll Check 50");
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();


        List<Message> messages = new MessageCreator.Builder(user, MessageType.SENT_CONTACT, image_name).quotedMessage(getQuotedMessage()).contacts(selectedContacts).buildContacts();
//


        for (MultiCheckExpandableGroup selectedContact : selectedContacts) {

            ArrayList<PhoneNumber> numbers = (ArrayList) selectedContact.getItems();
            Log.d("MainActivityLL", "Contact numbers  ==  ==   " + numbers.get(0).getNumber());
            Log.d("MainActivityLL", "Contact Title ==  ==   " + selectedContact.getTitle());





            ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);

            if (getQuotedMessage() != null) {
                Message quotedMessage = getQuotedMessage();
                hideReplyLayout();
                String replay_id = quotedMessage.getMessageId();

                Log.d("ContactClicked_nwe", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
                Log.d("ContactClicked_nwe", "message id =   " + image_name);
//                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
                addpopup.sendcontactReplay(getIntent().getStringExtra("id"), numbers.get(0).getNumber(), selectedContact.getTitle(), MessageType.SENT_CONTACT, image_name,replay_id);
            } else {
                Log.d("ContactClicked_nwe", "else =   " + image_name);
                addpopup.sendcontact(getIntent().getStringExtra("id"), numbers.get(0).getNumber(), selectedContact.getTitle(), MessageType.SENT_CONTACT, image_name);
            }




        }
        hideReplyLayout();
        for (Message message : messages) {
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());

            //calling updateChat because the 'RealmChangeListener' may not be alive since the user launched another activity
            updateChat(message);
        }

    }


    public List<String> ImageName = new ArrayList();
    public List<byte[]> ImageCDRIVES = new ArrayList();
    public List<String> lengthsizeArry = new ArrayList<>();
    public List<String> filePath = new ArrayList<>();


    public List<File> filefie = new ArrayList<>();

    public int sizesize = 0;

    //send multiple images
    public void sendImage(List<String> pathList) {
        Log.i("ImageClickSignble", "Scroll Check 51");
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
        if(filefie.size()>0){
            filefie.clear();
        }


        for (String imagePath : pathList) {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String image_name = ts + "_" + randomString.nextString();

            Log.i("ImageClickSignble", "ChatActivity message id =   " + image_name);
            Message message = new MessageCreator.Builder(user, MessageType.SENT_IMAGE, image_name).quotedMessage(getQuotedMessage()).path(imagePath).fromCamera(false).build();
//            ServiceHelper.startNetworkRequest(this, message.getMessageId(), message.getChatId());
            if (getQuotedMessage() != null) {
                Message quotedMessage = getQuotedMessage();
                hideReplyLayout();
                replay_id = quotedMessage.getMessageId();

                Log.d("SendImageLucky", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
                Log.d("SendImageLucky", "message id =   " + image_name);
//                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
            } else {
                replay_id = "NA";
//                addpopup.sendmessage(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name);
            }

            String pathpath = imagePath;
            Bitmap bm = BitmapFactory.decodeFile(pathpath);


            byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            CDRIVES = byteArrayOutputStream.toByteArray();
            encoded = Base64.encodeToString(CDRIVES, Base64.DEFAULT);
            long lengthbmp = CDRIVES.length;
            String lengthsize = size(lengthbmp);
            Log.i("ImageClickSignble", "Image Path  =   1 " + imagePath);
            updateChat(message);


            filePath.add(imagePath);
            File file = new File(imagePath);
            filefie.add(file);
            lengthsizeArry.add(lengthsize);
            ImageCDRIVES.add(CDRIVES);
            ImageName.add(image_name);


//            ChatWebAPI addpopup = new ChatWebAPI(ChatActivity.this);
//            addpopup.sendmessage(getIntent().getStringExtra("id"),"data:image/jpeg;base64," +encodedtwo,MessageType.SENT_IMAGE);
        }


        sizesize = pathList.size() - 1;
        Log.d("ImageClickSignble", "image_name  =   " + sizesize);
//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExampleFile(filePath.get(0), ImageCDRIVES.get(0), ChatActivity.this, getIntent().getStringExtra("id"), ImageName.get(0), lengthsizeArry.get(0), sizesize, 0, ImageName.get(0));
        hideReplyLayout();
        Log.i("ImageClickSignble", "message replay_id =   " + replay_id);
        Log.i("ImageClickSignble", "message sizesize =   " + sizesize);
//
//        Intent intent = new Intent(ChatActivity.this, BackgroundNotificationImageGalleryService.class);
//
//        intent.putExtra("encodedtwo", ImageCDRIVES.get(0));
//        intent.putExtra("id", getIntent().getStringExtra("id"));
//        intent.putExtra("image_name", ImageName.get(0));
//        intent.putExtra("lengthsize", lengthsizeArry.get(0));
//        intent.putExtra("sizesize", sizesize);
//        intent.putExtra("position", 0);
//        intent.putExtra("replay_id",replay_id);
//        startService(intent);
        sendImageMultiple(getIntent().getStringExtra("id"),ImageCDRIVES.get(0),ImageName.get(0),filefie.get(0),String.valueOf(sizesize),0,lengthsizeArry.get(0));
    }
    public void sendImageMultiple( final String U_id,byte[] encodedtwo,String name,final File finalFile,final String size,final int position,final String image_size){
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getApplicationContext());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;



        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Upload")
                .setContentText("Uploading Image")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initRetrofitMultiple(U_id,name, encodedtwo,finalFile,size,position,image_size);

    }
    private void initRetrofitMultiple(final String U_id,String image_name, byte[] encodedtwo,final File finalFile,final String size,final int position,final String image_size) {

        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING);
        observer = transferUtility.upload(
                spacename + "/callify/upload/images", //empty bucket name, included in endpoint
                image_name + ".jpeg",
                finalFile, //a File object that you want to upload
                filePermission
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {
                    onDownloadComplete(true);

                    Log.i("CCpWalletCCpWallet", "COMPLETED id  2 =     " );

                    Log.d("uploadExampleImageFile", "COMPLETED id  2 =     " );
                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS);




                    ChatWebAPI addpopup = new ChatWebAPI(getApplicationContext());

                    addpopup.sendmessageImage(U_id, image_name + ".jpeg," + image_size, MessageType.SENT_IMAGE, sizesize, position, image_name,replay_id);
//                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS);

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


                double minus = bytesTotal - bytesCurrent;
                Log.d("uploadExampmageFile", "minus  2 =     " + minus);
                double divided = minus / bytesTotal;
                Log.d("uploadExampmageFile", "divided  2 =     " + divided);
                double multi = divided *100;
                Log.d("uploadExampmageFile", "divide id  2 =     " + multi);
                double sub = 100- multi;


                Integer y = (int) sub;
                Log.d("uploadExampmageFile", "sub id  2 =     " + y);


                updateNotification(y);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i("CCpWalletCCpWallet", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void replydataimage(int i) {
        Log.d("WalletScrollCheck", "Scroll Check 52");
        Log.d("CheckImageSize", "replydataimage position  =   " + i);

        edit.putInt("Image_check", 0);
        edit.apply();

//        Intent intent = new Intent(ChatActivity.this, BackgroundNotificationImageGalleryService.class);
//
//        intent.putExtra("encodedtwo", ImageCDRIVES.get(i));
//        intent.putExtra("id", getIntent().getStringExtra("id"));
//        intent.putExtra("image_name", ImageName.get(i));
//        intent.putExtra("lengthsize", lengthsizeArry.get(i));
//        intent.putExtra("sizesize", sizesize);
//        intent.putExtra("position", i);
//        intent.putExtra("replay_id",replay_id);
//        startService(intent);
        sendImageMultiple(getIntent().getStringExtra("id"),ImageCDRIVES.get(i),ImageName.get(i),filefie.get(i),String.valueOf(sizesize), i,lengthsizeArry.get(i));

//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExampleFile(filePath.get(i), ImageCDRIVES.get(i), ChatActivity.this, getIntent().getStringExtra("id"), ImageName.get(i), lengthsizeArry.get(i), sizesize, i, ImageName.get(i));
    }

    public static String size(long size) {
        Log.d("WalletScrollCheck", "Scroll Check 52");
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    //send only one image
    private void sendImage(String filePath, boolean isFromCamera, byte[] encodedtwo, String Size,File files) {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();


        Message message = new MessageCreator.Builder(user, MessageType.SENT_IMAGE, image_name).quotedMessage(getQuotedMessage()).path(filePath).fromCamera(isFromCamera).build();
        if (getQuotedMessage() != null) {
            Message quotedMessage = getQuotedMessage();

            replay_id = quotedMessage.getMessageId();

            Log.i("ImageClickSignble", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
//                Log.d("replyItemClicked_nwe", "message id =   " + image_name);
//                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
        } else {
            replay_id = "NA";
//                addpopup.sendmessage(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name);
        }
        updateChat(message);

        Log.i("ImageClickSignble", "Image Path 2 replay_id =    "+replay_id);

        String lengthsize = size(Long.parseLong(Size));
//        Log.d("WalletCheckTimeStemp","TimeStemp  =    "+ts+"_"+randomString.nextString());
//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExampleImageFile(filePath, encodedtwo, ChatActivity.this, getIntent().getStringExtra("id"), image_name, lengthsize);
//        Intent intent = new Intent(ChatActivity.this, BackgroundNotificationService.class);
//        Log.i("SendImageLuckyimage", "Image Path 2 encodedtwo =    "+encodedtwo);
//        Log.i("SendImageLuckyimage", "Image Path 2 id =    "+getIntent().getStringExtra("id"));
//        Log.i("SendImageLuckyimage", "Image Path 2 image_name =    "+image_name);
//        Log.i("SendImageLuckyimage", "Image Path 2 lengthsize =    "+lengthsize);
//        Log.i("SendImageLuckyimage", "Image Path 2 replay_id =    "+replay_id);
//        Log.i("SendImageLuckyimage", "Image Path 2 filePath =    "+filePath);
//        intent.putExtra("encodedtwo", encodedtwo);
//        intent.putExtra("id", getIntent().getStringExtra("id"));
//        intent.putExtra("image_name", image_name);
//        intent.putExtra("lengthsize", lengthsize);
//        intent.putExtra("replay_id",replay_id);
        Log.i("ImageClickSignble", "one size =   " + lengthsize);

        sendImageSingle(getIntent().getStringExtra("id"),encodedtwo,image_name,files,lengthsize);
//        intent.putExtra("file",filePath);
//        startService(intent);
        hideReplyLayout();
    }


    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    String accesskey = "4SYUKBCFA4KASIHESCTP";
    String secretkey = "cWCsWzfqX4h1Y6ULx+8OE0hrPjf5CCiDPFOSIdkxOSA";
    String spacename = "gpslabindia";

    String spaceregion = "https://nyc3.digitaloceanspaces.com";
    String filename = "example_image";
    CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;
    String filetype = "jpeg";
    public TransferUtility transferUtility;
//    public int counter = 0;

    public AmazonS3Client client;
    public BasicAWSCredentials credentials;
    public TransferObserver observer;


    public void sendImageSingle( final String U_id,byte[] encodedtwo,String name,final File finalFile,final String size){
        credentials = new BasicAWSCredentials(accesskey, secretkey);

        client = new AmazonS3Client(credentials, Region.getRegion("us-east-1"));
        client.setEndpoint(spaceregion);
        transferUtility = new TransferUtility(client, getApplicationContext());
        CannedAccessControlList filePermission = CannedAccessControlList.PublicRead;



        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("id", "an", NotificationManager.IMPORTANCE_LOW);

            notificationChannel.setDescription("no sound");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        notificationBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Upload")
                .setContentText("Uploading Image")
                .setDefaults(0)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initRetrofit(U_id,name, encodedtwo,finalFile,size);

    }
    private void initRetrofit(final String U_id,String image_name, byte[] encodedtwo,final File finalFile,final String size) {

        RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.LOADING);
        observer = transferUtility.upload(
                spacename + "/callify/upload/images", //empty bucket name, included in endpoint
                image_name + ".jpeg",
                finalFile, //a File object that you want to upload
                filePermission
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {
                    onDownloadComplete(true);

                    Log.i("ImageClickSignble", "COMPLETED id  2 =     " );
                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS);
                    ChatWebAPI addpopup = new ChatWebAPI(getApplicationContext());
                    addpopup.sendmessage(U_id, image_name + ".jpeg," + size, MessageType.SENT_IMAGE, image_name,replay_id);
//                    RealmHelper.getInstance().updateDownloadUploadStat(image_name, DownloadUploadStat.SUCCESS);

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {


                double minus = bytesTotal - bytesCurrent;
                Log.d("uploadExampmageFile", "minus  2 =     " + minus);
                double divided = minus / bytesTotal;
                Log.d("uploadExampmageFile", "divided  2 =     " + divided);
                double multi = divided *100;
                Log.d("uploadExampmageFile", "divide id  2 =     " + multi);
                double sub = 100- multi;


                Integer y = (int) sub;
                Log.d("uploadExampmageFile", "sub id  2 =     " + y);


                updateNotification(y);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.i("CCpWalletCCpWallet", "error id  2 =     " + ex.getMessage());
//                Toast.makeText(activity, "Space upload error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void updateNotification(int currentProgress) {

        Log.d("uploadExampleImageFile", "updateNotification id  2 =   counter =>  " + currentProgress);
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Upload: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendProgressUpdate(boolean downloadComplete) {

//        Intent intent = new Intent(MainActivity.PROGRESS_UPDATE);
//        intent.putExtra("downloadComplete", downloadComplete);
//        LocalBroadcastManager.getInstance(BackgroundNotificationService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_upload_done);
        notificationBuilder.setContentText("Image Upload Complete");
        notificationManager.notify(0, notificationBuilder.build());
        notificationManager.cancelAll();

    }



    public ArrayList<String> video_name_array = new ArrayList<>();
    public ArrayList<File> filePath_array = new ArrayList<>();

    public ArrayList<String> video_size_array = new ArrayList<>();

    public ArrayList<String> video_extension_array = new ArrayList<>();

    public ArrayList<File> video_thum_path_array = new ArrayList<>();

    public int sizesize_video = 0;
    public ArrayList<String> file_path_path = new ArrayList<>();

    private void sendTheVideo(List<String> pathList,List<String> opath) {

        Log.i("VideoCheckknow", "Scroll Check 54");


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


//        for(int i=0;i<pathList.size();i++){
//            String path = pathList.get(i);
//            String pathnew = opath.get(i);

        for (String path : pathList) {

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            RandomString randomString = new RandomString();
            String video_name = ts + "_" + randomString.nextString();

            video_name_array.add(video_name);


            Log.i("VideoCheckknow","paths ============= bease path ================="+String.valueOf(path));
//            Log.i("VideoCheckknow","paths ============= bease pathnew ================="+String.valueOf(pathnew));

            Message message = new MessageCreator.Builder(user, MessageType.SENT_VIDEO, video_name).quotedMessage(getQuotedMessage()).path(path).context(this).build();
            if (getQuotedMessage() != null) {
                Message quotedMessage = getQuotedMessage();
                hideReplyLayout();
                replay_id = quotedMessage.getMessageId();

                Log.i("VideoCheckknow", "quotedMessage  " + quotedMessage.getMessageId());


//            String id = quotedMessage.getMessageId();
//            Log.d("sendMessagesendMessage", "Replay id =   "+id);
//                Log.d("replyItemClicked_nwe", "message id =   " + image_name);
//                addpopup.sendmessage_reply(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name, replay_id);
            } else {
                replay_id = "NA";
//                addpopup.sendmessage(getIntent().getStringExtra("id"), text, MessageType.SENT_TEXT, image_name);
            }




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


            Log.i("VideoCheckknow", "extension =   " + extension);
            Log.i("VideoCheckknow", "lengthsize =   " + lengthsize);
            Log.i("VideoCheckknow", "size =     " + lengthsize);
            Log.i("VideoCheckknow", "Imagefile =   " + Imagefile);
            updateChat(message);


//            spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//            spacesFileRepository.uploadExamplevideoFile(file, ChatActivity.this, getIntent().getStringExtra("id"), video_name, lengthsize,extension);
        }
        hideReplyLayout();


//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExamplevideothumFile(video_thum_path_array.get(0), video_name_array.get(0), "jpg");
//        spacesFileRepository.uploadExamplevideoFile(filePath_array.get(0), ChatActivity.this, getIntent().getStringExtra("id"), video_name_array.get(0), video_size_array.get(0), video_extension_array.get(0), sizesize_video, 0);
        Log.i("VideoCheckknow", "replay_id =   " + replay_id);
        Intent intent = new Intent(ChatActivity.this, BackgroundNotificationVideoSendService.class);
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

//    public void replydatavideo(int i) {
//        Log.d("WalletScrollCheck","Scroll Check 55");
//        Log.d("WalletCheckLucky", "replydataimage position  =   " + i);
//        spacesFileRepository = new SpacesFileRepository(ChatActivity.this);
//        spacesFileRepository.uploadExamplevideoFile(filePath_array.get(i), ChatActivity.this, getIntent().getStringExtra("id"), video_name_array.get(i), video_size_array.get(i), video_extension_array.get(i), sizesize_video, i);
//
//
//        spacesFileRepository.uploadExamplevideothumFile(video_thum_path_array.get(i), video_name_array.get(i), "jpg");
//    }

    private void hideReplyLayout() {
        animateReplyLayout(false);
        currentQuotedMessage = null;
    }

    //scroll to last OR update the unread count
    private void updateChat(Message message) {
        Log.i("VideoCheckknow", "Scroll Check 5");
        Log.i("VideoCheckknow", "UpdateChat");
        if (message.getType() == MessageType.GROUP_EVENT)
            return;

        //if the message was send by the user then scroll to last
        if (message.getFromId().equals(FireManager.getUid()) && message.getMessageStat() == MessageStat.PENDING) {
            scrollToLast();
        } else {
            //if the message was sent by Receiver and its state is still pending
//
            //get index from the message
            int i = messageList.indexOf(message);
            //if it's -1 (not exists) return
            if (i == -1)
                return;


            //get last visible item on screen
            int lastVisibleItemPosition = getLastVisibileItem();

            //if the last message is visible then we will scroll to last
            //the user in this case is at before the last message that inserted
            // therefore a new message was inserted and we want to scroll to it
            //"-2" because one for index and one for previous message
            if (messageList.size() - 2 == lastVisibleItemPosition) {
                scrollToLast();
            } else {
                //otherwise the user may was checking another messages
                //and for that we want to show the unreadCount badge with the count
                if (lastVisibleItemPosition != i && !message.getMessageId().equals(previousMessageIdForScroll) && message.getType() != MessageType.GROUP_EVENT) {
                    unreadCount++;
                    countUnreadBadge.setText(unreadCount + "");
                    countUnreadBadge.setVisibility(View.VISIBLE);
                    btnScroll.show();
                }
                previousMessageIdForScroll = message.getMessageId();
            }
//            }
        }
    }

    private Message getQuotedMessage() {
        Log.d("sendMessagesendMessage", "currentQuotedMessage");
        if (quotedMessageFrame.getVisibility() == View.GONE)
            return null;


        return currentQuotedMessage;
    }

    @Override
    public void onContainerViewClick(int pos, @NotNull View view, @NotNull Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 59");
        if (isInActionMode) {
            viewModel.itemSelected(pos, message);
        } else {
            switch (message.getType()) {
                case MessageType.SENT_IMAGE:
                case MessageType.RECEIVED_IMAGE:
                    if (message.getType() == MessageType.SENT_IMAGE ||
                            message.getDownloadUploadStat() == DownloadUploadStat.SUCCESS) {
                        String path = message.getLocalPath();
                        String messageId = message.getMessageId();
                        Log.i("ChatsendImageKnow", "Path = " + path);
                        Log.i("ChatsendImageKnow", "messageId = " + messageId);
                        Log.i("ChatsendImageKnow", "User id = " + user.getUid());
                        Log.i("ChatsendImageKnow", "pos = " + pos);
                        if (!FileUtils.isFileExists(path)) {
                            Toast.makeText(ChatActivity.this, R.string.item_deleted_from_storage, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(ChatActivity.this, FullscreenActivity.class);
                        intent.putExtra(IntentUtils.EXTRA_PATH, path);
                        intent.putExtra(IntentUtils.UID, user.getUid());
                        intent.putExtra(IntentUtils.EXTRA_MESSAGE_ID, messageId);
                        intent.putExtra(IntentUtils.EXTRA_STARTING_POSITION, pos);

                        int firstVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                        Log.i("ChatsendImageKnow", "firstVisibleItemPosition = " + firstVisibleItemPosition);
                        Log.i("ChatsendImageKnow", "lastVisibleItemPosition = " + lastVisibleItemPosition);

                        intent.putExtra(IntentUtils.EXTRA_FIRST_VISIBLE_ITEM_POSITION, firstVisibleItemPosition);
                        intent.putExtra(IntentUtils.EXTRA_LAST_VISIBLE_ITEM_POSITION, lastVisibleItemPosition);
                        startActivity(intent);

//                        if (!mIsDetailsActivityStarted) {
//                            mIsDetailsActivityStarted = true;

//                        ImageView imageView = view.findViewById(R.id.img_msg);
//                        if (imageView != null && ViewCompat.getTransitionName(imageView) != null)
//                            Log.i("ChatsendImageKnow", "firstVisibleItemPosition = " + firstVisibleItemPosition);
//                            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(ChatActivity.this,
//                                    imageView, ViewCompat.getTransitionName(imageView)).toBundle());

//                        }
                    }
                    break;

                case MessageType.RECEIVED_VIDEO:
                case MessageType.SENT_VIDEO:
                    if (message.getType() == MessageType.SENT_VIDEO ||
                            message.getDownloadUploadStat() == DownloadUploadStat.SUCCESS) {

                        String path = message.getLocalPath();
                        String messageId = message.getMessageId();

                        if (!FileUtils.isFileExists(path)) {
                            Toast.makeText(ChatActivity.this, R.string.item_deleted_from_storage, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(ChatActivity.this, FullscreenActivity.class);
                        intent.putExtra(IntentUtils.EXTRA_PATH, path);
                        intent.putExtra(IntentUtils.UID, user.getUid());
                        intent.putExtra(IntentUtils.EXTRA_MESSAGE_ID, messageId);
                        intent.putExtra(IntentUtils.EXTRA_STARTING_POSITION, pos);

                        int firstVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        int lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                        intent.putExtra(IntentUtils.EXTRA_FIRST_VISIBLE_ITEM_POSITION, firstVisibleItemPosition);
                        intent.putExtra(IntentUtils.EXTRA_LAST_VISIBLE_ITEM_POSITION, lastVisibleItemPosition);


//                        if (!mIsDetailsActivityStarted) {
//                            mIsDetailsActivityStarted = true;


                        startActivity(intent);


//                        }
                    }
                    break;

                case MessageType.SENT_LOCATION:
                case MessageType.RECEIVED_LOCATION:
                    Intent openMapIntent = IntentUtils.getOpenMapIntent(message.getLocation());
                    if (openMapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(openMapIntent);
                    }
                    break;

                case MessageType.RECEIVED_FILE:
                case MessageType.SENT_FILE:
                    if (message.getDownloadUploadStat() == DownloadUploadStat.SUCCESS)
                        onFileClick(message);

                    break;

                case MessageType.SENT_CONTACT:
                case MessageType.RECEIVED_CONTACT:
//                    Intent intent = new Intent(this, ContactDetailsActivity.class);
//                    intent.putExtra(IntentUtils.EXTRA_MESSAGE_ID, message.getMessageId());
//                    intent.putExtra(IntentUtils.EXTRA_CHAT_ID, message.getChatId());
//                    startActivity(intent);
                    break;

                case MessageType.RECEIVED_AUDIO:
                case MessageType.SENT_AUDIO:
                case MessageType.RECEIVED_VOICE_MESSAGE:
                case MessageType.SENT_VOICE_MESSAGE:
                    int progress = viewModel.getAudibleProgressForId(message.getMessageId()) == -1 ? 0 : viewModel.getAudibleProgressForId(message.getMessageId());
                    playAudio(message.getMessageId(), message.getLocalPath(), pos, progress);
                    break;


            }
        }
    }

    public void onFileClick(Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 60");
        try {
            startActivity(IntentUtils.getOpenFileIntent(this, message.getLocalPath()));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.cannot_open_this_file, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //this is called from adapter when the user clicks on Play Button on Voice/Audio Message
    public void playAudio(final String id, final String url, final int pos,
                          final int progress) {
        Log.d("WalletScrollCheck", "Scroll Check 61");
        //start audio service and play audio


        final Message message = messageList.get(pos);


        //update the received voice message when the user listens to it
        if (message != null && !message.isGroup() && message.isVoiceMessage()
                && !message.getFromId().equals(FireManager.getUid())
                && !message.isVoiceMessageSeen()) { //check if it's already listened or not

        }


    }

    public void seekTo(String id, int progress) {
        Log.d("WalletScrollCheck", "Scroll Check 62");
    }

    @Override
    public void onSeek(@NotNull Message message, int progress, int seekBarMax) {
        Log.d("WalletScrollCheck", "Scroll Check 64");
        if (isInActionMode) return;

        //if user changed the seekbar while it's not playing
        String messageId = message.getMessageId();

        if (seekBarMax == 100) {
            int max = (int) Util.getMediaLengthInMillis(this, message.getLocalPath());
            if (max == 0) return; //if file not found or missing permissions

            int realProgress = max / 100 * progress;
            viewModel.setAudibleMax(messageId, max);
            viewModel.setAudibleProgress(messageId, realProgress, null);


        }
        viewModel.setAudibleProgress(messageId, progress, null);


        seekTo(messageId, progress);
    }

    @Override
    public void onMessageClick(@NotNull RealmContact contact) {


        if (isInActionMode) return;

        onContactBtnMessageClick(contact);
    }

    public void onContactBtnMessageClick(RealmContact contact) {


    }

    @Override
    public void onAddContactClick(@NotNull RealmContact contact) {

        if (isInActionMode) return;



        Intent addContactIntent = IntentUtils.getAddContactIntent(contact);
        startActivity(addContactIntent);
    }


    public List<Integer> position = new ArrayList<>();

    @Override
    public void onItemViewClick(int pos, @NotNull View itemView, @NotNull Message message) {


        if (isInActionMode){


            if(position.contains(pos)){
                Log.d("WalletScrollCheckisInActionMode", "Scroll Check 68  ==   true");
                for(int i=0;i<position.size();i++){
                    if(pos == position.get(i)){
                        position.remove(i);

                        setBg(true, itemView);
                    }
                }
            }else {
                position.add(pos);
                Log.d("WalletScrollCheckisInActionMode", "Scroll Check 68  ==   false  ====     ====    "+position.toString());

                setBackgroundColor(true,itemView);

            }

            viewModel.itemSelected(pos, message);

        }

    }

    @Override
    public void onLongClick(int pos, @NotNull View itemView, @NotNull Message message) {
        Log.d("WalletScrollCheckisInActionMode", "Scroll Check 69  ==   "+isInActionMode);
        if (!isInActionMode) {
            if(position.size()>0){
                position.clear();
            }
            position.add(pos);
            Log.d("WalletScrollCheckisInActionMode", "Scroll Check 69  ==   "+position.toString());
            onActionModeStarted();
            viewModel.itemSelected(pos, message);
            setBackgroundColor(true,itemView);
        }
    }

    public void setBg(Boolean isAdded, View view){
        int  addedColor = this.getResources().getColor(R.color.transparendt);
        view.setBackgroundColor(addedColor);
    }

    public void setBackgroundColor(Boolean isAdded,View view){
      int  addedColor = this.getResources().getColor(R.color.item_selected_background_color);
        int notAddedColor = 0x00000000;
        if (isAdded){
            view.setBackgroundColor(addedColor);
        }else {
            view.setBackgroundColor(notAddedColor);
        }
    }
    public void onActionModeStarted() {
        //exit search and remove search from toolbar
        // if isInSearchMode
        if (isInSearchMode)
            exitSearchMode();

        //if it's not in action mode before
        //remove old menu items from toolbar
        //inflate action items and hide userInfo
        if (!isInActionMode) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_action_chat);
            hideOrShowUserInfo(true);
        }


        isInActionMode = true;

        //set items selected count as visible
        tvCounterAction.setVisibility(View.VISIBLE);
    }

    public void updateActionModeItemsCount(int itemsCount) {
        tvCounterAction.setText(itemsCount + "");
    }
    private int currentTypingState = TypingStat.NOT_TYPING;
    private void exitSearchMode() {
        isInSearchMode = false;
        searchViewToolbar.onActionViewCollapsed();
        searchGroup.setVisibility(View.GONE);
        toolbar.inflateMenu(R.menu.menu_chat);
        hideOrShowUserInfo(false);
        //update online and typing tvs visibility after exiting search mode
        updateToolbarTvsVisibility(currentTypingState != TypingStat.NOT_TYPING);
        adapter.notifyDataSetChanged();
    }
    String presenceStat = "";
    float initialToolbarTranslationY = 0;
    private void updateToolbarTvsVisibility(boolean hideOnlineStatToolbar) {
        if (isInActionMode || isInSearchMode) return;


        if (isGroup || isBroadcast) {
            if (hideOnlineStatToolbar) {
                tvTypingStatToolbar.setVisibility(View.VISIBLE);
                availableStatToolbar.setVisibility(View.GONE);
            } else {
                availableStatToolbar.setVisibility(View.VISIBLE);
                tvTypingStatToolbar.setVisibility(View.GONE);
            }
        } else {
            if (hideOnlineStatToolbar) {
                tvTypingStatToolbar.setVisibility(View.VISIBLE);
                availableStatToolbar.setVisibility(View.GONE);
            } else {
                tvTypingStatToolbar.setVisibility(View.GONE);
                if (presenceStat.equals(""))
                    availableStatToolbar.setVisibility(View.GONE);

                else
                    availableStatToolbar.setVisibility(View.VISIBLE);
            }
        }

        float spacing = DpUtil.toPixel(3, this);

        if (isGroup)
            userNameToolbarChatActivity.animate().translationY(initialToolbarTranslationY - spacing).start();

//        animate online tv
        else if (availableStatToolbar.getText().toString().equals("") && tvTypingStatToolbar.getText().toString().equals("")) {
            userNameToolbarChatActivity.animate().translationY(initialToolbarTranslationY + spacing).start();

        } else {
//            do not move it up unless it's down
            if (initialToolbarTranslationY - spacing != initialToolbarTranslationY) {
                if (presenceStat.equals("") && currentTypingState == TypingStat.NOT_TYPING) return;

                userNameToolbarChatActivity.animate().translationY(initialToolbarTranslationY - spacing).start();
                availableStatToolbar.animate().translationY(initialToolbarTranslationY - spacing).start();
                tvTypingStatToolbar.animate().translationY(initialToolbarTranslationY - spacing).start();
            }
        }


    }
    //this is called from adapter when the user is clicked on "X" Button
    //to cancel  upload or download process
    public void cancelDownloadOrUpload(Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 70");
//        if (MessageType.isSentType(message.getType()))
////            DownloadManager.cancelUpload(message);
//        else
//            DownloadManager.cancelDownload(message);
    }

    @Override
    public void onProgressButtonClick(int pos, @NotNull View itemView, @NotNull Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 71");
        Log.d("updateDowloadUpload", "chatActivity message id 99 =   " + message.getMessageId());
        if (isInActionMode) {
            viewModel.itemSelected(pos, message);
        } else {
            if (message.getDownloadUploadStat() == LOADING) {
                cancelDownloadOrUpload(message);
            } else if (message.getDownloadUploadStat() == CANCELLED || message.getDownloadUploadStat() == FAILED) {

                if (MessageType.isSentType(message.getType())) {
                    upload(message);
                } else {
                    download(message);
                }

            }
        }
    }

    public void upload(Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 72");
        RealmHelper.getInstance().updateDownloadUploadStat(message.getMessageId(), LOADING);

    }

    public void download(Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 73");
        RealmHelper.getInstance().changeDownloadOrUploadStat(message.getMessageId(), LOADING);

    }

    @Override
    public void onQuotedMessageClick(int pos, @NotNull View itemView, @NotNull Message message) {
        Log.d("WalletScrollCheck", "Scroll Check 74");
        QuotedMessage quotedMessage = message.getQuotedMessage();
        if (quotedMessage.getStatus() != null) {
            Status status = RealmHelper.getInstance().getStatus(quotedMessage.getStatus().getStatusId());
            if (status != null) {
//                Intent intent = new Intent(this, ViewStatusActivity.class);
//                intent.putExtra(IntentUtils.UID, status.getUserId());
//                startActivity(intent);
            }
        } else {
            highlightQuotedMessage(quotedMessage);
        }
    }

    public void highlightQuotedMessage(QuotedMessage message) {
        Log.d("WalletScrollCheck", "Scroll Check 75");
        int index = Message.getPosFromId(message.getMessageId(), messageList);
        if (index != -1) {
            scrollAndHighlightQuotedMessage(index);
        }
    }

    private void scrollAndHighlightQuotedMessage(final int index) {
        Log.d("WalletScrollCheck", "Scroll Check 76");
        recyclerView.scrollToPosition(index);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("CheckChatAPI", "Check Scorll View     scrollAndHighlightQuotedMessage");
                //get view holder of this textView
                RecyclerView.ViewHolder viewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(index);
                if (viewHolderForAdapterPosition != null) {
                    //get textView;
                }
                //highlight text
            }
        }, 100);

    }


}