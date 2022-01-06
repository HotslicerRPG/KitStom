package net.hotslicer.kit.data;

import lombok.RequiredArgsConstructor;
import net.hotslicer.kit.KitExtension;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jenya705
 */
@RequiredArgsConstructor
public class KitManager {

    private final KitExtension extension;

    public void giveKit(KitData data, Player player) {
        if (extension.getConfig().isReplaceItems()) {
            player.getInventory().clear();
        }
        data.getItems().forEach((index, item) -> player.getInventory().setItemStack(index, item));
    }

    public KitData generateKitData(String name, AbstractInventory inventory) {
        Map<Integer, ItemStack> items = new HashMap<>();
        int i = 0;
        for (ItemStack itemStack: inventory.getItemStacks()) {
            if (itemStack.getMaterial() != Material.AIR) {
                items.put(i, itemStack);
            }
            i++;
        }
        return new KitData(name, items);
    }

}
