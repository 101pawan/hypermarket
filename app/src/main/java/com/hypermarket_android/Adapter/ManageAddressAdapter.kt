package com.hypermarket_android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ManageAddressResponse

class ManageAddressAdapter(
    private val context: Context,
    private val manageAddressListener: ManageAddressClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mManageAddressList: MutableList<Item> = mutableListOf()

    companion object {
        const val MANAGE_ADDRESS_CONTENT = 1
        const val MANAGE_ADDRESS_FOOTER = 2
        const val TAG_ADDRESS_FOOTER = "tag_address_footer"
        const val TAG_PROMOCODE_FOOTER = "tag_promocode_footer"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        var holder: RecyclerView.ViewHolder? = null

        when (viewType) {
            MANAGE_ADDRESS_CONTENT -> holder =
                ContentViewHolder(
                    inflater.inflate(
                        R.layout.content_manage_address,
                        parent,
                        false
                    )
                )

            MANAGE_ADDRESS_FOOTER -> holder =
                FooterViewHolder(
                    inflater.inflate(
                        R.layout.footer_manage_address,
                        parent,
                        false
                    )
                )
        }
        return holder!!
    }


    override fun getItemViewType(position: Int): Int {
        return mManageAddressList[position].type!!
    }

    override fun getItemCount(): Int {
        return mManageAddressList.size
    }


    fun setData(itemList: List<Item>) {
        this.mManageAddressList.clear()
        mManageAddressList.addAll(itemList)
        notifyDataSetChanged()
    }


    fun getData(): List<Item> {
        return mManageAddressList
    }


    /**
     * Item object using builder pattern.
     */

    class Item(
        internal var type: Int? = 0,
        internal var addRessItem: ManageAddressResponse.UserAddres? = null
    ) {

        data class Builder(
            private var type: Int? = 0,
            private var addRessItem: ManageAddressResponse.UserAddres? = null

        ) {

            fun type(type: Int?) = apply { this.type = type }
            fun addressItem(addRessItem: ManageAddressResponse.UserAddres?) =
                apply { this.addRessItem = addRessItem }

            fun build() = Item(
                type,
                addRessItem

            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            val item = mManageAddressList[holder.adapterPosition]

            when (item.type) {
                MANAGE_ADDRESS_CONTENT -> {
                    bindManageAddressContent(holder as ContentViewHolder, item)
                }
                MANAGE_ADDRESS_FOOTER -> {
                    bindManageAddressFooter(holder as FooterViewHolder, item)
                }


            }
        }
    }


    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameValueTV = itemView.findViewById<TextView>(R.id.tv_name_value)
        var homeOfficeOtherTV = itemView.findViewById<TextView>(R.id.tv_home_office_other)
        var phoneNoTV = itemView.findViewById<TextView>(R.id.tv_phone_no_value)
        var fullAddressTV = itemView.findViewById<TextView>(R.id.tv_address)
        var addressEditIV = itemView.findViewById<ImageView>(R.id.iv_address_edit)
        var addressDeleteIV = itemView.findViewById<ImageView>(R.id.iv_address_delete)
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var addNewAddressBtnTV = itemView.findViewById<TextView>(R.id.tv_add_new_address)


    }

    private fun bindManageAddressContent(holder: ContentViewHolder, item: Item) {
        holder.addressEditIV.setOnClickListener {
            manageAddressListener.onEditBtnClick(item.addRessItem)
        }

        holder.addressDeleteIV.setOnClickListener {
            manageAddressListener.onDeleteBtnClick(item.addRessItem);
        }

         holder.apply {
            nameValueTV.text = item.addRessItem?.name ?: ""
            homeOfficeOtherTV.text = item.addRessItem?.save_as ?: ""
            phoneNoTV.text = "${item.addRessItem?.alt_country_code ?: ""} ${item.addRessItem?.alt_mobile_number ?: ""}"
             fullAddressTV.text = " ${item.addRessItem?.house_number ?: ""}, ${item.addRessItem?.building_tower ?: ""}, ${item.addRessItem?.society_locality ?: ""} "



        }

    }

    private fun bindManageAddressFooter(holder: FooterViewHolder, item: Item) {
        holder.addNewAddressBtnTV.setOnClickListener { manageAddressListener.onSaveBtnClick()}

    }


    interface ManageAddressClickListener {
        fun onSaveBtnClick()
        fun onEditBtnClick(addRessItem: ManageAddressResponse.UserAddres?)
        fun onDeleteBtnClick(addRessItem: ManageAddressResponse.UserAddres?)
    }


}







