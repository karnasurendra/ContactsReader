package com.tech.contactsreader.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tech.contactsreader.R
import com.tech.contactsreader.models.MyContactsModel
import kotlinx.android.synthetic.main.contacts_row.view.*
import java.lang.StringBuilder

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
            if (contactsModel.contactThumbnail.isNotEmpty()) {
                Glide.with(itemView.context).load(Uri.parse(contactsModel.contactThumbnail))
                    .error(R.drawable.ic_baseline_account_circle_24).into(itemView.profile_image)
            } else {
                Glide.with(itemView.context).load(R.drawable.ic_baseline_account_circle_24)
                    .into(itemView.profile_image)
            }
            val numbers = StringBuilder()
            for (i in contactsModel.numbers.indices) {
                if (i == contactsModel.numbers.size - 1) {
                    numbers.append("(+${contactsModel.numbers[i].countryCode}) - ${contactsModel.numbers[i].number}")
                } else {
                    numbers.append("(+${contactsModel.numbers[i].countryCode}) - ${contactsModel.numbers[i].number} , ")
                }
            }
            itemView.numbers_of_contact.text = numbers.toString()
        }

    }
}