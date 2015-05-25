package gui;


import de.leipzig.htwk.controller.Controller;
import de.leipzig.htwk.main.Main;
import de.leipzig.htwk.searchApi.SearchApiExecption;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import pdf.box.access.PDFDocument;
import visualize.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Erstellung der GUI
 *
 * @author Sebastian Hügelmann
 */

public class GUI extends Stage {
  private static final int windowHeight = 768;
  private static final int windowWidth = 1024;
  private static final int FAROO = 0;
  private static final int DUCKDUCKGO = 1;

  private int startMode = 0; // gibt an ob die Kog Suche aus der PDFBox oder direkt gestartet wird
  private Controller mController;
  public ArrayList<String> tags = new ArrayList<String>();
  public ArrayList<String> url = new ArrayList<String>();
  public ArrayList<String> kwic = new ArrayList<String>();
  private BorderPane pane1 = new BorderPane();
  Scene start;
  private Stage stage;
  TextField suchleiste;
  private Timeline timeline = new Timeline();
  private DoubleProperty stroke = new SimpleDoubleProperty(100.0);
  BorderPane loadingPane = new BorderPane();
  Scene loadingScene;
  ArrayList<PDFDocument> pdfBoxDocuments = new ArrayList<PDFDocument>();

  private static GUI instance;



  /**
   * Konstruktor wird benötigt um eine Instanz der GUI zu erstellen. Threadunsichere Instanzerstellung.
   *
   */
  private GUI() {
    /* Notwendig um eine Instanz der GUI zu erstellen. Wichtig für aufrufen aus PDFBox */
    start = new Scene(pane1);
    stage = new Stage();
    suchleiste = new TextField();
    loadingScene = new Scene(loadingPane);
    mController = new Controller();

    mController.setGUI(this);
    mController.setParameter("de", "web", 1);

    /* Anzeige der Stage */
    stage.setTitle("Kognitive Suche");
    stage.centerOnScreen();

    stage.setWidth(windowWidth);
    stage.setHeight(windowHeight);
    stage.setScene(drawHomeScreen());

    stage.setResizable(true);
    stage.show();
  }


  /**
   * Methode zur Erstellung des Buttons mit Logo für die Rückkehr auf die Startseite.
   *
   * @author Sebastian Hügelmann
   * @return HBox
   */
  public HBox goHomeButton() {

    HBox hboxHOME = new HBox();
    final ImageView imv = new ImageView();
    final Image image = new Image("file:bild.jpg");
    imv.setImage(image);
    imv.setCursor(Cursor.HAND);

    imv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        System.out.println("Tile pressed");
        stage.setScene(drawHomeScreen());
      }
    });

    hboxHOME.getChildren().add(imv);
    hboxHOME.setAlignment(Pos.CENTER);
    hboxHOME.setPadding(new Insets(15, 15, 15, 15));
    return hboxHOME;
  }


  private String InputStream() {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * Methode zeichnet die Startszene
   *
   * @author Sebastian Hügelmann
   * @return Scene
   */
  public Scene drawHomeScreen() {
    pane1.getChildren().clear();
    HBox hbox1 = new HBox();// horizontale Box für Suchleiste und Buttons
    HBox hbox2 = new HBox();// schliessen box
    HBox hbox3 = new HBox();// Horizontale Box für Buttons von vbox2 und vbox3
    VBox vbox1 = new VBox();// vertikale Box für Logo, hbox2
    VBox vbox2 = new VBox();// Vertikale Box für Sprachauswahl Buttons
    VBox vbox3 = new VBox();// Vertikale Box für Suchartauswahl Buttons
    final String[] SelectedLanguage = {"de"}; // Variable für Sprachauswahl
    final String[] Selectedsrc = {"web"}; // Variable für Suchartauswahl
    pane1.setStyle("-fx-background-color: #FFF;");
    pane1.setCenter(vbox1);
    pane1.setBottom(hbox2);// schliessen

    hbox1.setAlignment(Pos.CENTER);
    hbox2.setAlignment(Pos.BOTTOM_RIGHT);// Rechte ecke postionsbestimmung closebox

    hbox3.setAlignment(Pos.BOTTOM_CENTER);

    hbox3.setPadding(new Insets(-50));
    hbox3.setSpacing(20);

    vbox2.setPadding(new Insets(10));
    vbox2.setSpacing(10);

    vbox3.setPadding(new Insets(10));
    vbox3.setSpacing(10);

    hbox1.setPadding(new Insets(15, 30, 15, 30)); /* Bestimmt den Abstand vom Rand nach Innen */
    hbox1.setSpacing(20); /* Bestimmt den Abstand der Elemente voneinander */
    hbox1.setStyle("-fx-background-color: #FFF;"); /* Bestimmt die Hintergrundfarbe */
    vbox1.setStyle("-fx-background-color: #FFF;");
    vbox1.setAlignment(Pos.CENTER);
    vbox1.setSpacing(50);
    suchleiste.setMaxWidth(200);
    suchleiste.clear();

    Button close = new Button("Schliessen");// button zum schliessen

    close.setOnAction(new EventHandler<ActionEvent>() {

      public void handle(ActionEvent event) {
        Platform.exit();

      }

    });

    suchleiste.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
          startQuery();
        }
      }

    });

    Button sucheF = new Button("Suche in F");

    sucheF.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent sucheF) {
        startQuery();
      }
    });

    Button sucheP = new Button("Suche in P");
    sucheP.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent sucheP) {
        // mController.startSearchP();
        // kwic = mController.getKeywords2();
        // url = mController.getDocName();
        // tags = mController.getKeywords1(); // Ohne Sortierung soviel ich weiß

      }

    });
    hbox1.getChildren().addAll(suchleiste, sucheF, sucheP);
    vbox1.getChildren().addAll(goHomeButton(), hbox1, hbox3);
    return start;
  }

  /**
   * Diese Methode startet die Suche aus dem Controller
   *
   */
  public void startQuery() {
    stage.setScene(loadIndicator());

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          mController.querySearchEngine(DUCKDUCKGO ,suchleiste.getText());
        } catch (SearchApiExecption searchApiExecption) {
          searchApiExecption.printStackTrace();
        }
      }
    });//runlater Ende
    
