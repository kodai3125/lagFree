package app.tsutsui.tuttu.original

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

open class DataEvent(
    @PrimaryKey open var id:String= UUID.randomUUID().toString(),
    open var imageId: Int = 0,
    open var events: RealmList<String> = RealmList(),
    open var createdAt: Date = Date(System.currentTimeMillis()),
    open var title:String=""

): RealmObject()