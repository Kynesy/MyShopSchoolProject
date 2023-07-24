Setup: a- Il programma è strettamente legato al database di MySql. In src->DbInterface->DbUser va inserito username
          e password del database in locale
       b- Per inviare mail vanno inserite le credenziali della mail che effettua l'invio in
	  scr->Business->MailUtils

1. Affinchè diano esisto positivo i junit test, è necessario che il db sia vuoto
2. Il file del database si chiama tmp e non siamo riusciti a cambiare il nome
3. Se il database di tmp non è il database predefinito su mysql, alcuni metodi dao potrebbero dare errori,
   anche se non abbiamo testato il programma con il db tmp non predefinito.
4. Il programma è abbastanza guidato all'avvio in quanto molte funzioni sono bloccate a database vuoto.
   Per usarlo è necessario prima creare un amministratore con il pulsante in alto a destra. Una volta creato,
   sarà possibile creare tutte le entità necessarie al funzionamento di MyShop(produttori, punti vendita,
   categorie). Ciò darà la possibilità di creare articoli e sbloccherà anche la possibilità di registrarsi.