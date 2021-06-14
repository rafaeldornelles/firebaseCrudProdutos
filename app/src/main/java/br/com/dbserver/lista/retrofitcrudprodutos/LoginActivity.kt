package br.com.dbserver.lista.retrofitcrudprodutos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.local.LocalStore
import com.google.firebase.ktx.Firebase

const val CHAVE_EMAIL_PREFERENCE = "email"
const val CHAVE_USERNAME_PREFERENCE = "username"
const val NOME_SHARED_PREFERENCES = "user_preferences"

class LoginActivity : AppCompatActivity() {
    val ti_email: TextInputLayout by lazy {
        findViewById<TextInputLayout>(R.id.ti_login_email)
    }
    val ti_senha: TextInputLayout by lazy {
        findViewById<TextInputLayout>(R.id.ti_login_senha)
    }
    val bt_login: MaterialButton by lazy {
        findViewById<MaterialButton>(R.id.bt_login)
    }
    val bt_cadastro: MaterialButton by lazy {
        findViewById<MaterialButton>(R.id.bt_cadastro)
    }

    val auth = FirebaseAuth.getInstance()

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

        val cadastroView: View = layoutInflater.inflate(R.layout.alert_cadastro, null)
        bt_cadastro.setOnClickListener {
            AlertDialog.Builder(this)
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
                .show()
        }
    }

    private fun loginUser(task: AuthResult) {
        val user = task.user
        getSharedPreferences(NOME_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().apply {
            putString(CHAVE_EMAIL_PREFERENCE, user?.email)
            putString(CHAVE_USERNAME_PREFERENCE, user?.displayName)
            commit()
        }

        startActivity(Intent(this, MainActivity::class.java))
    }
}
