package com.gpslab.kaun.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.model.CallerDetails;
import com.gpslab.kaun.model.GetMessageTable;
import com.gpslab.kaun.model.GetSMSTable;
import com.gpslab.kaun.model.GetSpamCall;
import com.gpslab.kaun.model.GetSpamMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MyDbHandler extends SQLiteOpenHelper {
    int total = 0;
    // 1. आप को jetne bhi culom bnane hai Unke variable declear krke chalte hai
    private static String colId = "id";
    private static String mobile_one = "mobile_one";
    private static String mobile_two = "mobile_two";
    private static String email = "email";
    private static String photo = "photo";
    private static String name = "name";
    private static String firstTable = "first_table";




    ///// sms
    private static String sms_key = "sms_key";
    private static String sms_url = "sms_url";
    private static String sms_name = "sms_name";
    private static String sms_tableT = "sms_table";
    ///// sms


    ///// spam message
    private static String spam_msg_number = "spam_msg_number";
    private static String spam_msg_name = "spam_msg_name";
    private static String spam_msg_count = "spam_msg_count";
    private static String spam_msg_table = "spam_msg_table";
    ///// spam message


    ///// spam call
    private static String spam_call_number = "spam_call_number";
    private static String spam_call_name = "spam_call_name";
    private static String spam_call_count = "spam_call_count";
    private static String spam_call_usally_calls = "spam_call_usally_calls";
    private static String spam_call_call_activity = "spam_call_call_activity";
    private static String spam_cal_table = "spam_call_table";
    ///// spam call


    private static String index = "indexnew";

    private static String caller_name_id = "caller_name_id";

    private static String caller_name = "caller_name";
    private static String caller_no = "caller_no";
    private static String caller_read = "caller_read";
    private static String call_type = "call_type";
    private static String sim_type = "sim_type";
    private static String date = "date";
    private static String duration = "duration";
    private static String calllogTable = "call_log_table";
    private static String CalllogT = "call_log_new_table";


    // log database start
    private static String c_id = "c_id";
    private static String fname = "fname";
    private static String lname = "lname";
    private static String emails = "email";
    private static String mobile = "mobile";

    private static String profile_image = "profile_image";
    private static String gender = "gender";
    private static String is_active = "is_active";
    private static String provider = "provider";
    private static String location = "location";

    private static String zone = "zone";
    private static String is_spam = "is_spam";
    private static String address = "address";
    private static String company_name = "company_name";
    private static String job_title = "job_title";

    private static String company_email = "company_email";
    private static String facebook = "facebook";
    private static String twitter = "twitter";
    private static String website = "website";
    private static String tags = "tags";


    private static String badges = "badges";
    private static String score = "score";
    private static String spanCount = "spanCount";
    private static String created_at = "created_at";
    private static String checkcalllogTable = "check_call_log_table";
    private static String messageTable = "message_table";

    private static String msg_id = "msg_id";
    private static String msg_read = "msg_read";
    private static String msg_address = "msg_address";
    private static String msg_body = "msg_body";
    private static String msg_date = "msg_date";
    private static String msg_photo = "msg_photo";
    private static String msg_company_name = "msg_company_name";









    private static String contacts_id = "contacts_id";
    private static String contacts_image = "contacts_image";
    private static String contacts_status = "contacts_status";
    private static String contacts_name = "contacts_name";
    private static String contacts_chat = "contacts_chat";

    private static String contacts_firstTable = "contacts_firstTable";





// log database start


    // 1. इसमें हमें context pass करना होगा
    // 2. database का नाम।
    // 3. Cursorfactory में null pass करना होगा।
    // 4. database का version देना होगा।
    // जैसे आप ये सब information pass करेगे data base create कर देगा।
    public MyDbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 1. onCreate method के आन्दर database create करने का code लिखना है।
    // 2. SQLiteDatabase एक class है।
    // 3. SQLiteDatabase class create database को reprjent करती है।

    @Override
    public void onCreate(SQLiteDatabase db) {
        // जैसे आप का oncreate method चलेगा तो हम table को create करेगे।

        String create_user = "CREATE TABLE " + firstTable + "(" + colId + " INTEGER PRIMARY KEY," + name + " TEXT ," +
                caller_name_id + " TEXT ," +
                mobile_one + " TEXT ," +
                mobile_two + " TEXT ," +
                email + " TEXT ," +
                photo + " TEXT )";


        String contacts_user = "CREATE TABLE " + contacts_firstTable + "(" + colId + " INTEGER PRIMARY KEY," + contacts_id + " TEXT ," +
                contacts_image + " TEXT ," +
                contacts_status + " TEXT ," +
                contacts_name + " TEXT )";

        String contacts_user_sms = "CREATE TABLE " + sms_tableT + "(" + colId + " INTEGER PRIMARY KEY," + sms_key + " TEXT ," +
                sms_url + " TEXT ," +
                sms_name + " TEXT )";



        String contacts_spam_sms = "CREATE TABLE " + spam_msg_table + "(" + colId + " INTEGER PRIMARY KEY," + spam_msg_number + " TEXT ," +
                spam_msg_name + " TEXT ," +
                spam_msg_count + " TEXT )";


        String contacts_spam_call = "CREATE TABLE " + spam_cal_table + "(" + colId + " INTEGER PRIMARY KEY," + spam_call_number + " TEXT ," +
                spam_call_name + " TEXT ," +
                spam_call_count + " TEXT ," +
                spam_call_usally_calls + " TEXT ," +
                spam_call_call_activity + " TEXT )";

        String create_user_message = "CREATE TABLE " + messageTable + "(" + colId + " INTEGER PRIMARY KEY," + msg_id + " TEXT ," +
                msg_read + " TEXT ," +
                msg_address + " TEXT ," +
                msg_body + " TEXT ," +
                msg_date + " TEXT ," +
                msg_photo + " TEXT ," +
                msg_company_name + " TEXT )";





        String create_call_log = "CREATE TABLE " + CalllogT + "(" + colId + " INTEGER PRIMARY KEY," +
                caller_name + " TEXT NOT NULL," +
                index + " TEXT NOT NULL," +
                caller_no + " TEXT NOT NULL," +
                call_type + " TEXT NOT NULL," +
                sim_type + " TEXT NOT NULL," +
                date + " TEXT NOT NULL," +
                profile_image + " TEXT NOT NULL," +
                caller_read + " TEXT NOT NULL," +
                duration + " TEXT NOT NULL)";


//        String create_call_log_check = "CREATE TABLE " + checkcalllogTable + "(" + colId + " INTEGER PRIMARY KEY," +
//                c_id + " TEXT NOT NULL," +
//                fname + " TEXT NOT NULL," +
//                lname + " TEXT NOT NULL," +
//                email + " TEXT NOT NULL," +
//                mobile + " TEXT NOT NULL," +
//                profile_image + " TEXT NOT NULL," +
//                gender + " TEXT NOT NULL," +
//                is_active + " TEXT NOT NULL," +
//                provider + " TEXT NOT NULL," +
//                location + " TEXT NOT NULL," +
//                zone + " TEXT NOT NULL," +
//                is_spam + " TEXT NOT NULL," +
//                address + " TEXT NOT NULL," +
//                company_name + " TEXT NOT NULL," +
//                job_title + " TEXT NOT NULL," +
//                company_email + " TEXT NOT NULL," +
//                facebook + " TEXT NOT NULL," +
//                twitter + " TEXT NOT NULL," +
//                website + " TEXT NOT NULL," +
//                tags + " TEXT NOT NULL," +
//                badges + " TEXT NOT NULL," +
//                score + " TEXT NOT NULL," +
//                spanCount + " TEXT NOT NULL," +
//                created_at + " TEXT NOT NULL)";


        db.execSQL(contacts_user_sms);

        //
        db.execSQL(contacts_user);

        db.execSQL(contacts_spam_sms);
        db.execSQL(contacts_spam_call);

        //
        db.execSQL(create_user);
        //
        db.execSQL(create_user_message);

        //
        db.execSQL(create_call_log);
    }

    // 1. onUpgrade method के अन्दर अगर आप को database को upgrade करने का code लिखना होगा।
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean checkrow(String Mobile) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM first_table WHERE mobile_one=?";

        // Add the String you are searching by here.
        // Put it in an array to avoid an unrecognized token error
        Cursor cursor = db.rawQuery(selectString, new String[]{Mobile});

        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            //here, count is records found


            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    //// check row present or not
    public String mobilesearch;

    public String getSingleUserInfo(String mobile) {

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + firstTable + " WHERE " + mobile_one + "=" + mobile, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            mobilesearch = cursor.getString(cursor.getColumnIndex(name));
        }

        return mobilesearch;

    }

    public int firstqtyprice() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(qty) as Total FROM first_table", null);

        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }


        return total;
    }

    public int firsttotalprice() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(totalprice) as Total FROM first_table", null);

        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("Total"));
        }


        return total;
    }

    public void add_cities(String datenew) {
        SQLiteDatabase database = this.getWritableDatabase();
        String sql = "INSERT INTO " + firstTable + " VALUE " + datenew;
        SQLiteStatement statement = database.compileStatement(sql);
        database.beginTransaction();

    }

    public int insertUser(FirstTableData user) {
        int i = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(name, user.getName());
        contentValues.put(caller_name_id, user.getId());
        contentValues.put(mobile_one, user.getMobileone());

        contentValues.put(mobile_two, user.getMobiletwo());
        contentValues.put(email, user.getEmail());
        contentValues.put(photo, user.getPhoto());
        Log.d("walletwallet", "inseart name = " + user.getName());
        Log.d("walletwallet", "inseart Mobile = " + user.getMobileone());
        Log.d("walletwallet", "inseart Mobile two = " + user.getMobiletwo());

        sqLiteDatabase.insert(firstTable, null, contentValues);
        i = 1;
        return i;
    }

    public void InsertMessageSQLiteDatabase(String datadata) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String SQLiteDataBaseQueryHolder = "INSERT INTO message_table(`msg_id`,`msg_read`,`msg_address`,`msg_body`,`msg_date`,`msg_photo`,`msg_company_name`) VALUES " + datadata;
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + SQLiteDataBaseQueryHolder);
//        sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);
        SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
        statement.execute();


    }



    public void InsertSMSDatabase(String datadata) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String SQLiteDataBaseQueryHolder = "INSERT INTO sms_table(`sms_key`,`sms_url`,`sms_name`) VALUES " + datadata;
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + SQLiteDataBaseQueryHolder);

        SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
        statement.execute();


    }


    public void InsertSpamMessage(String datadata) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String SQLiteDataBaseQueryHolder = "INSERT INTO spam_msg_table(`spam_msg_number`,`spam_msg_name`,`spam_msg_count`) VALUES " + datadata;
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + SQLiteDataBaseQueryHolder);

        SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
        statement.execute();


    }



    public void InsertSpamCall(String datadata) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String SQLiteDataBaseQueryHolder = "INSERT INTO spam_call_table(`spam_call_number`,`spam_call_name`,`spam_call_count`,`spam_call_usally_calls`,`spam_call_call_activity`) VALUES " + datadata;
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + SQLiteDataBaseQueryHolder);

        SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
        statement.execute();


    }

    public ArrayList<GetSMSTable> viewSMSNew() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + sms_tableT;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetSMSTable> arrayList = new ArrayList<GetSMSTable>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetSMSTable user = new GetSMSTable();
                user.setIds(cursor.getString(0));
                user.setSms_key(cursor.getString(1));
                user.setSms_url(cursor.getString(2));
                user.setSms_name(cursor.getString(3));

                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }


    public void InsertContactSQLiteDatabase(String datadata) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String SQLiteDataBaseQueryHolder = "INSERT INTO contacts_firstTable(`contacts_id`,`contacts_image`,`contacts_status`,`contacts_name`) VALUES " + datadata;
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + SQLiteDataBaseQueryHolder);

        SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
        statement.execute();


    }








    public void InsertDataIntoSQLiteDatabase(String datadata) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String SQLiteDataBaseQueryHolder = "INSERT INTO first_table(`name`,`caller_name_id`,`mobile_one`,`mobile_two`,`email`,`photo`) VALUES " + datadata;
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + SQLiteDataBaseQueryHolder);
//        sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);
        SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
        statement.execute();


    }



    public void InsertLogCallerDataIntoSQLiteDatabase(String datadata) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String SQLiteDataBaseQueryHolder = "INSERT INTO call_log_new_table(`caller_name`,`indexnew`,`caller_no`,`call_type`,`sim_type`,`date`,`profile_image`,`caller_read`,`duration`) VALUES " + datadata;
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + SQLiteDataBaseQueryHolder);
//        sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);
        SQLiteStatement statement = sqLiteDatabase.compileStatement(SQLiteDataBaseQueryHolder);
        statement.execute();


    }

    public int insertcalllog(GetCallLogTable user) {
//        Handler handler = new Handler();
//        handler.postDelayed()
        int i = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(caller_name, user.getName());
        contentValues.put(caller_no, user.getNop());
        contentValues.put(call_type, user.getCall_type());
        contentValues.put(sim_type, user.getSim_type());
        contentValues.put(date, user.getDate());
        contentValues.put(profile_image, user.getImage());
        contentValues.put(duration, user.getDuration());

        Log.d("walletwallet", "inseart getCall_type = " + contentValues.toString());


        sqLiteDatabase.insert(CalllogT, null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        i = 1;
        return i;
    }


    public int insertCaller(CallerDetails callerDetails) {
        int i = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c_id, callerDetails.getId());
        contentValues.put(fname, callerDetails.getFname());
        contentValues.put(lname, callerDetails.getLname());
        contentValues.put(email, callerDetails.getEmail());
        contentValues.put(mobile, callerDetails.getMobile());
        contentValues.put(profile_image, callerDetails.getProfile_image());

        contentValues.put(gender, callerDetails.getGender());
        contentValues.put(is_active, callerDetails.getIs_active());
        contentValues.put(provider, callerDetails.getProvider());
        contentValues.put(location, callerDetails.getLocation());
        contentValues.put(zone, callerDetails.getZone());
        contentValues.put(is_spam, callerDetails.getIs_spam());


        contentValues.put(address, callerDetails.getAddress());
        contentValues.put(company_name, callerDetails.getCompany_name());
        contentValues.put(job_title, callerDetails.getJob_title());
        contentValues.put(company_email, callerDetails.getCompany_email());
        contentValues.put(facebook, callerDetails.getFacebook());
        contentValues.put(twitter, callerDetails.getTwitter());


        contentValues.put(website, callerDetails.getWebsite());
        contentValues.put(tags, callerDetails.getTags());
        contentValues.put(badges, callerDetails.getBadges());
        contentValues.put(score, callerDetails.getScore());
        contentValues.put(spanCount, callerDetails.getSpanCount());
        contentValues.put(created_at, callerDetails.getCreated_at());

        sqLiteDatabase.insert(calllogTable, null, contentValues);
        i = 1;
        return i;
    }

    //// data को delete करने के लिए
    /// आप को जिस user का data delete करना है उसकी id pass करेगे।
    public int deleteAll() {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from " + firstTable);
        sqLiteDatabase.close();


        i = 1;
        return i;
    }



    public int deleteSMSAll() {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from " + sms_tableT);
        sqLiteDatabase.close();


        i = 1;
        return i;
    }



    public int deleteSpamSMSAll() {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from " + spam_msg_table);
        sqLiteDatabase.close();


        i = 1;
        return i;
    }



    public int deleteSpamCallAll() {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from " + spam_cal_table);
        sqLiteDatabase.close();


        i = 1;
        return i;
    }


    //// data को delete करने के लिए
    /// आप को जिस user का data delete करना है उसकी id pass करेगे।
    public int deletecallerAll() {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from " + calllogTable);
        sqLiteDatabase.close();


        i = 1;
        return i;
    }


    public int deletecalllogAll() {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from " + CalllogT);
        sqLiteDatabase.close();


        i = 1;
        return i;
    }


    public int deletemessageAll() {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.execSQL("delete from " + messageTable);
        sqLiteDatabase.close();


        i = 1;
        return i;
    }

    public int deletecontacts(){
        int i=0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+contacts_firstTable);

        sqLiteDatabase.close();

        i=1;
        return i;
    }

    //// data को delete करने के लिए
    /// आप को जिस user का data delete करना है उसकी id pass करेगे।
    public int deleteUser(String id) {
        int i = 0;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        sqLiteDatabase.delete(firstTable, colId + "=?", new String[]{id});
        sqLiteDatabase.close();


        i = 1;
        return i;
    }

    /// log table count
    public long getCallLogCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, CalllogT);
        db.close();
        return count;
    }


    public long getMessageCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, messageTable);
        db.close();
        return count;
    }


    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, firstTable);
        db.close();
        return count;
    }


    /// इसमें जिस user की details देखनी है उसकी id को pass ना करा कर एक Collection return करा सकते है।
    // तो हम एक Array List बनायेगे
    // और Array List को Genric बनादेते है।
    public ArrayList<FirstTableData> viewUser() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + firstTable;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<FirstTableData> arrayList = new ArrayList<FirstTableData>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                FirstTableData user = new FirstTableData();

                user.setName(cursor.getString(1));
                user.setId(cursor.getString(2));
                user.setMobileone(cursor.getString(3));
                user.setMobiletwo(cursor.getString(4));
                user.setEmail(cursor.getString(5));
                user.setPhoto(cursor.getString(6));

                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }

    /// इसमें जिस user की details देखनी है उसकी id को pass ना करा कर एक Collection return करा सकते है।
    // तो हम एक Array List बनायेगे
    // और Array List को Genric बनादेते है।
    public ArrayList<CallerDetails> viewCallLogs() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + firstTable;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<CallerDetails> arrayList = new ArrayList<CallerDetails>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                CallerDetails user = new CallerDetails();
                user.setId(cursor.getString(1));
                user.setFname(cursor.getString(2));
                user.setLname(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                user.setMobile(cursor.getString(5));
                user.setProfile_image(cursor.getString(6));
                user.setGender(cursor.getString(7));


                user.setIs_active(cursor.getString(8));
                user.setProvider(cursor.getString(9));
                user.setLocation(cursor.getString(10));
                user.setZone(cursor.getString(11));
                user.setIs_spam(cursor.getString(12));
                user.setAddress(cursor.getString(13));
                user.setCompany_name(cursor.getString(14));


                user.setJob_title(cursor.getString(15));
                user.setCompany_email(cursor.getString(16));
                user.setFacebook(cursor.getString(17));
                user.setTwitter(cursor.getString(18));
                user.setWebsite(cursor.getString(19));
                user.setTags(cursor.getString(20));
                user.setBadges(cursor.getString(21));


                user.setScore(cursor.getString(22));
                user.setSpanCount(cursor.getString(23));
                user.setCreated_at(cursor.getString(24));


                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }

    //// filter message

    public ArrayList<GetMessageTable> viewfiltermessageRow() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT DISTINCT " + msg_address + " FROM " + messageTable;
