package engine.model;

/**
 * DynamicModel extends Model with the ability to change the map dimensions and
 * cell size at runtime.
 *
 * Imane – implement the resize / cellSize update methods so that the world grid
 * is recreated safely while preserving or migrating existing entities.
 */
public class DynamicModel extends Model {

    public DynamicModel(int nrows, int ncols, double cellSizeMeters) {
        super(nrows, ncols, cellSizeMeters);
    }

    /**
     * Change world size. Existing entities that fall outside new bounds should
     * be removed or clamped. Viewports must be informed as well.
     */
    public void resize(int newRows, int newCols) {
        // TODO (Imane): implement grid reallocation & entity repositioning
    }

    /**
     * Update the size (in meters) of each cell.
     */
    public void setCellSizeMeters(double meters) {
        // TODO (Imane): adapt positions, wrap logic and notify views.
    }
} 