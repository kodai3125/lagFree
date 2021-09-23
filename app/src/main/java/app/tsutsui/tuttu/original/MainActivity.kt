package app.tsutsui.tuttu.lagfree

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener  {

    var x=0
    var zisa=0
    var years=0
    var month=0
    var day=0
    var hour=0
    var min=0

    var hour2=0
    var min2=0

    var sleepTimeHours=0
    var sleepTimeMin=0

    var sleepHoursH=0
    var sleepHoursM=0

    lateinit var DepartureTime:EditText
    lateinit var FlightTime:EditText
    lateinit var Sleep:EditText
    lateinit var SleepH:EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DepartureTime=findViewById(R.id.DepartureTime)
        FlightTime=findViewById(R.id.FlightTime)
        Sleep=findViewById(R.id.editTextSleep)
        SleepH=findViewById(R.id.editTextSleepHours)

        DepartureTime.setTextColor(Color.parseColor("#374A4D"))
        FlightTime.setTextColor(Color.parseColor("#374A4D"))
        Sleep.setTextColor(Color.parseColor("#374A4D"))
        SleepH.setTextColor(Color.parseColor("#374A4D"))

        val spinnerItems= arrayOf("アラスカ",
                "アラブ首長国連邦",
                "アルゼンチン",
                "イギリス",
                "イタリア",
                "イラン",
                "インド",
                "エジプト",
                "オーストラリア",
                "カナダ",
                "ギリシャ",
                "サウジアラビア",
                "シカゴ",
                "シンガポール",
                "スペイン",
                "タイ",
                "ドイツ",
                "ニューカレドニア",
                "ニュージーランド",
                "ニューヨーク",
                "パキスタン",
                "バングラデシュ",
                "ブラジル",
                "フランス",
                "ハワイ",
                "ミャンマー",
                "メキシコ",
                "ロサンゼルス",
                "韓国",
                "香港",
                "台湾",
                "中華人民共和国",
                "日本"
        )

        val adapter = ArrayAdapter(this, R.layout.cuztom_spinner, spinnerItems)

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)

        val spinner=findViewById<Spinner>(R.id.spinner)

        spinner.adapter = adapter

        val spinner2=findViewById<Spinner>(R.id.spinner2)

        spinner2.adapter = adapter

        val button=findViewById<Button>(R.id.button)

        var a=""
        var b=""

        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                printTime()
                a=spinnerItems[spinner.selectedItemPosition]
                b=spinnerItems[spinner2.selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                printTime()
                a=spinnerItems[spinner.selectedItemPosition]
                b=spinnerItems[spinner2.selectedItemPosition]
            }

        }
        spinner2.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                printTime()
                a=spinnerItems[spinner.selectedItemPosition]
                b=spinnerItems[spinner2.selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                printTime()
                a=spinnerItems[spinner.selectedItemPosition]
                b=spinnerItems[spinner2.selectedItemPosition]
            }
        }


        button.setOnClickListener {

            if(DepartureTime.text.isEmpty()){
                Toast.makeText(this,"出発時間を入力してください",Toast.LENGTH_LONG).show()
            }
            else if (FlightTime.text.isEmpty()){
                Toast.makeText(this,"フライト時間を入力してください",Toast.LENGTH_LONG).show()
            }
            else if (Sleep.text.isEmpty()){
                Toast.makeText(this,"就寝時間を入力して下ださい",Toast.LENGTH_LONG).show()
            }
            else if (SleepH.text.isEmpty()){
                Toast.makeText(this,"睡眠時間を入力して下ださい",Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this, ScheduleActivity::class.java)

                intent.putExtra("year",years)
                intent.putExtra("month",month)
                intent.putExtra("day",day)
                intent.putExtra("hour",hour)
                intent.putExtra("min",min)

                intent.putExtra("hour2",hour2)
                intent.putExtra("min2",min2)

                intent.putExtra("country1",a)
                intent.putExtra("country2",b)

                intent.putExtra("timelag",zisa)

                intent.putExtra("sleepTimeH",sleepTimeHours)
                intent.putExtra("sleepTimeM",sleepTimeMin)
                intent.putExtra("sleepH",sleepHoursH)
                intent.putExtra("sleepM",sleepHoursM)

                startActivity(intent)

            }

        }
        DepartureTime.setOnClickListener(){

            val newFragment = DatePick()
            newFragment.show(supportFragmentManager, "datePicker")
            x=1

        }
        FlightTime.setOnClickListener(){
            val newFragment = TimePick()
            newFragment.show(supportFragmentManager, "timePicker")
            x=2

        }
        Sleep.setOnClickListener(){
            val newFragment = TimePick()
            newFragment.show(supportFragmentManager, "timePicker")
            x=3
        }
        SleepH.setOnClickListener(){
            val newFragment = TimePick()
            newFragment.show(supportFragmentManager,"timePicker")
            x=4
        }


    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val str1=String.format(Locale.US,"%d/%d/%d",year,monthOfYear+1,dayOfMonth)
        years=year
        month=monthOfYear
        day=dayOfMonth
        val newFragment = TimePick()
        newFragment.show(supportFragmentManager, "timePicker")

        DepartureTime.setText(str1)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val str = String.format(Locale.US, "%d:%d", hourOfDay, minute)

        if (x==1){
            DepartureTime.setText(DepartureTime.text.toString()+" "+str)
            hour=hourOfDay
            min=minute
        }

        else if (x==2){
            FlightTime.setText(str)
            hour2=hourOfDay
            min2=minute
        }

        else if (x==3){
            Sleep.setText(str)
            sleepTimeHours=hourOfDay
            sleepTimeMin=minute
        }

        else if (x==4){
            SleepH.setText(str)
            sleepHoursH=hourOfDay
            sleepHoursM=minute
        }

    }

    fun printTime() {

        val spinner=findViewById<Spinner>(R.id.spinner)
        val spinner2=findViewById<Spinner>(R.id.spinner2)

        val textView=findViewById<TextView>(R.id.textView)
        val textView3=findViewById<TextView>(R.id.textView3)

        var country1 = spinner.selectedItemPosition
        var country2=spinner2.selectedItemPosition


        val list= arrayListOf("America/Juneau","Asia/Dubai","America/Argentina/Buenos_Aires","Europe/London","Europe/Rome","Asia/Tehran","Asia/Kolkata","Africa/Cairo","Australia/Canberra","America/Toronto","Europe/Athens","Asia/Riyadh","America/Chicago","Asia/Singapore","Europe/Madrid","Asia/Bangkok","Europe/Berlin","Pacific/Noumea","Pacific/Auckland","America/New_York","Asia/Karachi","Asia/Dhaka","America/Sao_Paulo","Europe/Paris","US/Hawaii","Asia/Rangoon","America/Mexico_City","America/Los_Angeles","Asia/Seoul","Asia/Hong_Kong","Asia/Taipei","Asia/Shanghai","Asia/Tokyo")

        var tm1 =TimeZone.getTimeZone(list[country1])
        var timelag=tm1.rawOffset

        var tm2=TimeZone.getTimeZone(list[country2])
        var timelag2=tm2.rawOffset

        val sfd = SimpleDateFormat("YYYY/MM/dd HH.mm")
        val sfd2=SimpleDateFormat("YYYY/MM/dd HH.mm")
        sfd.timeZone=tm1
        sfd2.timeZone=tm2

        textView.text=sfd.format(Date())

        textView3.text=sfd2.format(Date())

        var timelag3=(timelag-timelag2)/3600000
        zisa=timelag3

    }

}