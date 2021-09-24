package app.tsutsui.tuttu.original

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.tsutsui.tuttu.lagfree.MainActivity
import app.tsutsui.tuttu.lagfree.R
import app.tsutsui.tuttu.lagfree.ScheduleActivity
import app.tsutsui.tuttu.lagfree.StartActivity
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class ListActivity : AppCompatActivity(),checkFragment.checkFragmentListener {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var id:String=""
    var indicator=0
    lateinit var adapter:CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        indicator=intent.getIntExtra("indicator",0)

        val textView=findViewById<TextView>(R.id.textView9)
        val button11=findViewById<Button>(R.id.button11)

        val list=readAll()


        button11.setOnClickListener{
            val intent=Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

        if (list.isEmpty()){
            textView.text="保存されたフライトはありません"
        }
        adapter=CustomAdapter(this,list,object :CustomAdapter.OnItemClickListener{
            override fun onItemClick(item: DataEvent) {
                val intent= Intent(this@ListActivity,ScheduleActivity::class.java)
                intent.putExtra("id",item.id)
                intent.putExtra("tag",1)
                startActivity(intent)
            }
        },object :CustomAdapter.DeleteListener{
            override fun buttonTapped(item: DataEvent,position:Int) {
                //val newFragmet=checkFragment()
                //var bundle=Bundle()
                //bundle.putInt("DELETE",2)
                //bundle.putString("ID",item.id)
                //positionE=position
                //newFragmet.arguments=bundle
                //newFragmet.show(supportFragmentManager,"checkFragment")
                Toast.makeText(this@ListActivity,"${item.title}　が削除されました",Toast.LENGTH_SHORT).show()
                delete(item.id)
                if (list.isEmpty()){
                    textView.text="保存されたフライトはありません"
                }

            }
        },true)

        val button9=findViewById<Button>(R.id.button9)
        button9.setOnClickListener{
            val newFragmet=checkFragment()
            var bundle=Bundle()
            bundle.putInt("ADD",1)
            newFragmet.arguments=bundle
            newFragmet.show(supportFragmentManager,"checkFragment")
        }

        val recycelrView=findViewById<RecyclerView>(R.id.recyclerView)
        recycelrView.layoutManager=LinearLayoutManager(this)
        recycelrView.adapter=adapter



        title = "フライト一覧"

        if (indicator==1){
            Toast.makeText(this,"フライトが保存されました",Toast.LENGTH_SHORT).show()
        }
        else if (indicator==2){
            Toast.makeText(this,"フライトが上書き保存されました",Toast.LENGTH_SHORT).show()
        }


    }

    override fun onDataPass(data: String) {
        id=data

    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

    override fun onDialogPositiveClick2(dialog: DialogFragment) {
        //val realm=Realm.getDefaultInstance()
        //val list=realm.where(DataEvent::class.java).equalTo("id",id).findFirst()!!.title
        //Toast.makeText(this,"$list　は削除されました",Toast.LENGTH_SHORT).show()
        //realm.close()
        //delete(id)
    }

    override fun onDialogNegativeClick2(dialog: DialogFragment) {

    }

    fun readAll():RealmResults<DataEvent>{
        return realm.where(DataEvent::class.java).findAll().sort("createdAt",Sort.ASCENDING)
    }

    fun delete(id:String){
        realm.executeTransaction {
            val event=realm.where(DataEvent::class.java).equalTo("id",id).findFirst()
                ?: return@executeTransaction
            event.deleteFromRealm()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}