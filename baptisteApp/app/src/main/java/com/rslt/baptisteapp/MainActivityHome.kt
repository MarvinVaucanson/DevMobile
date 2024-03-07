package com.rslt.baptisteapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

const val EXTRA_MESSAGE = "Change de fenetre"

class MainActivityHome : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listcontact = mutableListOf<Contact>()

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

        listView.setOnItemClickListener { adapterView, view, position, id ->
            val selectedItem = adapter.getItem(position) as String
            Log.i("Objet selectionné :", selectedItem)
            val thecontact = getByname(selectedItem, listcontact)
            Log.i("Objet selectionné :", thecontact.toString())
            if (thecontact != null) {
                val toastMessage = "Nom: ${thecontact.nom}, Prénom: ${thecontact.prenom}, Num de tel: ${thecontact.numeroTel},Date de naissance: ${thecontact.dateNaissance}"
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Contact introuvable", Toast.LENGTH_LONG).show()
            }
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = adapter.getItem(position) as String
            adapter.remove(selectedItem)
            val thecontact = getByname(selectedItem, listcontact)
            listcontact.remove(thecontact)
            true
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

                    if (contact != null) {
                        listcontact.add(contact)
                        Log.i("Contact créé :", "${contact.nom} ${contact.prenom}")
                    } else {
                        Log.e("Erreur :", "Le contact créé est null")
                    }

                    contact?.let { //on utilise l'objet contact
                        Log.i("Contact créé :", "${it.nom} ${it.prenom}")
                        adapter.add("${it.nom} ${it.prenom}") //on ajoute à l'adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
}


