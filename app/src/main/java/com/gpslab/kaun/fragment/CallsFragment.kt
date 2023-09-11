package com.gpslab.kaun.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlomi.hidely.hidelyviews.HidelyImageView
import com.gpslab.kaun.R
import com.gpslab.kaun.adapter.NewCallsAdapter
import com.gpslab.kaun.calling.CallingActivity
import com.gpslab.kaun.calling.PerformCall
import com.gpslab.kaun.digitaloceanspaces.RandomString
import com.gpslab.kaun.status.FragmentCallback
import com.gpslab.kaun.status.MainViewModel
import com.gpslab.kaun.view.*
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_calls.*
import java.util.*

class CallsFragment : Fragment(), ActionMode.Callback, NewCallsAdapter.OnClickListener {


    private var fireCallList: RealmResults<FireCall>? = null
    private val selectedFireCallListActionMode: MutableList<FireCall> = ArrayList()
    private lateinit var adapter: NewCallsAdapter
    var listener: FragmentCallback? = null
    var actionMode: ActionMode? = null
    val fireManager = FireManager()

    val viewModel: MainViewModel by activityViewModels()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as FragmentCallback
        } catch (castException: ClassCastException) {
            /** The activity does not implement the listener.  */
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_calls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initAdapter()


    }

    private fun initAdapter() {
        fireCallList = RealmHelper.getInstance().allCalls
        adapter = NewCallsAdapter(fireCallList, selectedFireCallListActionMode, activity, this@CallsFragment)
        rv_calls.layoutManager = LinearLayoutManager(activity)
        rv_calls.adapter = adapter
    }

    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        this.actionMode = actionMode
        actionMode.menuInflater.inflate(R.menu.menu_action_calls, menu)
        actionMode.title = "1"
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
        if (actionMode != null && menuItem != null) {
            if (menuItem.itemId == R.id.menu_item_delete) deleteClicked()
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        actionMode?.finish()
    }

    private fun deleteClicked() {
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle(R.string.confirmation)
        dialog.setMessage(R.string.delete_calls_confirmation)
        dialog.setNegativeButton(R.string.no, null)
        dialog.setPositiveButton(R.string.yes) { dialogInterface, i ->
            for (fireCall in selectedFireCallListActionMode) {
                RealmHelper.getInstance().deleteCall(fireCall)
            }
            exitActionMode()
        }
        dialog.show()
    }

    override fun onDestroyActionMode(actionMode: ActionMode) {
        this.actionMode = null
        selectedFireCallListActionMode.clear()
        adapter?.notifyDataSetChanged()
    }

    private fun itemRemovedFromActionList(selectedCircle: HidelyImageView, itemView: View, fireCall: FireCall) {
        selectedFireCallListActionMode.remove(fireCall)
        if (selectedFireCallListActionMode.isEmpty()) {
            actionMode?.finish()
        } else {
            selectedCircle.hide()
            itemView.setBackgroundColor(-1)
            actionMode?.title = selectedFireCallListActionMode.size.toString() + ""
        }
    }

    private fun itemAddedToActionList(selectedCircle: HidelyImageView, itemView: View, fireCall: FireCall) {
        selectedCircle.show()
        itemView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.item_selected_background_color))
        selectedFireCallListActionMode.add(fireCall)
        actionMode?.title = selectedFireCallListActionMode.size.toString() + ""
    }

    fun exitActionMode() {
        actionMode?.finish()
    }


//    override fun onSearchClose() {
//        super.onSearchClose()
//        adapter = NewCallsAdapter(fireCallList, selectedFireCallListActionMode, activity, this@CallsFragment)
//        rv_calls.adapter = adapter
//    }

    override fun onItemClick(selectedCircle: HidelyImageView, itemView: View, fireCall: FireCall) {
        if (actionMode != null) {
            if (selectedFireCallListActionMode.contains(fireCall)) {
                itemRemovedFromActionList(selectedCircle, itemView, fireCall)
            } else {
                itemAddedToActionList(selectedCircle, itemView, fireCall)
            }
        } else if (fireCall.user != null && fireCall.user.uid != null) {
            val tsLong = System.currentTimeMillis() / 1000
            val ts = tsLong.toString()
            val randomString = RandomString()
            val image_name = ts + "_" + randomString.nextString()
            // Continue with delete operation
            // Continue with delete operation
            if(fireCall.isVideo){
                val intent = Intent(context, CallingActivity::class.java)
                intent.putExtra(IntentUtils.CALL_TYPE, 2)
                intent.putExtra(IntentUtils.CALL_DIRECTION, FireCallDirection.OUTGOING)
                intent.putExtra(IntentUtils.UID, fireCall.user.uid)
                intent.putExtra(IntentUtils.CALL_ID, image_name)
                intent.putExtra(IntentUtils.CALL_ACTION_TYPE, IntentUtils.ACTION_START_NEW_CALL)
                intent.putExtra(IntentUtils.PHONE, fireCall.user.uid)
                startActivity(intent)
            }else {
                val intent = Intent(context, CallingActivity::class.java)
                intent.putExtra(IntentUtils.CALL_TYPE, 1)
                intent.putExtra(IntentUtils.CALL_DIRECTION, FireCallDirection.OUTGOING)
                intent.putExtra(IntentUtils.UID, fireCall.user.uid)
                intent.putExtra(IntentUtils.CALL_ID, image_name)
                intent.putExtra(IntentUtils.CALL_ACTION_TYPE, IntentUtils.ACTION_START_NEW_CALL)
                intent.putExtra(IntentUtils.PHONE, fireCall.user.uid)
                startActivity(intent)
            }

//            PerformCall(requireActivity(), fireManager, disposables).performCall(fireCall.isVideo, fireCall.user.uid)
        }
    }

    override fun onIconButtonClick(view: View, fireCall: FireCall) {
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val randomString = RandomString()
        val image_name = ts + "_" + randomString.nextString()
        if(fireCall.isVideo){
            val intent = Intent(context, CallingActivity::class.java)
            intent.putExtra(IntentUtils.CALL_TYPE, 2)
            intent.putExtra(IntentUtils.CALL_DIRECTION, FireCallDirection.OUTGOING)
            intent.putExtra(IntentUtils.UID, fireCall.user.uid)
            intent.putExtra(IntentUtils.CALL_ID, image_name)
            intent.putExtra(IntentUtils.CALL_ACTION_TYPE, IntentUtils.ACTION_START_NEW_CALL)
            intent.putExtra(IntentUtils.PHONE, fireCall.user.uid)
            startActivity(intent)
        }else {
            val intent = Intent(context, CallingActivity::class.java)
            intent.putExtra(IntentUtils.CALL_TYPE, 1)
            intent.putExtra(IntentUtils.CALL_DIRECTION, FireCallDirection.OUTGOING)
            intent.putExtra(IntentUtils.UID, fireCall.user.uid)
            intent.putExtra(IntentUtils.CALL_ID, image_name)
            intent.putExtra(IntentUtils.CALL_ACTION_TYPE, IntentUtils.ACTION_START_NEW_CALL)
            intent.putExtra(IntentUtils.PHONE, fireCall.user.uid)
            startActivity(intent)
        }
    }

    override fun onLongClick(selectedCircle: HidelyImageView, itemView: View, fireCall: FireCall) {

    }
}