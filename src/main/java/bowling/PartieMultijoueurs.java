package bowling;

import java.util.HashMap;
import java.util.Map;

/**
 * PartieMultijoueurs : Gère une partie de bowling pour plusieurs joueurs.
 */
public class PartieMultijoueurs implements IPartieMultiJoueurs {

	private String[] nomsDesJoueurs; 
	private Map<String, PartieMonoJoueur> partiesParJoueur; 
	private int indexJoueurCourant; 
	private boolean partieDemarree; 

	public PartieMultijoueurs() {
		partiesParJoueur = new HashMap<>();
		indexJoueurCourant = 0;
		partieDemarree = false;
	}

	@Override
	public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
		if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
			throw new IllegalArgumentException("Il faut au moins un joueur pour commencer une partie.");
		}

		this.nomsDesJoueurs = nomsDesJoueurs;
		
		for (String nom : nomsDesJoueurs) {
			partiesParJoueur.put(nom, new PartieMonoJoueur());
		}

		indexJoueurCourant = 0;
		partieDemarree = true;

		return prochainTirMessage();
	}

	@Override
	public String enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
		if (!partieDemarree) {
			throw new IllegalStateException("La partie n'est pas démarrée.");
		}

		String joueurCourant = nomsDesJoueurs[indexJoueurCourant];
		PartieMonoJoueur partieCourante = partiesParJoueur.get(joueurCourant);

		boolean continuerTour = partieCourante.enregistreLancer(nombreDeQuillesAbattues);

		if (!continuerTour) {
			indexJoueurCourant = (indexJoueurCourant + 1) % nomsDesJoueurs.length;
			
			if (tousLesJoueursOntTermine()) {
				partieDemarree = false;
				return "Partie terminée";
			}
		}

		return prochainTirMessage();
	} 

	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		if (!partiesParJoueur.containsKey(nomDuJoueur)) {
			throw new IllegalArgumentException("Joueur inconnu");
		}

		return partiesParJoueur.get(nomDuJoueur).score();
	}

	private String prochainTirMessage() {
		String joueurCourant = nomsDesJoueurs[indexJoueurCourant];
		PartieMonoJoueur partieCourante = partiesParJoueur.get(joueurCourant);

		return String.format("Prochain tir : joueur %s, tour n° %d, boule n° %d",
			joueurCourant,
			partieCourante.numeroTourCourant(),
			partieCourante.numeroProchainLancer());
	}

	public boolean tousLesJoueursOntTermine() {
		for (String joueur : nomsDesJoueurs) {
			if (!partiesParJoueur.get(joueur).estTerminee()) {
				return false;
			}
		}
		return true;
	}
}
