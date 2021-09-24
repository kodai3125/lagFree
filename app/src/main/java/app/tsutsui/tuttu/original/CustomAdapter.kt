package app.tsutsui.tuttu.original

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.tsutsui.tuttu.lagfree.R
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

class CustomAdapter (
    private  val context: Context,
    private  var list: OrderedRealmCollection<DataEvent>?,
    private var listener:OnItemClickListener,
    private var listener2:DeleteListener,
    private val autoUpdate:Boolean
):
    RealmRecyclerViewAdapter<DataEvent, CustomAdapter.EventViewHolder>(list,autoUpdate){

    override fun getItemCount(): Int =list?.size?:0

    fun getItem():OrderedRealmCollection<DataEvent>?=list

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event:DataEvent=list?.get(position)?:return

        holder.container.setOnClickListener{
            listener.onItemClick(event)
        }
        holder.imageView.setImageResource(event.imageId)
        holder.contentTextView.text=event.title
        holder.dateTextView.text= SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE).format(event.createdAt)
        holder.button.setOnClickListener{
            listener2.buttonTapped(event,position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val v= LayoutInflater.from(context).inflate(R.layout.item_cell,parent,false)
        return EventViewHolder(v)
    }

    class EventViewHolder(view: View): RecyclerView.ViewHolder(view){
        val container: LinearLayout =view.findViewById(R.id.container1)
        val imageView: ImageView =view.findViewById(R.id.imageView)
        val contentTextView: TextView =view.findViewById(R.id.contentTextView)
        val dateTextView:TextView=view.findViewById(R.id.dateTextView)
        val button: Button =view.findViewById(R.id.button)
    }

    interface OnItemClickListener{
        fun onItemClick(item:DataEvent)
    }

    interface DeleteListener{
        fun buttonTapped(item:DataEvent,position: Int)
    }
}