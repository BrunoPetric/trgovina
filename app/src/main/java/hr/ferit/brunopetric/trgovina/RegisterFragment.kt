package hr.ferit.brunopetric.trgovina

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {


    private lateinit var etEmail: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvRedirectLogin: Button

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        super.onCreate(savedInstanceState)

        etEmail = view.findViewById(R.id.emailText2)
        etConfPass = view.findViewById(R.id.passwordText3)
        etPass = view.findViewById(R.id.passwordText2)
        btnSignUp = view.findViewById(R.id.registerButton2)
        tvRedirectLogin = view.findViewById(R.id.loginButton2)

        auth = Firebase.auth

        btnSignUp.setOnClickListener {
            signUpUser()
        }
        tvRedirectLogin.setOnClickListener {
            val loginFragment = LoginFragment()
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, loginFragment)
            fragmentTransaction?.commit()
        }


        return view
    }


    private fun signUpUser() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()

        // check pass
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(activity, "Email i lozinka ne mogu biti prazni", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(activity, "Lozinka i potvrda lozinke nisu iste", Toast.LENGTH_SHORT)
                .show()
            return
        }
        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener() {
            if (it.isSuccessful) {
                Toast.makeText(activity, "Registracija uspjesna", Toast.LENGTH_SHORT).show()
                val loginFragment = LoginFragment()
                val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.fragmentContainerView, loginFragment)
                fragmentTransaction?.commit()
            } else {
                Toast.makeText(activity, "Registracija nije uspjela!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}