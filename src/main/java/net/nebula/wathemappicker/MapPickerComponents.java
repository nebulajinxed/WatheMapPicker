package net.nebula.wathemappicker;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class MapPickerComponents implements EntityComponentInitializer {
    public MapPickerComponents() {
    }

    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, MapVotePlayerComponent.KEY).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(MapVotePlayerComponent::new);
    }
}