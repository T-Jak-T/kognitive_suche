package simple.algorithm;

import java.util.ArrayList;

/**
 * @author vnaumchy
 * @author sulusan
 * @deprecated Die Klasse wie das ganze Packet wurde als Ausgangspunkt gedacht
 **/
public class SimAlgTags {


  private int zaehler;
  private String tag;
  private ArrayList<String> links = new ArrayList<String>();

  public SimAlgTags(String tag, String url) {
    this.tag = tag;
    links.add(url);
    this.zaehler = 1;
  }

  public String gettag() {
    return tag;
  }

  public int getzaehler() {
    return zaehler;
  }

  public void setzaehler() {
    zaehler++;
  }

  public void addlink(String url) {
    links.add(url);
  }

  public ArrayList<String> getlinks() {
    return links;
  }

  public void overrideaddresslist(ArrayList<String> links) {
    this.links = links;
  }



}
