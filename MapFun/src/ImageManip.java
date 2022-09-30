import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageManip {
	public static void main(String[] args) throws IOException {
		int width = 920;
		int height = 460;
		
		BufferedImage image = null;
		File f = null;
		
		try {
			f = new File("O:\\Vanatas\\Vanatas_Water_Test.png");
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
				
				if (argb[3] == argb[2]) {
					double water =  waterDist(image, x, y, width, height) / 3000 * 255; //approaches 255 as distance from water increases (use 20016 for global)
					argb[1] = (255 - (int) water >= 0) ? (255 - (int) water) : 0;
					argb[2] = (255 - 2 * (int) water >= 0) ? 255 - 2 * (int) water : 0;
					argb[3] = (255 - 2 * (int) water >= 0) ? 255 - 2 * (int) water : 0;
				}
				
				System.out.println((double) (460*x+y)/(width*height) * 100 + "% Complete");
				
				setARGB(image, x, y, argb);
			}
		}
		
		try {
			f = new File("O:\\Vanatas\\Vanatas_Water_edited.png");
			ImageIO.write(image, "png", f);
			System.out.println("Writing complete.");
	    }
		catch(IOException e) {
			System.out.println("Error: " + e);
	    }
	}
	
	public static int[] getARGB(BufferedImage image, int x, int y) {
		Color pixel = new Color(image.getRGB(x, y));
		int[] argb = {pixel.getAlpha(), pixel.getRed(), pixel.getGreen(), pixel.getBlue()};
		return argb;
	}
	
	public static void setARGB(BufferedImage image, int x, int y, int[] rgb) {
		image.setRGB(x, y, (new Color(rgb[1], rgb[2], rgb[3], rgb[0]).getRGB()));
	}
	
	//Equirectangular distance calculation (Haversine formula)
	public static double distance (int x1, int y1, int x2, int y2, int width, int height) {
		double lat1 = ((double) (y1 - height) * -90 / height);
		double lat2 = ((double) (y2 - height) * -90 / height);
		int r = 6371;
		
		double phi1 = lat1 * Math.PI/180;
		double phi2 = lat2 * Math.PI/180;
		double dphi = (lat1-lat2) * Math.PI/180;
		double dlam = (((double) (x2 - width) * -90 / height)-((double) (x1 - width) * -90 / height)) * Math.PI/180;
		
		double a = Math.sin(dphi/2) * Math.sin(dphi/2) + Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlam/2) * Math.sin(dlam/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		return r * c;
	}
	
	public static double waterDist(BufferedImage image, int x, int y, int width, int height) {
		HashSet<String> visited = new HashSet<String>();
		ArrayList<double[]> distList = new ArrayList<double[]>();
		
		double[] pixel = {0, x, y};
		distList.add(pixel);
		visited.add(pixel[1] + " " + pixel[2]);
		
		while (getARGB(image, (int) pixel[1], (int) pixel[2])[3] == getARGB(image, (int) pixel[1], (int) pixel[2])[2]) {
			
			double[] n = new double[2], s = new double[2];
			if (pixel[2] == 0) {
				n[0] = ((pixel[1] + height) % width);
				n[1] = pixel[2];
			}
			else {
				n[0] = pixel[1];
				n[1] = pixel[2] - 1;
			}
			double[] e = {((pixel[1] + 1) % width), pixel[2]};
			if (pixel[2] == height - 1) {
				s[0] = ((pixel[1] + height) % width);
				s[1] = pixel[2];
			}
			else {
				s[0] = pixel[1];
				s[1] = pixel[2] + 1;
			}
			double[] w = {((pixel[1] - 1 + width) % width), pixel[2]};
			
			int i = 0;
			int c = 0;
			double[][] toAdd = new double[4][3];
			if (!visited.contains(n[0] + " " + n[1])) {
				toAdd[c][0] = distance((int) n[0], (int) n[1], x, y, width, height);
				toAdd[c][1] = n[0];
				toAdd[c][2] = n[1];
				c++;
			}
			if (!visited.contains(e[0] + " " + e[1])) {
				toAdd[c][0] = distance((int) e[0], (int) e[1], x, y, width, height);
				toAdd[c][1] = e[0];
				toAdd[c][2] = e[1];
				c++;
			}
			if (!visited.contains(s[0] + " " + s[1])) {
				toAdd[c][0] = distance((int) s[0], (int) s[1], x, y, width, height);
				toAdd[c][1] = s[0];
				toAdd[c][2] = s[1];
				c++;
			}
			if (!visited.contains(w[0] + " " + w[1])) {
				toAdd[c][0] = distance((int) w[0], (int) w[1], x, y, width, height);
				toAdd[c][1] = w[0];
				toAdd[c][2] = w[1];
				c++;
			}
			while (c < 4) toAdd[c++][0] = -1;
			for (int j = 0; j < 4; j++) {
				for (int k = j + 1; k < 4; k++) {
					if (toAdd[j][0] > toAdd[k][0]) {
						double[] temp = toAdd[j];
						toAdd[j] = toAdd[k];
						toAdd[k] = temp;
					}
				}
			}
			c = 0;
			for (c = 0; c < 4; c++) {
				while (toAdd[c][0] != -1 && i < distList.size() && distList.get(i)[0] < toAdd[c][0]) i++;
				if (toAdd[c][0] != -1) {
					double[] d = {(int) toAdd[c][0], (int) toAdd[c][1], (int) toAdd[c][2]};
					distList.add(i, d);
					visited.add(d[1] + " " + d[2]);
				}
			}
			
			if (!distList.isEmpty()) {
				pixel = distList.get(0);
				distList.remove(0);
			}
		}
		return pixel[0];
	}
}
