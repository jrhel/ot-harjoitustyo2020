/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui.stage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author J
 */
public class MessageStage {
    private Stage stage;
    private Label message;

    public MessageStage(String message) {
        this.stage = new Stage();
        this.message = new Label(message);    
        showMessage();
    }
    
    private void showMessage() {
        Scene scene = new Scene(message);
        stage.setScene(scene);       
        stage.show();
    }
    
}
