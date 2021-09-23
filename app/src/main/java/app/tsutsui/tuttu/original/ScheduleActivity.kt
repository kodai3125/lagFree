    package app.tsutsui.tuttu.lagfree

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.roundtableapps.timelinedayviewlibrary.Event
import com.roundtableapps.timelinedayviewlibrary.EventView
import com.roundtableapps.timelinedayviewlibrary.TimeLineLayout
import org.json.JSONArray
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.reflect.KMutableProperty

class ScheduleActivity() : AppCompatActivity(),EventPicker.EventDialogLister,FragmentEvent.FragmentEventLister{

    var event=""
    var eventStart=0F
    var eventStartM=0F
    var eventFin=0F
    var eventFinM=0F

    var choice=0

    var list= arrayListOf<ArrayList<String>>()

    var startTimeList= arrayListOf<String>()
    var startTineListM= arrayListOf<String>()
    var endTimeList= arrayListOf<String>()
    var endTimeListM= arrayListOf<String>()
    var eventNameList= arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val country1 = intent.getStringExtra("country1")
        val country2 = intent.getStringExtra("country2")
        val timelag = intent.getIntExtra("timelag", 0)

        val year = intent.getIntExtra("year", 0)
        val month = intent.getIntExtra("month", 0)
        val day = intent.getIntExtra("day", 0)
        val hour = intent.getIntExtra("hour", 0)
        val min = intent.getIntExtra("min", 0)

        val hour2 = intent.getIntExtra("hour2", 0)
        val min2 = intent.getIntExtra("min2", 0)

        val sleepTimeH=intent.getIntExtra("sleepTimeH",0)
        val sleepTimeM=intent.getIntExtra("sleepTimeM",0)

        val sleepH=intent.getIntExtra("sleepH",0)
        val sleepM=intent.getIntExtra("sleepM",0)

        val button2=findViewById<Button>(R.id.button2)
        val button3=findViewById<Button>(R.id.button3)


        val calendar = GregorianCalendar.getInstance()
        calendar.set(year, month, day, hour, min)
        calendar.add(GregorianCalendar.HOUR_OF_DAY, -timelag)

        val calendar2 = GregorianCalendar.getInstance()
        calendar2.set(year, month, day, hour, min)
        calendar2.add(GregorianCalendar.HOUR_OF_DAY, -timelag)
        calendar2.add(GregorianCalendar.HOUR_OF_DAY, hour2)
        calendar2.add(GregorianCalendar.MINUTE, min2)

        button2.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {

            var newFragment = EventPicker()
            newFragment.show(supportFragmentManager,"eventPicker")

        }

        val calendar3=GregorianCalendar.getInstance()
        calendar3.set(GregorianCalendar.HOUR_OF_DAY,sleepTimeH)
        calendar3.set(GregorianCalendar.MINUTE,sleepTimeM)
        calendar3.add(GregorianCalendar.HOUR_OF_DAY,sleepH)
        calendar3.add(GregorianCalendar.MINUTE,sleepM)

        val wakeTimeH=calendar3.get(GregorianCalendar.HOUR_OF_DAY)
        val wakeTimeM=calendar3.get(GregorianCalendar.MINUTE)

        val totalSleep=sleepH.toFloat()+(sleepM.toFloat()/60)
        val totalWake=24-totalSleep

        val ratio=totalSleep/totalWake

        var initialE=0F

        val sleepTime=sleepTimeH.toFloat()+(sleepTimeM.toFloat()/60)
        val wakeTime=wakeTimeH.toFloat()+(wakeTimeM.toFloat()/60)
        val departureTime=hour.toFloat()+(min.toFloat()/60)


        if (sleepTime<wakeTime && (departureTime<=sleepTime || departureTime>=wakeTime)){
            if (departureTime>=wakeTime){
                initialE=totalSleep-ratio*(departureTime-wakeTime)
            }
            else if (departureTime<=sleepTime){
                initialE=totalSleep-ratio*(24-wakeTime+departureTime)
            }
        }
        else if (sleepTime>wakeTime && departureTime>=wakeTime && departureTime <= sleepTime){
            initialE=totalSleep-ratio*(departureTime-wakeTime)
        }
        else if (sleepTime<wakeTime && departureTime>=sleepTime && departureTime<=wakeTime){
            initialE=departureTime-sleepTime
        }
        else if (sleepTime>wakeTime && (departureTime>=sleepTime || departureTime<=wakeTime)){
            if (departureTime>=sleepTime){
                initialE=departureTime-sleepTime
            }
            else if (departureTime<=wakeTime){
                initialE=24-sleepTime+departureTime
            }
        }

