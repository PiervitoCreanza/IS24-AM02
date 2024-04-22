@startuml
    group Draw Resource Card [ok]
    ClientA -> Server : drawCardFromResourceDeck(playerName)
    Server -> ClientA : {status: "success", []}
    ClientB -> Server : drawCardFromResourceDeck(playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Draw Resource Card [failed due to empty deck]
     ClientA -> Server : drawCardFromResourceDeck(playerName)
     Server -> ClientA : {status: "failed", message: "Deck is empty"}
end

group Draw Resource Card [failed due to wrong game status]
     ClientA -> Server : drawCardFromResourceDeck(playerName)
     Server -> ClientA : {status: "failed", message: "Cannot drawCard in current game status"}
end


@enduml
