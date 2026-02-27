package com.ytgld.malstone.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.sammy.malum.client.screen.codex.screens.ArcanaProgressionScreen;
import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.client.CommonEntries;
import com.ytgld.malstone.items.init.BaseItem;
import com.ytgld.malstone.items.init.Twisted;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow public abstract int guiWidth();
    @Shadow public abstract int guiHeight();
    @Shadow private ItemStack tooltipStack;
    @Shadow @Final private PoseStack pose;


    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At(value = "RETURN" ))
    private void Malstone$ClientTooltipPositioner6(Font p_283128_, List<Component> p_282716_, Optional<TooltipComponent> p_281682_, int p_283678_, int p_281696_, CallbackInfo ci) {
        List<ClientTooltipComponent> list = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(this.tooltipStack, p_282716_, p_281682_, p_283678_, guiWidth(), guiHeight(), p_283128_);
        this.Malstone$ClientTooltipPositioner(p_283128_, list, p_283678_, p_281696_, DefaultTooltipPositioner.INSTANCE);
    }

    @Inject(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V", at = @At(value = "RETURN" ))
    private void Malstone$ClientTooltipPositioner5(Font p_282739_, List<Component> p_281832_, int p_282191_, int p_282446_, CallbackInfo ci) {
        List<ClientTooltipComponent> components = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(this.tooltipStack, p_281832_, p_282191_, guiWidth(), guiHeight(), p_282739_);
        this.Malstone$ClientTooltipPositioner(p_282739_, components, p_282191_, p_282446_, DefaultTooltipPositioner.INSTANCE);
    }
//    @Inject(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/world/item/ItemStack;)V", at = @At(value = "RETURN" ),remap = false)
//    private void Malstone$ClientTooltipPositioner4(Font font, List<? extends FormattedText> tooltips, int mouseX, int mouseY, ItemStack stack, CallbackInfo ci) {
//        List<ClientTooltipComponent> components = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponents(stack, tooltips, mouseX, guiWidth(), guiHeight(), font);
//        this.Malstone$ClientTooltipPositioner(font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
//
//    }
//    @Inject(method = "renderComponentTooltipFromElements(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/world/item/ItemStack;)V", at = @At(value = "RETURN" ),remap = false)
//    private void Malstone$ClientTooltipPositioner3(Font font, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int mouseY, ItemStack stack, CallbackInfo ci) {
//        List<ClientTooltipComponent> components = net.minecraftforge.client.ForgeHooksClient.gatherTooltipComponentsFromElements(stack, elements, mouseX, guiWidth(), guiHeight(), font);
//        this.Malstone$ClientTooltipPositioner(font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
//    }
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V", at = @At(value = "RETURN" ))
    private void Malstone$ClientTooltipPositioner2(Font p_282192_, List<? extends FormattedCharSequence> p_282297_, int p_281680_, int p_283325_, CallbackInfo ci) {
        this.Malstone$ClientTooltipPositioner(p_282192_, p_282297_.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), p_281680_, p_283325_, DefaultTooltipPositioner.INSTANCE);
    }
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;II)V", at = @At(value = "RETURN" ))
    private void Malstone$ClientTooltipPositioner1(Font p_281627_, List<FormattedCharSequence> p_283313_, ClientTooltipPositioner p_283571_, int p_282367_, int p_282806_, CallbackInfo ci) {
        this.Malstone$ClientTooltipPositioner(p_281627_, p_283313_.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), p_282367_, p_282806_, p_283571_);
    }


    @Unique
    public void Malstone$ClientTooltipPositioner(Font p_282675_, List<ClientTooltipComponent> p_282615_, int x, int y, ClientTooltipPositioner p_282442_) {
        if (tooltipStack.getItem() instanceof BaseItem) {
            RenderTooltipEvent.Pre preEvent = ForgeHooksClient.onRenderTooltipPre(this.tooltipStack,
                    (GuiGraphics) (Object) this, x, y, guiWidth(), guiHeight(), p_282615_, p_282675_, p_282442_);
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
                new ResourceLocation(Malstone.MODID,
                        "textures/gui/tooltip/tool_0_0.png"), topLeftX, topLeftY,  0, 0, 48,48, 48, 48);
        guiGraphics.pose().popPose();

        // 中间位置
        int middleX = x + (width - 48) / 2;
        int middleY = y - 3 - 6;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, -7,z);
        guiGraphics.blit(
                new ResourceLocation(Malstone.MODID,
                        "textures/gui/tooltip/tool_middle_0.png"),middleX, middleY, 0, 0,48,48  , 48, 48);
        guiGraphics.pose().popPose();
        // 右上角
        int topRightX = x + width + 3 - 48+6;
        int topRightY = y - 3 - 9;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, -2,z);
        guiGraphics.blit(
                new ResourceLocation(Malstone.MODID,
                        "textures/gui/tooltip/tool_0_1.png"), topRightX, topRightY, 0, 0,48,48 , 48, 48);
        guiGraphics.pose().popPose();

        if (!ModList.get().isLoaded("obscure_tooltips")) {
            // 左下角
            int bottomLeftX = x - 3 - 9 + 2;
            int bottomLeftY = y + height + 3 - 48 + 4;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 4, z);
            guiGraphics.blit(
                    new ResourceLocation(Malstone.MODID,
                            "textures/gui/tooltip/tool_1_0.png"), bottomLeftX, bottomLeftY, 0, 0, 48, 48, 48, 48);
            guiGraphics.pose().popPose();

            // 右下角
            int bottomRightX = x + width + 3 - 48 + 6;
            int bottomRightY = y + height + 3 - 48 + 4;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 4, z);
            guiGraphics.blit(
                    new ResourceLocation(Malstone.MODID,
                            "textures/gui/tooltip/tool_1_1.png"), bottomRightX, bottomRightY, 0, 0, 48, 48, 48, 48);
            guiGraphics.pose().popPose();
        }
    }
}
