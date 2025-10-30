package net.minecraft.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

/**
 * Minimal block state stub for offline compilation.
 */
public class BlockState {
        public boolean isOf(Object block) {
                return false;
        }

        public VoxelShape getCollisionShape(World world, BlockPos pos, ShapeContext context) {
                return VoxelShape.empty();
        }

        public VoxelShape getOutlineShape(World world, BlockPos pos, ShapeContext context) {
                return VoxelShape.empty();
        }
}
