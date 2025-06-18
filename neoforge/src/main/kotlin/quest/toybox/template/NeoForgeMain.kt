package quest.toybox.template

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import quest.toybox.template.misc.Utils

@Mod(Utils.MOD_ID)
class NeoForgeMain(bus: IEventBus, mod: ModContainer) {
    init {
        println("Hello World from NeoForge Main")
    }
}
