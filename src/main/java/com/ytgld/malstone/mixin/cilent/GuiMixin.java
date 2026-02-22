package com.ytgld.malstone.mixin.cilent;

import com.ytgld.malstone.Malstone;
import com.ytgld.malstone.attribute.AttReg;
import com.ytgld.malstone.client.MGuiGraphics;
import com.ytgld.malstone.client.MRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    public int leftHeight;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "renderArmorLevel")
    private void renderArmorLevel$Malstone(GuiGraphics p_283143_, CallbackInfo ci) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            float i = player.getData(AttReg.BloodShield_.get());
            if (i > 0&&$showAlpha$Malstone > 0) {
                int l = p_283143_.guiWidth() / 2 - 91;
                this.minecraft.getProfiler().push("blood_shield");
                $renderArmor$Malstone(p_283143_, player, p_283143_.guiHeight() - this.leftHeight + 10, 1, 0, l);
                this.minecraft.getProfiler().pop();
                this.leftHeight += 20;
            }
        }
    }

    @Unique
    private void $drawA1234$Malstone(int i,int x,GuiGraphics guiGraphics,int yy,
                                         ResourceLocation a1,
                                         ResourceLocation a2,
                                         ResourceLocation a3,
                                         ResourceLocation a4,
                                         int aa,
                                         int b,
                                         int c,
                                         int d,

                                         int offset,float a,float light
    ){
        ShaderInstance distorted = MRender.getLiveShaderInstance();
        if (i > 0) {
            int xx = (x) + offset * 8- 1;
            if (i > aa + 3) {
                new MGuiGraphics(()->distorted).blit(guiGraphics, a1,
                        9 + ((x) + (offset - 1) * 8- 1), yy, 0, 0, 9, 9, 9, 9, 1, 1, 1, a);
            }
            if (i == aa) {
                new MGuiGraphics(()->distorted).blit( guiGraphics,a4, xx, yy, 0, 0, 9, 9, 9, 9,1, 1,1,a);
            }
            if (i == b) {
                new MGuiGraphics(()->distorted).blit(guiGraphics, a3, xx, yy, 0, 0, 9, 9, 9, 9,1, 1,1,a);
            }
            if (i == c) {
                new MGuiGraphics(()->distorted).blit( guiGraphics,a2, xx, yy, 0, 0, 9, 9, 9, 9,1, 1,1,a);
            }
            if (i == d) {
                new MGuiGraphics(()->distorted).blit( guiGraphics,a1, xx, yy, 0, 0, 9, 9, 9, 9,1, 1,1,a);
            }
        }
    }
    @Unique
    private float $showAlpha$Malstone = 255;
    @Unique
    private float $lightAmout$Malstone = 0;
    @Unique
    private  void $renderArmor$Malstone(GuiGraphics guiGraphics, Player player, int y, int heartRows, int height, int x) {
        float is = player.getData(AttReg.BloodShield_);
        int i = (int) is;
        if (i > 0) {
            int hurtTime = player.hurtTime;
            if (hurtTime > 0) {
                $showAlpha$Malstone = 255;
            }
            if (i>=player.getAttributeValue(AttReg.MaxBloodShield) - 1){
                if (hurtTime <= 0) {
                    if ($lightAmout$Malstone >=0.45f) {
                        if ($showAlpha$Malstone > 0) {
                            $showAlpha$Malstone -= 2.5f;
                        }
                    }else {
                        $lightAmout$Malstone += 0.0125f;
                    }
                }else {
                    $lightAmout$Malstone = 0;
                }
            }else {
                $lightAmout$Malstone = 0;
            }
            float alpha = $showAlpha$Malstone;
            float light = Math.min(0.45f,$lightAmout$Malstone);
            if ($showAlpha$Malstone < 0) {
                $showAlpha$Malstone = 0;
            }


            int yy = y - (heartRows - 1) * height - 10;

            ResourceLocation a1 = ResourceLocation.fromNamespaceAndPath(Malstone.MODID, "textures/gui/hyperplasia_1.png");
            ResourceLocation a2 = ResourceLocation.fromNamespaceAndPath(Malstone.MODID, "textures/gui/hyperplasia_2.png");
            ResourceLocation a3 = ResourceLocation.fromNamespaceAndPath(Malstone.MODID, "textures/gui/hyperplasia_3.png");
            ResourceLocation a4 = ResourceLocation.fromNamespaceAndPath(Malstone.MODID, "textures/gui/hyperplasia_4.png");


            if (i <= 40) {
                for (int offset = 0; offset < 10; offset++) {
                    $drawA1234$Malstone((int) i, x, guiGraphics, yy, a1, a2, a3, a4,

                            1 + offset * 4, 2 + offset * 4,
                            3 + offset * 4, 4 + offset * 4,

                            offset,alpha/255f,light);
                }
            }else {
                for (int j = 0; j < 10; j++) {
                    ShaderInstance distorted = MRender.getLiveShaderInstance();
                    new MGuiGraphics(()->distorted).blit(guiGraphics, a1,
                            (x) + j * 8- 1, yy,
                            0, 0, 9, 9, 9, 9,
                            1,1,1,alpha/255f);
                }
            }


        }
    }
}
