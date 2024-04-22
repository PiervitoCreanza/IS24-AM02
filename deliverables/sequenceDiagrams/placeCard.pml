@startuml
    group Place Card [ok]
    ClientA -> Server : setGameCard(coordinate, gameCard)
    Server -> ClientA : {status: "success", []}
    ClientB -> Server : setGameCard(coordinate, gameCard)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Place Card [failed due to wrong position]
     ClientA -> Server : setGameCard(coordinate, gameCard)
     Server -> ClientA : {status: "failed", message: "You can't place a card here"}
end

group Place Card [failed due to wrong game status]
     ClientA -> Server : setGameCard(coordinate, gameCard)
     Server -> ClientA : {status: "failed", message: "Cannot setGameCard in current game status"}
end


@enduml
