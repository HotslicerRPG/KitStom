package net.hotslicer.kit.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minestom.server.item.ItemStack;

import java.util.Map;

/**
 * @author Jenya705
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KitData {

    private String name;

    private Map<Integer, ItemStack> items;

}
