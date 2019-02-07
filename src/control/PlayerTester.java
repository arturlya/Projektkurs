package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.sound.Sound;
import model.*;
import model.Maps.Map1;
import model.Screens.IngameScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse PlayerTester
 * Besitzt eine Main-Methode
 * Beim Start der Main wird ein Spieler und 2 Player-Dummies erstellt
 * Wird verwendet um Spieler und Angriffe auszutesten, ohne auf einen Server connecten zu müssen
 */
public class PlayerTester{

    /**Main-Methode, die einen PlayerTester erstellt*/
    public static void main(String[] Args){
        new PlayerTester();
    }

    /**Das Bild vom Cursor*/
    private Image cursor;
    /**Der PhysicsController des Spiels*/
    private PhysicsController physicsController;
    /**Zahl, die dafür sorgt, dass Button-Inputs nur einmal pro Update-Aufruf ausgeführt werden*/
    private int i;

    /**
     * Konstruktor des PlayerTesters
     * Startet das Spiel, erstellt dafür alle nötigen Objekte wie z.B. Environment, PhysicsController, etc.
     */
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

        Game.loadEnvironment(new Environment("assets/maps/blank.tmx"));
        IngameScreen ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
        Game.getScreenManager().displayScreen(ingameScreen);
        Game.getEnvironment().add(new Map1(), RenderType.BACKGROUND);

        physicsController = new PhysicsController();
        new CollisionController(physicsController);
        new Timer();
        Game.getRenderEngine().setBaseRenderScale(1);


        Player player = new Gambler(100,100,true);
        player.setPlayerNumber(1);
        Player dummy = new Mage(500,500,true);
        dummy.setPlayerNumber(2);
        Player dummy2 = new Warrior(500,500,true);
        dummy.setPlayerNumber(3);
        Game.getEnvironment().add(player);
        Game.getEnvironment().add(player, RenderType.NORMAL);
        Game.getEnvironment().add(dummy);
        Game.getEnvironment().add(dummy,RenderType.NORMAL);
        Game.getEnvironment().add(dummy2);
        Game.getEnvironment().add(dummy2,RenderType.NORMAL);

        new InputProcessor(player);

        Game.start();
    }

    /**
     * Interne InputProcessor-Klasse
     * Implementiert IUpdatable
     * Steuert die Inputs des Spielers
     */
    private class InputProcessor implements IUpdateable {

        /**Spieler, der von den Inputs beeinflusst wird*/
        private Player player;

        /**
         * Konstruktor des InputProcessors
         * @param player Spieler der beeinflusst werden soll
         */
        public InputProcessor(Player player){
            this.player = player;
            Game.getLoop().attach(this);
        }

        /**
         * update-Methode aus dem IUpdatable-Interface
         */
        @Override
        public void update() {
            i = 1;
            processInputs();
        }

        /**
         * Methode, die die anderen Input-Mathoden aufruft
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
                    if(!player.isAttacking() || player.isInAir()) {
                        player.setDirectionLR(0);
                        if(!player.isInAir()) {
                            player.setLookingAt(0);
                        }
                        player.setDecelerating(false);
                    }
                    i = 0;
                }
            });
            Input.keyboard().onKeyReleased(StaticData.moveLeft, (key) -> {
                if(i == 1) {
                    player.setDecelerating(true);
                    i = 2;
                }
            });
            Input.keyboard().onKeyPressed(StaticData.moveRight, (key) -> {
                if(i == 1) {
                    if(!player.isAttacking() || player.isInAir()) {
                        player.setDirectionLR(1);
                        if(!player.isInAir()) {
                            player.setLookingAt(1);
                        }
                        player.setDecelerating(false);
                    }
                    i = 0;
                }
            });
            Input.keyboard().onKeyReleased(StaticData.moveRight, (key) -> {
                if(i == 1) {
                        player.setDecelerating(true);
                    i = 0;
                }
            });
            Input.keyboard().onKeyPressed(StaticData.lookUp, (key) ->{
                if(i == 1) {
                    player.setDirectionUD(0);
                }
                i = 0;
            });

            Input.keyboard().onKeyReleased(StaticData.lookUp, (key) ->{
                if(i == 1) {
                    player.setDirectionUD(-1);
                }
            });
            Input.keyboard().onKeyPressed(StaticData.lookDown, (key) -> player.setDirectionUD(1));
            Input.keyboard().onKeyReleased(StaticData.lookDown, (key) -> player.setDirectionUD(-1));
        }

        /**
         * Methode, die die Inputs für die Angriffe verarbeitet
         */
        private void processInputsAttacks(){
            if(!(player instanceof Warrior && (((Warrior) player).isGettingHooked()))) {
                Input.keyboard().onKeyTyped(StaticData.normalAttack, (key) -> {
                    if (player.getAttackWindDown() <= 0) {
                        player.setDecelerating(true);
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
                        player.setDecelerating(true);
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
                        player.setJumpCooldown(0.2);
                    }
                }
            });
        }
    }

}
