import java.awt.Point;

public class Cell {
	private int ID, x, y, newCost, cost, distance = -1;
	public Cell Previous = null;

	public Cell(int x, int y, int cost, int distance) {
		ID = -1;
		this.x = x;
		this.y = y;
		this.cost = cost;
		this.distance = distance;
		newCost = cost + distance;
	}

	public Cell(int x, int y, int cost) {
		ID = -1;
		this.x = x;
		this.y = y;
		this.cost = cost;
		getDistance();
		newCost = cost + distance;
	}

	public Cell(int x, int y) {
		ID = -1;
		this.x = x;
		this.y = y;
		this.newCost = -1;
		this.cost = -1;
		this.distance = -1;
	}

	public Cell(Point coordinate, int cost) {
		Previous = null;
		ID = -1;
		this.x = coordinate.x;
		this.y = coordinate.y;
		this.cost = cost;
		getDistance();
		newCost = cost + distance;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getC() {
		return cost;
	}

	public int getD() {
		return distance;
	}

	public int getF() {
		return newCost;
	}

	public int getID() {
		return ID;
	}

	public Point getCoordinate() {
		return new Point(x, y);
	}

	public void setCD(int cost, int distance) {
		this.cost = cost;
		this.distance = distance;
		newCost = cost + distance * 10;
	}

	public String toString(int i) {
		if (i == 1) {
			return "S:" + cost;
		} else if (i == 2) {
			return "D:" + distance;
		} else if (i == 0) {
			return "F:" + newCost;
		}
		return null;
	}

	public int getDistance() {

		if (distance == -1 && Main.End != null && Main.Start != null) {
			distance = Math.abs(x - Main.End.getX()) + Math.abs(y - Main.End.getY());
		}
		
		return distance;
	}

	public Cell getPrevious() {
		return Previous;

	}

	public void setPrevious(Cell Previous) {
		this.Previous = Previous;
	}

}
