@startuml

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
    -ArrayList<GameRecord> games
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

abstract class ClientNetworkMessage {
    -PlayerActionsEnum playerAction
    -<Record T> parameters
}
class PlaceCardClientMessage extends ClientNetworkMessage {
    -Coordinate coordinate
    -Card card
}
abstract class ServerConnection {
    -GameControllerMiddleware gameController
}

abstract class ClientConnection {
}


class TCPClientConnection extends ClientConnection implements PlayerActions {
    -Socket socket
}
class RMIClientConnection extends ClientConnection implements PlayerActions {
    -Loggable remoteHost
}
class TCPServerConnection extends ServerConnection implements ServerActions {
    -ServerSocket serverSocket
}
class RMIServerConnection extends ServerConnection implements ServerActions {
    -Loggable remoteHost
}


@enduml