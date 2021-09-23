package app.tsutsui.tuttu.original

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.tsutsui.tuttu.lagfree.R

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }
}