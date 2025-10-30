package chihalu.buildassist.compat.modmenu;

import chihalu.buildassist.client.gui.BuildAssistConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class BuildAssistModMenuIntegration implements ModMenuApi {
        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory() {
                return BuildAssistConfigScreen::new;
        }
}
