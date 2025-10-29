package net.minecraft.entity.player;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

/**
 * Simplified representation of a Minecraft player for compilation only.
 */
public class PlayerEntity {
        private final UUID uuid = UUID.randomUUID();
        private ItemStack heldStack = new ItemStack();

        public UUID getUuid() {
                return uuid;
        }

        public ItemStack getStackInHand(Hand hand) {
                return heldStack;
        }

        public void sendMessage(Text message, boolean actionBar) {
                // no-op for stub
        }
}
