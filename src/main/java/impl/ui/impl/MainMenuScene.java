package impl.ui.impl;

import impl.struct.Room;
import impl.ui.Scene;
import impl.util.animation.Animation;
import impl.util.animation.Easing;
import impl.util.font.Fonts;
import impl.util.image.ImageObject;
import impl.util.interfaces.Consts;
import impl.util.time.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainMenuScene extends Scene implements Consts {
    private final ImageObject backgroundImage = new ImageObject("mainmenu/background.JPG");
    private final Animation animation = new Animation(Easing.EASE_OUT_BACK, 750);
    private final TimeUtil updateTimer = new TimeUtil();
    private List<Room> rooms = new ArrayList<>();

    @Override
    public void init() {
        backgroundImage.load();
        updateTimer.reset();
        updateRoomList();
    }


    @Override
    public void render() {
        animation.loop(0.0, 1.0);
        backgroundImage.drawImg(0,0, DISPLAY.getWidth(), DISPLAY.getHeight());

        float offset = 0;
        for(Room room : rooms) {
            final String roomName = room.getRoomName();
            Fonts.genshin.drawString(roomName.isBlank() ? "???" : roomName, 10,10 + offset, 25,-1);
            offset += 20;
        }

        if (updateTimer.finished(25000)){
            updateRoomList();
        }
    }

    private void updateRoomList() {
        System.out.println("Updating rooms...");
        updateTimer.reset();
        CLIENT.getRoomList().thenAccept(roomList -> {
            rooms = roomList;
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return "Texture Scene";
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void onMouseReleased() {

    }

    @Override
    public void onKeyPressed(int keyCode, char c) {

    }
}