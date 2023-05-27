package generator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.Timer;

import main.*;
import util.Cell;

public class HuntAndKillGen {
	private final List<Cell> grid;
	private Cell current;

	public HuntAndKillGen(List<Cell> grid, MazeGridPanel panel) {
		this.grid = grid;
		current = grid.get(0);
		final Timer timer = new Timer(Maze.speed, null);
		timer.addActionListener(e -> {
			if (!grid.parallelStream().allMatch(Cell::isVisited)) {
				carve();
			} else {
				current = null;
				Maze.generated = true;
				timer.stop();
			}
			panel.setCurrent(current);
			panel.repaint();
			timer.setDelay(Maze.speed);
		});
		timer.start();
	}

	private void carve() {
		current.setVisited(true);
		Cell next = current.getUnvisitedNeighbour(grid);
		if (next != null) {
			current.removeWalls(next);
			current = next;
		} else {
			// hunt
			Optional<Cell> opt = grid.parallelStream()
					.filter(c ->
							c.isVisited() && c.getUnvisitedNeighboursList(grid).size() > 0)
					.findAny();
			opt.ifPresent(cell -> current = cell);
		}
	}
}
