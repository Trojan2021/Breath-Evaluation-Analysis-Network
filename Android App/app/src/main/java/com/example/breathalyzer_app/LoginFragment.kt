package com.example.breathalyzer_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.Log;
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment(R.layout.fragment_login) {
    companion object {
        private const val TAG = "Log In Fragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = Firebase.auth
        // Find the button in the fragment's layout
        val buttonContinue = view.findViewById<Button>(R.id.btnLogIn)
        val buttonCreateNew = view.findViewById<Button>(R.id.btnCreateNew)

        val usernameField = view.findViewById<EditText>(R.id.ditUsername)
        val passwordField = view.findViewById<EditText>(R.id.ditUserPassword)

        // Set the click listener on the button
        buttonContinue.setOnClickListener {
            var username = usernameField.text.toString()
            var password = passwordField.text.toString()

            // Checking if login fields are not empty
            if (Account.checkLoginInputs(requireContext(), username, password)) {
                // Contacting server to login
                Account.login(auth, requireActivity(), username, password) { success ->
                    if (success) {
                        val user = auth.currentUser
                        Toast.makeText(
                            requireContext(),
                            "Login Success",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val userAccount = Account(user?.uid, username)
                        val intent = Intent(requireContext(), FriendViewActivity::class.java).apply {
                            putExtra ("user", userAccount)
                        }
                        startActivity(intent)
                        Log.i(TAG, "Switching to Home Screen")
                    }
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Login Failed",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
            else {
                Toast.makeText(requireContext(), "Missing fields", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCreateNew.setOnClickListener {
            val CreateAccountFrag = CreateAccountFragment()

            // Actually replace fragment
            parentFragmentManager.beginTransaction().replace(R.id.frgLoginContainer, CreateAccountFrag).addToBackStack(null).commit()
            Log.i(TAG,"Switching to Create Account")
        }
    }
}