@startuml
package ServerNetworkMessage {
    abstract class ServerNetworkMessage {
        -status: String
        +getStatus(): String
        +setStatus(): void
    }

    class ErrorMessage extends ServerNetworkMessage {
        -message: String
        +getMessage(): String
        +setMessage(): void
    }

    class GetGamesMessage extends ServerNetworkMessage {
        -HashSet<GameRecord> games
    }
    class GameRecord {
        -String gameName
        -int joinedPlayers
        -int maxAllowedPlayers
    }
    GameRecord -- GetGamesMessage

    abstract class ViewUpdateMessage extends ServerNetworkMessage {
        -PlayerActionsEnum playerAction
    }

    class PlaceCardViewUpdateMessage extends ViewUpdateMessage {
        -Coordinate coordinate
        -Card card
    }
}

package ClientNetworkMessage {

    abstract class ClientNetworkMessage {
        -PlayerActionsEnum playerAction
    }
    class PlaceCardClientMessage extends ClientNetworkMessage {
        -Coordinate coordinate
        -Card card
    }
}


package Connection {
    package ServerConnection {
        abstract class ServerConnection {
            -GameControllerMiddleware gameController
        }
        class TCPServerConnection extends ServerConnection implements ServerActions {
            -ServerSocket serverSocket
        }
        class RMIServerConnection extends ServerConnection implements ServerActions {
            -Loggable remoteHost
        }
    }

    package ClientConnection {
        abstract class ClientConnection {
        }
        class TCPClientConnection extends ClientConnection implements PlayerActions {
            -Socket socket
        }
        class RMIClientConnection extends ClientConnection implements PlayerActions {
            -Loggable remoteHost
        }
    }

}

@enduml