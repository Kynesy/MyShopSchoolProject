package View;

import Model.Feedback;

public interface IArticlePannels {
    int getSelected(); //ritorna l'id dell'articolo selezionato o visualizzato

    Feedback getSelectedFeedback(); //ritorna il feedback selezionato, vale solo per i pannelli dei
    // dettagli che mostrano feedback (servizio e prodotto)
}
