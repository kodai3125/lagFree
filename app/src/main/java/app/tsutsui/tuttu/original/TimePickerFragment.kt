package app.tsutsui.tuttu.lagfree

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

import java.util.*




/**
 * A simple [Fragment] subclass.
 * Use the [TimePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimePick : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = GregorianCalendar.getInstance()

        val hour = c.get(GregorianCalendar.HOUR_OF_DAY)
        val minute = c.get(GregorianCalendar.MINUTE)

        return TimePickerDialog(
            activity,
            activity as MainActivity?,
            hour,
            minute,
            true
        )

    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

    }
}