package com.example.bowlingsam

import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.example_dialog.*

class HistoryListAdapter(val context: Context, val postureList: ArrayList<Posture>) : BaseAdapter() {
    private lateinit var historyDetailFragment: HistoryDetailFragment
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */

        val view : View
        val holder : ViewHolder

        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.history_list_item, null)
            holder = ViewHolder()
            holder.view_image1 = view.findViewById(R.id.posturePhotoImg)
            holder.view_text1 = view.findViewById(R.id.posture_number)
            holder.view_text2 = view.findViewById(R.id.date)
            holder.view_text3 = view.findViewById(R.id.correct_score)

            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val item = postureList[position]
        val resourceId = context.resources.getIdentifier(item.image, "drawable", context.packageName)
        holder.view_image1?.setImageResource(resourceId)
        holder.view_text1?.text = item.posture
        holder.view_text2?.text = item.date
        holder.view_text3?.text = item.score

        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dialog = CustomDialog(context)
                dialog.myDialog(item.posture)
            }
        })

        return view
    }

    fun log(){
        Toast.makeText(context, "클릭", Toast.LENGTH_SHORT).show()
    }

    override fun getItem(p0: Int): Any {
        return postureList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return postureList.size
    }

    private class ViewHolder {
        var view_image1 : ImageView? = null
        var view_text1 : TextView? = null
        var view_text2 : TextView? = null
        var view_text3 : TextView? = null
    }
}

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)
    val c = context

    fun myDialog(posture : String){
        dialog.setContentView(R.layout.example_dialog)
        dialog.dialog_video.setVideoPath("android.resource://com.example.bowlingsam/"+ R.raw.sample1)
        dialog.dialog_video.setOnPreparedListener {
            val mediaController = MediaController(c)
            mediaController.setAnchorView(dialog.dialog_video)
            dialog.dialog_video.setMediaController(mediaController)
            /*
            m : MediaPlayer ->
                m.setOnVideoSizeChangedListener { m : MediaPlayer?, width: Int, height: Int ->

                }

             */

        }
        dialog.dialog_video.requestFocus()
        dialog.dialog_video.start()


        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.dialog_text.setText(posture)


        dialog.show()
        dialog.dialog_button.setOnClickListener{
            dialog.dismiss()
        }
    }

}