        var arrivalTime=calendar2.get(Calendar.HOUR_OF_DAY).toFloat()+calendar2.get(Calendar.MINUTE).toFloat()/60
        var finalE=0F

        if (sleepTime<wakeTime && (arrivalTime<=sleepTime || arrivalTime>=wakeTime)){
            if (arrivalTime>=wakeTime){
                finalE=totalSleep-ratio*(arrivalTime-wakeTime)
            }
            else if (arrivalTime<=sleepTime){
                finalE=totalSleep-ratio*(24-wakeTime+arrivalTime)
            }
        }
        else if (sleepTime>wakeTime && arrivalTime>=wakeTime && arrivalTime <= sleepTime){
            finalE=totalSleep-ratio*(arrivalTime-wakeTime)
        }
        else if (sleepTime<wakeTime && arrivalTime>=sleepTime && arrivalTime<=wakeTime){
            finalE=arrivalTime-sleepTime
        }
        else if (sleepTime>wakeTime && (arrivalTime>=sleepTime || arrivalTime<=wakeTime)){
            if (arrivalTime>=sleepTime){
                finalE=arrivalTime-sleepTime
            }
            else if (arrivalTime<=wakeTime){
                finalE=24-sleepTime+arrivalTime
            }
        }

        val diff=finalE-initialE

        val flightTime=hour2.toFloat()+ (min2.toFloat()/60)

        val wakeSchedule=-(diff-flightTime)/(1+ratio)

        val sleepSchedule=(flightTime-wakeSchedule)

        val sleepList=sleepSchedule.toInt()

        val sleepListMin=(sleepSchedule-sleepList.toFloat())*60

