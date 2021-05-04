package com.tech.contactsreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tech.contactsreader.R
import com.tech.contactsreader.models.MyContactsModel
import kotlinx.android.synthetic.main.contacts_row.view.*

class MyContactsAdapter(private val mList: ArrayList<MyContactsModel>) :
    RecyclerView.Adapter<MyContactsAdapter.MyContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyContactsViewHolder {
        return MyContactsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.contacts_row, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyContactsViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    inner class MyContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(contactsModel: MyContactsModel) {
            itemView.name_of_contact.text = contactsModel.name
        }

    }
}