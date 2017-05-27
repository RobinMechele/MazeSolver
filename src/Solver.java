package mazesolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/*
 * In de Solver klasse staan alle methoden die gebruikt worden om de
 * "generated" doolhof op te lossen.
 */

public class Solver {
	Scanner scanner = new Scanner(System.in);
	private int grootte;
	private int startKolom;
	private int startRij;
	private char [][] doolhof;
	
	/*
	 * makeMaze() methode zal een random doolhof genereren met de gekozen grootte door de gebruiker,
	 * en zal dit opslaan in een tekstbestand genaamt "maze.txt".
	 */
	
	public void makeMaze() throws IOException{
		System.out.println("Geef een getal in die de grootte van het doolhof zal bepalen.");
		String userInput = scanner.nextLine();
		int grootte = Integer.parseInt(userInput);
		scanner.close();
		Maze mazeGen = new Maze(grootte);
		mazeGen.print();
		List<String> tekst = mazeGen.toListOfStrings();
		String bestandsnaam = "maze.txt";
		Path bestand = Paths.get(bestandsnaam);
		Files.write(bestand, tekst, Charset.forName("UTF-8"));
	}
	
	/*
	 * loadMaze() methode zal het bestand inlezen en een paar parameters in het tekstbestand aanpassen,
	 * zoals het toevoegen van 'S', 'E', etc. Dit wordt gebruikt in de methode solver() om te weten waar het begin van de doolhof is,
	 * alsook het einde.
	 */
	
	@SuppressWarnings("resource")
	public void loadMaze() throws IOException{
		File mazeBestand = new File("maze.txt");
		BufferedReader read = new BufferedReader(new FileReader(mazeBestand));
		int teller = 1;
		String wut = null;
		String lijnTekst = read.readLine();
		grootte = lijnTekst.length();
		doolhof = new char[grootte][grootte];
		
		for(int i = 0; i < grootte; i++){
			doolhof[0][i] = lijnTekst.charAt(i);
		}
		
		while((wut = read.readLine()) != null){
			for(int i = 0; i < wut.length(); i++){
				doolhof[teller][i] = wut.charAt(i);
			}
			teller++;
		}
		
		for (int i = 0; i < grootte; i ++){
			if(doolhof[i][0] != 'X'){
				startRij = i;
				startKolom = 0;
				doolhof[startRij][startKolom] = 'S';				
			}
			if(doolhof[i][grootte - 1] != 'X'){
				doolhof[i][grootte - 1] = 'E';
			}
		}
	}
	
	/*
	 *solver() methode zal ervoor zorgen om het doolhof op te lossen volgens recursieve algoritme, dat ook te vinden is op
	 *https://en.wikipedia.org/wiki/Maze_solving_algorithm 
	 *
	 *Wanneer hij niet oplosbaar is, dus een false teruggeeft, dan zal in de methode printResults() dit ook uitgeprint worden op de console.
	 *Indien hij wel is opgelost, dan zal hij de opgeloste maze met deze methode uitprinten.
	 */
	
	public boolean solver(int rij, int kolom){
		if(doolhof[rij][kolom] == 'E'){
			return true;
		}
		
		if(doolhof[rij][kolom] == 'X' || doolhof[rij][kolom] == 'A'){
			return false;
		}
		
		doolhof[rij][kolom] = 'A';
		
		//LINKS
		if(rij != 0){
			if(solver(rij-1, kolom)){
				doolhof[rij][kolom] = '@';
				return true;
			}
		}
		
		//RECHTS
		if(rij != grootte-1){
			if(solver(rij+1, kolom)){
				doolhof[rij][kolom] = '@';
				return true;
			}
		}
		
		//BOVEN
		if(kolom != 0){
			if(solver(rij, kolom-1)){
				doolhof[rij][kolom] = '@';
				return true;
			}
		}
		
		//ONDER
		if(kolom != grootte - 1){
			if(solver(rij, kolom + 1)){
				doolhof[rij][kolom] = '@';
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * De methode printDoolhof() zal ervoor zorgen dat de gebruikte letters om de maze op te lossen te wijzigen om dit duidelijker te maken voor
	 * de gebruiker. De verkeerde wegen (de letters A) worden dan ook naar een oorspronkelijke waarde teruggezet (" ").
	 */
	
	public void printDoolhof(){
		for(int i = 0; i < doolhof.length; i++){
			for(int j = 0; j < doolhof.length; j++){
				if(doolhof[i][j] == 'A'){
					doolhof[i][j] = ' ';
				}
				if(doolhof[i][j] == '@'){
					doolhof[i][j] = '#';
				}
			}
		}
		for(int i = 0; i < doolhof.length; i++){
			System.out.println(doolhof[i]);
		}
	}
	
	//Methode om het eindresultaat uit te printen.
	//Indien dit niet lukt, zal dit ook uitgeprint worden.
	public void printResults(){
		System.out.println("");
		System.out.println("Doolhof (niet opgelost)");
		printDoolhof();
		System.out.println("");
		System.out.println("Opgeloste doolhof");
		if(solver(startRij,startKolom)){
			printDoolhof();
			System.out.println("");
			System.out.println("Doolhof is oplosbaar :)");
		}
		else{
			System.out.println("Er is een fout in de doolhof en kan niet opgelost worden");
		}
	}
}
