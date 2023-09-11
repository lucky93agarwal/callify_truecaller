package com.gpslab.kaun.chat;

import com.gpslab.kaun.chat.NewMessageHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.gpslab.kaun.digitaloceanspaces.RandomString;
import com.gpslab.kaun.view.BitmapUtils;

import com.gpslab.kaun.view.DirManager;
import com.gpslab.kaun.view.DownloadUploadStat;
import com.gpslab.kaun.view.FireManager;
import com.gpslab.kaun.view.Message;
import com.gpslab.kaun.view.MessageStat;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.PhoneNumber;
import com.gpslab.kaun.view.QuotedMessage;
import com.gpslab.kaun.view.RealmContact;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.RealmLocation;
import com.gpslab.kaun.view.User;
import com.gpslab.kaun.view.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;

public class MyFCMService {
    private final Context _context;
//    public User user;

    public NewMessageHandler newMessageHandler;

    public MyFCMService(Context context) {
        this._context = context;
//        this.user = new User();
    }

    public void onMessageReceived(String sender, String chat_message, String chat_type, String attribute, String chat_id, String reply_id) {
        Log.d("CheckChatAPINew", "sender = " + sender);
        Log.d("CheckChatAPINew", "chat_message = " + chat_message);
        Log.d("CheckChatAPINew", "chat_type = " + chat_type);
        Log.d("CheckChatAPINew", "attribute = " + attribute);
        Log.d("CheckChatAPINew", "chat_id = " + chat_id);
        FireManager fireManager = new FireManager();
        CompositeDisposable disposables = new CompositeDisposable();
        newMessageHandler = new NewMessageHandler(_context, fireManager, disposables);
        handleNewMessage(sender, chat_message, chat_type, attribute, chat_id, reply_id);
    }


    public void onMessageReceivedImage(String sender, String chat_message, String chat_type, String attribute, String path, String message_id, String reply_id) {
        Log.i("ChatsendImageKnow", "sender = " + sender);
        Log.i("ChatsendImageKnow", "chat_message = " + chat_message);
        Log.i("ChatsendImageKnow", "chat_type = " + chat_type);
        Log.i("ChatsendImageKnow", "attribute = " + attribute);
        Log.i("ChatsendImageKnow", "path = " + path);
        Log.i("ChatsendImageKnow", "onMessageReceivedImage    reply_id   =    " + reply_id);

        FireManager fireManager = new FireManager();
        CompositeDisposable disposables = new CompositeDisposable();
        newMessageHandler = new NewMessageHandler(_context, fireManager, disposables);
        handleNewMessageImage(sender, chat_message, chat_type, attribute, path, message_id, reply_id);
    }


    public void onMessageReceivedContact(String sender, String contact_name, String chat_type, String contact_number, String message_id, String reply_id) {
        Log.d("onundeliveredMessage", "1  sender = " + sender);
        Log.d("onundeliveredMessage", "1  chat_message = " + contact_name);
        Log.d("onundeliveredMessage", "1  chat_type = " + chat_type);
        Log.d("onundeliveredMessage", "1  attribute = " + contact_number);

        FireManager fireManager = new FireManager();
        CompositeDisposable disposables = new CompositeDisposable();
        newMessageHandler = new NewMessageHandler(_context, fireManager, disposables);
        handleNewMessageContact(sender, contact_name, chat_type, contact_number, message_id, reply_id);
    }


    public void onMessageReceivedLocation(String sender, String latitude, String longitude, String address, String chat_type, String message_id, String reply_id) {
        Log.d("onundeliveredMessage", "1  sender = " + sender);
        Log.d("onundeliveredMessage", "1  chat_message = " + latitude);
        Log.d("onundeliveredMessage", "1  chat_type = " + chat_type);
        Log.d("onundeliveredMessage", "1  attribute = " + address);

        FireManager fireManager = new FireManager();
        CompositeDisposable disposables = new CompositeDisposable();
        newMessageHandler = new NewMessageHandler(_context, fireManager, disposables);
        handleNewMessageLocation(sender, latitude, longitude, address, chat_type, message_id, reply_id);
    }

