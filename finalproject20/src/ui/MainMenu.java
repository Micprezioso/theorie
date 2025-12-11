package ui;

import java.util.Scanner;

/**
 * Menu principal qui permet de choisir entre le Thème 1 et le Thème 2.
 */
public class MainMenu {
    private Scanner scanner;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Affiche le menu principal et gère les interactions utilisateur.
     */
    public void run() {
        boolean running = true;
        
        while (running) {
            printMenu();
            int choice = readInt("Votre choix : ");
            
            switch (choice) {
                case 1:
                    runTheme1();
                    break;
                case 2:
                    runTheme2();
                    break;
                case 0:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            
            if (running && choice != 0) {
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  PROJET THÉORIE DES GRAPHES - COLLECTE DES DÉCHETS");
        System.out.println("=".repeat(60));
        System.out.println("1) Thème 1 : Ramassage des encombrants et tournées de rues");
        System.out.println("2) Thème 2 : Optimiser les ramassages des points de collecte");
        System.out.println("0) Quitter");
        System.out.println("=".repeat(60));
    }

    private void runTheme1() {
        Theme1Menu theme1Menu = new Theme1Menu(scanner);
        theme1Menu.run();
    }

    private void runTheme2() {
        Theme2Menu theme2Menu = new Theme2Menu(scanner);
        theme2Menu.run();
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre entier : ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne
        return value;
    }
}

