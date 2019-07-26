package com.picpay.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.makeramen.roundedimageview.RoundedImageView
import com.picpay.R
import com.picpay.entity.People
import com.picpay.tools.cleanForSearch
import java.util.*

/*
* Created By neomatrix on 2019-07-19
*/
class AdapterPeople(
    private val context: Context,
    private val resource: Int,
    private val data: List<People>,
    private val mGlide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var dataFiltered: ArrayList<People>? = null
    private var valueFilter: ValueFilter? = null
    private var selection = ""
    private var itemClickListener: ItemClickListener? = null

    interface ItemClickListener {

        fun onItemClick(position: Int, item: People)
    }

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    init {
        this.dataFiltered = ArrayList()
        this.dataFiltered!!.addAll(data)

        filter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewModel: View

        viewModel = inflater.inflate(resource, parent, false)
        return DefaultVH(viewModel)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        processDefault(holder, position)
    }

    override fun getItemCount(): Int {
        return dataFiltered?.size ?: 0
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {

            valueFilter = ValueFilter()
        }

        return valueFilter!!
    }

    private inner class DefaultVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mLLBack: LinearLayout = itemView.findViewById(R.id.peoplelist_adapter_cell_ll_back)

        var ivPhoto: RoundedImageView = itemView.findViewById(R.id.peoplelist_adapter_cell_iv_photo)
        var mUserName: TextView = itemView.findViewById(R.id.peoplelist_adapter_cell_tv_nick)
        var mName: TextView = itemView.findViewById(R.id.peoplelist_adapter_cell_tv_name)

        init {
            mLLBack.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (itemClickListener != null) {
                val position = adapterPosition
                val item = dataFiltered!![position]

                itemClickListener!!.onItemClick(adapterPosition, item)
            }
        }
    }

    private fun processDefault(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataFiltered!![position]

        val defaultVH = holder as DefaultVH

        mGlide.load(item.img)
            .placeholder(R.drawable.ic_person_black_24dp)
            .into(defaultVH.ivPhoto)

        defaultVH.mUserName.text = item.username
        defaultVH.mName.text = item.name
    }

    private inner class ValueFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var constraint = constraint
            val results = FilterResults()

            selection = constraint as String

            try {
                val filterList = ArrayList<People>()

                if (constraint.isNotEmpty()) {
                    constraint = constraint.cleanForSearch()

                    for (i in data.indices) {
                        val mKeyS1 = data[i].username.cleanForSearch()
                        val mKeyS2 = data[i].name.cleanForSearch()

                        if (mKeyS1.contains(constraint) ||
                            mKeyS2.contains(constraint)
                        ) {

                            filterList.add(data[i])

                        } else {
                        }
                    }

                    results.count = filterList.size
                    results.values = filterList
                } else {
                    results.count = data.size
                    results.values = data
                }
            } catch (e: Exception) {
                results.count = data.size
                results.values = data
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataFiltered = results?.values as ArrayList<People>

            notifyDataSetChanged()
        }
    }
}
