package br.com.dbserver.lista.retrofitcrudprodutos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

const val CHAVE_EMAIL_PREFERENCE = "email"
const val CHAVE_USERNAME_PREFERENCE = "username"
const val NOME_SHARED_PREFERENCES = "user_preferences"

class LoginActivity : AppCompatActivity() {
    private val ti_email: TextInputLayout by lazy {
        findViewById<TextInputLayout>(R.id.ti_login_email)
    }
    private val ti_senha: TextInputLayout by lazy {
        findViewById<TextInputLayout>(R.id.ti_login_senha)
    }
    private val bt_login: MaterialButton by lazy {
        findViewById<MaterialButton>(R.id.bt_login)
    }
    private val bt_cadastro: MaterialButton by lazy {
        findViewById<MaterialButton>(R.id.bt_cadastro)
    }
    private val bt_loginGoogle: SignInButton by lazy {
        findViewById<SignInButton>(R.id.bt_login_google)
    }

    val auth = FirebaseAuth.getInstance()

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(this, gso)
    }
    private val RC_SIGN_IN = 453

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"

        bt_login.setOnClickListener {
            val email = ti_email.editText?.text.toString()
            val senha = ti_senha.editText?.text.toString()
            ti_email.error =  if(email.isEmpty()) "Insira um email" else null
            ti_senha.error =  if(senha.isEmpty()) "Insira uma senha" else null

            if (email.isEmpty() || senha.isEmpty()){
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener { task ->
                    loginUser(task)
                }
                .addOnFailureListener{e->
                    Toast.makeText(this, "Erro ao fazer Login: ${e.message}", Toast.LENGTH_LONG).show()
                }

        }

        val cadastroView: View = layoutInflater.inflate(R.layout.alert_cadastro, null, false)
        val alertCadastro = AlertDialog.Builder(this)
            .setView(cadastroView)
            .setTitle("Cadastrar Usuário")
            .setPositiveButton("Cadastrar"){dialogInterface, i ->
                val ti_email = cadastroView.findViewById<TextInputLayout>(R.id.ti_cadastro_email)
                val ti_senha = cadastroView.findViewById<TextInputLayout>(R.id.ti_cadastro_senha)

                val email = ti_email.editText?.text.toString()
                val senha = ti_senha.editText?.text.toString()

                ti_email.error = if (email.isEmpty()) "Insira um email" else null
                ti_senha.error = if (senha.isEmpty()) "Insira uma senha" else null

                if (email.isEmpty() || senha.isEmpty() ){
                    return@setPositiveButton
                }

                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnSuccessListener {task ->
                        loginUser(task)
                    }
                    .addOnFailureListener{e->
                        Toast.makeText(this, "Erro ao cadastrar Usuário: ${e.message}", Toast.LENGTH_LONG).show()
                    }


            }
            .create()


        bt_cadastro.setOnClickListener {
                alertCadastro.show()
        }

        bt_loginGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun loginUser(task: AuthResult) {
        val user = task.user
        getSharedPreferences(NOME_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().apply {
            putString(CHAVE_EMAIL_PREFERENCE, user?.email)
            putString(CHAVE_USERNAME_PREFERENCE, user?.displayName)
            apply()
        }

        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("DEBUG_LOGIN", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("DEBUG_LOGIN", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AAAA", "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.i("AAAA", user.toString())
                    task.result?.let {
                        loginUser(it)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AAAA", "signInWithCredential:failure", task.exception)

                }
            }
    }
}
