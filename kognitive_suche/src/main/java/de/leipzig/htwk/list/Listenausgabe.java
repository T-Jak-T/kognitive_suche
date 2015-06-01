/*
 * @author Ivan Ivanikov
 * @param Liste wird erzeugt indem die Waben überschrieben werden. Aufbau wie bei Google leicht und übersichtlich
 * scrollpane hinzugefügt falls Listen zu groß und unübersichtlich werden
 * Anbindung an Suchergebniss von Christian Schmidt
 */

package de.leipzig.htwk.list;

import de.leipzig.htwk.searchApi.Results;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

public class Listenausgabe {
  private int width;
  private int height;
  private int xpos;
  private int ypos;

  public ArrayList<String> tags = new ArrayList<String>();
  public ArrayList<String> url = new ArrayList<String>();
  public ArrayList<String> kwic = new ArrayList<String>();
  public ArrayList<String> title = new ArrayList<String>();

  /*
   * public Pane test() { Pane pane= new Pane();
   * 
   * Alert alert = new Alert(AlertType.INFORMATION); alert.setTitle("Information Dialog");
   * alert.setHeaderText("Look, an Information Dialog");
   * alert.setContentText("I have a great message for you!");
   * 
   * //alert.showAndWait(); Label label = new Label("test"); pane.getChildren().add(label); return
   * pane; }
   */

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setLayoutX(int xpos) { this.xpos = xpos; }

  public void setLayoutY(int ypos) { this.ypos = ypos; }

  /**
   * Erstellen der Liste
   *
   * @author Christian Schmidt
   */
  public Listenausgabe(Results results) {
    //mController.farooSearch(query); benötigt ?
    //Results results = mController.getResultList();
    for (int i = 0; i < results.getResults().size(); i++) {
      kwic.add(results.getResults().get(i).getKwic());
      title.add(results.getResults().get(i).getTitle());
      url.add(results.getResults().get(i).getUrl());
    }

  }

  public ScrollPane ergebnisausgabe() {
    // HBox hbox;
    VBox vbox1 = new VBox();
    VBox vbox2;
    BorderPane pane = new BorderPane();
    ScrollPane rol = new ScrollPane();
    Hyperlink[] link = new Hyperlink[50];
    Label[] label1 = new Label[50];
    Label[] label = new Label[25];
    // Label label2[] = new Label[25];
    int anzsucherg = (20 > url.size()) ? url.size() : 20; //lässt sich auch auf unter 20 Ergbnisse erweitern und Funktioniert
    
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    for (int k = 0; k < anzsucherg; k++) {
    	  Hyperlink h = new Hyperlink(url.get(k));
          h.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent link) {
            	  try {
                      Desktop.getDesktop().browse(new URI(h.getText()));//so sehen klickbare Links aus
                  } catch (Exception e) {
                  
            }
                System.out.println(url);
                System.out.println(link);
            }
          });
          link[k] = h;
      /*
       * arraylist.get(URL); from // link[k] = new Hyperlink("www.oracle.com");
       * /*arraylist.get(URL); from Arraylist
       */
      
      /* arraylist.get(KWIC) von arraylist */
      // label[k] = new
      // Label("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At");
      label1[k] = new Label(kwic.get(k));
      label[k] = new Label(title.get(k));
      vbox2 = new VBox();
      vbox2.setStyle("-fx-border-width: 2;");
      vbox2.setStyle("-fx-border-color: black;");
      vbox2.getChildren().addAll(label[k], label1[k], link[k]);
      vbox1.getChildren().add(vbox2);
      label[k].setMaxSize(600, 300);
      label[k].setWrapText(true);
      label[k].setStyle("-fx-label-padding: 0 0 10 0;");
      label[k].setStyle("-fx-font-weight: bold;");
      // label1[k].setMaxSize(300, 600);
      label1[k].setWrapText(true);
      label1[k].setStyle("-fx-label-padding: 0 0 0 0;");
      /* top right bottom left */

      /*
       * link[k].setOnAction(new EventHandler<ActionEvent>() {
       * 
       * @Override public void handle(ActionEvent o) {
       * getHostServices().showDocument(link[k].getText()); } });
       */
    }
    /*
     * @param Inhalt wird gefüllt und ein funktionierendes Scrollpane ist vorhanden, um durch längere Listen zu Scrollen
     */
    // vbox1.setMaxSize(1000, 1000);
    vbox1.setStyle("-fx-border-width: 2;");
    vbox1.setStyle("-fx-border-color: black;");
    /* vbox1.setSpacing(20); */
    pane.getChildren().clear();
    pane.setCenter(vbox1);
    rol.setPrefSize((double) width, (double) height);
    rol.setLayoutX((double) xpos);
    rol.setLayoutY((double) ypos);
    rol.setContent(pane);
    rol.setVbarPolicy(ScrollBarPolicy.ALWAYS);
    // pane.setStyle(-);
    // rol.setFitToHeight(true);
    // rol.setFitToWidth(true);



    /**
     * @author Sadik Ulusan
     */
    // Wenn die Hyperlinks angeklickt werden erkennt es das Program. Links werden ausgef�hrt und
    // durch den
    // Standardbrowser des Nutzers gestartet. PDF sind ebenso ausf�hrbar.
    /*
     * public void ergebnisausgabe(){
     * 
     * VBox vbox1 = new VBox(); Hyperlink link[] = new Hyperlink[25]; Label label[] = new Label[25];
     * int anzsucherg = 3;
     * 
     * final WebView browser = new WebView(); final WebEngine webEngine = browser.getEngine();
     * 
     * for (int k=0; k<anzsucherg;k++){ final String url = "http://www.oracle.com"; //final String
     * url = "H:/Dokumente/Eigene Bilder/NeueWegeMD.pdf"; //final String url =
     * "http://www.uefa.com/MultimediaFiles/Download/Regulations/uefaorg/Regulations/02/09/88/17/2098817_DOWNLOAD.pdf"
     * ; Hyperlink h = new Hyperlink(url); /*getHyperlink from Nodelist h.setOnAction(new
     * EventHandler<ActionEvent>() {
     * 
     * @Override public void handle(ActionEvent e) { System.out.println("Hyperlink geklickt!");
     * //webEngine.load(url);
     * 
     * //Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler " + //
     * "javascript:location.href=' " + url + " ' " ); try { Runtime.getRuntime().exec(
     * "rundll32 url.dll,FileProtocolHandler " + e ); } catch (IOException e1) {
     * e1.printStackTrace(); }
     * 
     * 
     * 
     * 
     * 
     * } }); link[k] = h; label[k] = new Label(
     * "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At"
     * ); vbox1.getChildren().addAll(link[k],label[k]); label[k].setMaxSize(300, 300);
     * label[k].setWrapText(true); label[k].setStyle("-fx-label-padding: 0 0 20 0;"); /*top right
     * bottom left }
     */

    return rol;



  }


}
