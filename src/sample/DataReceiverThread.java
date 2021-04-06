package sample;


import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

public class DataReceiverThread implements Runnable{
    CallbackFunction callbackFunction;
    File file;
    long endTime;
    Label label;
    final long startTime = System.currentTimeMillis();
    public DataReceiverThread(CallbackFunction callbackFunction, File file,Label label){
        this.callbackFunction = callbackFunction;
        this.file = file;
        this.label  = label;
        System.out.println("Constructor called for DataReceiver class");
    }
    @Override
    public void run() {
        System.out.println("Start run of receiver class");
        FileMetaData fileMetaData = new FileMetaData(file.getAbsolutePath(), file.length());
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("/Users/tarun/Downloads/rc/"+file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileReceiver fileReceiver = new FileReceiver(49452,fileWriter,file.length());
        try {
            fileReceiver.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Received Successfully");
        endTime = System.currentTimeMillis();
        long length = file.length()/(1024*1024);
        System.out.println(startTime+" "+endTime);
        double time = (endTime-startTime)/1000.0;
        double speed = length/time;
        System.out.println("time "+time);
        System.out.println("total size : "+length+" MB");
        System.out.println("total size : "+(length/1024)+" GB");
        System.out.println("speed :"+speed+" MB/s");
        callbackFunction.onFinish(label);
    }
}
