@startuml
    group Place Card [ok]
    ClientA -> Server : placeCard(playerName,coordinate,card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].placeCard(playerName,coordinate,card)
    Server -> ClientA : {status: "success", []}
    ClientB -> Server : placeCard(playerName,coordinate,card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].validatePlayerTurn(playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Place Card [failed due to wrong position]
     ClientA -> Server : placeCard(playerName,coordinate,card)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].placeCard(playerName,coordinate,card)
     Server -> ClientA : {status: "failed", message: "You can't place a card here"}
end

group Place Card [failed due to wrong game status]
     ClientA -> Server : placeCard(playerName,coordinate,card)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].placeCard(playerName,coordinate,card)
     Server -> ClientA : {status: "failed", message: "Cannot setGameCard in current game status"}
end


@enduml
