package items.logic;

import constants.Constants;

import java.awt.geom.Rectangle2D;

public class Heart extends Item {

    public Heart(float x, float y) {
        super(x, y , new Rectangle2D.Float(x, y , 32 * Constants.TILE_SCALE, 32 * Constants.TILE_SCALE));
    }

    @Override
    void handleItem() {

    }
}
