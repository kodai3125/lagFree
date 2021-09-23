package app.tsutsui.tuttu.lagfree

import android.os.Bundle
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment;
import java.util.*


class DatePick:DialogFragment(),DatePickerDialog.OnDateSetListener{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c =GregorianCalendar.getInstance()

        val year=c.get(GregorianCalendar.YEAR)
        val month=c.get(GregorianCalendar.MONTH)
        val day=c.get(GregorianCalendar.DAY_OF_MONTH)

        return DatePickerDialog(
            this.context as Context,
            activity as MainActivity?,
            year,
            month,
            day)

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

    }

}