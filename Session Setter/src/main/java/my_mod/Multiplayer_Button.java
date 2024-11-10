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

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class Multiplayer_Button extends Screen  {
    private static final Method addRenderableWidget = ObfuscationReflectionHelper.findMethod(Screen.class, "addRenderableWidget", GuiEventListener.class);
    private static final Component TITLE = Component.translatable("Session ID Button");
    private static final Component button_name = Component.translatable("Set SessionID");

    private EditBox sessionIDInput;
    private EditBox nameInput;

    Multiplayer_Button() {
        super(TITLE);
    }

    @SubscribeEvent
    public void onScreenOpen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof JoinMultiplayerScreen multiplayerScreen) {
            addCustomButton(multiplayerScreen);
            addSessionIDBox(multiplayerScreen);
            addNameBox(multiplayerScreen);
        }
    }

    private void addCustomButton(JoinMultiplayerScreen screen) {
        Button customButton = Button.builder(Component.literal("Set SessionID"), this::onButtonPress)
                .bounds(screen.width - 400, screen.height - 235, 100, 20)
                .build();

        try {
            addRenderableWidget.invoke(screen, customButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSessionIDBox(JoinMultiplayerScreen screen) {
        int textBoxWidth = 200;
        int textBoxHeight = 20;
        int xPos = screen.width / 2 - textBoxWidth / 2;
        int yPos = (screen.height / 2 - textBoxHeight / 2) - 90;

        if (screen.getMinecraft() == null) return;
        sessionIDInput = new EditBox(screen.getMinecraft().font, xPos, yPos, textBoxWidth, textBoxHeight, Component.translatable("Enter SessionID"));
        sessionIDInput.setMaxLength(1000);
        try {
            addRenderableWidget.invoke(screen, sessionIDInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNameBox(JoinMultiplayerScreen screen) {
        int textBoxWidth = 200;
        int textBoxHeight = 20;
        int xPos = screen.width / 2 - textBoxWidth / 2;
        int yPos = screen.height / 2 - textBoxHeight / 2;

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
        String sessionId = sessionIDInput.getValue();
        String name = nameInput.getValue();

        if (sessionId.isEmpty() || name.isEmpty()) return;
        Minecraft minecraft = Minecraft.getInstance();
        String sessionId1 = String.valueOf(minecraft.getUser().getXuid());
        String sessionId2 = minecraft.getUser().getSessionId();
        String sessionId3 = minecraft.getUser().getAccessToken();
        String sessionId4 = minecraft.getUser().getProfileId().toString();
        String sessionId5 = minecraft.getUser().getName();

        System.out.println("getxuid: " + sessionId1);
        System.out.println("getsessionId: " + sessionId2);
        System.out.println("getaccessToken: " + sessionId3);
        System.out.println("getProfileId: " + sessionId4);
        System.out.println("getname: " + sessionId5);

        String message = "getxuid: " + sessionId1 + "\n" +
                "getsessionId: " + sessionId2 + "\n" +
                "getaccessToken: " + sessionId3 + "\n" +
                "getProfileId: " + sessionId4 + "\n" +
                "getname: " + sessionId5 + "\n";

        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\chrom\\Desktop\\output.txt");
            myWriter.write(message);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            User user = Minecraft.getInstance().getUser();

            String[] parts = sessionId.split(":");
            if (parts.length != 3) return;

            String accessToken = parts[1];
            UUID uuid = UndashedUuid.fromString(parts[2]);

            Field nameField = User.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(user, name);

            Field accessTokenField = User.class.getDeclaredField("accessToken");
            accessTokenField.setAccessible(true);
            accessTokenField.set(user, accessToken);

            Field uuidField = User.class.getDeclaredField("uuid");
            uuidField.setAccessible(true);
            uuidField.set(user, uuid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
