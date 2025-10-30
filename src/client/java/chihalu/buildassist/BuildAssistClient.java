package chihalu.buildassist;

import chihalu.buildassist.config.BuildAssistConfig;
import net.fabricmc.api.ClientModInitializer;

public class BuildAssistClient implements ClientModInitializer {
        @Override
        public void onInitializeClient() {
                BuildAssistConfig.load();
        }
}