//        String data = "SELECT * FROM "+ CalllogT +" ORDER BY ROWID ASC LIMIT 1";

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetMessageTable> arrayListnew = new ArrayList<GetMessageTable>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
//                GetCallLogTable user = new GetCallLogTable();
//                user.setId(cursor.getString(0));
//                user.setName(cursor.getString(1));
                String MassageAddress = cursor.getString(0);


                String stst = "SELECT * FROM `message_table` WHERE `msg_address` = " + MassageAddress + " ORDER BY id ASC LIMIT 1";

                SQLiteDatabase sqLiteDatabasenew = this.getWritableDatabase();
                Cursor cursornew = sqLiteDatabasenew.rawQuery(stst, null);

                if (cursornew.moveToFirst()) {
                    do {
                        GetMessageTable user = new GetMessageTable();
                        user.setId(cursor.getString(0));
                        user.setMsg_id(cursor.getString(1));
                        user.setMsg_read(cursor.getString(2));
                        user.setMsg_address(cursor.getString(3));
                        user.setMsg_body(cursor.getString(4));
                        user.setMsg_date(cursor.getString(5));
                        user.setMsg_photo(cursor.getString(6));
                        user.setMsg_company_name(cursor.getString(7));

                        arrayListnew.add(user);
                    } while (cursornew.moveToNext());
                }
