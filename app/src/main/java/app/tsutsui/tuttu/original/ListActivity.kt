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
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class ListActivity : AppCompatActivity(),checkFragment.checkFragmentListener {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    var id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val list=readAll()

        val textView=findViewById<TextView>(R.id.textView9)

        if (list.isEmpty()){
            textView.text="保存されたフライトはありません"
        }

        val adapter=CustomAdapter(this,list,object :CustomAdapter.OnItemClickListener{
            override fun onItemClick(item: DataEvent) {
                val intent= Intent(this@ListActivity,ScheduleActivity::class.java)
                intent.putExtra("id",item.id)
                intent.putExtra("tag",1)
                startActivity(intent)
            }
        },object :CustomAdapter.DeleteListener{
            override fun buttonTapped(item: DataEvent) {
                val newFragmet=checkFragment()
                var bundle=Bundle()
                bundle.putInt("DELETE",2)
                bundle.putString("ID",item.id)
                newFragmet.arguments=bundle
                newFragmet.show(supportFragmentManager,"checkFragment")

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


    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }

    override fun onDialogPositiveClick2(dialog: DialogFragment) {
        delete(id)

        check()
    }

    fun check(){
        val textView=findViewById<TextView>(R.id.textView9)
        val list=readAll()
        if (list.isEmpty()){
            textView.text="保存されたフライトはありません"
        }
    }


    override fun onDialogNegativeClick2(dialog: DialogFragment) {

    }

    override fun onDataPass(data: String) {
        id=data
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