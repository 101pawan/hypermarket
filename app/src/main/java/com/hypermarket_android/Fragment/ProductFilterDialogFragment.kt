package com.hypermarket_android.Fragment


import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.hypermarket_android.R
import com.hypermarket_android.eventPojos.ProductShortEventPojo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.greenrobot.eventbus.EventBus

class ProductFilterDialogFragment : BottomSheetDialogFragment() {

    private var short:String=""
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_filter_dialog, container, false)


        getDialog()!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(R.color.shadow));

        //  val cardView=view.findViewById<CardView>(R.id.card_view)
        val tvCancel = view.findViewById<TextView>(R.id.tv_cancel)
        val tvNext = view.findViewById<TextView>(R.id.tv_next)
        val tvPriceHighToLow = view.findViewById<TextView>(R.id.tv_price_high_low)
        val tvPriceLowToHigh = view.findViewById<TextView>(R.id.tv_price_low_high)
        val tvPopularity = view.findViewById<TextView>(R.id.tv_popularity)
        val tvLatest = view.findViewById<TextView>(R.id.tv_latest)
       // cardView.setBackgroundResource(R.drawable.card_view_bg)




        tvCancel.setOnClickListener {
            dismiss()
        }
        tvNext.setOnClickListener {
             EventBus.getDefault().postSticky(ProductShortEventPojo(true,short))



        }

        tvPriceHighToLow.setOnClickListener {

            short="hightolow"
            EventBus.getDefault().postSticky(ProductShortEventPojo(true,short))
            dismiss()
        }

        tvPriceLowToHigh.setOnClickListener {
            short="lowtohigh"
            EventBus.getDefault().postSticky(ProductShortEventPojo(true,short))
            dismiss()
        }

        tvPopularity.setOnClickListener {
            short="Popularity"
            EventBus.getDefault().postSticky(ProductShortEventPojo(true,short))
            dismiss()
        }
        tvLatest.setOnClickListener {
            short="latest"
            EventBus.getDefault().postSticky(ProductShortEventPojo(true,short))
            dismiss()

        }
        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This removes black background below corners.
        setStyle(DialogFragment.STYLE_NO_FRAME, 0)
    }


}
