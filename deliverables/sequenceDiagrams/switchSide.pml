@startuml
    group Switch Side [ok]
    ClientA -> Server : switchSide(playerName, card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].switchCardSide(playerName, card)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate

    ClientB -> Server : switchSide(playerName, card)
    rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].validatePlayerTurn(playerName)
    Server -> ClientB : {status: failed, message: "It's not ClientB's turn"}

end

note right of Server #aqua
Any player can make switchSide at any time (his turn or not),
but the change happens only locally.

I can do switchSide whenever I want locally (even when it is not my turn).
To avoid client/server misalignment:
- If it has been turned, when it is the player's turn,
- BEFORE sending placeCard(), I send switchSide() to the Model;
- so it also avoids sort of “DDos” of switchSide().

The error below reproduces a case where you try to turn card ON MODEL
when it is not the appropriate time (as explained above)
end note

group Switch Side [failed due to wrong game status]
     ClientA -> Server : switchSide(playerName, card)
     rnote over Server #lightgrey : MainController.gameControllerMiddleWares[x].switchCardSide(playerName, card)
     Server -> ClientA : {status: "failed", message: "Cannot switchSide in current game status"}
end

@enduml
