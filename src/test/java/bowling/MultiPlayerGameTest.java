package bowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MultiPlayerGameTest {

	private PartieMultijoueurs partie;

	@BeforeEach
	public void setUp() {
		partie = new PartieMultijoueurs();
	}

	@Test
	void testDemarragePartie() {
		String message = partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 1", message,
			"Le démarrage doit indiquer le premier joueur avec le premier tir.");
	}

	@Test
	void testDemarrageAvecListeVide() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			partie.demarreNouvellePartie(new String[]{});
		});
		assertEquals("Il faut au moins un joueur pour commencer une partie.", exception.getMessage(),
			"Démarrer avec une liste vide doit lever une exception appropriée.");
	}

	@Test
	void testTousLesJoueursOntTermineImmediatementApresDemarrage() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		assertFalse(partie.tousLesJoueursOntTermine(),
			"Juste après le démarrage, aucun joueur ne doit avoir terminé.");
	}
	
	@Test
	void testScorePourAvecSpare() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		
		partie.enregistreLancer(7); 
		partie.enregistreLancer(3);  
		partie.enregistreLancer(5); 
		partie.enregistreLancer(3);  
		partie.enregistreLancer(4);
		
		assertEquals(18, partie.scorePour("Pierre"), "Le score de Pierre doit inclure le bonus du spare (7 + 3 + 4).");
		assertEquals(8, partie.scorePour("Paul"), "Le score de Paul doit être 8 (5 + 3).");
	}
	
	@Test
	void testScorePourJoueurInconnu() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			partie.scorePour("Jacques");
		});

		assertEquals("Joueur inconnu", exception.getMessage(), "Une exception doit être levée pour un joueur inconnu.");
	}

	@Test
	void testPremierLancer() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		String message = partie.enregistreLancer(5);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", message,
			"Après le premier lancer, le message doit indiquer la deuxième boule.");
	}

	@Test
	void testStrike() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		String message = partie.enregistreLancer(10); // Strike
		assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", message,
			"Après un strike, le joueur suivant doit être appelé immédiatement.");
	}

	@Test
	void testSpare() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		partie.enregistreLancer(7); // Boule 1
		String message = partie.enregistreLancer(3); // Boule 2 - Spare
		assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", message,
			"Après un spare, le joueur suivant doit être appelé.");
	}

	@Test
	void testPassageEntreJoueurs() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		partie.enregistreLancer(5); // Pierre : Boule 1
		partie.enregistreLancer(3); // Pierre : Boule 2
		String message = partie.enregistreLancer(4); // Paul : Boule 1
		assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 2", message,
			"Après le premier lancer de Paul, le message doit indiquer la deuxième boule.");
	}
}
