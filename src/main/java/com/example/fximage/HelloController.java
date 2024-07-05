package com.example.fximage;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public class HelloController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView imageView;
    private WritableImage writableImage;
    private PixelBuffer<ByteBuffer> pixelBuffer;
    private ByteBuffer byteBuffer;



    private AnimationTimer animationTimer;
    private Consumer<Double> updateEvent;
    private int frame = 0;

    @FXML
    private void initialize(){
        // setup animationtimer
        animationTimer = new AnimationTimer() {
            long last = 0;
            @Override
            public void handle(long now) {
                double deltaTime = (now - last) / 1_000_000.0;
                updateEvent.accept(deltaTime);
                last = now;
            }
        };
        updateEvent = this::update;

        imageView.fitWidthProperty().bind(anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(anchorPane.heightProperty());


        // makes no difference if I use allocate or allocateDirect
        // for this example.
        byteBuffer = ByteBuffer.allocateDirect(1000 * 1000 * 4);
        for (int k = 0; k < byteBuffer.capacity(); k += 4) {
            byteBuffer.put(k, (byte) 255);
            byteBuffer.put(k + 1, (byte) (0));
            byteBuffer.put(k + 2, (byte) 127);
            byteBuffer.put(k + 3, (byte) 255);
        }


        pixelBuffer = new PixelBuffer<ByteBuffer>(1000, 1000, byteBuffer, PixelFormat.getByteBgraPreInstance());
        writableImage = new WritableImage(pixelBuffer);
        imageView.setImage(writableImage);

        animationTimer.start();
    }

    private void update(double dt){
        // comment this line out -> dt is around 16.6ms
        pixelBuffer.updateBuffer(pb -> null);

        // print frame number and delta time
        if(dt > 20.0)
            System.out.println(frame + " " + dt);

        ++frame;
    }
}