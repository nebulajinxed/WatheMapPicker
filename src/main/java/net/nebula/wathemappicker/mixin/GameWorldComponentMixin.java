package net.nebula.wathemappicker.mixin;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameWorldComponent.class)
public class GameWorldComponentMixin {
    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Ljava/lang/Object;equals(Ljava/lang/Object;)Z"))
    private static boolean equals(Object instance, Object o) {
        return true;
    }
}
