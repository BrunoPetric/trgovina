package hr.ferit.brunopetric.trgovina

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class editFragment : Fragment(), proizvodEditRecyclerAdapter.ContentListener {

    private lateinit var proizvodAdapter: proizvodEditRecyclerAdapter
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_edit, container, false)
        val usernameText = arguments?.getString("USERNAME").toString()
        val backButton = view.findViewById<ImageButton>(R.id.backButton2)

        backButton.setOnClickListener {

            val mainFragment = mainFragment()
            val bundle = Bundle()
            bundle.putString("USERNAME", usernameText)
            mainFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, mainFragment)
            fragmentTransaction?.commit()
        }


        val list: ArrayList<proizvod> = ArrayList()
        db.collection("proizvod")
            .get()
            .addOnSuccessListener {  result ->
                for(data in result.documents){
                    val proizvod = data.toObject(proizvod::class.java)

                    if(proizvod != null && (proizvod.korisnik == usernameText || usernameText =="admin@gmail.com")){
                        proizvod.id = data.id
                        list.add(proizvod)
                    }
                }
                list.sortByDescending { it.date }
                proizvodAdapter = proizvodEditRecyclerAdapter(this@editFragment)
                proizvodAdapter.postItemsList(list)


                view?.findViewById<RecyclerView>(R.id.proizvodView)?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = proizvodAdapter
                }
            }


        return view
    }
     override fun onItemButtonClick(index: Int, Proizvod: proizvod, clickType: ItemClickType) {
        if(clickType == ItemClickType.REMOVE){
            proizvodAdapter.removeItem(index)
            db.collection("proizvod").document(Proizvod.id).delete()
            Toast.makeText(activity,"Proizvod uspjesno obrisan", Toast.LENGTH_LONG).show()
        }
        else if(clickType == ItemClickType.EDIT){
            db.collection("proizvod").document(Proizvod.id).set(Proizvod)
            Toast.makeText(activity,"Proizvod uspjesno uredjen", Toast.LENGTH_LONG).show()
        }
    }
}