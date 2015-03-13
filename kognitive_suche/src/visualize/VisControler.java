package visualize;

import javafx.scene.layout.Pane;


/**
 * @author Fabian Freihube
 */
public class VisControler {
	
	// Tobi am start
	
	private static int paneWidth;
	private static int paneHeight;
	
	private static int activePads = 5;

	public int getPaneWidth() {
		return paneWidth;
	}

	public void setPaneWidth(int paneWidth) {
		VisControler.paneWidth = paneWidth;
	}



	public int getPaneHeight() {
		return paneHeight;
	}



	public void setPaneHeight(int paneHeight) {
		VisControler.paneHeight = paneHeight;
	}



	public int getActivePads() {
		return activePads;
	}



	public void setActivePads(int activePads) {
		VisControler.activePads = activePads;
	}



	public Pane startVisualize ()  {
		  Pattern pattern = new Pattern(activePads, paneHeight, paneWidth);
		  return pattern.getPane();
	  }
}
