package net.nebula.wathemappicker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class MapVotePlayerComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<MapVotePlayerComponent> KEY = ComponentRegistry.getOrCreate(Wathemappicker.id("mapvote"), MapVotePlayerComponent.class);
    private final PlayerEntity player;
    public String voteTarget = "";

    public void reset() {
        this.voteTarget = "";
        this.sync();
    }

    public MapVotePlayerComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void clientTick() {
    }

    public void serverTick() {
    }

    public void setVoteTarget(String voteTarget) {
        this.voteTarget = voteTarget;
        this.sync();
    }

    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putString("votetarget", this.voteTarget);
    }

    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.voteTarget = tag.contains("votetarget") ? tag.getString("votetarget") : "";
    }
}
