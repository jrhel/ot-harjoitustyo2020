
package main.ui;

import javafx.application.Application;
import main.domain.Logic;
import javafx.stage.Stage;
import main.ui.stage.MainStage;

/**
 *
 * @author J
 */
public class GUI extends Application {
    
    private Logic logic;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        logic = new Logic();
        logic.ensureDataBaseExists();
        
        MainStage mainStage = new MainStage();
        mainStage.main(logic, stage);
    }
}
