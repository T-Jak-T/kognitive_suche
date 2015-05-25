package de.leipzig.htwk.searchApi;

import javafx.application.Platform;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.util.ArrayList;
import java.util.List;


/**
 * @Autor Hendrik Sawade.
 */
public class SearchApi{


    /**
     * Phantomjs driver
     */
    PhantomJSDriver driver;
    /**
     * Beinhaltet die alle Results als Result_ Objekte.
     */
    ArrayList<Result> resultList = new ArrayList<Result>();
    /**
     * URL der Suchmaschine.
     */
    private String url;

    /**
     * Setzt den Tag für den Search Button fest.
     */
    String nextButton;

    /**
     * titleClassList, linkClassList, descriptionClassList dienen zur Temporären Speicherung der Suchergebnisse
     * entsprechen der bedeutung.
     */
    List<WebElement> titleClassList = null;
    List<WebElement> linkClassList = null;
    List<WebElement> descriptionClassList = null;

    /**
     * Liste mit denThreads für die Result Liste
     */
    ArrayList<MakeResultListThread> threadlist = new ArrayList<MakeResultListThread>();

    /**
     * Klassen Parameter für large, url und snippet
     */
    String titleClass, linkClass, descriptionClass;

    /**
     * Wie viele Ergebnisse will man haben
     */
    int gesamtAnzahlErgebnisse;

    /**
     * Wie viele Ergebnisse gibt es pro Seite.
     */
    private int anzSiteResults;

    /**
     * Wie viele ResterErgebnisse sind noch vorhanden.
     * (gesamtAnzahlErgebnisse - anzSiteResults
     */
    int anzRestResults = 0;

    /**
     * Anzahl wie viele Ergebnisse noch übrig sind.
     */
    int restAnz;
    /**
     * No Results Class
     */
    String noresultclass;

    /**
     * SetUp für den Phantomjs Driver.
     */

    /**
     *
     * @param url
     * @param nextButton
     * @param titleClass
     * @param urlKlasse
     * @param snippetKlasse
     * @param noResultClass
     * @param gesamtAnzahlErgebnisse
     */

    public SearchApi(String url, String nextButton, String titleClass, String urlKlasse,
                     String snippetKlasse, String noResultClass, int gesamtAnzahlErgebnisse, PhantomJSDriver driver) {

        this.url = url;
        this.nextButton = nextButton;
        this.titleClass = titleClass;
        this.linkClass = urlKlasse;
        this.descriptionClass = snippetKlasse;
        this.noresultclass = noResultClass;
        this.gesamtAnzahlErgebnisse = gesamtAnzahlErgebnisse;
        this.anzRestResults = gesamtAnzahlErgebnisse;
        this.driver = driver;
    }
    /**
     * Dienen für die korrekte Darstellung des Suchbegriffes. Muss in html verträgliche Darstellung gebracht werden.
     */
    private static String[] REPLACEMENTS = { "%", "%25", " ", "%20", "!",
        "%21", "#", "%23", "\\$", "%24", "\"", "%22", "&", "%26", "’",
        "%27", "\\(", "%28", "\\)", "%29", "\\*", "%2A", "\\+", "%2B", ",",
        "%2C", "/", "%2F", ":", "%3A", ";", "%3B", "=", "%3D", "\\?",
        "%3F", "@", "%40", "\\[", "%5B", "]", "%5D" };

    /**
     * Diese Methode codiert die query nach URL-Envording Richtlien
     * @param query query
     * @return url Bearbeitete query String
     */
    private String encoding(String query) {

        for (int i = 0; i < REPLACEMENTS.length - 1; i = i + 2) {
            query = query.replaceAll(REPLACEMENTS[i], REPLACEMENTS[i + 1]);
        }
        return query;
    }

    /**
     * Anfrage an die Suchmaschine stellen, url wird zusammen gesezt und codiert.
     * @param query - Suchwort
     * @return Results Liste
     * @throws SearchApiExecption
     */
    public void query(String query) throws SearchApiExecption {
        query = encoding(query);
        searching(this.url + query);

    }

