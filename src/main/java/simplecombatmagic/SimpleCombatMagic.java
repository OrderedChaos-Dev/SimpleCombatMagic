package simplecombatmagic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import simplecombatmagic.capabilities.CombatMagic;
import simplecombatmagic.capabilities.CombatMagicStorage;
import simplecombatmagic.capabilities.ICombatMagic;

@Mod("simplecombatmagic")
public class SimpleCombatMagic
{
	public static final String MOD_ID = "simplecombatmagic";
    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleCombatMagic() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonStuff);

        MinecraftForge.EVENT_BUS.register(new SimpleCombatMagicEventBusHandler());
    }

    private void setup(final FMLCommonSetupEvent event){

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }
    
    private void doCommonStuff(final FMLCommonSetupEvent event) {
    	CapabilityManager.INSTANCE.register(ICombatMagic.class, new CombatMagicStorage(), CombatMagic::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event){
    }

    private void processIMC(final InterModProcessEvent event) {

    }
}
