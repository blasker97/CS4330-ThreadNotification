/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import static java.lang.Thread.State.RUNNABLE;
import static java.lang.Thread.State.TERMINATED;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    
    @FXML
    private Button threadOne;
    @FXML
    private Button threadTwo;
    @FXML
    private Button threadThree;
          
    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        //System.out.println("start task 1");
        if (task1 == null || task1.getState().equals(TERMINATED)){
            task1 = new Task1(2147483647, 1000000);
            task1.setNotificationTarget(this);
            task1.start();
            threadOne.setText("End Task 1");
        }
        else if(task1.getState().equals(RUNNABLE)){
            task1.end();
            threadOne.setText("Start Task 1");            
        }            
        
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("Task1 done.")) {
            task1.end();
            threadOne.setText("Start Task 1");
        }
        textArea.appendText("Task 1 ---> " + message + "\n");
    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        if (task2 == null || task2.getState().equals(TERMINATED)) {
            task2 = new Task2(2147483647, 1000000);
            task2.setOnNotification((String message) -> {
                if(message != "Task2 done."){
                    textArea.appendText("Task 2 ---> " + message + "\n");
                }
                else{
                    task2.end();
                    textArea.appendText(message + "\n");
                    threadTwo.setText("Start Task 2");
                }
            });
            
            task2.start();
            threadTwo.setText("End Task 2");
        }
        else if(task2.getState().equals(RUNNABLE)){
            task2.end();
            threadTwo.setText("Start Task 2");            
        }        
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        if (task3 == null || task3.getState().equals(TERMINATED)) {
            task3 = new Task3(2147483647, 1000000);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if((String)evt.getNewValue() != "Task3 done."){
                textArea.appendText("Task 3 ---> " + (String)evt.getNewValue() + "\n");
                }
                else{
                    task3.end();
                    textArea.appendText((String)evt.getNewValue() + "\n");
                    threadThree.setText("Start Task 3");
                }
            });
            
            task3.start();
            threadThree.setText("End Task 3");
        }
        else if(task3.getState().equals(RUNNABLE)){
            task3.end();
            threadThree.setText("Start Task 3");
        }
    } 
}
