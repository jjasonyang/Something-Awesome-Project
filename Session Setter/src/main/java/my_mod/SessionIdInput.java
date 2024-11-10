package my_mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class SessionIdInput extends Screen  {
    private static final Method ADD_RENDERABLE_WIDGET = ObfuscationReflectionHelper.findMethod(Screen.class, "addRenderableWidget", GuiEventListener.class);
    private static final Component TITLE = Component.translatable("Session ID Input");


    private EditBox textInput;


    public SessionIdInput() {
        super(TITLE);
    }

    @SubscribeEvent
    public void onScreenOpen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof JoinMultiplayerScreen multiplayerScreen) {
            addCustomEditBox(multiplayerScreen);
        }
    }

    private void addCustomEditBox(JoinMultiplayerScreen screen) {
        int textBoxWidth = 200;
        int textBoxHeight = 20;
        int xPos = screen.width / 2 - textBoxWidth / 2;
        int yPos = screen.height / 2 - textBoxHeight / 2;

        if (screen.getMinecraft() == null) return;
        textInput = new EditBox(screen.getMinecraft().font, xPos, yPos, textBoxWidth, textBoxHeight, Component.translatable("Enter SessionID"));


        try {
            ADD_RENDERABLE_WIDGET.invoke(screen, textInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
