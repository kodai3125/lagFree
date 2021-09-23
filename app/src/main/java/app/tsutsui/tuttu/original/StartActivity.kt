package app.tsutsui.tuttu.lagfree

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button =findViewById<Button>(R.id.button4)
        button.setOnClickListener{

            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }
}