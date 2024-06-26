@startuml
    group Get Games [ok]
    ClientA -> Server : getGames()
    rnote over Server #lightgrey : MainController.getGames()
    Server -> ClientA : {status: "success", data : [{gameName: "Game1", joinedPlayers: 1, maxAllowedPlayers: 3}]}
end

note right of ClientA
    getGames() gets called once the client is started.
    It fetches all the games that are available to join.
end note
@enduml
