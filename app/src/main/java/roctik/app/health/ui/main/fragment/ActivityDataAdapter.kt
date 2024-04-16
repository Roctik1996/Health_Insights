package roctik.app.health.ui.main.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import roctik.app.health.databinding.ItemActivityDataBinding
import roctik.app.health.domain.model.AggregateDataItem
import roctik.app.health.utils.ItemClickListener

class ActivityDataAdapter(private var itemListener: ItemClickListener<AggregateDataItem>) :
    RecyclerView.Adapter<ActivityDataAdapter.ActivityDataViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityDataViewHolder {
        return ActivityDataViewHolder(
            ItemActivityDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActivityDataViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class ActivityDataViewHolder(private val binding: ItemActivityDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun setData(item: AggregateDataItem) {

            with(binding) {
                lblTitle.setText(item.title)
                lblActivityValue.text = item.value
                lblUnit.setText(item.unit)

                root.setOnClickListener { itemListener.onItemClick(item) }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<AggregateDataItem>() {
        override fun areItemsTheSame(
            oldItem: AggregateDataItem,
            newItem: AggregateDataItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: AggregateDataItem,
            newItem: AggregateDataItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

}