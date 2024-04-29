@startuml
    group Draw Card from Field [ok]
    ClientA -> Server : drawCardFromField(playerName, card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].drawCardFromField(playerName,card)
    Server -> ClientA : {status: "success", []}
    ClientB -> Server : drawCardFromField(playerName, card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].validatePlayerTurn(playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}
end

group Draw Card from Field [failed due to wrong position]
     ClientA -> Server : drawCardFromField(playerName, card)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].drawCardFromField(playerName,card)
     Server -> ClientA : {status: "failed", message: "Card not found in playerName field"}
end

group Draw Card from Field [failed due to wrong game status]
     ClientA -> Server : drawCardFromField(playerName, card)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].drawCardFromField(playerName,card)
     Server -> ClientA : {status: "failed", message: "Cannot drawCard in current game status"}
end


@enduml
