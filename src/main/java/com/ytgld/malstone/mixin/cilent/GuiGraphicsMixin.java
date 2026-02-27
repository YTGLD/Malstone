package com.ytgld.malstone.mixin.cilent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.items.init.BaseItem;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin  {
    @Shadow @Final private Minecraft minecraft;
    @Shadow public abstract int guiWidth();
    @Shadow public abstract int guiHeight();

    @Shadow public abstract void flush();

    @Shadow private boolean managed;

    @Shadow private ItemStack tooltipStack;

    @Shadow public abstract PoseStack pose();

    @Shadow @Final private GuiSpriteManager sprites;

    @Shadow @Final private PoseStack pose;

    @Unique
    ItemStack cI1_21_9$itemstack = ItemStack.EMPTY;

    @Inject(at = @At(value = "RETURN"),method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V")
    public void Malstone$ClientTooltipPositioner(Font p_282675_, List<ClientTooltipComponent> p_282615_, int x, int y, ClientTooltipPositioner p_282442_, CallbackInfo ci) {
        if (tooltipStack.getItem() instanceof BaseItem) {
            RenderTooltipEvent.Pre preEvent = ClientHooks.onRenderTooltipPre(this.tooltipStack, (GuiGraphics) (Object) this, x, y, guiWidth(), guiHeight(), p_282615_, p_282675_, p_282442_);

            int i = 0;
            int j = p_282615_.size() == 1 ? -2 : 0;

            for (ClientTooltipComponent clienttooltipcomponent : p_282615_) {
                int k = clienttooltipcomponent.getWidth(preEvent.getFont());
                if (k > i) {
                    i = k;
                }

                j += clienttooltipcomponent.getHeight();
            }

            int i2 = i;
            int j2 = j;


            Vector2ic vector2ic = p_282442_.positionTooltip(this.guiWidth(), this.guiHeight(), preEvent.getX(), preEvent.getY(), i2, j2);

            int l = vector2ic.x();
            int i1 = vector2ic.y();
            this.pose.pushPose();
            this.pose.popPose();
            if (tooltipStack.getItem() instanceof Twisted) {
                this.pose.pushPose();
                Malstone$renderItemBlackShadowTooltipBackground((GuiGraphics) (Object) this, l, i1, i, j, 800);
                this.pose.popPose();
            }
        }
    }

    @Unique
    public void Malstone$renderItemBlackShadowTooltipBackground(GuiGraphics guiGraphics, int x, int y, int width, int height, int z) {
        // 左上角
        int topLeftX = x - 3 - 9+2;
        int topLeftY = y - 3 - 9;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, -2,z);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Malstone.MODID,
                        "textures/gui/tooltip/tool_0_0.png"), topLeftX, topLeftY,  0, 0, 48,48, 48, 48);
        guiGraphics.pose().popPose();

        // 中间位置
        int middleX = x + (width - 48) / 2;
        int middleY = y - 3 - 6;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, -7,z);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Malstone.MODID,
                        "textures/gui/tooltip/tool_middle_0.png"),middleX, middleY, 0, 0,48,48  , 48, 48);
        guiGraphics.pose().popPose();
        // 右上角
        int topRightX = x + width + 3 - 48+6;
        int topRightY = y - 3 - 9;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, -2,z);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Malstone.MODID,
                        "textures/gui/tooltip/tool_0_1.png"), topRightX, topRightY, 0, 0,48,48 , 48, 48);
        guiGraphics.pose().popPose();

        // 左下角
        int bottomLeftX = x - 3 - 9 + 2;
        int bottomLeftY = y + height + 3 - 48 + 4;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 4,z);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Malstone.MODID,
                        "textures/gui/tooltip/tool_1_0.png"),bottomLeftX, bottomLeftY,0, 0, 48,48, 48, 48);
        guiGraphics.pose().popPose();

        // 右下角
        int bottomRightX = x + width + 3 - 48 + 6;
        int bottomRightY = y + height + 3 - 48 + 4;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 4,z);
        guiGraphics.blit(
                ResourceLocation.fromNamespaceAndPath(Malstone.MODID,
                        "textures/gui/tooltip/tool_1_1.png"),bottomRightX, bottomRightY, 0, 0, 48,48, 48, 48);
        guiGraphics.pose().popPose();
    }
}
