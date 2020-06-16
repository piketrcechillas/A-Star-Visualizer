import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;

public class Renderer {
	public boolean ShowIndicator = true;
	private int width, height, MainWidth;
	private int Edge = 50;
	public int[] pixels;
	public int[] p;
	public String[][] img;
	public String[][] path;
	public int timer = 0;

	public Renderer(int width, int height, int MainWidth) {
		this.height = height;
		this.width = width;
		this.MainWidth = MainWidth;
		this.img = new String[width][height];
		this.path = new String[width][height];
		pixels = new int[width * height];
		p = new int[(MainWidth + 2 * Edge) * (height + 2 * Edge)];
	}

	int lineColor = 0;
	

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
		this.img = new String[width][height];
		this.path = new String[width][height];
	}
	
	public void render() {
		Arrays.fill(p, 0x7ec850);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = 0xf5f5f5 ;
				img[x][y] = "Grass";
			}
		}
		
		if (Main.End != null) {
			Point coordinates = (Main.End).getCoordinate();
			if (coordinates.getX() >= (Main.xLength - Edge / Main.widthCell)
					&& coordinates.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
					&& coordinates.getY() >= (Main.yLength - Edge / Main.widthCell)
					&& coordinates.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {

				img[Main.End.getX() - Main.xLength][Main.End.getY() - Main.yLength] = "Princess";
			}
		}

		// Wall
		for (int i = 0; i < Main.wall.size(); i++) {
			Point coordinate = ((Cell) Main.wall.get(i)).getCoordinate();
			if (coordinate.getX() >= (Main.xLength - Edge / Main.widthCell)
					&& coordinate.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
					&& coordinate.getY() >= (Main.yLength - Edge / Main.widthCell)
					&& coordinate.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {
				
				if(coordinate.x != -1 && coordinate.y != -1) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Rift";
				}
			}
		}
		
		// Rivers
		for (int i = 0; i < Main.river.size(); i++) {
			Point coordinate = ((Cell) Main.river.get(i)).getCoordinate();
			if (coordinate.getX() >= (Main.xLength - Edge / Main.widthCell)
					&& coordinate.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
					&& coordinate.getY() >= (Main.yLength - Edge / Main.widthCell)
					&& coordinate.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {
				
				if(coordinate.x != -1 && coordinate.y != -1) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "River";
				}
			}
			}
		
		// Forests
		for (int i = 0; i < Main.forest.size(); i++) {
			Point coordinate = ((Cell) Main.forest.get(i)).getCoordinate();
				if (coordinate.getX() >= (Main.xLength - Edge / Main.widthCell)
						&& coordinate.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
						&& coordinate.getY() >= (Main.yLength - Edge / Main.widthCell)
						&& coordinate.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {
					
					if(coordinate.x != -1 && coordinate.y != -1) {
						img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Forest";
					}
				}
				}
		
		// Mountains
		for (int i = 0; i < Main.mountain.size(); i++) {
			Point coordinate = ((Cell) Main.mountain.get(i)).getCoordinate();
				if (coordinate.getX() >= (Main.xLength - Edge / Main.widthCell)
						&& coordinate.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
						&& coordinate.getY() >= (Main.yLength - Edge / Main.widthCell)
						&& coordinate.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {
					
					if(coordinate.x != -1 && coordinate.y != -1) {
						img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Mountain";
					}
				}
				}

		// Probe
		for (int i = 0; i < Main.OngoingProbe.size(); i++) {
			Point coordinate = ((Cell) Main.OngoingProbe.get(i)).getCoordinate();

			if (coordinate.getX() >= (Main.xLength - Edge / Main.widthCell)
					&& coordinate.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
					&& coordinate.getY() >= (Main.yLength - Edge / Main.widthCell)
					&& coordinate.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {

				if (coordinate.equals(Main.Start.getCoordinate())) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Knight";
				} else if (coordinate.equals(Main.End.getCoordinate())) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Princess";
				}

				path[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Ongoing";


			}
		}

		// Finished Probe
		for (int i = 0; i < Main.ClosedProbe.size(); i++) {
			Point coordinate = ((Cell) Main.ClosedProbe.get(i)).getCoordinate();

			if (coordinate.getX() >= (Main.xLength - Edge / Main.widthCell)
					&& coordinate.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
					&& coordinate.getY() >= (Main.yLength - Edge / Main.widthCell)
					&& coordinate.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {


				if (coordinate.equals(Main.Start.getCoordinate())) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Knight";
				} else if (coordinate.equals(Main.End.getCoordinate())) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Princess";
				}

				path[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Closed";
			}
		}

		// Path
		for (int i = 0; i < Main.ShortPath.size(); i++) {
			Point coordinate = ((Cell) Main.ShortPath.get(i)).getCoordinate();

			if (coordinate.getX() >= (Main.xLength - Edge / Main.widthCell)
					&& coordinate.getX() < (Main.horizontal + Main.xLength + Edge / Main.widthCell)
					&& coordinate.getY() >= (Main.yLength - Edge / Main.widthCell)
					&& coordinate.getY() < (Main.vertical + Main.yLength + Edge / Main.widthCell)) {


				if (coordinate.equals(Main.Start.getCoordinate())) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Knight";
				} else if (coordinate.equals(Main.End.getCoordinate())) {
					img[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Princess";
				}

				path[coordinate.x - Main.xLength][coordinate.y - Main.yLength] = "Path";
			}
		}


		for (int i = 0; i < MainWidth; i++) {
			for (int j = 0; j < height; j++) {
				pixels[i + j * width] = p[Edge + i + (j + Edge) * (MainWidth + 2 * Edge)];
			}
		}

		if (IO.Grid) {
			for (int nw = 0; nw < MainWidth/Main.widthCell + 1; nw++) {
				for (int x = 1; x < height; x++) {
					pixels[nw * Main.widthCell + x * width] = lineColor;
				}
				for (int y = 1; y < MainWidth; y++) {
					if (y + nw * Main.heightCell * width < pixels.length)
						pixels[y + nw * Main.heightCell * width] = lineColor;
				}
			}
		}

		int color = 0;
		if (Main.input.ControlScreenDrag) {
			color = (Color.BLUE).hashCode();
		} else if (Main.input.run && !Main.input.ShiftToggle) {
			color = (Color.RED).hashCode();
		} else if (Main.input.ShiftToggle) {
			color = (Color.WHITE).hashCode();
		}
		for (int x = 0; x < MainWidth; x++) {
			pixels[x] = color;
			pixels[x + (height - 1) * width] = color;
			pixels[x * width + MainWidth] = color;
			pixels[x * width] = color;
		}

	}
}
