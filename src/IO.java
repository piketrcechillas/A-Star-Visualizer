import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class IO
		implements KeyListener, FocusListener, MouseListener, MouseMotionListener, MouseWheelListener {

	public static boolean Grid = true;
	public boolean run = false;
	public static int MouseX, MouseY;
	public String mode = "Wall";

	@Override
	public void mouseDragged(MouseEvent e) {

		if (e.isMetaDown()) {
			ShiftToggle = true;
		} else
			ShiftToggle = false;
		cellControl(e);
		sceneMap(e);

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.isMetaDown()) {
			ShiftToggle = true;
		} else
			ShiftToggle = false;
		cellControl(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		ShiftToggle = false;
		Preset = true;

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	boolean ShiftToggle = false, ControlScreenDrag = false, Preset = true, auto = false;
	boolean startset = false, endset = false;

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			ControlScreenDrag = false;
		}
	}

	public int initialX = 0, initialY = 0, initialxLength = 0, initialyLength = 0;

	public void sceneMap(MouseEvent e) {
		if (ControlScreenDrag && Preset) {
			initialX = e.getX();
			initialY = e.getY();
			initialxLength = Main.xLength;
			initialyLength = Main.yLength;
			Preset = false;
		}
		if (ControlScreenDrag) {
			Main.xLength = initialxLength + (int) ((initialX - e.getX()) / Main.widthCell);
			Main.yLength = initialyLength + (int) ((initialY - e.getY()) / Main.heightCell);
		}
	}

	public void cellControl(MouseEvent e) {
		if(mode.equals("Wall")) {
			Cell wall = null;
			if (!run && !ShiftToggle && !ControlScreenDrag) {
	
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;
	
				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					wall = new Cell(x, y);
					if (startset) {
						if (Main.End != null) {
							Main.Start = wall;
							Main.OngoingProbe.clear();
							Main.Start.setPrevious(Main.Start);
							Main.OngoingProbe.probePath(Main.Start);
							if (Main.End != null) {
								Main.Start.setCD(0, wall.getDistance());
							}
						}
						startset = false;
					} else if (endset) {
						Main.End = wall;
						endset = false;
					} else {
						Main.wall.buildTerrain(wall);
					}
					Main.iteration = 0;
				}
			}
			if (ShiftToggle && !ControlScreenDrag) {
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;
	
				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					wall = new Cell(x, y);
					Main.wall.remove(wall);
					Main.iteration = 0;
				}
			}
			wall = null;
		}
		
		
		if(mode.equals("River")) {
			Cell river = null;
			if (!run && !ShiftToggle && !ControlScreenDrag) {
	
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;
	
				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					river = new Cell(x, y);
					if (startset) {
						if (Main.End != null) {
							Main.Start = river;
							Main.OngoingProbe.clear();
							Main.Start.setPrevious(Main.Start);
							Main.OngoingProbe.probePath(Main.Start);
							if (Main.End != null) {
								Main.Start.setCD(0, river.getDistance());
							}
						}
						startset = false;
					} else if (endset) {
						Main.End = river;
						endset = false;
					} else {
						Main.river.buildTerrain(river);
					}
					Main.iteration = 0;
				}
			}
			if (ShiftToggle && !ControlScreenDrag) {
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;
	
				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					river = new Cell(x, y);
					Main.river.remove(river);
					Main.iteration = 0;
				}
			}
			river = null;
		}
		
		
		if(mode.equals("Forest")) {
			Cell forest = null;
			if (!run && !ShiftToggle && !ControlScreenDrag) {
	
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;

				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					forest = new Cell(x, y);
					if (startset) {
						if (Main.End != null) {
							Main.Start = forest;
							Main.OngoingProbe.clear();
							Main.Start.setPrevious(Main.Start);
							Main.OngoingProbe.probePath(Main.Start);
							if (Main.End != null) {
								Main.Start.setCD(0, forest.getDistance());
							}
						}
						startset = false;
					} else if (endset) {
						Main.End = forest;
						endset = false;
					} else {
						Main.forest.buildTerrain(forest);
					}
					Main.iteration = 0;
				}
			}
			if (ShiftToggle && !ControlScreenDrag) {
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;
	
				// System.out.println("Delete: [" + x + "," + y + "]");
				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					forest = new Cell(x, y);
					Main.forest.remove(forest);
					Main.iteration = 0;
				}
			}
			forest = null;
		}
		if(mode.equals("Mountain")) {
			Cell mountain = null;
			if (!run && !ShiftToggle && !ControlScreenDrag) {
	
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;
	
				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					mountain = new Cell(x, y);
					if (startset) {
						if (Main.End != null) {
							Main.Start = mountain;
							Main.OngoingProbe.clear();
							Main.Start.setPrevious(Main.Start);
							Main.OngoingProbe.probePath(Main.Start);
							if (Main.End != null) {
								Main.Start.setCD(0, mountain.getDistance());
							}
						}
						startset = false;
					} else if (endset) {
						Main.End = mountain;
						endset = false;
					} else {
						Main.mountain.buildTerrain(mountain);
					}
					Main.iteration = 0;
				}

			}
			if (ShiftToggle && !ControlScreenDrag) {
				int x = (int) (e.getX() / Main.widthCell) + Main.xLength;
				int y = (int) (e.getY() / Main.heightCell) + Main.yLength;

				if (x >= Main.xLength && x <= (Main.horizontal + Main.xLength) && y >= Main.yLength
						&& y <= (Main.vertical + Main.yLength)) {
					mountain = new Cell(x, y);
					Main.mountain.remove(mountain);
					Main.iteration = 0;
				}
			}
			mountain = null;
		}

		
	}


	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

}
