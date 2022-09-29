import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageManip {
	public static void main(String[] args) throws IOException {
		int width = 9176;
		int height = 4588;
		
		BufferedImage image = null;
		File f = null;
		
		try {
			f = new File("O:\\Vanatas\\Vanatas_Topo_BWB.png");
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			image = ImageIO.read(f);
			System.out.println("Reading complete.");
		}
		catch (IOException e)
		{
			System.out.println("Error: "+e);
		}
		
		int rgb = image.getRGB(0,0);
		int alpha = (rgb >> 24) & 0xFF;
		int red =   (rgb >> 16) & 0xFF;
		int green = (rgb >>  8) & 0xFF;
		int blue =  (rgb      ) & 0xFF;
		System.out.println("A: " + alpha + " R: " + red + " G: " + green + " B: " + blue);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				
			}
		}
		/*
		try {
			f = new File("O:\\Vanatas\\Vanatas_Topo_BWB_edited.png");
			ImageIO.write(image, "png", f);
			System.out.println("Writing complete.");
	    }
		catch(IOException e) {
			System.out.println("Error: "+e);
	    }
	    */
	}
}