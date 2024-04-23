@startuml
    group Choose Objective [ok]
    ClientA -> Server : setPlayerObjective(playerName, card)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
    ClientB -> Server : setPlayerObjective(playerName, card)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Choose Objective [failed due to wrong game status]
     ClientA -> Server : setPlayerObjective(playerName, card)
     Server -> ClientA : {status: "failed", message: "Cannot choose Objective Card in current game status"}
end


note right of ClientA
    IDEA: per non duplicare msg, si potrebbe
    SUCCESS -> invio indietro VirtualView
    FAILED -> invio indietro specifico messaggio di errore
end note
@enduml
