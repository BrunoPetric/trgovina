package hr.ferit.brunopetric.trgovina


import android.annotation.SuppressLint

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import android.content.Context
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class postFragment : Fragment() {
    var imgURL : String = ""
    private val db = Firebase.firestore
    lateinit var imageView : ImageView
    private var usernameText: String = ""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        imageView = view.findViewById(R.id.imageView)
        usernameText = arguments?.getString("USERNAME").toString()

        val username = view.findViewById<TextView>(R.id.usernameText2)
        username.text = "Prijavljeni ste kao: $usernameText"

        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener{
        goBack()
        }
        val buttonSave = view.findViewById<Button>(R.id.publishButton2)
        val editName = view.findViewById<EditText>(R.id.editName)
        val editDescription = view.findViewById<EditText>(R.id.editDescription)
        val editPrice = view.findViewById<EditText>(R.id.editPrice)
        val editKontakt = view.findViewById<EditText>(R.id.editKontakt)

        buttonSave.setOnClickListener {
            if(editDescription.text.toString() != "" && editName.text.toString() != "" && editPrice.text.toString() != "") {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                val formatted = current.format(formatter)
            val proizvod = proizvod(usernameText, editDescription.text.toString(), editName.text.toString(), imgURL,editPrice.text.toString().toDouble(), formatted,editKontakt.text.toString(),"")
            db.collection("proizvod").add(proizvod)
            //recyclerAdapter.addItem(person)
            Toast.makeText(activity, "Proizvod uspjesno objavljen", Toast.LENGTH_LONG).show()
                goBack()
            }else{
                Toast.makeText(activity, "Popunite sva polja", Toast.LENGTH_LONG).show()
            }
        }
        val odaberiButton = view.findViewById<Button>(R.id.odaberiSlikuButton)
        odaberiButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(galleryIntent)

        }

        return view
    }
    private fun goBack(){
        val mainFragment = mainFragment()
        val bundle = Bundle()
        bundle.putString("USERNAME", usernameText)
        mainFragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentContainerView, mainFragment)
        fragmentTransaction?.commit()
    }

    val storageRef = Firebase.storage.reference


    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data

                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = getFileName(requireActivity().application, imageUri!!)

                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storageRef.child("file/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    //Toast.makeText(activity, storageRef.downloadUrl.toString(), Toast.LENGTH_LONG).show()
                    // using glide library to display the image
                    storageRef.child("file/$sd").downloadUrl.addOnSuccessListener {
                        Glide.with(this)
                            .load(it)
                            .into(imageView)
                        imgURL = it.toString()

                        Toast.makeText(activity,"Slika uspjesno ucitana", Toast.LENGTH_LONG).show()

                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }

            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }
}