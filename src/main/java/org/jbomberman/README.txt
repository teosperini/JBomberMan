DA RISOLVERE:
(per adesso non si ripresenta il problema) sistemare TODO in view scontro con nemici
sistemare power up su bottom bar
aggiungere gestione profilo

FORSE sistemare se necessario movimento nemici che continuano a muoversi sullo stesso punto


1. aggiungere un text field dove inserire il nickname (in modo da far sapere al sistema chi sta giocando);
   ti consiglio di consentire un numero limitato di caratteri alfanumerici
2. visualizzare il nome del giocatore da qualche parte nella finestra di gioco (vicino al punteggio?)
3. memorizzare il punteggio massimo di un giocatore (record), il livello massimo raggiunto, il bilancio partite vinte/perse
4. prevedere una pagina "Scores"
5. prevedere una pagina "Credits"

UPDATE
1. il text field va aggiunto alla fine della partita, sia quando si decide di uscire che quando si vince / perde
    in teoria il gioco non dovrebbe finire mai, quindi per questo va aggiunto anche quando si vince al secondo livello,
    perchè la vittoria è solo per limitare il livello a 2, quindi insomma OGNI VOLTA CHE SI VA AL MENU
2. creare la pagina leaderboard, al posto di quella profilo, dove verranno visualizzati tutti i vari punteggi di tutte
    le persone in un elenco (usare ScrollPane(?))
3. nelle opzioni aggiungere possibilità disattivare musica