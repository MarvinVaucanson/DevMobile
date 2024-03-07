package com.rslt.baptisteapp

import android.util.Log
import android.widget.ArrayAdapter

data class Contact(
    val nom: String,
    val prenom: String,
    val dateNaissance: String,
    val numeroTel: String,
    val email: String,
    val genre: String,
    val favori: Boolean
)


fun creerContactAPartirDeTexte(texte: String): Contact? {
    val nomRegex = Regex("Nom : (.*?) \\n")
    val prenomRegex = Regex("Prenom : (.*?) \\n")
    val dateNaissanceRegex = Regex("Date de naissance : (.*?) \\n")
    val numeroTelRegex = Regex("Numero de telephone : (.*?) \\n")
    val emailRegex = Regex("Email : (.*?) \\n")
    val genreRegex = Regex("Genre : (.*?) \\n")
    val favoriRegex = Regex("Ajouté aux favoris")

    val nom = nomRegex.find(texte)?.groupValues?.get(1)?.replace("\\s".toRegex(), "")
    val prenom = prenomRegex.find(texte)?.groupValues?.get(1)?.replace("\\s".toRegex(), "")
    val dateNaissance = dateNaissanceRegex.find(texte)?.groupValues?.get(1)
    val numeroTel = numeroTelRegex.find(texte)?.groupValues?.get(1)
    val email = emailRegex.find(texte)?.groupValues?.get(1)
    val genre = genreRegex.find(texte)?.groupValues?.get(1)
    val favori = favoriRegex.containsMatchIn(texte)

    Log.i("Nom extrait :", nom ?: "null")
    Log.i("Prénom extrait :", prenom ?: "null")
    Log.i("date extrait :", dateNaissance ?: "null")
    Log.i("numerotel extrait :", numeroTel ?: "null")
    Log.i("email extrait :", email ?: "null")
    Log.i("genre extrait :", genre ?: "null")
    Log.i("fav extrait :", (favori ?: "null").toString())

    return if (nom != null && prenom != null && dateNaissance != null && numeroTel != null && email != null && genre != null) {
        Contact(nom, prenom, dateNaissance, numeroTel, email, genre, favori)
    } else {
        null
    }
}


fun getByname(name: String, contacts: List<Contact>): Contact? {
    val splitName = name.split(" ")
    if (splitName.size != 2) {
        return null
    }
    val nom = splitName[0]
    val prenom = splitName[1]

    return contacts.find { it.nom == nom && it.prenom == prenom }
}