        if (sleepSchedule==0F && timelag==0){

            if (loadArrayList2("from").isNotEmpty()){
                for (i in 0 until loadArrayList2("from").size){

                    addEvent(loadArrayList2("from")[i],loadArrayList2("fromM")[i],loadArrayList2("to")[i],loadArrayList2("toM")[i],loadArrayList2("name")[i])

                }
            }
            else{

            }

        }
        else{

            if (startTimeList.isEmpty()){

                addEvent("0","0",hour2.toFloat().toString(),min2.toFloat().toString(),"フライト")

                if (sleepSchedule>0 && sleepSchedule<flightTime){
                    addEvent("0","0",sleepList.toString(),sleepListMin.toString(),"睡眠")
                }
                else if (sleepSchedule>flightTime){
                    addEvent("0","0",hour2.toFloat().toString(),min2.toFloat().toString(),"睡眠")
                }
                else if (sleepSchedule<=0){
                    Toast.makeText(this,"計算の結果フライト中の睡眠は推奨されません",Toast.LENGTH_LONG).show()
                }

            }

        }

    }


    override fun onDataPass(data:String,data2:Int,data3:Int,data4:Int,data5:Int,data8:String){

        event=data
        eventStart=data2.toFloat()
        eventStartM=data3.toFloat()
        eventFin=data4.toFloat()
        eventFinM=data5.toFloat()

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        if (choice==1){

            val x=list.indexOf(arrayListOf(loadArrayList2("list2")[0],loadArrayList2("list2")[1],loadArrayList2("list2")[2],loadArrayList2("list2")[3],loadArrayList2("list2")[4]))

            if (startTimeList[x]=="0" && eventNameList[x]=="フライト"){
                Toast.makeText(this,"フライトは編集できません",Toast.LENGTH_SHORT).show()
            }
            else{
                startTimeList.removeAt(x)
                startTineListM.removeAt(x)
                endTimeList.removeAt(x)
                endTimeListM.removeAt(x)
                eventNameList.removeAt(x)

                startTimeList.add(eventStart.toString())
                startTineListM.add(eventStartM.toString())
                endTimeList.add(eventFin.toString())
                endTimeListM.add(eventFinM.toString())
                eventNameList.add(event)

                removeList("from")
                removeList("fromM")
                removeList("to")
                removeList("toM")
                removeList("name")

                saveList2("from",startTimeList)
                saveList2("fromM",startTineListM)
                saveList2("to",endTimeList)
                saveList2("toM",endTimeListM)
                saveList2("name",eventNameList)

                removeList("list2")

                val intent=Intent(this,ScheduleActivity::class.java)
                startActivity(intent)
            }

        }
        else{

            if (event=="フライト"){
                Toast.makeText(this,"フライトは追加できません",Toast.LENGTH_SHORT).show()
            }
            else{
                addEvent(eventStart.toString(),eventStartM.toString(),eventFin.toString(),eventFinM.toString(),event)
            }
        }

    }

    override fun onClickButton() {

        val bundle=Bundle()

        bundle.putString("START_H",loadArrayList2("list2")[0])
        bundle.putString("START_M",loadArrayList2("list2")[1])
        bundle.putString("FIN_H",loadArrayList2("list2")[2])
        bundle.putString("FIN_M",loadArrayList2("list2")[3])
        bundle.putString("EVENT_NAME",loadArrayList2("list2")[4])

        var newFragment = EventPicker()

        choice=1

        newFragment.arguments=bundle
        newFragment.show(supportFragmentManager,"eventPicker")


    }

    override fun onClickButton2() {

        val x=list.indexOf(arrayListOf(loadArrayList2("list2")[0],loadArrayList2("list2")[1],loadArrayList2("list2")[2],loadArrayList2("list2")[3],loadArrayList2("list2")[4]))

        if (startTimeList[x]=="0" && eventNameList[x]=="フライト"){
            Toast.makeText(this,"フライトは削除できません",Toast.LENGTH_SHORT).show()
        }

        else{
            startTimeList.removeAt(x)
            startTineListM.removeAt(x)
            endTimeList.removeAt(x)
            endTimeListM.removeAt(x)
            eventNameList.removeAt(x)


            removeList("from")
            removeList("fromM")
            removeList("to")
            removeList("toM")
            removeList("name")

            saveList2("from",startTimeList)
            saveList2("fromM",startTineListM)
            saveList2("to",endTimeList)
            saveList2("toM",endTimeListM)
            saveList2("name",eventNameList)

            val intent=Intent(this,ScheduleActivity::class.java)
            startActivity(intent)

        }

    }


    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

    private fun addEvent(fromTime:String, fromTimeM:String,toTime:String,toTimeM:String, eventName:String){

        val timeLine: TimeLineLayout =findViewById(R.id.timeLine)

        val myEvent= Event()

        val myEventView= EventView(

            this,
            myEvent.apply {
                startTime=fromTime.toFloat()+fromTimeM.toFloat()/60
                endTime=toTime.toFloat()+toTimeM.toFloat()/60
                title=eventName
            },

            itemsMargin = 1,

            setupView = { myView ->

                if (eventName.length<10){
                    if (eventName.length%2==0){
                        (" ".repeat(5-eventName.length/2)+eventName+" ".repeat(5-eventName.length/2)).also { myView.findViewById<TextView>(R.id.tvTitle).text = it }
                    }
                    else{
                        (" ".repeat(5-(eventName.length-1)/2)+eventName+" ".repeat(5-(eventName.length-1)/2)).also { myView.findViewById<TextView>(R.id.tvTitle).text = it }
                    }
                }
                else{
                    myView.findViewById<TextView>(R.id.tvTitle).text= " $eventName "
                    myView.findViewById<TextView>(R.id.tvTitle).setTextColor(Color.parseColor("#FFFFFF"))
                }
            },
            onItemClick = {event ->

                removeList("list2")
                saveList2("list2", arrayListOf(fromTime,fromTimeM,toTime,toTimeM,eventName))

                val dialog=FragmentEvent()
                dialog.show(supportFragmentManager,"test")

            }
        )

        myEventView.visibility=View.VISIBLE

        startTimeList.add(fromTime)
        startTineListM.add(fromTimeM)
        endTimeList.add(toTime)
        endTimeListM.add(toTimeM)
        eventNameList.add(eventName)

        list.add(arrayListOf(fromTime,fromTimeM,toTime,toTimeM,eventName))

        myEventView.setBackgroundColor(Color.parseColor("#7F9ED6DC"))

        timeLine.addEvent(myEventView)

    }

    private fun saveList2(key:String,list:ArrayList<String>){
        val sharedPref=this.getPreferences(Context.MODE_PRIVATE)
        val editor=sharedPref.edit()


        val jsonArray=JSONArray(list)
        editor.putString(key, jsonArray.toString())
        editor.apply()

    }

    private fun loadArrayList2(key:String): ArrayList<String>{

        val sharedPref=this.getPreferences(Context.MODE_PRIVATE)

        val jsonArray=JSONArray(sharedPref.getString(key, "[]"))

        val arrayList : ArrayList<String> = ArrayList()

        for (i in 0 until jsonArray.length()){
            arrayList.add(jsonArray.get(i) as String)
        }

        return arrayList
    }

    fun removeList(key:String){
        val sharedPref=this.getPreferences(Context.MODE_PRIVATE)
        val editor=sharedPref.edit()

        editor.apply {
            remove(key)
        }

    }
}

