    package app.tsutsui.tuttu.lagfree

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import app.tsutsui.tuttu.original.DataEvent
import app.tsutsui.tuttu.original.ListActivity
import app.tsutsui.tuttu.original.SaveFragment
import com.roundtableapps.timelinedayviewlibrary.Event
import com.roundtableapps.timelinedayviewlibrary.EventView
import com.roundtableapps.timelinedayviewlibrary.TimeLineLayout
import io.realm.Realm
import io.realm.RealmList
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

    class ScheduleActivity() : AppCompatActivity(),EventPicker.EventDialogLister,FragmentEvent.FragmentEventLister,SaveFragment.SaveFragmentListener{

    var event=""
    var eventStart=0F
    var eventStartM=0F
    var eventFin=0F
    var eventFinM=0F

    var choice=0
    var valid1=0

    var list= arrayListOf<ArrayList<String>>()

    var startTimeList= arrayListOf<String>()
    var startTineListM= arrayListOf<String>()
    var endTimeList= arrayListOf<String>()
    var endTimeListM= arrayListOf<String>()
    var eventNameList= arrayListOf<String>()

    var title=""


    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

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

        val id=intent.getStringExtra("id")
        val tag=intent.getIntExtra("tag",0)

        val button2=findViewById<Button>(R.id.button2)
        val button3=findViewById<Button>(R.id.button3)
        val button5=findViewById<Button>(R.id.button5)
        val button10=findViewById<Button>(R.id.button10)


        val calendar = GregorianCalendar.getInstance()
        calendar.set(year, month, day, hour, min)
        calendar.add(GregorianCalendar.HOUR_OF_DAY, -timelag)

        val calendar2 = GregorianCalendar.getInstance()
        calendar2.set(year, month, day, hour, min)
        calendar2.add(GregorianCalendar.HOUR_OF_DAY, -timelag)
        calendar2.add(GregorianCalendar.HOUR_OF_DAY, hour2)
        calendar2.add(GregorianCalendar.MINUTE, min2)

        button10.setOnClickListener{
            removeList("from")
            removeList("fromM")
            removeList("to")
            removeList("toM")
            removeList("name")
            removeList("list2")
            removeList("id")
            removeList("title")
            val intent=Intent(this,ListActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {

            removeList("from")
            removeList("fromM")
            removeList("to")
            removeList("toM")
            removeList("name")
            removeList("list2")
            removeList("id")
            removeList("title")

            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        button3.setOnClickListener {

            val newFragment = EventPicker()
            newFragment.show(supportFragmentManager,"eventPicker")

        }

        button5.setOnClickListener{
            val id2=loadArrayList2("id")

            if (id2.isEmpty()){
                val newFragment=SaveFragment()
                newFragment.show(supportFragmentManager,"saveFragment")
            }
            else if (loadArrayList2("title").isNotEmpty()){
                val newFragment=SaveFragment()
                val buldle=Bundle()
                buldle.putString("TITLE",loadArrayList2("title")[0])
                newFragment.arguments=buldle
                newFragment.show(supportFragmentManager,"saveFragment")
            }

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

                if (loadArrayList2("id").isEmpty()){
                    setTitle("名称未設定")
                }
                else{
                    setTitle(loadArrayList2("title")[0])
                }

                for (i in 0 until loadArrayList2("from").size){

                    addEvent(loadArrayList2("from")[i],loadArrayList2("fromM")[i],loadArrayList2("to")[i],loadArrayList2("toM")[i],loadArrayList2("name")[i])

                }
            }
            else if (tag==1){
                removeList("from")

                val realm=Realm.getDefaultInstance()

                val readList=realm.where(DataEvent::class.java).equalTo("id",id)

                val data=readList.findFirst()?.events

                for (i in 0 until (data!!.size/5)){
                    addEvent(data[5*i]!!.toString(),data[1+5*i]!!.toString(),data[2+5*i]!!.toString(),data[3+5*i]!!.toString(),data[4+5*i]!!.toString())
                }

                saveList2("title", arrayListOf(readList.findFirst()!!.title))
                saveList2("id", arrayListOf(id))

                setTitle(readList.findFirst()!!.title)

                realm.close()

            }

        }
        else{

            setTitle("名称未設定")

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

    override fun onDissmissListener(dialog: DialogFragment) {
        removeList("list2")
    }

    override fun onDissMiss(dialog: DialogFragment) {

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

    override fun onDataPass2(name: String,valid:Int) {
        title=name
        valid1=valid
    }

    override fun onDialogPositiveClick2(dialog: DialogFragment) {

        if (valid1!=2){
            realm.executeTransaction {
                val event=it.createObject(DataEvent::class.java,UUID.randomUUID().toString())
                event.imageId=R.drawable.icon_test
                val tempList=RealmList<String>()
                for(i in 0 until list.size){
                    tempList.add(startTimeList[i])
                    tempList.add(startTineListM[i])
                    tempList.add(endTimeList[i])
                    tempList.add(endTimeListM[i])
                    tempList.add(eventNameList[i])
                }
                event.events=tempList
                event.title=title
            }
            realm.close()

            removeList("from")
            removeList("fromM")
            removeList("to")
            removeList("toM")
            removeList("name")
            removeList("id")
            removeList("title")

            if (loadArrayList2("list2").isNotEmpty()){
                removeList("list2")
            }

            val intent=Intent(this,ListActivity::class.java)
            intent.putExtra("indicator",1)
            startActivity(intent)
        }
        else if (valid1==2){
            Toast.makeText(this,"名前を入力してください",Toast.LENGTH_LONG).show()
        }


    }

    override fun onDialogNegativeClick2(dialog: DialogFragment) {

    }

    override fun onDialogPositiveClick3(dialog: DialogFragment) {

        if (valid1!=2){
            val id=loadArrayList2("id")

            Realm.getDefaultInstance().use{realm->
                realm.executeTransaction {
                    val readList=realm.where(DataEvent::class.java).equalTo("id",id[0]).findFirst()
                    readList?.title=title
                    val tempList=RealmList<String>()
                    for(i in 0 until list.size){
                        tempList.add(startTimeList[i])
                        tempList.add(startTineListM[i])
                        tempList.add(endTimeList[i])
                        tempList.add(endTimeListM[i])
                        tempList.add(eventNameList[i])
                    }
                    readList?.events=tempList

                }
                realm.close()
            }

            removeList("from")
            removeList("fromM")
            removeList("to")
            removeList("toM")
            removeList("name")
            removeList("id")
            removeList("title")

            if (loadArrayList2("list2").isNotEmpty()){
                removeList("list2")
            }

            val intent=Intent(this,ListActivity::class.java)
            intent.putExtra("indicator",2)
            startActivity(intent)

        }
        else if (valid1==2){
            Toast.makeText(this,"名前を入力してください",Toast.LENGTH_LONG).show()
        }

    }

    override fun onDialogNegativeClick3(dialog: DialogFragment) {

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
        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()

    }
}