    public void onMessageReceivedVideo(String sender, String chat_message, String chat_type, String attribute, String path, String message_id, String reply_id) {
        Log.i("onundeliveredMessage", "1  sender = " + sender);
        Log.i("onundeliveredMessage", "1  chat_message = " + chat_message);
        Log.i("onundeliveredMessage", "1  chat_type = " + chat_type);
        Log.i("onundeliveredMessage", "1  attribute = " + attribute);
        Log.i("onundeliveredMessage", "1  path = " + path);
        FireManager fireManager = new FireManager();
        CompositeDisposable disposables = new CompositeDisposable();
        newMessageHandler = new NewMessageHandler(_context, fireManager, disposables);
        handleNewMessageVideo(sender, chat_message, chat_type, attribute, path, message_id, reply_id);
    }


    public void onMessageReceivedDoc(String sender, String chat_type, String path, String reply_id) {
        Log.i("onundeliveredMessage", "1  sender = " + sender);
        Log.i("onundeliveredMessage", "1  chat_type = " + chat_type);
        Log.i("onundeliveredMessage", "1  path = " + path);

        FireManager fireManager = new FireManager();
        CompositeDisposable disposables = new CompositeDisposable();
        newMessageHandler = new NewMessageHandler(_context, fireManager, disposables);
        handleNewMessageDoc(sender, chat_type, path, reply_id);
    }

    private void handleNewMessage(String sender, String chat_message, String chat_type, String attribute, String chat_id, String reply_id) {

        String messageId = chat_id;
//        user = users;

        User user = new User();
        Log.d("CheckReciveMessage", "id =  " + user.getUid());
        String phone = "8840149029";
        String content = chat_message;

        String timestamp = String.valueOf(new Date().getTime());
        int type = Integer.valueOf(chat_type);
        String fromId = sender;
        String toId = user.getUid();
        String metadata = attribute;


        Message message = new Message();
        message.setFromId(fromId);
        message.setContent(content);
        message.setChatId(fromId);
        message.setType(type);
        message.setToId(toId);
        message.setSeen(true);
        message.setTimestamp(timestamp);
        message.setForwarded(false);
        message.setMessageStat(MessageStat.RECEIVED);
        message.setFromPhone(phone);


        message.setMessageId(messageId);
        message.setMetadata(metadata);


        message.setDownloadUploadStat(DownloadUploadStat.DEFAULT);

        if (reply_id.equalsIgnoreCase("NA")) {
        } else {
            Message quotedMessage = RealmHelper.getInstance().getMessage(reply_id, fromId);

            message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
        }


        if (type == MessageType.RECEIVED_IMAGE) {
            message.setThumb(content);
//            message.setLocalPath(content);
        } else if (type == MessageType.RECEIVED_AUDIO) {
            message.setLocalPath(content);
        } else if (type == MessageType.RECEIVED_VIDEO) {

        }


        RealmHelper.getInstance().refresh();
        Message quotedMessage = RealmHelper.getInstance().getMessage(messageId, fromId);
        if (quotedMessage != null) {
            message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
//            Log.d("WalletRecive","message =    "+quotedMessage.toString());
//            Log.d("WalletRecive","messageToQuotedMessage =    "+QuotedMessage.messageToQuotedMessage(quotedMessage));
        }


        Log.d("CheckChatAPINew", "message =    " + message.toString());
        RealmHelper.getInstance().saveObjectToRealm(message);

        //save chat if this the first message in this chat
        RealmHelper.getInstance().saveUnreadMessage("123", "1");
//        newMessageHandler.handleNewMessage(phone, message);

        RealmHelper.getInstance().saveChatIfNotExists(message, user);


//        ((ChatActivity)_context).scrollToLast();

    }


