package com.hypermarket_android.ui.home

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.hypermarket_android.dataModel.UserData


class AutoSuggestAdapter(@NonNull context: Context?, resource: Int) :
    ArrayAdapter<kotlin.String>(context!!, resource), Filterable {
    private val mlistData: kotlin.collections.MutableList<UserData>
    fun setData(list: kotlin.collections.List<UserData>?){
        mlistData.clear()
        mlistData.addAll((list)!!)
    }
    override  fun getCount(): Int{
        return mlistData.size
    }
    @Nullable
    override  fun getItem(position: Int): kotlin.String {
        return mlistData.get(position).name.toString()
    }
    /**
     * Used to Return the full object directly from adapter.
     *
     * @param position
     * @return
     */
    fun getObject(position: Int): kotlin.String {
        return mlistData.get(position).name.toString()
    }
    fun getProductId(position: Int): Int? {
        return mlistData.get(position).id
    }

    @NonNull
    override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults: FilterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = mlistData
                    filterResults.count = mlistData.size
                }
                return filterResults
            }

            protected override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
    init {
        mlistData = ArrayList()
    }

}