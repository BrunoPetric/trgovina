package hr.ferit.brunopetric.trgovina


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class proizvodRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<proizvod> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProizvodViewHolder(

            LayoutInflater.from(parent.context).inflate(
                R.layout.proivod_layout, parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProizvodViewHolder -> {
                holder.bind(items[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun postItemsList(data: ArrayList<proizvod>) {
        items = data
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
        private val price: TextView =
            itemView.findViewById(R.id.productPrice)
        private val contact: TextView =
            itemView.findViewById(R.id.productContack)

        fun bind(proizvod: proizvod) {
            Glide
                .with(itemView.context)
                .load(proizvod.photoUrl)
                .into(photo)
            name.text = proizvod.name
            description.text = proizvod.description
            price.text = proizvod.price.toString() + "€"
            contact.text = "Kontakt: " + proizvod.kontakt
        }

    }
}