    private void handleNewMessageContact(String sender, String contact_name, String chat_type, String contact_number, String message_id, String reply_id) {
        final int random = new Random().nextInt(61) + 20;
        String messageId = message_id;
//        user = users;

        User user = new User();
        Log.d("CheckReciveMessage", "id =  " + user.getUid());
        String phone = "8840149029";
        String content = contact_name;

        String timestamp = String.valueOf(new Date().getTime());
        int type = Integer.valueOf(chat_type);
        String fromId = sender;
        String toId = user.getUid();
//        String metadata = attribute;


        Message message = new Message();
        message.setSeen(true);
        message.setContent(content);
        message.setFromPhone(phone);
        message.setTimestamp(timestamp);
        message.setForwarded(false);
        message.setFromId(fromId);
        message.setType(type);
        message.setMessageId(messageId);
        message.setMessageStat(MessageStat.RECEIVED);
        message.setMetadata(contact_number);
        message.setToId(toId);
        message.setChatId(fromId);

        message.setDownloadUploadStat(DownloadUploadStat.DEFAULT);

//        List<String>item = new ArrayList<>();
//        item.add(contact_number);
//        ArrayList<PhoneNumber> numbers = (ArrayList) item;
////        Log.d("MainActivityLL", "Contact numbers  ==  ==   " + numbers.get(0).getNumber());
//        RealmContact realmContact = new RealmContact(contact_name, numbers);
//
//
//        message.setContact(realmContact);


        if (reply_id.equalsIgnoreCase("NA")) {

        } else {

            RealmHelper.getInstance().refresh();
            Message quotedMessage = RealmHelper.getInstance().getMessage(reply_id, fromId);
            if (quotedMessage != null) {
                message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
//            Log.d("WalletRecive","message =    "+quotedMessage.toString());
//            Log.d("WalletRecive","messageToQuotedMessage =    "+QuotedMessage.messageToQuotedMessage(quotedMessage));
            }
        }


        Log.d("WalletRecive", "message =    " + message.toString());
        RealmHelper.getInstance().saveObjectToRealm(message);

        //save chat if this the first message in this chat
        RealmHelper.getInstance().saveUnreadMessage("123", "1");
//        newMessageHandler.handleNewMessage(phone, message);


        RealmHelper.getInstance().saveChatIfNotExists(message, user);


    }


    private void handleNewMessageLocation(String sender, String latitude, String longitude, String address, String chat_type, String message_id, String reply_id) {
        final int random = new Random().nextInt(61) + 20;
        String messageId = message_id;
//        user = users;

        User user = new User();
        Log.d("CheckReciveMessage", "id =  " + user.getUid());
        String phone = "8840149029";
        String content = address;

        String timestamp = String.valueOf(new Date().getTime());
        int type = Integer.valueOf(chat_type);
        String fromId = sender;
        String toId = user.getUid();
//        String metadata = attribute;


        Message message = new Message();
        message.setSeen(true);
        message.setContent(content);
        message.setFromPhone(phone);
        message.setTimestamp(timestamp);
        message.setFromId(fromId);
        message.setType(type);
        message.setForwarded(false);
        message.setMessageId(messageId);
        message.setMetadata(content);
        message.setToId(toId);
        message.setMessageStat(MessageStat.RECEIVED);
        message.setChatId(fromId);
        message.setDownloadUploadStat(DownloadUploadStat.DEFAULT);


        RealmLocation location = new RealmLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), address, "");
        message.setLocation(location);


        if (reply_id.equalsIgnoreCase("NA")) {

        } else {

            RealmHelper.getInstance().refresh();
            Message quotedMessage = RealmHelper.getInstance().getMessage(reply_id, fromId);
            if (quotedMessage != null) {
                message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
//            Log.d("WalletRecive","message =    "+quotedMessage.toString());
//            Log.d("WalletRecive","messageToQuotedMessage =    "+QuotedMessage.messageToQuotedMessage(quotedMessage));
            }
        }


        Log.d("WalletRecive", "message =    " + message.toString());
        RealmHelper.getInstance().saveObjectToRealm(message);

        //save chat if this the first message in this chat
        RealmHelper.getInstance().saveUnreadMessage("123", "1");