//                user.setNop(cursor.getString(0));
//                user.setCall_type(cursor.getString(3));
//                user.setSim_type(cursor.getString(4));
//                user.setDate(cursor.getString(5));
//                user.setImage(cursor.getString(6));
//                user.setDuration(cursor.getString(7));


//                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayListnew;
    }



    ///// Call Log View
    public ArrayList<GetMessageTable> viewMessageNew() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + messageTable;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetMessageTable> arrayList = new ArrayList<GetMessageTable>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetMessageTable user = new GetMessageTable();
                user.setId(cursor.getString(0));
                user.setMsg_id(cursor.getString(1));
                user.setMsg_read(cursor.getString(2));
                user.setMsg_address(cursor.getString(3));
                user.setMsg_body(cursor.getString(4));
                user.setMsg_date(cursor.getString(5));
                user.setMsg_photo(cursor.getString(6));
                user.setMsg_company_name(cursor.getString(7));


                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }




    ///// Call Log View
    public ArrayList<FirstTableData> viewContact() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM first_table ";
        Log.i("GetContactswalletimages", "GetContacts query = " + st);
        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।
        ArrayList<FirstTableData> arrayList = new ArrayList<FirstTableData>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                FirstTableData user = new FirstTableData();

                user.setName(cursor.getString(1));
                user.setId(cursor.getString(2));
                user.setMobileone(cursor.getString(3));
                user.setMobiletwo(cursor.getString(4));
                user.setEmail(cursor.getString(5));
                user.setPhoto(cursor.getString(6));

                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }



    public ArrayList<GetMessageTable> viewMessageNewNew() throws UnsupportedEncodingException {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        String otp = "verification code";
        byte[] datamobileone = otp.getBytes("UTF-8");
        String base64mobileone = Base64.encodeToString(datamobileone, Base64.DEFAULT);
        Log.d("Toseefbhai","Print = "+base64mobileone);
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + messageTable +" WHERE msg_body LIKE '%"+base64mobileone+"%'";

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetMessageTable> arrayList = new ArrayList<GetMessageTable>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetMessageTable user = new GetMessageTable();
                user.setId(cursor.getString(0));
                user.setMsg_id(cursor.getString(1));
                user.setMsg_read(cursor.getString(2));
                user.setMsg_address(cursor.getString(3));
                user.setMsg_body(cursor.getString(4));
                user.setMsg_date(cursor.getString(5));
                user.setMsg_photo(cursor.getString(6));
                user.setMsg_company_name(cursor.getString(7));


                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }

    ///// Call Log View
    public ArrayList<GetCallLogTable> viewCallLogsNew() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + CalllogT;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetCallLogTable> arrayList = new ArrayList<GetCallLogTable>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetCallLogTable user = new GetCallLogTable();
                user.setId(cursor.getString(0));
                user.setName(cursor.getString(1));
                user.setNop(cursor.getString(2));
                user.setCall_type(cursor.getString(3));
                user.setSim_type(cursor.getString(4));
                user.setDate(cursor.getString(5));
                user.setImage(cursor.getString(6));
                user.setDuration(cursor.getString(7));


                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }



    public ArrayList<GetSpamMessage> viewSpamMsg() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + spam_msg_table;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetSpamMessage> arrayList = new ArrayList<GetSpamMessage>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetSpamMessage user = new GetSpamMessage();
                user.setSpam_msg_id(cursor.getString(0));
                user.setSpam_msg_number(cursor.getString(1));
                user.setSpam_msg_name(cursor.getString(2));
                user.setSpam_msg_count(cursor.getString(3));

                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }

    public ArrayList<GetSpamCall> viewSpamCall() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + spam_cal_table;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetSpamCall> arrayList = new ArrayList<GetSpamCall>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetSpamCall user = new GetSpamCall();
                user.setSpam_call_id(cursor.getString(0));
                user.setSpam_call_number(cursor.getString(1));
                user.setSpam_call_name(cursor.getString(2));
                user.setSpam_call_count(cursor.getString(3));
                user.setSpam_call_usally_calls(cursor.getString(4));
                user.setSpam_call_call_activity(cursor.getString(5));

                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }
    ///// Call Log View
    public ArrayList<GetChatContactList> viewChatContact() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM " + contacts_firstTable;

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetChatContactList> arrayList = new ArrayList<GetChatContactList>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetChatContactList user = new GetChatContactList();
                user.setId(cursor.getString(0));
                user.setContacts_id(cursor.getString(1));
                user.setContacts_image(cursor.getString(2));
                user.setContacts_status(cursor.getString(3));
                user.setContacts_name(cursor.getString(4));


                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }


    ///// Call Log View
    public ArrayList<GetCallLogTable> viewCallLogsFirstRow() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।

        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT * FROM call_log_new_table Group BY caller_no ORDER BY date ASC";
