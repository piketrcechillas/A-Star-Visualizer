import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main extends Canvas implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private Thread thread;
	private JFrame frame;
	private Renderer renderer;
	public static int horizontal = 20, vertical = 20, xLength = 0, yLength = 0, MaxScale = 35, MinScale = 35;
	public static int width = 1000;
	public static int MainWidth = 800;
	public static int height = 800;
	
	private JButton setStart;
	private JButton setEnd;
	private JButton run;
	private JButton nextStep;
	private JButton reset;
	private JButton showGrid;
	private JButton addWall;
	private JButton addRiver;
	private JButton addForest;
	private JButton addMountain;
	
	
	private boolean running = false;
	public static IO input;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	public static Cell Start, End;
	public static Terrain wall, river, forest, mountain, OngoingProbe = new Terrain(), ClosedProbe = new Terrain(), ShortPath = new Terrain();
	public static int CostS = 1, CostR = 2, CostF = 3, CostM = 4;
	public static int widthCell, heightCell;

	public static Main core;
	
	public Main() {
		Dimension size = new Dimension(width * 1, height * 1);
		setPreferredSize(size);
		renderer = new Renderer(width, height, MainWidth);
		frame = new JFrame();
		input = new IO();
		
		JPanel panel2 = this.createContentPanel();
		frame.add(panel2, BorderLayout.EAST);
		
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		addMouseWheelListener(input);

		widthCell = MainWidth / horizontal;
		heightCell = height / vertical;
		wall = new Terrain();
		river = new Terrain();
		forest = new Terrain();
		mountain = new Terrain();
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "A*");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		Main.initialize();
	}
	
	
	public static void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		core = new Main();
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		UIManager.setLookAndFeel(lookAndFeel);
		core.frame.setResizable(false);
		core.frame.setTitle("Save the Princess");
		core.frame.add(core);
		core.frame.pack();
		core.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		core.frame.setLocationRelativeTo(null);
		core.frame.setVisible(true);
		Main.Scale(35);
		Main.buildSurrounding();
		core.start();
	}
	public static void restart() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		core.stop();
		Main.initialize();
	}
	
	public JPanel createContentPanel() {
		setStart = new JButton("Set Starting Position");
		setEnd = new JButton("Set Ending Position");	
		run = new JButton("Run");
		nextStep = new JButton("Next Step");
		reset = new JButton("Reset");
		showGrid = new JButton("Toggle Grid");
		addWall = new JButton("Add Unpassable Rift");
		addRiver = new JButton("Add River - 2 Stamina");
		addForest = new JButton("Add Forest - 3 Stamina");
		addMountain = new JButton("Add Mountain - 4 Stamina");
		
		
		setStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.startset = true;
			}
		});
		
		setEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.endset = true;
			}
		});
		
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.auto = !input.auto;
			}
		});
		
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.auto = !input.auto;Main.timerf=0;
				Main.wall.clear();
				Main.river.clear();
				Main.forest.clear();
				Main.mountain.clear();
				Main.End = null;
				Main.ClosedProbe.clear();
				Main.OngoingProbe.clear();
				Main.ShortPath.clear();
				Main.completed = false;
				Main.reach = false;
				input.auto = false;
				Main.buildSurrounding();
			}
		});
		
		showGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IO.Grid = !IO.Grid;
			}
		});
		
		nextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.run = !input.run;
			}
		});
		
		
		addWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.mode = "Wall";
			}
		});
		
		
		addRiver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.mode = "River";
			}
		});
		
		addForest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.mode = "Forest";
			}
		});
		
		addMountain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				input.mode = "Mountain";
			}
		});
		
		
		
		
		JPanel panel2 = new JPanel();
		
		panel2.setLayout(new GridLayout(5, 1));
		panel2.add(setStart);
		panel2.add(setEnd);
		panel2.add(run);
		panel2.add(nextStep);
		panel2.add(reset);
		panel2.add(showGrid);
		panel2.add(addWall);
		panel2.add(addRiver);
		panel2.add(addForest);
		panel2.add(addMountain);
		
		return panel2;
	}
	

	public static long timerf = 0;
	boolean FastS=false;

	@Override
	public void run() {
		while (running) {
			update();
			render();

		}
		frame.setTitle("Save the Princess");
		stop();
	}

	public static int iteration = 0;
	static boolean completed = false;
	static boolean reach = false;

	public void update() {

		if ((input.run || input.auto) && Main.Start != null && Main.End != null && !reach) {

				OngoingProbe.sortList();
				if (OngoingProbe.size() == 0) {
					JOptionPane.showMessageDialog(null, "No path available");
					stop();
					
				} else {
					Expand((Cell) OngoingProbe.get(0));
				}

				iteration++;
				input.run = false;
			
		}

		if (reach && !completed) {
			BackTracking();
			completed = true;
			System.out.println(ShortPath.toString());
		}
	}
	

	public void render() {	
		String dist = "   0";
		if(ClosedProbe.size() > 0) {
			Cell init = (Cell) ClosedProbe.get(0);
			dist = String.valueOf(init.toString(2));
		}
		
		
		String currD = "   0";
		String currC = "   0";
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		renderer.clear();
		renderer.render();

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = renderer.pixels[i];
		}
		
		Image grass = Toolkit.getDefaultToolkit().getImage(".\\img\\grass.png");
		Image rift = Toolkit.getDefaultToolkit().getImage(".\\img\\rift.png");
		Image river = Toolkit.getDefaultToolkit().getImage(".\\img\\river.png");
		Image mountain = Toolkit.getDefaultToolkit().getImage(".\\img\\mountain.png");
		Image forest = Toolkit.getDefaultToolkit().getImage(".\\img\\forest.png");
		Image princess = Toolkit.getDefaultToolkit().getImage(".\\img\\princess.png");
		Image knight = Toolkit.getDefaultToolkit().getImage(".\\img\\knight.png");
		Image closed = Toolkit.getDefaultToolkit().getImage(".\\img\\closed.png");
		Image ongoing = Toolkit.getDefaultToolkit().getImage(".\\img\\ongoing.png");
		Image path = Toolkit.getDefaultToolkit().getImage(".\\img\\path.png");


		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, (int) (getWidth() * 1), (int) (getHeight() * 1), null);
		String[][] imgArr = renderer.img;
		String[][] pathArr = renderer.path;

		for(int i = 0; i < 23; i++) {
			for(int j = 0; j < 23; j++) {
				if(imgArr[i][j] == "Grass") {
					g.drawImage(grass, 3+35*i, 2+35*j, this);
				}
				if(imgArr[i][j] == "Rift") {
					g.drawImage(rift, 3+35*i, 2+35*j, this);
				}
				if(imgArr[i][j] == "River") {
					g.drawImage(river, 3+35*i, 2+35*j, this);
				}
				if(imgArr[i][j] == "Forest") {
					g.drawImage(forest, 3+35*i, 2+35*j, this);
				}
				if(imgArr[i][j] == "Mountain") {
					g.drawImage(mountain, 3+35*i, 2+35*j, this);
				}
				if(imgArr[i][j] == "Princess") {
					g.drawImage(princess, 3+35*i, 2+35*j, this);
				}
				if(imgArr[i][j] == "Knight") {
					g.drawImage(knight, 3+35*i, 2+35*j, this);
				}
			}
		}
		
		for(int i = 0; i < 23; i++) {
			for(int j = 0; j < 23; j++) {
				if(pathArr[i][j] == "Closed") {
					g.drawImage(closed, 3+35*i, 2+35*j, this);
				}
				if(pathArr[i][j] == "Ongoing") {
					g.drawImage(ongoing, 3+35*i, 2+35*j, this);
				}
				if(pathArr[i][j] == "Path") {
					g.drawImage(path, 3+35*i, 2+35*j, this);
				}
			}
		}
		
		
		
		
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 12));
			Cell tmp;
			for (int i = 0; i < OngoingProbe.size(); i++) {
				tmp = ((Cell) OngoingProbe.get(i));
				if (!((tmp.getX() - xLength) >= 23 || (tmp.getY() - yLength) > 23
						|| (tmp.getX() - xLength) < 0 || (tmp.getY() - yLength) < 0)) {
					
					g.setColor(Color.BLUE);
					
					g.drawString(tmp.toString(1), (tmp.getX() - xLength) * heightCell + 1,
					(tmp.getY() - yLength) * heightCell + heightCell - 20);
					
					g.setColor(Color.BLACK);
					
					g.drawString(tmp.toString(2), (tmp.getX() - xLength) * heightCell + 1,
							(tmp.getY() - yLength) * heightCell + heightCell - 5);
					
					
				}
			}
			
			for (int i = 0; i < ClosedProbe.size(); i++) {
				tmp = ((Cell) ClosedProbe.get(i));
				currD = tmp.toString(2);
				currC = tmp.toString(1);
				if (!((tmp.getX() - xLength) >= 23 || (tmp.getY() - yLength) > 23
						|| (tmp.getX() - xLength) < 0 || (tmp.getY() - yLength) < 0)) {
					
					g.setColor(Color.BLUE);
					
					g.drawString(tmp.toString(1), (tmp.getX() - xLength) * heightCell + 1,
							(tmp.getY() - yLength) * heightCell + heightCell - 20);
					
					g.setColor(Color.BLACK);
					
					g.drawString(tmp.toString(2), (tmp.getX() - xLength) * heightCell + 1,
							(tmp.getY() - yLength) * heightCell + heightCell - 5);
					
					
				}
			}
		g.setFont(new Font("TimesRoman", Font.BOLD, 14));
		
		g.drawString("Distance: ", 35*23, 35*7);
		g.drawString(dist.substring(2), 35*28, 35*7);
			
		g.drawString("Distance Remains: ", 35*23, 35*10);
		g.drawString(currD.substring(2), 35*28, 35*10);
		
		g.drawString("Stamina Spent: ", 35*23, 35*13);
		g.drawString(currC.substring(2), 35*28, 35*13);
		
		g.dispose();
		bs.show();
	}

	
	
	
	
	
	@SuppressWarnings("unchecked")
	public void Expand(Cell current) {
		if (current.getD() == 0) {
			reach = true;
			ClosedProbe.add(current);
		} else {
			Cell newCell = null;
			Cell terrain = null;
			ClosedProbe.add(current);
			int sleft = CostS;
			int sright = CostS;
			int sdown = CostS;
			int sup = CostS;
			
			terrain = new Cell(current.getCoordinate().x - 1, current.getCoordinate().y);
			if(river.search(terrain) != -1) {
				sleft = CostR;
			}
			if(forest.search(terrain) != -1) {
				sleft = CostF;
			}
			if(mountain.search(terrain) != -1) {
				sleft = CostM;
			}
			
			terrain = new Cell(current.getCoordinate().x + 1, current.getCoordinate().y);
			if(river.search(terrain) != -1) {
				sright = CostR;
			}
			if(forest.search(terrain) != -1) {
				sright = CostF;
			}
			if(mountain.search(terrain) != -1) {
				sright = CostM;
			}
			
			terrain = new Cell(current.getCoordinate().x, current.getCoordinate().y - 1);
			if(river.search(terrain) != -1) {
				sdown = CostR;
			}
			if(forest.search(terrain) != -1) {
				sdown = CostF;
			}
			if(mountain.search(terrain) != -1) {
				sdown = CostM;
			}
			
			terrain = new Cell(current.getCoordinate().x, current.getCoordinate().y + 1);
			if(river.search(terrain) != -1) {
				sup = CostR;
			}
			if(forest.search(terrain) != -1) {
				sup = CostF;
			}
			if(mountain.search(terrain) != -1) {
				sup = CostM;
			}
			
			
			
			newCell = new Cell(new Point(current.getCoordinate().x - 1, current.getCoordinate().y), sleft + current.getC());	
			newCell.setPrevious(current);
			OngoingProbe.probePath(newCell);
			
			newCell = new Cell(new Point(current.getCoordinate().x + 1, current.getCoordinate().y), sright + current.getC());
			newCell.setPrevious(current);
			OngoingProbe.probePath(newCell);
			
			newCell = new Cell(new Point(current.getCoordinate().x, current.getCoordinate().y - 1), sdown + current.getC());
			newCell.setPrevious(current);
			OngoingProbe.probePath(newCell);
			
			newCell = new Cell(new Point(current.getCoordinate().x, current.getCoordinate().y + 1), sup + current.getC());
			newCell.setPrevious(current);
			OngoingProbe.probePath(newCell);	
			
			OngoingProbe.remove(current);
		}
	}

	@SuppressWarnings("unchecked")
	private void BackTracking() {
		Cell current = (Cell) ClosedProbe.get(ClosedProbe.search(End));
		ShortPath.add(current);
		do {
			current = current.getPrevious();
			ShortPath.add(current);
		} while (!current.getCoordinate().equals(Start.getCoordinate()));
	}


	public static void Scale(double scale) {

		widthCell = (int) (scale);
		heightCell = (int) (scale);
		horizontal = MainWidth / widthCell;
		vertical = height / heightCell;
	}

	public static void buildSurrounding() {

		
		for(int x = -1; x < 33; x++) {
			Cell wall = new Cell(x, 23);
			Cell wall2 = new Cell(x, -1);
			Main.wall.buildTerrain(wall);
			Main.wall.buildTerrain(wall2);
		}
		
		for(int x = -1; x < 33; x++) {
			Cell wall = new Cell(23,x);
			Cell wall2 = new Cell(-1, x);
			Main.wall.buildTerrain(wall);
			Main.wall.buildTerrain(wall2);
		}
		
		
	}
	
	
	
	
}
