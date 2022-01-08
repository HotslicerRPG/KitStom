package net.hotslicer.kit;

import lombok.Data;
import net.minestom.server.coordinate.Pos;

@Data
public class BlockPosition {

    private int x;
    private int y;
    private int z;
    private float yaw;
    private float pitch;

    public Pos toPos() {
        return new Pos(x + 0.5, y, z + 0.5, yaw, pitch);
    }

}
