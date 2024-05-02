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
        class ClientMessage implements Message {}
    }
    interface TCPObservable {
                +void setObserver(TCPObserver observer)
                +void removeObserver(TCPObserver observer)
            }
    class TCPConnectionHandler implements TCPObservable {
        -ArrayList<TCPObserver> observers
        +void setObserver(TCPObserver observer)
        +void removeObserver(TCPObserver observer)
        +void sendMessage(String msg)
    }

    interface TCPObserver {
        +void notify(String msg)
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
        TCPServerAdapter <|.. TCPObserver
        TCPServerAdapter *-- TCPConnectionHandler

        note left of TCPServerAdapter
            This class converts the messages from text to objects and vice versa.
            It also sends the messages to the clients
        end note

        class RMIConnectionHandler implements ServerMessageHandler {
            +void sendMessage(ServerMessage message)
        }


      class "NetworkCommandMapper" implements "ClientActions" {
                  -HashSet<ServerMessageHandler> connections
                  -HashMap<String gameName, ServerMessageHandler> connectionMap
                  -MainController mainController
                  -void broadcastMessage(ServerMessage msg, String gameName)
                  +void addConnection(ServerMessageHandler connection, String gameName)
                  +void removeConnection(ServerMessageHandler connection, String gameName)
                  +void broadcastMessage(ServerMessage msg, String gameName)
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
                +void handleMessage(ClientMessage message)
                +void closeConnection()
            }
            class "ClientCommandMapper" implements "ClientActions" {

            }
            class RMIConnectionHandler implements ClientMessageHandler {
                +void handleMessage(ClientMessage message)
            }
            ClientCommandMapper *-- ClientMessageHandler
            class TCPClientAdapter implements ClientMessageHandler {
                -TCPConnectionHandler connection
                +void sendMessage(ClientMessage message)
            }
            TCPClientAdapter <|.. TCPObserver
            TCPClientAdapter *-- TCPConnectionHandler
            note left of ClientCommandMapper
                This class sends the messages to the server
                and updates the view
            end note
        }
    }
}
@enduml