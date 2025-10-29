package net.minecraft.util.hit;

import net.minecraft.util.math.BlockPos;

/**
 * Minimal stub used to expose the block position of a hit result.
 */
public class BlockHitResult {
        private final BlockPos blockPos;

        public BlockHitResult(BlockPos blockPos) {
                this.blockPos = blockPos;
        }

        public BlockPos getBlockPos() {
                return blockPos;
        }
}
