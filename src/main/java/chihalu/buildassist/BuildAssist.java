package chihalu.buildassist;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildAssist implements ModInitializer {
        public static final String MOD_ID = "buildassist";

        private static final Map<UUID, StartPoint> START_POINTS = new ConcurrentHashMap<>();

        public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

        @Override
        public void onInitialize() {
                UseBlockCallback.EVENT.register(BuildAssist::handleBlockUse);
                LOGGER.info("BuildAssist initialized");
        }

        private static ActionResult handleBlockUse(net.minecraft.entity.player.PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
                if (world.isClient()) {
                        return ActionResult.PASS;
                }

                if (hand != Hand.MAIN_HAND || !player.getStackInHand(hand).isOf(Items.WOODEN_HOE)) {
                        return ActionResult.PASS;
                }

                BlockPos clickedPos = hitResult.getBlockPos();
                BlockState originalState = world.getBlockState(clickedPos);
                UUID playerId = player.getUuid();
                RegistryKey<World> dimension = world.getRegistryKey();

                StartPoint startPoint = START_POINTS.get(playerId);
                if (startPoint == null || !startPoint.dimension.equals(dimension)) {
                        START_POINTS.put(playerId, new StartPoint(clickedPos, dimension));
                        player.sendMessage(Text.translatable("text.buildassist.start_point"), true);
                        revertBlockIfTilled(world, clickedPos, originalState);
                        return ActionResult.PASS;
                }

                BlockPos startPos = startPoint.position;
                long xCount = Math.abs(clickedPos.getX() - startPos.getX()) + 1L;
                long yCount = Math.abs(clickedPos.getY() - startPos.getY()) + 1L;
                long zCount = Math.abs(clickedPos.getZ() - startPos.getZ()) + 1L;
                long total = xCount * yCount * zCount;

                START_POINTS.remove(playerId);
                player.sendMessage(Text.translatable("text.buildassist.block_count", total), true);
                revertBlockIfTilled(world, clickedPos, originalState);
                return ActionResult.PASS;
        }

        private record StartPoint(BlockPos position, RegistryKey<World> dimension) {
        }

        private static void revertBlockIfTilled(World world, BlockPos clickedPos, BlockState originalState) {
                if (!(world instanceof ServerWorld serverWorld)) {
                        return;
                }

                serverWorld.getServer().execute(() -> {
                        if (serverWorld.getBlockState(clickedPos).isOf(Blocks.FARMLAND)
                                        && !originalState.isOf(Blocks.FARMLAND)) {
                                serverWorld.setBlockState(clickedPos, originalState);
                        }
                });
        }
}
