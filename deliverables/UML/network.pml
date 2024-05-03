@startuml
package Network {
    package message {
        interface Message {}
        abstract class ServerMessage implements Message {
            -MessageStatusEnum status
            +getMessageStatus()
        }
        class ErrorServerMessage extends ServerMessage {
            -String message
            +ErrorServerMessage(String message)
        }
        class SuccessServerMessage extends ServerMessage {
            -ServerActionEnum action
            +GameServerMessage(ServerActionEnum action)
        }
        class ViewUpdateMessage extends SuccessServerMessage {
            -GameControllerView view
            +ViewUpdateServerMessage(GameControllerView view)
        }
        abstract class ClientMessage implements Message {}
        class GetGamesClientMessage extends ClientMessage {}
        class CreateGameClientMessage extends ClientMessage {}
        class JoinGameClientMessage extends ClientMessage {}
        class ChoosePlayerColorClientMessage extends ClientMessage {}
        class SetPlayerObjectiveClientMessage extends ClientMessage {}
        class PlaceCardClientMessage extends ClientMessage {}
        class DrawCardFromFieldClientMessage extends ClientMessage {}
        class DrawCardFromResourceDeckClientMessage extends ClientMessage {}
        class DrawCardFromGoldDeckClientMessage extends ClientMessage {}
        class SwitchCardSideClientMessage extends ClientMessage {}

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


        interface ServerMessageHandler {
            +void sendMessage(ServerMessage message)
            +void closeConnection()
        }

        note left of ServerMessageHandler
            Interface that is implemented by the classes that handle the messages
        end note

        class TCPServerAdapter implements ServerMessageHandler {
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

        class RMIClientConnectionHandler implements ServerMessageHandler, ClientActions {
            +void sendMessage(ServerMessage message)
        }


      class "NetworkCommandMapper" implements "ClientActions" {
                  -HashSet<ServerMessageHandler> connections
                  -HashMap<String gameName, ServerMessageHandler> connectionMap
                  -MainController mainController
                  -void broadcastMessage(ServerMessage msg, String gameName)
                  +void addConnection(ServerMessageHandler connection, String gameName)
                  +void removeConnection(ServerMessageHandler connection, String gameName)
              }
              NetworkCommandMapper *-- ServerMessageHandler
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
            class RMIServerConnectionHandler implements ClientMessageHandler, ServerActions {
                +void sendMessage(ClientMessage message)
            }
            ClientCommandMapper *-- ClientMessageHandler
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