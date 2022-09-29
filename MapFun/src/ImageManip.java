import java.io.File;
import java.io.IOException;
import java.awt.Color;
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
			System.out.println("Error: " + e);
		}
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int[] argb = getARGB(image, x, y);
				//manipulation done here
				setARGB(image, x, y, argb);
			}
		}
		
		try {
			f = new File("O:\\Vanatas\\Vanatas_Topo_BWB_edited.png");
			ImageIO.write(image, "png", f);
			System.out.println("Writing complete.");
	    }
		catch(IOException e) {
			System.out.println("Error: " + e);
	    }
	}
	
	public static int[] getARGB(BufferedImage image, int x, int y) {
		Color pixle = new Color(image.getRGB(x, y));
		int[] argb = {pixle.getAlpha(), pixle.getRed(), pixle.getGreen(), pixle.getBlue()};
		return argb;
	}
	
	public static void setARGB(BufferedImage image, int x, int y, int[] rgb) {
		image.setRGB(x, y, (new Color(rgb[1], rgb[2], rgb[3], rgb[0]).getRGB()));
	}
}
