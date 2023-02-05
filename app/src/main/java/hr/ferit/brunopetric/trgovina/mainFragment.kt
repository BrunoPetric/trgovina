package hr.ferit.brunopetric.trgovina

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class mainFragment : Fragment() {

    private lateinit var proizvodAdapter: proizvodRecyclerAdapter
    private val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val username = arguments?.getString("USERNAME").toString()
        //Toast.makeText(activity, "Dobrodosao $username", Toast.LENGTH_LONG).show()
        // Inflate the layout for this fragment


        val list: ArrayList<proizvod> = ArrayList()
        db.collection("proizvod")
            .get()
            .addOnSuccessListener {  result ->
                for(data in result.documents){
                    val proizvod = data.toObject(proizvod::class.java)

                    if(proizvod != null){
                        if(proizvod.kontakt=="")
                            proizvod.kontakt=proizvod.korisnik
                        proizvod.id = data.id
                        list.add(proizvod)
                    }
                }
                list.sortByDescending { it.date }
                proizvodAdapter = proizvodRecyclerAdapter()
                proizvodAdapter.postItemsList(list)


                view?.findViewById<RecyclerView>(R.id.proizvodView)?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = proizvodAdapter
                }
            }

        val publish = view.findViewById<ImageButton>(R.id.publishButton)
        publish.setOnClickListener {

            val publishFragment = postFragment()
            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            publishFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, publishFragment)
            fragmentTransaction?.commit()

        }

        val edit = view.findViewById<ImageButton>(R.id.editButton)
        edit.setOnClickListener {

            val editFragment = editFragment()
            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            editFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, editFragment)
            fragmentTransaction?.commit()

        }
        val logOut = view.findViewById<ImageButton>(R.id.logOut)
        logOut.setOnClickListener {

            val loginFragment = LoginFragment()
            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            loginFragment.arguments = bundle
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, loginFragment)
            fragmentTransaction?.commit()

            val sharedPref = activity?.getSharedPreferences("Login", Context.MODE_PRIVATE)
            val Ed = sharedPref!!.edit()
            Ed.putString("Unm", null)
            Ed.putString("Psw", null)
            Ed.commit()
            Toast.makeText(activity, "Uspjesno ste odjavljeni", Toast.LENGTH_LONG).show()
        }
        val searchButton = view.findViewById<ImageButton>(R.id.searchButton)
        val searchText = view.findViewById<EditText>(R.id.editTextPretrazi)
        searchButton.setOnClickListener{
            val list: ArrayList<proizvod> = ArrayList()
            db.collection("proizvod")
                .get()
                .addOnSuccessListener {  result ->
                    for(data in result.documents){
                        val proizvod = data.toObject(proizvod::class.java)

                        if(proizvod != null && (proizvod.name.toString().lowercase().contains(searchText.text.toString().lowercase()) || proizvod.description!!.lowercase().contains(searchText.text.toString().lowercase()))){
                            proizvod.id = data.id
                            list.add(proizvod)
                        }
                    }
                    proizvodAdapter = proizvodRecyclerAdapter()
                    proizvodAdapter.postItemsList(list)


                    view?.findViewById<RecyclerView>(R.id.proizvodView)?.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = proizvodAdapter
                    }
                }
        }

        return view
    }


}


