package com.pandora.api.mixin.mixins;

import com.google.common.base.Predicate;
import com.pandora.client.module.ModuleManager;
import com.pandora.client.module.modules.misc.NoEntityTrace;
import com.pandora.client.module.modules.render.NoRender;
import com.pandora.client.module.modules.render.RenderTweaks;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public class MixinEntityRenderer {
   @Redirect(
      method = {"orientCamera"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/multiplayer/WorldClient;rayTraceBlocks(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;"
)
   )
   public RayTraceResult rayTraceBlocks(WorldClient world, Vec3d start, Vec3d end) {
      return ModuleManager.isModuleEnabled("RenderTweaks") && ((RenderTweaks)ModuleManager.getModuleByName("RenderTweaks")).viewClip.getValue() ? null : world.func_72933_a(start, end);
   }

   @Redirect(
      method = {"getMouseOver"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"
)
   )
   public List<Entity> getEntitiesInAABBexcluding(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
      return (List)(((NoEntityTrace)ModuleManager.getModuleByName("NoEntityTrace")).noTrace() ? new ArrayList() : worldClient.func_175674_a(entityIn, boundingBox, predicate));
   }

   @Inject(
      method = {"hurtCameraEffect"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void hurtCameraEffect(float ticks, CallbackInfo info) {
      if (ModuleManager.isModuleEnabled("NoRender") && ((NoRender)ModuleManager.getModuleByName("NoRender")).hurtCam.getValue()) {
         info.cancel();
      }

   }
}
