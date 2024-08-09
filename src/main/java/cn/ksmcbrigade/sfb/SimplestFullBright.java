package cn.ksmcbrigade.sfb;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionInstance.class)
public class SimplestFullBright
{

    @Shadow @Final public Component caption;

    @Unique
    public boolean sfb$enabled = false;

    @Unique
    public int sfb$timer = 0;

    @Inject(method = "get",at = @At("RETURN"),cancellable = true)
    public void get(CallbackInfoReturnable<Double> cir){
        Minecraft MC = Minecraft.getInstance();
        if(!this.caption.equals(MC.options.gamma().caption)) return;
        if(MC.getWindow()==null) return;
        if(MC.player!=null && sfb$timer==0 && InputConstants.isKeyDown(MC.getWindow().getWindow(), GLFW.GLFW_KEY_C)){
            sfb$timer = MC.getFps()*6;
            sfb$enabled = !sfb$enabled;
            MC.player.sendSystemMessage(Component.literal("FullBright: "+sfb$enabled));
        }
        else if(sfb$timer>0){
            sfb$timer--;
        }
        else{
            sfb$timer = MC.getFps()*6;
        }
        if(sfb$enabled){
            cir.setReturnValue(3000.0D);
        }
    }

}
