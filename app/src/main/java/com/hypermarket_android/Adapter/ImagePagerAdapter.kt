package com.hypermarket_android.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.chrisbanes.photoview.PhotoView
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.listener.PhotoViewClickListener
import com.hypermarket_android.listener.RecyclerViewClickListener

class ImagePagerAdapter(
    internal var mContext: Context,
    internal var listOfPhotos: List<ProductDetailDataModel.ProductData.ImageData>) : PagerAdapter() {

    private lateinit var recyclerViewClickListener: RecyclerViewClickListener
    override fun getCount(): Int {
        return listOfPhotos.size
    }

    internal var mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull `object`: Any): Boolean {
        return view === `object`
    }

    @SuppressLint("CutPasteId")
    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val itemView =
            mLayoutInflater.inflate(R.layout.image_list_item, container, false)

        val ivPhoto = itemView.findViewById<PhotoView>(R.id.photo_img_view)
        Glide.with(mContext).load(listOfPhotos[position].images).placeholder(R.drawable.no_image).thumbnail(0.01f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(ivPhoto)
        container.addView(itemView)

        itemView.setTag("myview" + position);


        return itemView
    }


    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }


    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }


    override fun getItemPosition(@NonNull `object`: Any): Int {
        return POSITION_NONE
    }
}