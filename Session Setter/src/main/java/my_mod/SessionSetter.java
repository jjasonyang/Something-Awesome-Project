package my_mod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SessionSetter.MODID)
public class SessionSetter
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public SessionSetter()
    {
        Multiplayer_Button multiplayer_button = new Multiplayer_Button();
        MinecraftForge.EVENT_BUS.register(multiplayer_button);
    }
}
