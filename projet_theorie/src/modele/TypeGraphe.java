package modele;

/**
 * Types de graphes selon les hypothèses HO1, HO2, HO3.
 */
public enum TypeGraphe {
    NON_ORIENTE,    // HO1 : toutes les rues à double sens
    ORIENTE,        // HO2 : certaines rues à sens unique
    MIXTE           // HO3 : mélange de rues à double sens et sens unique
}

