package sample;


import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

public class DataSenderThread implements Runnable {
    File file;
    CallbackFunction callbackFunction;
    Label label;
    public DataSenderThread(CallbackFunction callbackFunction , File file, Label label) {
        this.file = file;
        this.callbackFunction = callbackFunction;
        this.label = label;
        System.out.println("Constructor called for DataSender  class");
    }

    @Override
    public void run() {
        System.out.println("Start run of sender class");
        FileSender fileSender = null;
        try {
            fileSender = new FileSender(49452);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileReader fileReader = new FileReader(fileSender,file.getAbsolutePath());
            fileReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callbackFunction.onFinish(label);
    }
}
