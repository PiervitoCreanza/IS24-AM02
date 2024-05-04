@startuml
package Network {
    package message {
        interface Message {}
        abstract class ServerMessage implements Message {
            -ServerActionEnum serverAction
            +getServerAction()
        }
        class MessageServerSubClasses extends ServerMessage {
            -UpdateViewServerMessage
            -DeleteGameServerMessage
            -GetGamesServerMessage
            -ErrorServerMessage
        }
        abstract class ClientMessage implements Message {
            -PlayerActionEnum playerAction
            #String gameName
            #String playerName
            +PlayerActionEnum getPlayerAction()
            +String getGameName()
            +String getPlayerName()
            +int getNPlayers()
            +String getPlayerColor()
            +ObjectiveCard getObjectiveCard()
            +Coordinate getCoordinate()
            +GameCard getGameCard()
        }
        class ClientMessageSubClasses extends ClientMessage {
            -GetGamesClientMessage
            -CreateGameClientMessage
            -DeleteGameClientMessage
            -JoinGameClientMessage
            -ChoosePlayerColorClientMessage
            -SetPlayerObjectiveClientMessage
            -PlaceCardClientMessage
            -DrawCardFromFieldClientMessage
            -DrawCardFromResourceDeckClientMessage
            -DrawCardFromGoldDeckClientMessage
            -SwitchCardSideClientMessage
        }
    }
    interface Observable {
                +void setObserver(Observer observer)
                +void removeObserver(Observer observer)
            }
    class TCPConnectionHandler implements Observable {
        -ArrayList<Observer> observers
        +void setObserver(Observer observer)
        +void removeObserver(Observer observer)
        +void sendMessage(String msg)
    }

    interface Observer<T> {
        +void notify(T msg)
    }

    note bottom of TCPConnectionHandler
            TCPConnectionHandler is the main class that handles the TCP connection
            It is Observable because it has to notify a class when it receives a message
        end note

    package "Server" {


        interface MessageHandler {
            +void sendMessage(ServerMessage message)
            +void closeConnection()
        }

        note left of MessageHandler
            Interface that is implemented by the classes that handle the messages
        end note

        class TCPServerAdapter implements MessageHandler {
                -TCPConnectionHandler connection
                +void notify(String msg)
                +void sendMessage(ServerMessage message)
        }
        TCPServerAdapter <|.. Observer
        TCPServerAdapter *-- TCPConnectionHandler

        note left of TCPServerAdapter
            This class converts the messages from text to objects and vice versa.
            It also sends the messages to the clients
        end note

        interface RMIClientActions extends ClientActions {
            +getGames(String ip, Int port,...)
            +joinGame(String ip, Int port, String gameName,...)
            +createGame(String ip, Int port, String gameName, ...)
        }

        class RMIServerAdapter implements MessageHandler, ClientActions {
            -String playerName
            -String gameName
            -ClientActions stub
            +sendMessage(ServerMessage message)
            +closeConnection()

        }
        RMIServerAdapter *-- RMIServerConnectionHandler

        class RMIServerConnectionHandler implements RMIClientActions {
            +void sendMessage(ServerMessage message)
            -connectToClient(String ip, Int port)
        }


      class "NetworkCommandMapper" implements "ClientActions" {
                  -HashSet<MessageHandler> connections
                  -HashMap<String gameName, MessageHandler> connectionMap
                  -MainController mainController
                  -void broadcastMessage(ServerMessage msg, String gameName)
                  +void addConnection(MessageHandler connection, String gameName)
                  +void removeConnection(MessageHandler connection, String gameName)
              }
              NetworkCommandMapper *-- MessageHandler
        note left of NetworkCommandMapper
                Executes actions on the controllers, retrieves the view
                and sends it performing .sendMessage(ServerMessage)
                on all the clients that need to receive the message
            end note
        }

        package "Client"{
            interface ClientMessageHandler {
                +void sendMessage(ClientMessage message)
                +void closeConnection()
            }
            class "ClientCommandMapper" implements "ServerActions" {
                -ClientMessageHandler connection
            }
            class RMIClientConnectionHandler implements ServerActions {
                +void receiveMessage(ServerMessage message)
            }
            Client *-- RMIClientConnectionHandler
            ClientCommandMapper *-- ClientMessageHandler

            interface RMIClientActions {
                +connect(String ip, Int port);
            }

            class RMIClientAdapter implements ClientMessageHandler {
                -ServerActions stub
                +sendMessage(ServerMessage message)
                +closeConnection()
            }

            class TCPClientAdapter implements ClientMessageHandler {
                -TCPConnectionHandler connection
                +void sendMessage(ClientMessage message)
            }
            TCPClientAdapter <|.. Observer
            TCPClientAdapter *-- TCPConnectionHandler
            note left of ClientCommandMapper
                This class sends the messages to the server
                and updates the view
            end note
        }
    }
}
@enduml