package my_mod;

import com.mojang.util.UndashedUuid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import net.minecraft.client.User;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class Multiplayer_Button {
    private static final Method addRenderableWidget = ObfuscationReflectionHelper.findMethod(Screen.class, "addRenderableWidget", GuiEventListener.class);

    private EditBox sessionIDInput;
    private EditBox nameInput;

    @SubscribeEvent
    public void onScreenOpen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof JoinMultiplayerScreen multiplayerScreen) {
            addCustomButton(multiplayerScreen);
            addTextBoxes(multiplayerScreen);
        }
    }

    private void addCustomButton(JoinMultiplayerScreen screen) {
        Button customButton = Button.builder(Component.literal("Set SessionID"), this::onButtonPress)
                .bounds(2, 2, 100, 20)
                .build();

        try {
            addRenderableWidget.invoke(screen, customButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTextBoxes(JoinMultiplayerScreen screen) {
        int textBoxWidth = 200;
        int textBoxHeight = 15;
        int xPos = screen.width / 2 - textBoxWidth / 2;
        int yPos = 2;

        if (screen.getMinecraft() == null) return;
        sessionIDInput = new EditBox(screen.getMinecraft().font, xPos, yPos, textBoxWidth, textBoxHeight, Component.translatable("Enter SessionID"));
        sessionIDInput.setMaxLength(1000);
        try {
            addRenderableWidget.invoke(screen, sessionIDInput);
        } catch (Exception e) {
            e.printStackTrace();
        }

        textBoxWidth = 100;
        xPos = screen.width - textBoxWidth - 10;

        if (screen.getMinecraft() == null) return;
        nameInput = new EditBox(screen.getMinecraft().font, xPos, yPos, textBoxWidth, textBoxHeight, Component.translatable("Enter Name"));
        nameInput.setMaxLength(1000);
        try {
            addRenderableWidget.invoke(screen, nameInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onButtonPress(Button button) {
        // get sessionId and name from text box
        String sessionId = sessionIDInput.getValue();
        String name = nameInput.getValue();

        // return if the text box is empty
        if (sessionId.isEmpty() || name.isEmpty()) return;

        try {
            // get current user
            User user = Minecraft.getInstance().getUser();

            // split session id into accessToken and uuid
            String[] parts = sessionId.split(":");

            // return if invalid session id
            if (parts.length != 3) return;

            // set variables
            String accessToken = parts[1];
            UUID uuid = UndashedUuid.fromString(parts[2]);

            // make name field accessible and set name
            Field nameField = User.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(user, name);

            // make name field accessible and set accessToken
            Field accessTokenField = User.class.getDeclaredField("accessToken");
            accessTokenField.setAccessible(true);
            accessTokenField.set(user, accessToken);

            // make name field accessible and set uuid
            Field uuidField = User.class.getDeclaredField("uuid");
            uuidField.setAccessible(true);
            uuidField.set(user, uuid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
