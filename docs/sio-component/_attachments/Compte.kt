package com.banque

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Compte(val nomClient: String, solde: Double, val decouvert: Int = 0) {

    val journalOperations = mutableListOf<String>()

    val dateHeureMnSecFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss")

    var solde : Double = solde
        private set  // en lecture seule pour les autres

    fun crediter(montant: Double) {
        this.solde += montant
        journalOperations.add("${LocalDateTime.now().format(dateHeureMnSecFormatter)} ${this.nomClient} CREDITER : $montant")
    }

    fun debiter(montant: Double) {
      this.solde -= montant
      journalOperations.add("${LocalDateTime.now().format(dateHeureMnSecFormatter)} ${this.nomClient} DEBITER : $montant")
    }

    fun isDebitPossible(montant: Double): Boolean {
        return this.solde + this.decouvert - montant >= 0
    }

    fun transfertTO(other: Compte, montant: Double) {
        if (this.isDebitPossible(montant)) {
            journalOperations.add(
                "${LocalDateTime.now().format(dateHeureMnSecFormatter)} ${this.nomClient} TRANSFERT : $montant to ${other.nomClient}"
            )
            other.crediter(montant)
            this.debiter(montant)
        }
    }

}