package net.minecraft.item;

/**
 * Minimal item stack used for offline builds.
 */
public class ItemStack {
        private Item item = Items.WOODEN_HOE;

        public boolean isOf(Item other) {
                return item != null && item.getId().equals(other.getId());
        }
}
