package hr.ferit.brunopetric.trgovina

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

enum class ItemClickType{
    EDIT,
    REMOVE
}

class proizvodEditRecyclerAdapter(val listener: ContentListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var items: MutableList<proizvod> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ProizvodViewHolder(

                LayoutInflater.from(parent.context).inflate(
                    R.layout.proivod_edit_layout, parent,
                    false
                )
            )

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is ProizvodViewHolder -> {
                    holder.bind(position, items[position], listener)
                }
            }

        }

        override fun getItemCount(): Int {
            return items.size
        }

        fun postItemsList(data: ArrayList<proizvod>) {
            items = data
        }
    fun removeItem(index: Int){
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }
        class ProizvodViewHolder constructor(
            itemView: View
        ) : RecyclerView.ViewHolder(itemView) {
            private val photo: ImageView =
                itemView.findViewById(R.id.productPhoto)
            private val name: TextView =
                itemView.findViewById(R.id.productName)
            private val description: TextView =
                itemView.findViewById(R.id.productDescription)
            private val price: EditText =
                itemView.findViewById(R.id.productPrice2)

            private val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
            private val editButton = itemView.findViewById<ImageButton>(R.id.editButton2)

            fun bind(index: Int, proizvod: proizvod, listener: ContentListener) {
                Glide
                    .with(itemView.context)
                    .load(proizvod.photoUrl)
                    .into(photo)
                name.text = proizvod.name
                description.text = proizvod.description
                price.hint = proizvod.price.toString() + "€"

                editButton.setOnClickListener{
                    proizvod.name = name.text.toString()
                    proizvod.description = description.text.toString()
                    if( price.text.toString().toDoubleOrNull() != null) {
                        proizvod.price = price.text.toString().toDouble()
                    }
                    listener.onItemButtonClick(index, proizvod, ItemClickType.EDIT)
                }
                deleteButton.setOnClickListener{
                    listener.onItemButtonClick(index, proizvod, ItemClickType.REMOVE)
                }
            }
            }
            interface ContentListener{
                fun onItemButtonClick(index: Int, Proizvod: proizvod, clickType: ItemClickType)
        }
}