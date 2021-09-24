package app.tsutsui.tuttu.original

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.tsutsui.tuttu.lagfree.R
import java.lang.ClassCastException

class checkFragment:DialogFragment() {

    interface checkFragmentListener{
        fun onDialogPositiveClick(dialog:DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDialogPositiveClick2(dialog: DialogFragment)
        fun onDialogNegativeClick2(dialog: DialogFragment)
        fun onDataPass(data:String)
    }

    var listener:checkFragmentListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener=context as checkFragment.checkFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement SaveFragmentListener" )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder= AlertDialog.Builder(activity)
        val inflater=requireActivity().layoutInflater
        val view=inflater.inflate(R.layout.fragment_check,null)
        val type=arguments?.getInt("ADD")
        val type2=arguments?.getInt("DELETE")
        val id=arguments?.getString("ID")

        if (type==1){
            builder.setView(view)
                .setTitle("フライトを追加しますか？")
                .setPositiveButton("追加"){dialog,id->
                    listener?.onDialogPositiveClick(this)

                }
                .setNegativeButton("戻る"){dialog,id->
                    listener?.onDialogNegativeClick(this)
                }

        }
        else if (type2==2&&id!=null){
            builder.setView(view)
                .setTitle("フライトを削除しますか？")
                .setPositiveButton("削除"){dialog2,id2->
                    listener?.onDialogPositiveClick2(this)
                    listener?.onDataPass(id)

                }
                .setNegativeButton("戻る"){dialog,id->
                    listener?.onDialogNegativeClick2(this)
                }
        }

        return builder.create()
    }
}