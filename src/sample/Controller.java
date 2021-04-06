package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    static int currentFile = -1;
    HashMap<String ,String > finish = new HashMap<>();
    @FXML
    private Button remove;

    @FXML
    void removeList(ActionEvent event) {
        if(currentFile!=-1) {
            observableList.remove(0,currentFile);
            while (currentFile!=-1) arrayList.remove(0);
        }
        currentFile = 0;
    }

    @FXML
    private ListView<Label> listView;

    ObservableList<Label> observableList = FXCollections.observableArrayList();
    ArrayList<File> arrayList  = new ArrayList<>();
    FileChooser fil_chooser = new FileChooser();

    @FXML
    private Label fileName;


    @FXML
    private Button select;

    @FXML
    private Button send;

    @FXML
    void selectFile(ActionEvent event) {

        List<File> selectedFiles = fil_chooser.showOpenMultipleDialog(select.getScene().getWindow());
        if (selectedFiles != null) {
            for(File file : selectedFiles) {
                fileName.setText(file.getAbsolutePath());
                System.out.println(file.length() / (1024 * 1024) + " MB");
                if (file.isFile()) System.out.println("File");
                else System.out.println("Directory");
                Label label = new Label(file.getName());
                label.setStyle("-fx-text-fill: green");
                observableList.add(label);
                arrayList.add(file);
            }
        }
    }

    CallbackFunction callbackFunction;

    @FXML
    void sendFile(ActionEvent event) throws IOException {

        callbackFunction = new CallbackFunction() {
            @Override
            public void onFinish(Label file) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        file.setStyle("-fx-text-fill: blue");
                    }
                });
                if(finish.containsKey(file.getText())){
                    currentFile = finish.size();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    next();
                }else {
                    finish.put(file.getText(), "FINISHED");
                }

            }
        };

        if (currentFile == -1 && observableList.size() != 0) {
            System.out.println("Calling next");
            currentFile = 0;
            next();
            send.setDisable(true);
        }

    }

    public void next(){
        if(currentFile>=arrayList.size()){
            System.out.println(currentFile + " "+ arrayList.size());
            send.setDisable(false);
            return;
        }
        File file = arrayList.get(currentFile);
        Label label = observableList.get(currentFile);
        DataReceiverThread dataReceiverThread = new DataReceiverThread(callbackFunction,file,label);
        Thread thread = new Thread(dataReceiverThread);
        thread.start();

        DataSenderThread dataSenderThread = new DataSenderThread(callbackFunction,file,label);
        Thread thread1 = new Thread(dataSenderThread);
        thread1.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(observableList);
        CommonData.controller = this;
        fil_chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Select files", "*.*"));
    }

}
