import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Terrain extends ArrayList {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Terrain() {
		super();
	}

	@SuppressWarnings("unchecked")
	public void buildTerrain(Cell cell) {
		if (search(cell) == -1) {
			boolean blocked = false;
			if (Main.Start != null) {
				if (cell.getCoordinate().equals(Main.Start.getCoordinate())) {
					blocked = true;
				}
			}

			if (Main.wall.size() > 0) {
				if (Main.wall.search(cell) != -1) {
					blocked = true;
				}
			}
			if (Main.End != null) {
				if (cell.getCoordinate().equals(Main.End.getCoordinate())) {
					blocked = true;
				}
			}
			if (!blocked) {
				this.add(cell);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void probePath(Cell cell) {
		if (search(cell) == -1) {
			boolean blocked = false;

			if (Main.wall.size() > 0) {
				if (Main.wall.search(cell) != -1) {
					blocked = true;
				}
			}
			if (Main.ClosedProbe.size() > 0) {
				if (Main.ClosedProbe.search(cell) != -1) {
					blocked = true;
				}
			}
			if (Main.Start != null&&this.size()!=0) {
				if (cell.getCoordinate().equals(Main.Start.getCoordinate())) {
					blocked = true;
				}
			}

			if (!blocked) {
				this.add(cell);
			}
		}else{
			int i=search(cell);
			Cell previous=(Cell) this.get(i);
			if(previous.getC()>cell.getC()){
				this.remove(i);
				this.add(cell);
			}
		}
	}
	
	
	public void remove(Cell cell) {
		int n = search(cell);
		if (n != -1) {
			this.remove(n);
		}

	}

	public int search(Cell cell) {
		int n = -1;
		for (int i = 0; i < this.size(); i++) {
			if (((Cell) this.get(i)).getCoordinate().equals(cell.getCoordinate())) {
				n = i;
				break;
			}
		}
		return n;
	}
	
	
	public static void mergeSort(Cell data[], int begin, int end) {
		int low = begin;
		int high = end;
		if (low >= high) {
			return;
		}
		int middle = (low + high) / 2;
		mergeSort(data, low, middle);
		mergeSort(data, middle + 1, high);
		int end_left = middle;
		int start_right = middle + 1;
		while ((begin <= end_left) && (start_right <= high)) {
			if (data[low].getF() < data[start_right].getF()) {
				low++;
			} else {
				Cell Temp = data[start_right];
				for (int k = start_right - 1; k >= low; k--) {
					data[k + 1] = data[k];
				}
				data[low] = Temp;
				low++;
				end_left++;
				start_right++;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sortList() {
		Cell[] temp = new Cell[this.size()];
		for (int i = 0; i < this.size(); i++) {
			temp[i] = (Cell) this.get(i);
		}
		mergeSort(temp, 0, temp.length - 1);
		this.clear();
		for (int i = 0; i < temp.length; i++) {
			this.add(temp[i]);
		}
	}
	

}
