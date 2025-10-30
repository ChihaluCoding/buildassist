package chihalu.buildassist;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import chihalu.buildassist.config.BuildAssistConfig;
import chihalu.buildassist.config.DisplayLocation;
import chihalu.buildassist.config.OverlayHighlightColor;
import chihalu.buildassist.config.OverlayStyle;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.Heightmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildAssist implements ModInitializer {
        public static final String MOD_ID = "buildassist";

        private static final Map<UUID, StartPoint> START_POINTS = new ConcurrentHashMap<>();

        public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

        @Override
        public void onInitialize() {
                BuildAssistConfig.load();
                AttackBlockCallback.EVENT.register(BuildAssist::handleBlockAttack);
                ServerTickEvents.END_WORLD_TICK.register(BuildAssist::handleWorldTick);
                LOGGER.info("BuildAssist initialized");
        }

        private static ActionResult handleBlockAttack(net.minecraft.entity.player.PlayerEntity player, World world, Hand hand,
                        BlockPos clickedPos, Direction direction) {
                if (world.isClient()) {
                        return ActionResult.PASS;
                }

                if (!(player instanceof ServerPlayerEntity serverPlayer)) {
                        return ActionResult.PASS;
                }

                if (hand != Hand.MAIN_HAND || !serverPlayer.getStackInHand(hand).isOf(Items.WOODEN_HOE)) {
                        return ActionResult.PASS;
                }

                BlockState originalState = world.getBlockState(clickedPos);
                UUID playerId = serverPlayer.getUuid();
                RegistryKey<World> dimension = world.getRegistryKey();

                StartPoint startPoint = START_POINTS.get(playerId);
                if (startPoint == null || !startPoint.dimension.equals(dimension)) {
                        START_POINTS.put(playerId, new StartPoint(clickedPos, dimension, serverPlayer));
                        serverPlayer.sendMessage(Text.translatable("text.buildassist.start_point"), true);
                        revertBlockIfTilled(world, clickedPos, originalState);
                        return ActionResult.SUCCESS;
                }

                BlockPos startPos = startPoint.position();
                BlockPos adjustedPos = new BlockPos(clickedPos.getX(), startPos.getY(), clickedPos.getZ());
                long xDiff = Math.abs(adjustedPos.getX() - startPos.getX());
                long yDiff = Math.abs(adjustedPos.getY() - startPos.getY());
                long zDiff = Math.abs(adjustedPos.getZ() - startPos.getZ());
                long xCount = xDiff + 1L;
                long yCount = yDiff + 1L;
                long zCount = zDiff + 1L;
                long total = xCount * yCount * zCount;

                START_POINTS.remove(playerId);
                boolean overlay = BuildAssistConfig.getDisplayLocation() == DisplayLocation.ACTION_BAR;
                boolean isAxisAlignedLine = yDiff == 0 && ((xDiff == 0L && zDiff != 0L) || (zDiff == 0L && xDiff != 0L));
                boolean isDiagonalLine = yDiff == 0 && xDiff == zDiff && xDiff != 0L;
                if (isDiagonalLine || isAxisAlignedLine) {
                        long lineCount = Math.max(xDiff, zDiff) + 1L;
                        String translationKey = isDiagonalLine ? "text.buildassist.block_count_diagonal"
                                        : "text.buildassist.block_count_line";
                        serverPlayer.sendMessage(Text.translatable(translationKey, total, lineCount), overlay);
                } else {
                        serverPlayer.sendMessage(Text.translatable("text.buildassist.block_count", total), overlay);
                }
                revertBlockIfTilled(world, clickedPos, originalState);
                return ActionResult.SUCCESS;
        }

        private static void handleWorldTick(ServerWorld world) {
                if (START_POINTS.isEmpty()) {
                        return;
                }

                int overlayRange = Math.max(0, BuildAssistConfig.getOverlayRange());
                if (overlayRange == 0) {
                        return;
                }

                boolean requireHoe = !BuildAssistConfig.isOverlayAlwaysVisible();

                START_POINTS.values().stream()
                                .filter(startPoint -> startPoint.dimension().equals(world.getRegistryKey()))
                                .forEach(startPoint -> {
                                        ServerPlayerEntity player = startPoint.player();
                                        if (player == null) {
                                                return;
                                        }
                                        if (requireHoe
                                                        && !player.getStackInHand(Hand.MAIN_HAND).isOf(Items.WOODEN_HOE)) {
                                                return;
                                        }
                                        spawnOverlay(world, player, startPoint.position(), overlayRange);
                                });
        }

        private static void spawnOverlay(ServerWorld world, ServerPlayerEntity player, BlockPos origin, int range) {
                OverlayStyle style = BuildAssistConfig.getOverlayStyle();
                ParticleEffect baseEffect = style.getBaseEffect();
                OverlayHighlightColor highlightColor = BuildAssistConfig.getOverlayHighlightColor();
                ParticleEffect highlightEffect = highlightColor.getParticleEffect();
                int interval = Math.max(1, BuildAssistConfig.getOverlayHighlightInterval());
                boolean includeOrigin = BuildAssistConfig.isHighlightIncludeOrigin();

                ParticleEffect originEffect = includeOrigin ? highlightEffect : baseEffect;
                spawnOverlayAt(world, player, origin.getX(), origin.getY(), origin.getZ(), originEffect, style);
                for (int step = 1; step <= range; step++) {
                        ParticleEffect effect = (step % interval == 0) ? highlightEffect : baseEffect;
                        spawnOverlayAt(world, player, origin.getX() + step, origin.getY(), origin.getZ(), effect, style);
                        spawnOverlayAt(world, player, origin.getX() - step, origin.getY(), origin.getZ(), effect, style);
                        spawnOverlayAt(world, player, origin.getX(), origin.getY(), origin.getZ() + step, effect, style);
                        spawnOverlayAt(world, player, origin.getX(), origin.getY(), origin.getZ() - step, effect, style);
                }
        }

        private static void spawnOverlayAt(ServerWorld world, ServerPlayerEntity player, int x, int baseY, int z,
                        ParticleEffect effect, OverlayStyle style) {
                int surfaceY = resolveSurfaceY(world, x, baseY, z);
                double baseX = x + 0.5;
                double baseYPos = surfaceY + 1.02;
                double baseZ = z + 0.5;
                for (OverlayStyle.Offset offset : style.getOffsets()) {
                        emitParticle(world, player, baseX + offset.x(), baseYPos + offset.y(), baseZ + offset.z(), effect);
                }
        }

        private static int resolveSurfaceY(ServerWorld world, int x, int fallbackY, int z) {
                int topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z) - 1;
                int bottomY = world.getBottomY();
                if (topY < bottomY) {
                        return fallbackY;
                }
                return Math.max(fallbackY, topY);
        }

        private static void emitParticle(ServerWorld world, ServerPlayerEntity player, double x, double y, double z,
                        ParticleEffect effect) {
                world.spawnParticles(player, effect, true, true, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        }

        private record StartPoint(BlockPos position, RegistryKey<World> dimension, ServerPlayerEntity player) {
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
