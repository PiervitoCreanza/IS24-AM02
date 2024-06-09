module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires com.google.gson;
    requires java.logging;
    requires java.net.http;
    requires java.rmi;
    requires org.apache.logging.log4j;
    requires org.apache.commons.cli;
    requires java.desktop;

    // Exports
    exports it.polimi.ingsw.gui to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.card to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.card.corner to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.card.gameCard;
    exports it.polimi.ingsw.model.card.gameCard.front to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.card.gameCard.front.goldCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.card.objectiveCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.player to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.utils to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.model.utils.store to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.controller to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.controller.gameController to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.data to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.server to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.server.actions to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.server.message to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.server.message.adapter to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.server.message.successMessage to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.server.RMI to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.server.TCP to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.virtualView to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.actions to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.message to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.message.adapter to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.message.gameController to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.message.mainController to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.RMI to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.TCP to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.network.client.connection to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.commandLine to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.controller to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.utils to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.cards.gameCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.cards.gameCard.corner to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.cards.objectiveCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.cards.objectiveCard.itemGroup to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.decks to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.leaderBoard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.player.playerBoard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.player.playerInventory to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.player.playerInventory.playerHand to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.component.player.playerItems to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.drawer to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.tui.view.scene to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;

    //GUI exports
    // Opens
    opens it.polimi.ingsw.gui to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.card to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.card.corner to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.card.gameCard;
    opens it.polimi.ingsw.model.card.gameCard.front to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.card.gameCard.front.goldCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.card.objectiveCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.player to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.utils to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.model.utils.store to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.controller to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.controller.gameController to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.data to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.server to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.server.actions to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.server.message to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.server.message.adapter to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.server.message.successMessage to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.server.RMI to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.server.TCP to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.virtualView to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.actions to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.message to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.message.adapter to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.message.gameController to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.message.mainController to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.RMI to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.TCP to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.network.client.connection to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.commandLine to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.controller to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.utils to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.cards.gameCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.cards.gameCard.corner to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.cards.objectiveCard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.cards.objectiveCard.itemGroup to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.decks to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.leaderBoard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.player.playerBoard to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.player.playerInventory to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.player.playerInventory.playerHand to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.component.player.playerItems to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    opens it.polimi.ingsw.tui.view.drawer to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;

    opens it.polimi.ingsw.tui.view.scene to com.google.gson, java.rmi, javafx.graphics, javafx.fxml;
    exports it.polimi.ingsw.gui.controllers to com.google.gson, java.rmi, javafx.fxml, javafx.graphics;
    opens it.polimi.ingsw.gui.controllers to com.google.gson, java.rmi, javafx.fxml, javafx.graphics;
    exports it.polimi.ingsw.gui.controllers.gameSceneController to com.google.gson, java.rmi, javafx.fxml, javafx.graphics;
    opens it.polimi.ingsw.gui.controllers.gameSceneController to com.google.gson, java.rmi, javafx.fxml, javafx.graphics;
    exports it.polimi.ingsw.gui.dataStorage to com.google.gson, java.rmi, javafx.fxml, javafx.graphics;
    opens it.polimi.ingsw.gui.dataStorage to com.google.gson, java.rmi, javafx.fxml, javafx.graphics;
}