//        newMessageHandler.handleNewMessage(phone, message);

        RealmHelper.getInstance().saveChatIfNotExists(message, user);


    }

    private void handleNewMessageImage(String sender, String chat_message, String chat_type, String attribute, String path, String message_id, String reply_id) {
        final int random = new Random().nextInt(61) + 20;
        String messageId = message_id;
//        user = users;

        User user = new User();
        Log.i("ChatsendImageKnow", "id =  " + user.getUid());
        String phone = "8840149029";
        String content = chat_message;

        String timestamp = String.valueOf(new Date().getTime());
        int type = Integer.valueOf(chat_type);
        String fromId = sender;
        String toId = user.getUid();
        String metadata = attribute;


        Log.i("ChatsendImageKnow", "handleNewMessageImage    reply_id   =    " + reply_id);

        Message message = new Message();
        message.setSeen(true);
        message.setContent(content);
        message.setFromPhone(phone);
        message.setTimestamp(timestamp);
        message.setFromId(fromId);
        message.setType(type);
        message.setMessageId(messageId);
        message.setMetadata(metadata);
        message.setForwarded(false);
        message.setMessageStat(MessageStat.RECEIVED);
        message.setToId(toId);
        message.setChatId(fromId);
        message.setDownloadUploadStat(DownloadUploadStat.SUCCESS);
        File file = DirManager.generateFile(type);

        if (type == MessageType.RECEIVED_IMAGE) {
            message.setThumb(content);
//
//            BitmapUtils.compressImage(path, file);
//            String filePath = file.getPath();
            message.setLocalPath(path);
        } else if (type == MessageType.RECEIVED_AUDIO) {
            message.setLocalPath(path);
        }

        if (reply_id.equalsIgnoreCase("NA")) {

        } else {

            RealmHelper.getInstance().refresh();
            Message quotedMessage = RealmHelper.getInstance().getMessage(reply_id, fromId);
            if (quotedMessage != null) {
                message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
//            Log.d("WalletRecive","message =    "+quotedMessage.toString());
//            Log.d("WalletRecive","messageToQuotedMessage =    "+QuotedMessage.messageToQuotedMessage(quotedMessage));
            }
        }


//        Message quotedMessage = RealmHelper.getInstance().getMessage(messageId, fromId);
//        if (quotedMessage != null){
//            message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
////            Log.d("WalletRecive","message =    "+quotedMessage.toString());
////            Log.d("WalletRecive","messageToQuotedMessage =    "+QuotedMessage.messageToQuotedMessage(quotedMessage));
//        }


        Log.i("ChatsendImageKnow", "message =    " + message.toString());
        RealmHelper.getInstance().saveObjectToRealm(message);

        //save chat if this the first message in this chat
        RealmHelper.getInstance().saveUnreadMessage("123", "1");
//        newMessageHandler.handleNewMessage(phone, message);


        RealmHelper.getInstance().saveChatIfNotExists(message, user);


    }


    private void handleNewMessageVideo(String sender, String chat_message, String chat_type, String attribute, String path, String message_id, String reply_id) {
        final int random = new Random().nextInt(61) + 20;
        String messageId = message_id;
//        user = users;

        User user = new User();
        Log.d("CheckReciveMessage", "id =  " + user.getUid());
        String phone = "8840149029";
        String content = chat_message;


        String timestamp = String.valueOf(new Date().getTime());
        int type = Integer.valueOf(chat_type);
        String fromId = sender;
        String toId = user.getUid();
        String metadata = attribute;


        Message message = new Message();
        message.setSeen(true);
        message.setContent(content);
        message.setFromPhone(phone);
        message.setTimestamp(timestamp);
        message.setVideoThumb(content);
        message.setThumb(content);
        message.setForwarded(false);
        message.setFromId(fromId);
        message.setType(type);
        message.setMessageId(messageId);
        message.setMetadata(metadata);
        message.setMessageStat(MessageStat.RECEIVED);
        message.setToId(toId);
        message.setChatId(fromId);
        message.setDownloadUploadStat(DownloadUploadStat.SUCCESS);


        if (type == MessageType.RECEIVED_VIDEO) {


            File file = new File(path);
            //get video size
            String videoSize = Util.getFileSizeFromLong(file.length(), true);


//            Bitmap videoThumbBitmap = BitmapUtils.getThumbnailFromVideo(path);
//            //generate blurred thumb to send it to other user
//            String thumb = BitmapUtils.decodeImage(videoThumbBitmap);
//            //generate normal video thumb without blur to show it in recyclerView
//            String videoThumb = BitmapUtils.generateVideoThumbAsBase64(videoThumbBitmap);


//            message.setThumb(content);
//            message.setLocalPath(path);


            message.setLocalPath(path);
            message.setThumb(content);
//            message.setVideoThumb(videoThumb);
            message.setMetadata(videoSize);
        }

        if (reply_id.equalsIgnoreCase("NA")) {

        } else {

            RealmHelper.getInstance().refresh();
            Message quotedMessage = RealmHelper.getInstance().getMessage(reply_id, fromId);
            if (quotedMessage != null) {
                message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
//            Log.d("WalletRecive","message =    "+quotedMessage.toString());
//            Log.d("WalletRecive","messageToQuotedMessage =    "+QuotedMessage.messageToQuotedMessage(quotedMessage));
            }
        }


        Log.d("WalletRecive", "message =    " + message.toString());
        RealmHelper.getInstance().saveObjectToRealm(message);

        //save chat if this the first message in this chat
        RealmHelper.getInstance().saveUnreadMessage("123", "1");
//        newMessageHandler.handleNewMessage(phone, message);

        RealmHelper.getInstance().saveChatIfNotExists(message, user);


    }


    private void handleNewMessageDoc(String sender, String chat_type, String path, String reply_id) {
        final int random = new Random().nextInt(61) + 20;

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        RandomString randomString = new RandomString();
        String image_name = ts + "_" + randomString.nextString();
        String messageId = image_name;


        User user = new User();
        Log.d("CheckReciveMessage", "id =  " + user.getUid());
        String phone = "8840149029";


        String timestamp = String.valueOf(new Date().getTime());
        int type = Integer.valueOf(chat_type);
        String fromId = sender;
        String toId = user.getUid();
        final String fileName = Util.getFileNameFromPath(path);

        File file = new File(path);
        String fileSize = Util.getFileSizeFromLong(file.length(), true);

        Message message = new Message();
//        message.setSeen(true);
//        message.setLocalPath(path);
//        message.setFromPhone(phone);
//        message.setTimestamp(timestamp);
//        message.setFromId(fromId);
//        message.setType(type);
//        message.setMessageId(messageId);
//        message.setMetadata(fileName);
//        message.setToId(toId);
//        message.setChatId(fromId);
//        message.setFileSize(fileSize);
//        message.setDownloadUploadStat(DownloadUploadStat.SUCCESS);


        message.setLocalPath(path);


        message.setType(type);
        message.setFromId(FireManager.getUid());
        message.setToId(toId);
        message.setTimestamp(String.valueOf(new Date().getTime()));
        message.setChatId(fromId);
        message.setMessageStat(MessageStat.RECEIVED);
        message.setMessageId(messageId);
        message.setForwarded(false);
        message.setMetadata(fileName);
        message.setFileSize(fileSize);
        message.setDownloadUploadStat(DownloadUploadStat.SUCCESS);


        if (reply_id.equalsIgnoreCase("NA")) {

        } else {
            RealmHelper.getInstance().refresh();
            Message quotedMessage = RealmHelper.getInstance().getMessage(reply_id, fromId);
            if (quotedMessage != null) {
                message.setQuotedMessage(QuotedMessage.messageToQuotedMessage(quotedMessage));
//            Log.d("WalletRecive","message =    "+quotedMessage.toString());
//            Log.d("WalletRecive","messageToQuotedMessage =    "+QuotedMessage.messageToQuotedMessage(quotedMessage));
            }
        }


        Log.d("WalletRecive", "message =    " + message.toString());
        RealmHelper.getInstance().saveObjectToRealm(message);

        //save chat if this the first message in this chat
        RealmHelper.getInstance().saveUnreadMessage("123", "1");
//        newMessageHandler.handleNewMessage(phone, message);

        RealmHelper.getInstance().saveChatIfNotExists(message, user);


    }
}
