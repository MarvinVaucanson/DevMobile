package com.rslt.baptisteapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListAdapter
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import android.widget.ListView
import com.rslt.baptisteapp.Contact
import com.rslt.baptisteapp.creerContactAPartirDeTexte

const val EXTRA_MESSAGE = "Change de fenetre"

class MainActivityHome : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)
        listView = findViewById(R.id.listView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList())
        listView.adapter = adapter

        val button = findViewById<Button>(R.id.buttonHome)
        button.setOnClickListener {
            changerfenetre()
        }
    }

    private fun changerfenetre(){
        val intent = Intent(this, MainActivity::class.java)
        mainActivityResultLauncher.launch(intent)
    }

    private val mainActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                result.data?.also { data ->
                    val testData = data.getStringExtra("ContactData")
                    Log.i("Informations De Retour :", testData.toString())

                    val contact = creerContactAPartirDeTexte(testData.toString()) //on creer un objet contact

                    contact?.let { //on utilise l'objet contact
                        Log.i("Contact créé :", "${it.nom} ${it.prenom}")
                        adapter.add("${it.nom} ${it.prenom}") //on ajoute à l'adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
}