//        String data = "SELECT * FROM "+ CalllogT +" ORDER BY ROWID ASC LIMIT 1";

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);

        Log.d("walletwallet", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetCallLogTable> arrayList = new ArrayList<GetCallLogTable>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
                GetCallLogTable user = new GetCallLogTable();
                user.setId(cursor.getString(0));
                user.setName(cursor.getString(1));
                user.setIndex(cursor.getString(2));
                user.setNop(cursor.getString(3));
                user.setCall_type(cursor.getString(4));
                user.setSim_type(cursor.getString(5));
                user.setDate(cursor.getString(6));
                user.setImage(cursor.getString(7));
                user.setCaller_read(cursor.getString(8));
                user.setDuration(cursor.getString(9));


                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayList;
    }


    public ArrayList<GetCallLogTable> viewCallalltRow(String Mobile) {



        ArrayList<GetCallLogTable> arrayListnew = new ArrayList<GetCallLogTable>();


        String stst = "SELECT * FROM `call_log_new_table` WHERE `caller_no` = " + Mobile + " ORDER BY id ASC";

        SQLiteDatabase sqLiteDatabasenew = this.getWritableDatabase();
        Cursor cursornew = sqLiteDatabasenew.rawQuery(stst, null);

        if (cursornew.moveToFirst()) {
            do {
                GetCallLogTable user = new GetCallLogTable();
                user.setId(cursornew.getString(0));
                user.setName(cursornew.getString(1));
                user.setIndex(cursornew.getString(2));
                user.setNop(cursornew.getString(3));
                user.setCall_type(cursornew.getString(4));
                user.setSim_type(cursornew.getString(5));
                user.setDate(cursornew.getString(6));
                user.setImage(cursornew.getString(7));
                user.setCaller_read(cursornew.getString(8));
                user.setDuration(cursornew.getString(9));

                arrayListnew.add(user);
            } while (cursornew.moveToNext());
        }
//                user.setNop(cursor.getString(0));
//                user.setCall_type(cursor.getString(3));
//                user.setSim_type(cursor.getString(4));
//                user.setDate(cursor.getString(5));
//                user.setImage(cursor.getString(6));
//                user.setDuration(cursor.getString(7));


//                arrayList.add(user);


        return arrayListnew;
    }

    public ArrayList<GetMessageTable> viewmessageRow(String Mobile) {



        ArrayList<GetMessageTable> arrayListnew = new ArrayList<GetMessageTable>();


        String stst = "SELECT * FROM `message_table` WHERE `msg_address` = " + Mobile + " ORDER BY id ASC";

        SQLiteDatabase sqLiteDatabasenew = this.getWritableDatabase();
        Cursor cursornew = sqLiteDatabasenew.rawQuery(stst, null);

        if (cursornew.moveToFirst()) {
            do {
                GetMessageTable user = new GetMessageTable();
                user.setId(cursornew.getString(0));
                user.setMsg_id(cursornew.getString(1));
                user.setMsg_read(cursornew.getString(2));
                user.setMsg_address(cursornew.getString(3));
                user.setMsg_body(cursornew.getString(4));
                user.setMsg_date(cursornew.getString(5));
                user.setMsg_photo(cursornew.getString(6));
                user.setMsg_company_name(cursornew.getString(7));

                arrayListnew.add(user);
            } while (cursornew.moveToNext());
        }
//                user.setNop(cursor.getString(0));
//                user.setCall_type(cursor.getString(3));
//                user.setSim_type(cursor.getString(4));
//                user.setDate(cursor.getString(5));
//                user.setImage(cursor.getString(6));
//                user.setDuration(cursor.getString(7));


//                arrayList.add(user);


        return arrayListnew;
    }


    /// Call Log Update


    public void view_update(final String ids){
        String st = "UPDATE " + CalllogT + " SET " + caller_read+"='1' WHERE "+colId+"=ids";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(st);
    }

    public boolean updateUser(GetCallLogTable user){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();





        ContentValues contentValues = new ContentValues();
        contentValues.put(colId ,user.getId());
        contentValues.put(caller_name ,user.getName());
        contentValues.put(index ,user.getIndex());
        contentValues.put(caller_no ,user.getNop());
        contentValues.put(call_type ,user.getCall_type());
        contentValues.put(sim_type ,user.getSim_type());

        contentValues.put(date ,user.getDate());
        contentValues.put(profile_image ,user.getImage());

        contentValues.put(caller_read ,user.getCaller_read());
        contentValues.put(duration ,user.getDuration());
        sqLiteDatabase.update(CalllogT,contentValues,colId+"=?",new String[]{user.getId()});
        return true;
    }
