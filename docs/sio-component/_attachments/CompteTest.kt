import com.banque.Compte
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CompteTest {

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun testGetNomClient() {
        var c : Compte = Compte("NomTest", 0.0,)
        assertEquals("NomTest", c.nomClient)
        c = Compte("NomTest2", 100.0,)
        assertEquals("NomTest2", c.nomClient)
    }

    @Test
    fun testCrediter(){
        val c = Compte("NomTest", 0.0,)
        c.crediter(50.0)
        assertEquals(50.0, c.solde)
        c.crediter(100.0)
        assertEquals(150.0, c.solde)
    }

    @Test
    fun testDebiter(){
        val c = Compte("NomTest", 100.0,)
        c.debiter(50.0)
        assertEquals(50.0, c.solde)
    }


    @Test
    fun testTransfertPossible(){
        val c1 = Compte("NomTest", 100.0, decouvert = 50)
        val c2 = Compte("NomTest", 100.0, decouvert = 0)
        c1.transfertTO(c2, 120.0)
        assertEquals(-20.0, c1.solde)
        assertEquals(220.0, c2.solde)
    }

    @Test
    fun testTransfertImpossible(){
        val c1 = Compte("NomTest", 100.0, decouvert = 0)
        val c2 = Compte("NomTest", 100.0, decouvert = 0)
        c1.transfertTO(c2, 120.0)
        assertEquals(100.0, c1.solde)
        assertEquals(100.0, c2.solde)
    }

    @Test
    fun testIsDebitPossible(){
        val c = Compte("NomTest", 100.0,)
        val opOk : Boolean = c.isDebitPossible(50.0)
        assertTrue(opOk)
    }

    @Test
    fun testIsDebitPossibleSoldeZero(){
        val c = Compte("NomTest", 100.0, decouvert = 0)
        val opOk : Boolean = c.isDebitPossible(100.0)
        assertTrue(opOk)
    }

    @Test
    fun testIsDebitImpossible(){
        val c = Compte("NomTest", 100.0, decouvert = 50)
        val opOk : Boolean = c.isDebitPossible(151.0)
        assertFalse(opOk)
    }

    @Test
    fun testJournalDesOperations(){
        val c = Compte("NomTest", 100.0, decouvert = 50)
        c.debiter(50.0)
        c.debiter(10.0)
        c.crediter(1000.0)
        val c2 = Compte("Toto", 10.0, decouvert = 0)
        c.transfertTO(c2, 500.0)
        // le transfert journalise 2 opérations : "transfert" " "debiter"
        assertEquals(5, c.journalOperations.size)
    }


    @Test
    fun testJournalDesOperationsDebitCredit(){
        val c = Compte("NomTest", 100.0, decouvert = 50)
        assertEquals(0, c.journalOperations.size)

        c.debiter(50.0)
        assertEquals(1, c.journalOperations.size)
        assertTrue(c.journalOperations[0].contains("DEBITER"))
        assertFalse(c.journalOperations[0].contains("CREDITER"))

        c.debiter(10.0)
        assertEquals(2, c.journalOperations.size)
        assertTrue(c.journalOperations[1].contains("DEBITER"))

        c.crediter(1000.0)
        assertEquals(3, c.journalOperations.size)
        assertTrue(c.journalOperations[2].contains("CREDITER"))
        assertFalse(c.journalOperations[2].contains("DEBITER"))

        val c2 = Compte("Toto", 10.0, decouvert = 0)
        c.transfertTO(c2, 500.0)
        // le transfert journalise 2 opérations : "transfert" et "debiter"
        assertTrue(c.journalOperations[3].contains("TRANSFERT"))
        assertTrue(c.journalOperations.last().contains("DEBITER"))

        // juste pour voir le journal de opérations de c
        println(c.journalOperations.joinToString("\n"))
    }


}