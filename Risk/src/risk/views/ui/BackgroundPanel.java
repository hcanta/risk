/**
 * Package contains the various UI components of the risk game
 */
package risk.views.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Class that handles the background Image of the game
 * @author hcanta
 */
public class BackgroundPanel
  extends JPanel
{
  private static final long serialVersionUID = 1L;
  private String fChoice;
  private Image fImage;
  
  /**
   * Crates a BackgroundPanel Object
   * @param imageName The path to the image file to be used
   */
  public BackgroundPanel(String imageName)
  {
    fChoice = imageName;
    
    try
    {
      fImage = ImageIO.read(BackgroundPanel.class.getClassLoader().getResource("img/" + fChoice));
    }
    catch (IOException localIOException) {}
  }
  


  /**
   * Set the Icon Back if it was removed
   * @param icon Icon to be set
   */
  public void setBack(Icon icon)
  {
    ImageIcon image = (ImageIcon)icon;
    fImage = image.getImage();
    Graphics g = getGraphics();
    paintComponent(g);
  }
  

  /***
   * Paints the graphic component
   * @param graphic Graphic component to be painted
   */
  protected void paintComponent(Graphics graphic)
  {
    super.paintComponent(graphic);
    if (fImage != null)
    {
    	graphic.drawImage(fImage, 0, 0, fImage.getWidth(getRootPane()), fImage.getHeight(getRootPane()), Color.BLACK, getRootPane());
    }
  }
}
