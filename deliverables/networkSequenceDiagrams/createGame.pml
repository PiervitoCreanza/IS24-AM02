@startuml
group Create Game [ok]
    ClientA -> Server : createGame(gameName,nPlayers,playerName)
    rnote over Server #lightgrey : MainController.createGame(gameName,nPlayers,playerName)
    Server -> ClientA : {status: "success"}, VirtualViewUpdate

end
group Create Game [failed]
ClientA -> Server : createGame(gameName,nPlayers,playerName)
rnote over Server #lightgrey : MainController.createGame(gameName,nPlayers,playerName)
    Server -> ClientA : {status: "failed", message: "gameName already exists"}
end

group Create Game [failed]
ClientA -> Server : createGame(gameName,nPlayers,playerName)
rnote over Server #lightgrey : MainController.createGame(gameName,nPlayers,playerName)
    Server -> ClientA : {status: "failed", message: "invalid number of players"}
end

group Create Game [failed]
ClientA -> Server : createGame(gameName,nPlayers,playerName)
rnote over Server #lightgrey : MainController.createGame(gameName,nPlayers,playerName)
    Server -> ClientA : {status: "failed", message: "invalid gameName"}
end

group Create Game [failed]
ClientA -> Server : createGame(gameName,nPlayers,playerName)
rnote over Server #lightgrey : MainController.createGame(gameName,nPlayers,playerName)
    Server -> ClientA : {status: "failed", message: "invalid playerName"}
end


@enduml
