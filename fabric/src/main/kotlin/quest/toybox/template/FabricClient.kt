package quest.toybox.template

import net.fabricmc.api.ClientModInitializer

object FabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        println("Hello World from Fabric Client")
    }
}
