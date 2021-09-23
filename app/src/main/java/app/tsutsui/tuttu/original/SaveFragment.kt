package app.tsutsui.tuttu.original

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import app.tsutsui.tuttu.lagfree.R
import java.lang.ClassCastException

class SaveFragment:DialogFragment() {
    interface SaveFragmentListener{
        fun onDialogPositiveClick(dialog:DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDataPass(name:String)
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

        builder.setView(saveView)
            .setTitle("フライトを保存する")
            .setPositiveButton("保存"){dialog,id->

                title=editText.text.toString()

                listener?.onDataPass(title)
                listener?.onDialogPositiveClick(this )
            }
            .setNegativeButton("戻る"){dialog2,id2->
                listener?.onDialogNegativeClick(this)

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