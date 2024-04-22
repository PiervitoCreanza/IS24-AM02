@startuml
    group Switch Side [ok]
    ClientA -> Server : switchSide(playerName, card)
    Server -> ClientA : {status: "success"}

    ClientB -> Server : switchSide(playerName, card)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}

end

note right of Server #aqua
Qualsiasi giocatore puó fare switchSide in qualsiasi momento (suo turno o no),
ma la modifica avviene solamente localmente.

Posso fare switchSide quando voglio in locale (anche quando non é il mio turno).
Per evitare disallineamenti client/server:
- Se è stata girata, quando é il turno del player,
- PRIMA di inviare placeCard(), invio switchSide() al Model;
- cosí si evita anche sorta di "DDos" di switchSide()

L'errore qui sotto riproduce un caso in cui si prova  a girare carta SUL MODEL
quando non é il momento opportuno (come spiegato qui sopra)
end note

group Switch Side [failed due to wrong game status]
     ClientA -> Server : switchSide(playerName, card)
     Server -> ClientA : {status: "failed", message: "Cannot switchSide in current game status"}
end

@enduml
