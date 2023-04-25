package ma.enset.blocking;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.*;
import java.net.Socket;

public class JavaClient extends Application {
    PrintWriter pw;
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Chat Client");
        BorderPane borderPane=new BorderPane();
        Scene scene=new Scene(borderPane,500,550);
        Label labelHost=new Label("host");
        TextField textFieldHost=new TextField("localhost");
        Label labelPort=new Label("port");
        TextField textFielport=new TextField("1234");
        Button buttonConnecter=new Button("connecter");
        HBox hBox=new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new javafx.geometry.Insets(10));
        hBox.setBackground(new Background(new BackgroundFill(Color.rgb(35,165,120),null,null)));
        hBox.getChildren().addAll(labelHost,textFieldHost,labelPort,textFielport,buttonConnecter);
        borderPane.setTop(hBox);
        VBox vBox=new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        ObservableList<String> observableList= FXCollections.observableArrayList();
        ListView<String> listView=new ListView<String>(observableList);
        vBox.getChildren().add(listView);
        borderPane.setCenter(vBox);
        Label labelMessage=new Label("Message");
        TextField textFieldMessage=new TextField();
        textFieldMessage.setPrefSize(320,25);
        Button buttonEnvoyer=new Button("Envoyer");
        HBox hBox1=new HBox();
        hBox1.setSpacing(10);
        hBox1.setPadding(new Insets(10));
        hBox1.getChildren().addAll(labelMessage,textFieldMessage,buttonEnvoyer);
        hBox1.setBackground(new Background(new BackgroundFill(Color.rgb(35,165,120),null,null)));
        borderPane.setBottom(hBox1);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        buttonConnecter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String host=textFieldHost.getText();
                int port=Integer.parseInt(textFielport.getText());
                try {
                    Socket socket=new Socket(host,port);
                    InputStream is= socket.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);
                    BufferedReader br=new BufferedReader(isr);
                    OutputStream os=socket.getOutputStream();
                    pw=new PrintWriter(os,true);
                    new Thread(()->{
                        while (true) {
                            try {
                                String response = br.readLine();
                                Platform.runLater(()->{
                                    observableList.add(response);
                                });
                            }  catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        buttonEnvoyer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String message=textFieldMessage.getText();
                pw.println(message);
                textFieldMessage.setText("");
            }
        });
    }
}
