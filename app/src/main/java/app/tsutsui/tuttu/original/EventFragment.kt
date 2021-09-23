package app.tsutsui.tuttu.lagfree

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException


class EventPicker:DialogFragment(){

    interface EventDialogLister{
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDataPass(data:String,data2:Int,data3:Int,data4:Int,data5:Int,data8:String)
    }

    var mLister:EventDialogLister?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mLister=context as EventDialogLister
        } catch (e:ClassCastException){
            throw ClassCastException("$context must implement EventDialogLister")
        }

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var eventTime=0
        var eventMin=0
        var eventTime2=0
        var eventMin2=0

        val builder=AlertDialog.Builder(activity)
        val inflater=requireActivity().layoutInflater
        val eventView=inflater.inflate(R.layout.event_fragment,null)

        val numberPicker:NumberPicker=eventView.findViewById(R.id.hourPicker)
        val minPicker:NumberPicker=eventView.findViewById(R.id.minutePicker)
        val numberPicker2:NumberPicker=eventView.findViewById(R.id.hourPicker2)
        val minPicker2:NumberPicker=eventView.findViewById(R.id.minPicker2)
        val event:EditText=eventView.findViewById(R.id.event)

        val name=arguments?.getString("EVENT_NAME")
        val startH=arguments?.getString("START_H")
        val startM=arguments?.getString("START_M")
        val endH=arguments?.getString("FIN_H")
        val endM=arguments?.getString("FIN_M")
        var nameO=""

        if (name!=null){

            nameO=name
        }

        if (name!=null){

            event.setText(name)

        }

        if (numberPicker!=null){
            numberPicker.minValue=0
            numberPicker.maxValue=24

            numberPicker.wrapSelectorWheel=true
            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                eventTime=newVal
            }

            if (startH!=null){
                numberPicker.value=startH.toFloat().toInt()
            }
        }

        if (minPicker!=null){
            minPicker.minValue=0
            minPicker.maxValue=59

            minPicker.wrapSelectorWheel=true
            minPicker.setOnValueChangedListener{picker2,oldVal2,newVal2->
                eventMin=newVal2

            }

            if (startM!=null){
                minPicker.value=startM.toFloat().toInt()
            }
        }

        if (numberPicker2!=null){
            numberPicker2.minValue=0
            numberPicker2.maxValue=24

            numberPicker2.wrapSelectorWheel=true
            numberPicker2.setOnValueChangedListener { picker3, oldVal3, newVal3 ->
                eventTime2=newVal3
            }

            if (endH!=null){
                numberPicker2.value=endH.toFloat().toInt()
            }
        }

        if (minPicker2!=null){
            minPicker2.minValue=0
            minPicker2.maxValue=59

            minPicker2.wrapSelectorWheel=true
            minPicker2.setOnValueChangedListener{picker4,oldVal4,newVal4->
                eventMin2=newVal4

            }

            if (endM!=null){
                minPicker2.value=endM.toFloat().toInt()
            }
        }



        builder.setView(eventView)
            .setTitle("イベントを追加/編集")
            .setPositiveButton("Add"){dialog,id->


                var eventText=event.text.toString()

                mLister?.onDataPass(eventText,eventTime,eventMin,eventTime2,eventMin2,nameO)
                mLister?.onDialogPositiveClick(this)


            }
            .setNegativeButton("Cancel"){dialog2,id2->
                mLister?.onDialogNegativeClick(this)

            }


        return builder.create()
    }

    override fun onDestroy() {

        super.onDestroy()

    }

    override fun onDetach() {

        super.onDetach()
        mLister=null

    }
}