package app.tsutsui.tuttu.original

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import app.tsutsui.tuttu.lagfree.R
import org.w3c.dom.Text
import java.lang.ClassCastException

class flightInfoFragment:DialogFragment() {

    interface flightInfoFragmentListener{
        fun onDialogPositiveClickI(dialog:DialogFragment)

    }

    var listener:flightInfoFragmentListener?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener=context as flightInfoFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement SaveFragmentListener" )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?):Dialog {
        val builder=AlertDialog.Builder(activity)
        val inflater1=requireActivity().layoutInflater
        val view=inflater1.inflate(R.layout.flight_info,null)

        val textView18=view.findViewById<TextView>(R.id.textView18)
        val textView20=view.findViewById<TextView>(R.id.textView20)
        val textView22=view.findViewById<TextView>(R.id.textView22)
        val textView24=view.findViewById<TextView>(R.id.textView24)

        val a1=arguments?.getString("VALUE1")
        val a2=arguments?.getString("VALUE2")
        val a3=arguments?.getString("VALUE3")
        val a4=arguments?.getString("VALUE4")
        val a5=arguments?.getString("VALUE5")
        val a6=arguments?.getString("VALUE6")
        val a7=arguments?.getString("VALUE7")
        val a8=arguments?.getString("VALUE8")
        val a9=arguments?.getString("VALUE9")
        val a10=arguments?.getString("VALUE10")
        val a11=arguments?.getString("VALUE11")
        val a12=arguments?.getString("VALUE12")
        val a13=arguments?.getString("VALUE13")

        textView18.text="$a1/$a2/$a3/$a4:$a5 $a12　出発"
        textView20.text="$a13"
        textView24.text="$a8:$a9"
        textView22.text="$a10:$a11"

        builder.setView(view).setTitle("フライト情報").setPositiveButton("戻る"){dialog,id->
            listener?.onDialogPositiveClickI(this)
        }

        return builder.create()

    }
}