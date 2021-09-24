package app.tsutsui.tuttu.original

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.tsutsui.tuttu.lagfree.R
import java.lang.ClassCastException

class SaveFragment:DialogFragment() {
    interface SaveFragmentListener{

        fun onDialogPositiveClick2(dialog:DialogFragment)
        fun onDialogNegativeClick2(dialog: DialogFragment)
        fun onDialogPositiveClick3(dialog: DialogFragment)
        fun onDialogNegativeClick3(dialog: DialogFragment)
        fun onDataPass2(name:String,valid:Int)
    }

    var listener:SaveFragmentListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener=context as SaveFragmentListener
        }catch (e:ClassCastException){
            throw ClassCastException("$context must implement SaveFragmentListener" )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var title=""

        val builder=AlertDialog.Builder(activity)
        val inflater=requireActivity().layoutInflater
        val saveView=inflater.inflate(R.layout.fragment_save,null)

        val editText=saveView.findViewById<EditText>(R.id.nameEdittext)


        val titleExist=arguments?.getString("TITLE")

        if (titleExist!=null){
            editText.setText(titleExist)
        }

        if (titleExist==null){
            builder.setView(saveView)
                .setTitle("フライトに名前を付けて保存してください")
                .setPositiveButton("保存"){dialog,id->

                    title=editText.text.toString()
                    if (editText.text.isNotBlank()){
                        listener?.onDataPass2(title,1)
                    }
                    else{
                        listener?.onDataPass2(title,2)
                    }

                    listener?.onDialogPositiveClick2(this )
                }
                .setNegativeButton("戻る"){dialog2,id2->
                    listener?.onDialogNegativeClick2(this)

                }
        }
        else{
            builder.setView(saveView)
                .setTitle("フライトを上書き保存する")
                .setPositiveButton("上書き保存"){dialog3,id3->
                    title=editText.text.toString()
                    if (editText.text.isNotBlank()){
                        listener?.onDataPass2(title,1)
                    }
                    else{
                        listener?.onDataPass2(title,2)
                    }
                    listener?.onDialogPositiveClick3(this)
                }
                .setNegativeButton("戻る") {dialog4,id4->
                    listener?.onDialogNegativeClick3(this)
                }
        }




        return builder.create()

    }
    override fun onDestroy() {

        super.onDestroy()

    }



    override fun onDetach() {
        super.onDetach()
        listener=null
    }

}