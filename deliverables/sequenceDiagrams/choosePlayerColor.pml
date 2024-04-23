@startuml
    group Choose Player Color [ok & failed due to unallowed player]
    ClientA -> Server : choosePlayerColor(playerName, playerColor)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate
    ClientB -> Server : choosePlayerColor(gameName, playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Choose Player Color [failed due to wrong game status]
     ClientA -> Server : choosePlayerColor(playerName, playerColor)
     Server -> ClientA : {status: "failed", message: "Cannot choose Player Color in current game status"}
end

group Choose Player Color [failed due color already chosen]
     ClientA -> Server : choosePlayerColor(playerName, playerColor)
     Server -> ClientA : {status: "failed", message: "This color is already chosen"}
end


note right of Server #aqua
Serve una classe Connection che associ a
un gameName la connessione del client
per sapere QUALE gameController chiamare?
end note
@enduml
