package net.nebula.wathemappicker.client.gui.widget;

import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.doctor4t.wathe.util.ShopEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;


public class MapVotingWidget extends ButtonWidget {

    public final LimitedInventoryScreen screen;
    private final ItemStack itemSupplier;
    public final String translatable;

    public MapVotingWidget(
            LimitedInventoryScreen screen,
            int x,
            int y,
            ItemStack itemSupplier,
            PressAction onPress,
            String translatable
    ) {
        super(x, y, 16, 16, Text.literal(""), onPress, DEFAULT_NARRATION_SUPPLIER);
        this.screen = screen;
        this.itemSupplier = itemSupplier;
        this.translatable = translatable;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        // Draw background slot texture
        context.drawGuiTexture(
                ShopEntry.Type.TOOL.getTexture(),
                this.getX() - 7,
                this.getY() - 7,
                30,
                30
        );

        // Draw supplied item
        context.drawItem(itemSupplier, this.getX(), this.getY());

        // Highlight + tooltip when hovered
        if (this.isHovered()) {
            drawSlotHighlight(context, this.getX(), this.getY(), 0);
            context.drawTooltip(
                    MinecraftClient.getInstance().textRenderer,
                    Text.translatable(translatable),
                    this.getX() + 10,
                    this.getY() + 16
            );
        }
    }

    private void drawSlotHighlight(DrawContext context, int x, int y, int z) {
        int color = 0x93000000; // semi-transparent dark
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 14, color, color, z);
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y + 14, x + 15, y + 15, color, color, z);
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y + 15, x + 14, y + 16, color, color, z);
    }
}
