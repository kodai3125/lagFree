package app.tsutsui.tuttu.lagfree

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.lang.ClassCastException

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.reflect.KMutableProperty

class FragmentEvent:DialogFragment() {

    interface FragmentEventLister{

        fun onClickButton()
        fun onClickButton2()
        fun onDissMiss(dialog:DialogFragment)
    }

    var mListr:FragmentEventLister?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)


        try{
            mListr=context as FragmentEventLister
        } catch (e: ClassCastException){
            throw ClassCastException("$context must implement EventDialogLister")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder=AlertDialog.Builder(activity)
        val inflater=requireActivity().layoutInflater
        val view=inflater.inflate(R.layout.schedule_fragment,null)



        val button:Button=view.findViewById(R.id.button6)
        val button2:Button=view.findViewById(R.id.button7)


        button.setOnClickListener(){

            mListr?.onClickButton()

            dismiss()

        }

        button2.setOnClickListener(){

            mListr?.onClickButton2()

            dismiss()

        }


        builder.setView(view).setOnDismissListener {
            mListr?.onDissMiss(this)
        }

        return builder.create()
    }

    override fun onDestroy() {

        super.onDestroy()

    }

    override fun onDetach() {

        super.onDetach()
        mListr=null
    }
}

