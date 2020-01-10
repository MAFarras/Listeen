package com.example.listeen.RegisterLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listeen.R
import com.example.listeen.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        login2_text.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        register_button.setOnClickListener {
            val email = email_edt.text.toString()
            val password = password_edt.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Insert Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) {
                            return@addOnCompleteListener
                            val intent = Intent(this, RegisterActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            saveUserToFirebaseDatabase()
                            Toast.makeText(this, "Succesfully Registered", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        Log.d("Main", "Failed Registered: ${it.message}")
                        Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        //Respond to text change events in enterEmail//

        RxTextView.afterTextChangeEvents(email_edt!!)
            .skipInitialValue()
            .map {
                Lemail.error = null
                it.view().text.toString()
            }
            .debounce(400,
                TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(validateEmailAddress)
            .compose(retryWhenError {
                Lemail.error = it.message
            })
            .subscribe()

//Rinse and repeat for the enterPassword EditText//

        RxTextView.afterTextChangeEvents(password_edt!!)
            .skipInitialValue()
            .map {
                Lpassword.error = null
                it.view().text.toString()
            }
            .debounce(400, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(validatePassword)
            .compose(retryWhenError {
                Lpassword.error = it.message
            })
            .subscribe()
    }
    private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> = ObservableTransformer { observable ->
        observable.retryWhen { errors ->

            ///Use the flatmap() operator to flatten all emissions into a single Observable//

            errors.flatMap {
                onError(it)
                Observable.just("")
            }

        }
    }

//Define our ObservableTransformer and specify that the input and output must be a string//

    private val validatePassword = ObservableTransformer<String, String> { observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() }

//Only allow passwords that are at least 7 characters long//

                .filter { it.length > 7 }

//If the password is less than 7 characters, then throw an error//

                .singleOrError()

//If an error occurs.....//

                .onErrorResumeNext {
                    if (it is NoSuchElementException) {

//Display the following message in the passwordError TextInputLayout//

                        Single.error(Exception("Your password must be 7 characters or more"))

                    } else {
                        Single.error(it)
                    }
                }
                .toObservable()


        }

    }

//Define an ObservableTransformer, where we’ll perform the email validation//

    private val validateEmailAddress = ObservableTransformer<String, String> { observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() }

//Check whether the user input matches Android’s email pattern//

                .filter {
                    Patterns.EMAIL_ADDRESS.matcher(it).matches()

                }

//If the user’s input doesn’t match the email pattern...//

                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException) {

////Display the following message in the emailError TextInputLayout//

                        Single.error(Exception("Please enter a valid email address"))
                    } else {
                        Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private fun saveUserToFirebaseDatabase(){
        val name=user_edt.text.toString()
        val email=email_edt.text.toString()
        val uid=FirebaseAuth.getInstance().uid.toString()

        val ref = FirebaseDatabase.getInstance().getReference("/User/$uid")
        val user=
            User(uid, name, email)

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Saved Succesfully",Toast.LENGTH_SHORT).show()
            }
    }
}