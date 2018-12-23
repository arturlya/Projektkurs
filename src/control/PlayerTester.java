package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.sound.Sound;
import model.*;
import model.Maps.Map1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerTester{

    public static void main(String[] Args){
        new PlayerTester();
    }

    private ScreenController screenController;
    private Image cursor;
    private PhysicsController physicsController;
    private int i;
    int j;

    public PlayerTester(){
        Game.init();
        Game.getConfiguration().graphics().setFullscreen(false);
        new Config();
        try {
            cursor = ImageIO.read(new File("assets/img/cursor.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        Game.getScreenManager().getRenderComponent().setCursor(cursor,0,0);
        screenController = new ScreenController();
        screenController.setIngameScreen(new Map1());

        physicsController = new PhysicsController();
        new CollisionController(physicsController);
        Game.getEnvironment().add(new Timer());
        Game.getRenderEngine().setBaseRenderScale(1);


        Player player = new Mage(100,100,true);
        Player dummy = new Mage(500,500,false);
        Game.getEnvironment().add(player);
        Game.getEnvironment().add(player, RenderType.NORMAL);
        Game.getEnvironment().add(dummy);
        Game.getEnvironment().add(dummy,RenderType.NORMAL);

        new InputProcessor(player);


        Game.start();
    }

    private class InputProcessor implements IUpdateable {

        private Player player;

        public InputProcessor(Player player){
            this.player = player;
            Game.getLoop().attach(this);
        }

        @Override
        public void update() {
            i = 1;
            processInputs();
        }

        /**
         * Methode, die, falls der Player spielbar ist, die anderen Input-Mathoden aufruft
         */
        private void processInputs(){
            processInputsDirections();
            processInputsAttacks();
            processInputsJump();
        }

        /**
         * Methode, die die richtungsbeeinflussenden Inputs verarbeitet
         */
        private void processInputsDirections(){
            Input.keyboard().onKeyPressed(StaticData.moveLeft, (key) -> {
                if(i == 1) {
                    if (!(player.getAttackWindDown() > 0 && player.isInAir())) {
                        player.setDirectionLR(0);
                        player.setLookingAt(0);
                        player.setMoving(true);
                    }
                    i = 0;
                }
            });
            Input.keyboard().onKeyReleased(StaticData.moveLeft, (key) -> {
                if(i == 1) {
                    if (!(player.getAttackWindDown() > 0 && player.isInAir())) {
                        player.setMoving(false);
                        player.setDirectionLR(-1);
                    }
                    i = 2;
                }
            });
            Input.keyboard().onKeyPressed(StaticData.moveRight, (key) -> {
                if(i == 1) {
                    if (!(player.getAttackWindDown() > 0 && player.isInAir())) {
                        player.setDirectionLR(1);
                        player.setLookingAt(1);
                        player.setMoving(true);
                    }
                    i = 0;
                }
            });
            Input.keyboard().onKeyReleased(StaticData.moveRight, (key) -> {
                if(i == 1) {
                    if (!(player.getAttackWindDown() > 0 && player.isInAir())) {
                        player.setMoving(false);
                        player.setDirectionLR(-1);
                    }
                    i = 0;
                }
            });
            Input.keyboard().onKeyPressed(StaticData.moveUp, (key) ->{
                if(i == 1) {
                    player.setDirectionUD(0);
                }
                i = 0;
            });

            Input.keyboard().onKeyReleased(StaticData.moveUp, (key) ->{
                if(i == 1) {
                    player.setDirectionUD(-1);
                }
            });
            Input.keyboard().onKeyPressed(StaticData.moveDown, (key) -> player.setDirectionUD(1));
            Input.keyboard().onKeyReleased(StaticData.moveDown, (key) -> player.setDirectionUD(-1));
        }

        /**
         * Methode, die die Inputs für die Angriffe verarbeitet
         */
        private void processInputsAttacks(){
            Input.keyboard().onKeyTyped(StaticData.normalAttack, (key) -> {
                if (player.getAttackWindDown() <= 0) {
                    player.setHorizontalSpeed(0);
                    if (player.getDirectionLR() != -1) {
                        player.normalAttackRun();
                    } else if (player.getDirectionUD() == 1) {
                        player.normalAttackDown();
                    } else if (player.getDirectionUD() == 0) {
                        player.normalAttackUp();
                    } else {
                        player.normalAttackStand();
                    }
                }
            });
            Input.keyboard().onKeyTyped(StaticData.specialAttack, (key) -> {
                if (player.getAttackWindDown() <= 0) {
                    player.setHorizontalSpeed(0);
                    if (player.getDirectionUD() == 0) {
                        player.specialAttackUp();
                    } else if (player.getDirectionLR() != -1) {
                        player.specialAttackRun();
                    } else if (player.getDirectionUD() == 1) {
                        player.specialAttackDown();
                    } else {
                        player.specialAttackStand();
                    }
                }
            });
        }

        /**
         * Methode, die den Input für den Jump verarbeitet
         */
        private void processInputsJump(){
            Input.keyboard().onKeyTyped(StaticData.jump, (key) -> {
                if(player.getAttackWindDown() <= 0){
                    if(player.getJumpsAvailable() > 0 && player.getJumpCooldown() <= 0){
                        player.setVerticalSpeed(-700);
                        player.setInAir(true);
                        player.setJumpsAvailable(player.getJumpsAvailable()-1);
                        player.setJumpCooldown(0.5);
                    }
                }
            });
        }
    }

}