//    public boolean updateRowUser() {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//
//
//
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(call_type, "1");
//
//
//        sqLiteDatabase.update(CalllogT, contentValues, "call_type=?", new String[]{user.getId()});
//        return true;
//    }


    ///// Call Log View
    public ArrayList<GetCallLogTable> viewCallLogstRow() {

        // user data को view करने का सारा code यहाँ लिखा जायेगा।
        // तो इसके लिए हम एक Query बनायेगे।
        // तो SELECT query लिखनी होगी क्यो कि हमें सभी data उठाना है इस लिए * का प्रयोग करेगे।


        /// यदि आप किसी एक Spcefic User का data show कराना चाहते है तो आप viewUswer(String id) method में user की id pass करायेगे।
        // और इसी Query में आगे where की condicition लगायेगे।
        // Example:- String st = "SELECT * FROM"+key_table+ "WHERE"+ key_id = id;
        String st = "SELECT DISTINCT " + caller_no + " FROM " + CalllogT;
//        String data = "SELECT * FROM "+ CalllogT +" ORDER BY ROWID ASC LIMIT 1";

        // अब सारी की सारी information को किसी ना किसी ListView या Table में store करा सकते है। और सारा का सारा data उसमें मगा सकते है।
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        // इसका एक method है row query उसमें इसे exiqute कर सकते है।

        // rawQuery method जब सारी information निकाल कर लायेगा। तो उसे सभाल कर रखने के लिए एक Cursor का ऊपयेग करते है।
        Cursor cursor = sqLiteDatabase.rawQuery(st, null);
//        Log.d("YUYULuckyYUYTUNowkyY", "cursor = " + cursor);
        // अब cursor इस data को send करने के लिए Cursor Pointer का  प्रयोग करते है।
        // इस Cursor Pointer की Position minus( - )में होती ही।

        ArrayList<GetCallLogTable> arrayListnew = new ArrayList<GetCallLogTable>();

        /// अगर Cursor पहली Positiono पर पहुच गया है तो हम data निकाल सकते है।
        if (cursor.moveToFirst()) {

            do {
//                GetCallLogTable user = new GetCallLogTable();
//                user.setId(cursor.getString(0));
//                user.setName(cursor.getString(1));
                String Mobile = cursor.getString(0);
                Log.d("YUYULuckyYUYTUNowkyY", "Mobile = " + Mobile);

                String stst = "SELECT * FROM `call_log_new_table` WHERE `caller_no` = " + Mobile + " ORDER BY id ASC LIMIT 1";

                SQLiteDatabase sqLiteDatabasenew = this.getWritableDatabase();
                Cursor cursornew = sqLiteDatabasenew.rawQuery(stst, null);

                if (cursornew.moveToFirst()) {
                    do {
                        GetCallLogTable user = new GetCallLogTable();
                        user.setId(cursornew.getString(0));
                        user.setName(cursornew.getString(1));
                        user.setIndex(cursornew.getString(2));
                        user.setNop(cursornew.getString(3));
                        Log.d("YUYULuckyYUYTUNowkyY", "Mobile New = " + cursornew.getString(3));
                        user.setCall_type(cursornew.getString(4));
                        user.setSim_type(cursornew.getString(5));
                        user.setDate(cursornew.getString(6));
                        user.setImage(cursornew.getString(7));
                        user.setCaller_read(cursornew.getString(8));
                        user.setDuration(cursornew.getString(9));

                        arrayListnew.add(user);
                    } while (cursornew.moveToNext());
                }
//                user.setNop(cursor.getString(0));
//                user.setCall_type(cursor.getString(3));
//                user.setSim_type(cursor.getString(4));
//                user.setDate(cursor.getString(5));
//                user.setImage(cursor.getString(6));
//                user.setDuration(cursor.getString(7));


//                arrayList.add(user);
            } while (cursor.moveToNext());


        }


        return arrayListnew;
    }

    public ArrayList<GetCallLogTable> getAllCotacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CalllogT + " WHERE " + caller_no + " IN(SELECT DISTINCT " + caller_no + " FROM " + CalllogT + ")", null);
