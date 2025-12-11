import ui.MainMenu;

/**
 * Point d'entrée principal du programme de collecte des déchets.
 * Lance le menu principal permettant de choisir entre le Thème 1 et le Thème 2.
 */
public class Main {
    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.run();
    }
}