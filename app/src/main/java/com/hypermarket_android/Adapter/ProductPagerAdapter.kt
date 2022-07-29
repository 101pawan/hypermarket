package com.hypermarket_android.Adapter

import android.R.attr
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.hypermarket_android.MyApplication
import com.hypermarket_android.R
import com.hypermarket_android.dataModel.ProductDetailDataModel
import com.hypermarket_android.listener.PhotoViewClickListener
import com.hypermarket_android.listener.RecyclerViewClickListener
import com.santalu.aspectratioimageview.AspectRatioImageView
import android.widget.LinearLayout
import com.bumptech.glide.request.target.CustomTarget
import android.R.attr.path
import kotlinx.coroutines.runBlocking


class ProductPagerAdapter(
    internal var mContext: Context,
    internal var listOfPhotos: List<ProductDetailDataModel.ProductData.ImageData>,var productData: ProductDetailDataModel.ProductData
) : PagerAdapter() {

    private lateinit var recyclerViewClickListener:RecyclerViewClickListener
    private lateinit var photoViewClickListener: PhotoViewClickListener
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
            mLayoutInflater.inflate(R.layout.item_product_detail_pager_layout, container, false)

        val ivPhoto = itemView.findViewById<ImageView>(R.id.imgDisplay)
        val ivHeart = itemView.findViewById<ImageView>(R.id.iv_heart)
        val ivShare = itemView.findViewById<ImageView>(R.id.iv_share)
        ivShare.setImageResource(R.drawable.ic_share)
        if (productData.is_wishlist==1){
            ivHeart.setImageResource(R.drawable.ic_heart_fill)
        }else{
            ivHeart.setImageResource(R.drawable.ic_heart_un_fill_black)
        }
        Glide.with(mContext).load(listOfPhotos[position].images).placeholder(R.drawable.no_image).thumbnail(0.01f)
            .diskCacheStrategy(DiskCacheStrategy.ALL).override(MyApplication.getScreenWidth())
            .into(ivPhoto)

        container.addView(itemView)

        ivPhoto.setOnClickListener{
            recyclerViewClickListener.onClick(R.id.imgDisplay,position)
        }


        ivHeart.setOnClickListener {

            recyclerViewClickListener.onClick(R.id.iv_heart, 0)
        }

        ivShare.setOnClickListener {
            photoViewClickListener.onClick(ivPhoto )
        }

        itemView.setTag("myview" + position);


        return itemView
    }


    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }


    fun setPhotoViewClickListener(photoViewClickListener: PhotoViewClickListener) {
        this.photoViewClickListener = photoViewClickListener
    }

    fun setRecyclerViewClickListener(recyclerViewClickListener: RecyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener
    }
    fun updateListItem(photosList: List<ProductDetailDataModel.ProductData.ImageData>){
        runBlocking {
            Log.e("photosList",photosList.toString())
            listOfPhotos = photosList
            notifyDataSetChanged()
        }
    }


    override fun getItemPosition(@NonNull `object`: Any): Int {
        return POSITION_NONE
    }
}