package com.gpslab.kaun.view

import android.content.Context
import io.reactivex.Maybe

class FireManager {
    companion object {

        //fix for com.google.firebase.database.DatabaseException: Invalid Firebase Database path: #21#.
        // Firebase Database paths must not contain '.', '#', '$', '[', or ']'
        //if a phone number contains one of these characters we will skip this number since it's not a Phone Number

        //get this user's uid
        private val deniedFirebaseStrings = arrayOf(".", "#", "$", "[", "]")
        @JvmStatic
        val uid: String
            get() = ""

        @JvmStatic
        fun isHasDeniedFirebaseStrings(deniedString: String): Boolean {
            for (deniedFirebaseString in deniedFirebaseStrings) {
                if (deniedString.contains(deniedFirebaseString)) {
                    return true
                }
            }
            return false
        }




        //check if there is a new photo for this user and download it
        //check for both thumb and full photo



    }
    fun setMessagesAsRead(context: Context, chatId: String) {
        //get unread messages
        val results = RealmHelper.getInstance().getUnReadIncomingMessages(chatId)
        for (message in results) {
//            ServiceHelper.startUpdateMessageStatRequest(context, message.messageId, uid, chatId, MessageStat.READ)
        }
    }
}