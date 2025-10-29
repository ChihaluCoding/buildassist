package chihalu.buildassist;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
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
                UUID playerId = player.getUuid();
                RegistryKey<World> dimension = world.getRegistryKey();

                StartPoint startPoint = START_POINTS.get(playerId);
                if (startPoint == null || !startPoint.dimension.equals(dimension)) {
                        START_POINTS.put(playerId, new StartPoint(clickedPos, dimension));
                        player.sendMessage(Text.translatable("text.buildassist.start_point", clickedPos.getX(), clickedPos.getY(), clickedPos.getZ()), true);
                        return ActionResult.PASS;
                }

                BlockPos startPos = startPoint.position;
                int dx = Math.abs(clickedPos.getX() - startPos.getX());
                int dy = Math.abs(clickedPos.getY() - startPos.getY());
                int dz = Math.abs(clickedPos.getZ() - startPos.getZ());
                int total = dx + dy + dz;

                START_POINTS.remove(playerId);
                player.sendMessage(Text.translatable("text.buildassist.distance", dx, dy, dz, total), true);
                return ActionResult.PASS;
        }

        private record StartPoint(BlockPos position, RegistryKey<World> dimension) {
        }
}
