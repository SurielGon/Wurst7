/*
 * Copyright (C) 2014 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

@Mixin(Screen.class)
public abstract class ScreenMixin
{
	@Shadow
	protected MinecraftClient minecraft;
	
	@Inject(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V",
		ordinal = 0),
		method = {
			"sendMessage(Ljava/lang/String;Z)V"},
		cancellable = true)
	private void onSendChatMessage(String message, boolean toHud, CallbackInfo ci)
	{
		if(toHud)
			return;
		minecraft.getNetworkHandler().sendPacket(new ChatMessageC2SPacket(message));
		ci.cancel();
	}
}
