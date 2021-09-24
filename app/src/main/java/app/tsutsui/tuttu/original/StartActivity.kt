package app.tsutsui.tuttu.lagfree

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import app.tsutsui.tuttu.original.ListActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button =findViewById<Button>(R.id.button4)
        button.setOnClickListener{

            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val button2=findViewById<Button>(R.id.button8)
        button2.setOnClickListener(){
            val intent=Intent(this,ListActivity::class.java)
            startActivity(intent)
        }


    }
}