    /**
     * Kappselt den Suchvorgang.
     * @param completeUrl String zur Suchmaschine, einschließlich des Suchwortes
     * @return
     */
    private void searching(String completeUrl) throws SearchApiExecption {

        try {
            driver.get(completeUrl);
        } catch (WebDriverException ioe) {
            ioe.printStackTrace();

            Platform.exit();
            throw new SearchApiExecption("Keine Internetverbindung möglich");
        }

        try {
            long zstVorher = System.currentTimeMillis();
            while (resultList.size() < gesamtAnzahlErgebnisse) {
                List<WebElement> noResults = getList(this.noresultclass);
                if(noResults.size() == 0 && anzRestResults != 0) {
                    makeClassLists();
                    createAnzResultObjects();

                    for (int i = 0; i < restAnz;i++) {
                        MakeResultListThread th = new MakeResultListThread(titleClassList.get(i).getText(), descriptionClassList.get(i).getText(),
                            linkClassList.get(i).getAttribute("href").toString(),this);
                        th.start();
                        threadlist.add(th);
                    }



                    if (resultList.size() < gesamtAnzahlErgebnisse) {
                        moreResults();
                    }
                } else {
                    break;
                }
            }
            int ready = 0;
            while(ready < threadlist.size()-1) {
                for(MakeResultListThread th: threadlist) {
                    if(!th.isAlive()) {
                        //                    threadlist.remove(th);
                        ready++;
                    }
                }
            }

            long zstNachher = System.currentTimeMillis(); // Zeitmessung
            System.out.println("Zeit benötigt: DuckDuckGo Suche Result Liste erstellen + alles andere: " + ((zstNachher - zstVorher))
                + " millisec");

        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchApiExecption("Beim Erstellen der Ergebnisliste ist ein Fehler aufgetreten");
        }

    }

    /**
     * Erstellt die Listen mit den Web Element Klassen
     * @throws SearchApiExecption
     */
    protected void makeClassLists() throws SearchApiExecption {
        this.titleClassList = (getList(this.titleClass));
        this.linkClassList = (getList(this.linkClass));
        this.descriptionClassList = (getList(this.descriptionClass));
    }

    /**
     * Kümmert sich dadrum das es mehr Suchergebnisse gibt.
     */
    protected void moreResults() throws SearchApiExecption {
        try {
            WebElement next = driver.findElement(By.className(this.nextButton));
            next.click();
        } catch (WebDriverException e) {
            e.printStackTrace();
            throw new SearchApiExecption("Fehler bei klicken des next Buttons");
        }
    }



    /**
     * Gibt eine Result_ Liste zurück,mit den Result_ Objekt.
     * @return resultList Beinhaltet eine List mit Result_ Objekten.
     */
    private void makeResultList() {


    }

    /**
     * Berechnet wie viele Result_ Objekte noch erstellt werden müssen, anhand wie viele ergebnisse
     * pro Seite es gibt.
     * @return anz Anzahl wie viele Ergebnisse es gibt.
     */
    private void createAnzResultObjects() {

        if (anzRestResults >= anzSiteResults) {
            anzRestResults = anzRestResults - anzSiteResults;
            restAnz = anzSiteResults;
        } else {
            restAnz = anzRestResults;
        }

    }



    /**
     * Sucht nach einer bestimmten Kklasse, die man übergibt. Die Klasse wo die Ergennisse drin stehen.
     * @param className
     * @return Liste mit den gefundenen Suchergebnissen der angegebenen Klasse.
     */
    protected List<WebElement> getList(String className) throws SearchApiExecption {
        List<WebElement> list = null;
        try {

            list = driver.findElements(By.className(className));
            if (list.size() != 0) {
                this.anzSiteResults = list.size();
            } else if(className.equals(noresultclass) && list.size() != 0) {
                anzRestResults = 0;
            }

        } catch (WebDriverException e) {
            e.printStackTrace();
            throw new SearchApiExecption("Bei dem Suchen von Ergebnissen, in den Tags, ist ein Fehler aufgetreten.");
        }
        return list;
    }

    public Results getResultList() throws SearchApiExecption{
        Results results = new Results();
        results.setResults(resultList);
        if (resultList.isEmpty()){
            throw new SearchApiExecption("Result Liste ist leer, keine Ergebnisse Gefunden");
        }
        return results;
    }


    public void setResult(Result r)  {
        /**
         * Prüft ob alle Threads fertig abgearbeitet sind
         */
        synchronized (this) {
            resultList.add(r);
        }
    }


}

