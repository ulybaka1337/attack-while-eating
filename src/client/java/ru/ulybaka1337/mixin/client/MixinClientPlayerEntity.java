package ru.ulybaka1337.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

@Mixin(value = ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity {

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(at = @At("HEAD"), method = "tick")
    public void tickHook(CallbackInfo info) {
        if (mc.crosshairTarget instanceof BlockHitResult crossHair && crossHair.getBlockPos() != null && mc.options.attackKey.isPressed() && !mc.world.getBlockState(crossHair.getBlockPos()).isAir()) {
            mc.interactionManager.attackBlock(crossHair.getBlockPos(), crossHair.getSide());
            mc.player.swingHand(Hand.MAIN_HAND);
        }

        if (mc.crosshairTarget instanceof EntityHitResult ehr && ehr.getEntity() != null && mc.options.attackKey.isPressed() && mc.player.getAttackCooldownProgress(0.5f) > 0.9f) {
            mc.interactionManager.attackEntity(mc.player, ehr.getEntity());
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
