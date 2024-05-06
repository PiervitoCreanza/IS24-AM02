@startuml
    group Draw Gold Card [ok]
    ClientA -> Server : drawCardFromGoldDeck(playerName)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].drawCardFromGoldDeck(playerName)
    Server -> ClientA : {status: "success", []}
    ClientB -> Server : drawCardFromGoldDeck(playerName)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].validatePlayerTurn(playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Draw Gold Card [failed due to empty deck]
     ClientA -> Server : drawCardFromGoldDeck(playerName)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].drawCardFromGoldDeck(playerName)
     Server -> ClientA : {status: "failed", message: "Deck is empty"}
end

group Draw Gold Card [failed due to wrong game status]
     ClientA -> Server : drawCardFromGoldDeck(playerName)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].drawCardFromGoldDeck(playerName)
     Server -> ClientA : {status: "failed", message: "Cannot drawCard in current game status"}
end


@enduml