//    final Task task = new Task(){                               // 1
//        @Override
//        public Void call() {
//        	try {
//				mController.querySearchEngine(DUCKDUCKGO ,suchleiste.getText());
//			} catch (SearchApiExecption e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            return null;
//        } 
//    };
//    new Thread(task).start();
//    System.out.println("gestartet");
    
//    Task task = new Task<Void>() {
//        @Override public Void call() {
//        	try {
//				mController.querySearchEngine(DUCKDUCKGO ,suchleiste.getText());
//			} catch (SearchApiExecption e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            stage.setScene(mController.getVisual());
//            return null;
//        }
//    };
//
//    Thread th = new Thread(task);
//    th.setDaemon(true);
//    th.start();
    
  }

  /**
   * Setter zum setzten des Suchleistens Textes nach Auswahl einer Kategorie in der Visualisierung.
   *
   * @author Sebastian Hügelmann
   * @param suchleiste Suchleiste für den Suchbegriff.
   */

  public void setSuchleisteText(String suchleiste) {
    System.out.println("Übergebener Begriff " + suchleiste);
    this.suchleiste.setText(suchleiste);
    System.out.println(this.suchleiste.getText());
    startQuery();
  }

  public static int getWindowheight() {
    return windowHeight;
  }

  public static int getWindowwidth() {
    return windowWidth;
  }

  public void setStageScene(Scene visual) {
    this.stage.setScene(visual);
    stage.setScene(visual);
  }

  public Stage getStage() {
    return this.stage;
  }


  private Scene loadIndicator() {
    System.out.println("Ladebalken Methode gestartet!");
    loadingPane.setStyle("-fx-background-color: #FFF;");
    timeline.setCycleCount(Timeline.INDEFINITE);

    final KeyValue kv = new KeyValue(stroke, 0);
    final KeyFrame kf = new KeyFrame(Duration.millis(1500), kv);

    timeline.getKeyFrames().add(kf);
    timeline.play();

    // Vertikale Box für Loading Bar und darunter Label
    VBox root = new VBox(3);
    root.setAlignment(Pos.CENTER);

    // Stackpane für den Balken
    StackPane progressIndicator = new StackPane();

    // Ladebalken
    Rectangle bar = new Rectangle(350, 13);
    bar.setFill(Color.TRANSPARENT);
    bar.setStroke(Color.BLACK);
    bar.setArcHeight(15);
    bar.setArcWidth(15);
    bar.setStrokeWidth(2);

    // Viereck im balken
    Rectangle progress = new Rectangle(342, 6);
    progress.setFill(Color.BLACK);
    progress.setStroke(Color.BLACK);
    progress.setArcHeight(8);
    progress.setArcWidth(8);
    progress.setStrokeWidth(1.5);
    progress.getStrokeDashArray().addAll(3.0, 7.0, 3.0, 7.0);
    progress.strokeDashOffsetProperty().bind(stroke);

    // Hinzufügen zur StackPane
    progressIndicator.getChildren().add(progress);
    progressIndicator.getChildren().add(bar);

    // Stackpane zur VBox um darunter das label zu packen
    root.getChildren().add(progressIndicator);

    Text label = new Text("Loading...");
    label.setFill(Color.BLACK);

    // Label wird hinzugefügt
    root.getChildren().add(label);

    // Jetzt ist alles in der VBox root
    loadingPane.setCenter(root);
    return loadingScene;
  }

  public TextField getSuchleiste() {
    // Auto-generated method stub
    return suchleiste;
  }

  public static GUI getInstance() {
    if (instance == null) {
      instance = new GUI();
    }
    return instance;

  }
  
  public Controller getController() {
    return mController;
  }
}