//        res.moveToFirst();
        ArrayList<GetCallLogTable> arrayList = new ArrayList<GetCallLogTable>();

        if (cursor.moveToFirst()) {

            do {
                GetCallLogTable user = new GetCallLogTable();
                user.setId(cursor.getString(0));
                user.setName(cursor.getString(1));
                user.setNop(cursor.getString(2));
                user.setCall_type(cursor.getString(3));
                user.setSim_type(cursor.getString(4));
                user.setDate(cursor.getString(5));
                user.setImage(cursor.getString(6));
                user.setDuration(cursor.getString(7));


                arrayList.add(user);
            } while (cursor.moveToNext());


        }
//        while (res.isAfterLast() == false) {
//            if ((res != null) && (res.getCount() > 0))
//                array_list.add(res.getString(res.getColumnIndex("countRecords")));
//            res.moveToNext();
//        }
        return arrayList;
    }


    public boolean updateUserxv(FirstTableData user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();





        ContentValues contentValues = new ContentValues();
        contentValues.put(name, user.getName());
        contentValues.put(mobile_one, user.getMobileone());
        contentValues.put(mobile_two, user.getMobiletwo());
        contentValues.put(email, user.getEmail());
        contentValues.put(photo, user.getPhoto());

        sqLiteDatabase.update(firstTable, contentValues, "productid=?", new String[]{user.getId()});
        return true;
    }




    public boolean updateContact(GetChatContactList user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();





        ContentValues contentValues = new ContentValues();
        contentValues.put(contacts_id, user.getContacts_id());
        contentValues.put(contacts_image, user.getContacts_image());
        contentValues.put(contacts_status, user.getContacts_status());
        contentValues.put(contacts_chat, user.getContacts_chat());
        contentValues.put(contacts_name, user.getContacts_name());

        sqLiteDatabase.update(contacts_firstTable, contentValues, "contacts_id=?", new String[]{user.getContacts_id()});
        return true;
    }

}

