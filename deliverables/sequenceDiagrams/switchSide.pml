@startuml
    group Switch Side [ok]
    ClientA -> Server : switchSide(playerName, card)
    Server -> ClientA : {status: "success"}

end

note right of Server #aqua
Posso fare switchSide in ogni momento?
Idealmente sì, ma nella realtà come facciamo?
Facciamo update solo della playerBoard via network
e le Hand sono separate?
Così quando aggiorno la hand aggiorno solo quello
per questione di performance?

Non mischiare rete con dati
Con .map o .filter mappo player su socket

Potremmo firmare msg con nick player

oppure mappare UID

end note


@enduml
