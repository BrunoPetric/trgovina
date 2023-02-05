package hr.ferit.brunopetric.trgovina

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var tvRedirectSignUp: Button
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText

     var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        val btnLogin = view.findViewById<Button>(R.id.loginButton)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        etEmail = view.findViewById(R.id.emailText)
        tvRedirectSignUp = view.findViewById(R.id.registerButton)
        etPass = view.findViewById(R.id.passwordText)

        val sp1 = activity?.getSharedPreferences("Login", MODE_PRIVATE)
        val unm = sp1?.getString("Unm", null)
        val pass = sp1?.getString("Psw", null)
        if(unm != null && pass != null){
            progressBar.visibility = View.VISIBLE
            etEmail.setText(unm)
            etPass.setText(pass)
            login()
        }


        // View Binding
        tvRedirectSignUp.setOnClickListener {
            val registerFragment = RegisterFragment()
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, registerFragment)
            fragmentTransaction?.commit()
        }

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            login()
            }
        // Inflate the layout for this fragment
        return view
    }

        private fun login() {
            val email = etEmail.text.toString()
            val pass = etPass.text.toString()

            if (email.isBlank() || pass.isBlank()) {
                Toast.makeText(activity, "Email i lozinka ne mogu biti prazni", Toast.LENGTH_SHORT).show()
                return
            }
            // calling signInWithEmailAndPassword(email, pass)
            // function using Firebase auth object
            // On successful response Display a Toast
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener() {
                if (it.isSuccessful) {
                    Toast.makeText(activity, "Uspjesna prijava kao: " + email, Toast.LENGTH_SHORT).show()

                    val sharedPref = activity?.getSharedPreferences("Login", MODE_PRIVATE)
                    val Ed = sharedPref!!.edit()
                    Ed.putString("Unm", email)
                    Ed.putString("Psw", pass)
                    Ed.commit()

                    val mainFragment = mainFragment()
                    val bundle = Bundle()
                    bundle.putString("USERNAME", etEmail.text.toString())
                    mainFragment.arguments = bundle
                    val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.fragmentContainerView, mainFragment)
                    fragmentTransaction?.commit()

                } else
                    Toast.makeText(activity, "Prijava nije uspjela", Toast.LENGTH_SHORT).show()
            }
        }
}
