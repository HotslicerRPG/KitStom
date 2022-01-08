package net.hotslicer.kit.instance;

import lombok.Data;
import net.hotslicer.kit.BlockPosition;

@Data
public class KitInstanceConfiguration {

    private String path = "";

    private BlockPosition[] spawnPositions = new BlockPosition[0];

}
