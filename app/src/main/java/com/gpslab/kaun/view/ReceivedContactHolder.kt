package com.gpslab.kaun.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import com.gpslab.kaun.R


class ReceivedContactHolder(context: Context, itemView: View) : BaseReceivedHolder(context, itemView),ContactHolderBase {

    private val relativeContactInfo: RelativeLayout = itemView.findViewById(R.id.relative_contact_info)
    private val tvContactName: TextView = itemView.findViewById(R.id.tv_contact_name)
    private val btnMessageContact: Button = itemView.findViewById(R.id.btn_message_contact)
    private val btnAddContact: Button = itemView.findViewById(R.id.btn_add_contact)

    override var contactHolderInteraction: ContactHolderInteraction? = null

    override fun bind(message: Message, user: User) {
        super.bind(message, user)
        //set contact name
        tvContactName.text = message.content



        //send a message to this contact if installed this app
        btnMessageContact.setOnClickListener {
//            contactHolderInteraction?.onMessageClick(message.contact)
        }


        //add this contact to phonebook
        btnAddContact.setOnClickListener {


            val intent = Intent(ContactsContract.Intents.Insert.ACTION)
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
            intent.putExtra(ContactsContract.Intents.Insert.NAME, message.content)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, message.metadata)
            val origin = context as Activity
            origin.startActivityForResult(intent, 1)
//            contactHolderInteraction?.onAddContactClick(message.contact)
        }

    }


}
