package com.rslt.baptisteapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.util.Patterns
import android.widget.ImageView
import android.widget.RadioButton

class MainActivity : AppCompatActivity() {

    private lateinit var textnom: EditText
    private lateinit var textprenom: EditText
    private lateinit var datenaiss: EditText
    private lateinit var numtel: EditText
    private lateinit var email: EditText
    private lateinit var genre: RadioGroup
    private lateinit var fav: CheckBox
    private lateinit var butt1: Button
    private var REQUEST_GALLERY = 130
    private val REQUEST_IMAGE_CAPTURE = 120

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.buttonV)

        val buttongal = findViewById<Button>(R.id.buttonGallery)
        val buttonphoto = findViewById<Button>(R.id.buttonphoto)

        val textnom = findViewById<EditText>(R.id.editTextNom)
        val textprenom = findViewById<EditText>(R.id.editTextPrenom)
        val datenaisse = findViewById<EditText>(R.id.editTextDate)
        val numtel = findViewById<EditText>(R.id.editTextNum)
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val genre = findViewById<RadioGroup>(R.id.radioGroup)
        val fav = findViewById<CheckBox>(R.id.checkBox)


        buttongal.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_GALLERY)
        }

        buttonphoto.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

        button.setOnClickListener{
            Log.i("Informations :", fav.isChecked().toString())

            val nom = textnom.text.toString().trim()
            val prenom = textprenom.text.toString().trim()
            val emailSTR = email.text.toString().trim()
            val genreId = genre.checkedRadioButtonId

            // Vérifier si les champs obligatoires sont vides
            if (nom.isEmpty() || prenom.isEmpty() || emailSTR.isEmpty() || genreId == -1) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailSTR).matches()) {
                Toast.makeText(this, "Veuillez saisir une adresse email valide", Toast.LENGTH_SHORT).show()
            } else {

                val genreText = if (genreId != -1) {
                    val genreButton = findViewById<RadioButton>(genreId)
                    genreButton.text.toString()
                } else {
                    ""
                }

                val favori = if (fav.isChecked()) {
                    "Ajouté aux favoris"
                } else {
                    ""
                }

                Log.i("Informations :", favori)

                val texte: String = "Nom : ${textnom.text} \n" +
                        "Prenom : ${textprenom.text} \n" +
                        "Date de naissance : ${datenaisse.text} \n" +
                        "Numero de telephone : ${numtel.text} \n" +
                        "Email : ${email.text} \n" +
                        "Genre : ${genreText} \n" +
                        "${favori} \n"

                //val duration = Toast.LENGTH_SHORT

                //val toast = Toast.makeText(this, texte, duration) // in Activity
                //.show()

                val alert: AlertDialog.Builder = AlertDialog.Builder(this)
                alert
                    .setMessage(texte)
                    .setTitle("Voici les informations enregistrés")
                    .setPositiveButton("Ok") { dialog, which ->
                        val intent = Intent().apply {
                            putExtra("ContactData",texte)
                        }
                        setResult(Activity.RESULT_OK, intent)

                        finish()

                    }
                    .setNegativeButton("Annuler") { dialog, which ->
                        textnom.text.clear()
                        textprenom.text.clear()
                        datenaisse.text.clear()
                        numtel.text.clear()
                        email.text.clear()
                        genre.clearCheck()
                        fav.isChecked = false
                        dialog.dismiss()
                    }
                val dialog: AlertDialog = alert.create()
                dialog.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                val imageView = findViewById<ImageView>(R.id.imageView)
                imageView.setImageURI(selectedImageUri)
            } else {
                Toast.makeText(this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(imageBitmap)
        }
    }

}