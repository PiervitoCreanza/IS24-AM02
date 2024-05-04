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
        class SubClasses extends ClientMessage {
            -GetGamesClientMessage
            -CreateGameClientMessage
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


        interface MessageHandler<T> {
            +void sendMessage(T message)
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

        class RMIClientConnectionHandler implements MessageHandler, ClientActions {
            +void sendMessage(ServerMessage message)
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