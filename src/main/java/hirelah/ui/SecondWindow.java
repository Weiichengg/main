package hirelah.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * Second Window, currently for showing the lists of Questions during interviews.
 */
public class SecondWindow extends UiPart<Stage> {

    private static final String FXML = "SecondWindow.fxml";


    @FXML
    private StackPane listPanelStackPane;

    public SecondWindow(Stage stage) {
        super(FXML, stage);

    }

    public SecondWindow() {
        this(new Stage());
    }

    /**
     * Fills up the Node to show and shows this window.
     */
    public void show(UiPart<Region> n) {
        listPanelStackPane.getChildren().add(n.getRoot());
        this.getRoot().show();
    }

    /**
     * Removes all Nodes being shown on this window and hides this window.
     */
    public void hide() {
        listPanelStackPane.getChildren().clear();
        this.getRoot().hide();
    }

}
