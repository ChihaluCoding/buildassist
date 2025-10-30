package net.minecraft.util.shape;

import net.minecraft.util.math.Direction;

/**
 * Minimal voxel shape representation for stub builds.
 */
public class VoxelShape {
        public static VoxelShape empty() {
                return new VoxelShape();
        }

        public boolean isEmpty() {
                return true;
        }

        public double getMax(Direction.Axis axis) {
                return 1.0;
        }
}
