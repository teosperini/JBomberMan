package org.jbomberman.model;

import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.jbomberman.utils.*;
import javafx.scene.input.KeyCode;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class MainModel extends Observable {

    private static final int POINTS_FOR_A_COIN = 400;

    private static final int POINTS_FOR_AN_ENEMY = 200;

    // how much the character can move every time a key is pressed
    private static final int MOVEMENT = 1;

    private static final ArrayList<KeyCode> KEY_CODES = new ArrayList<>(List.of(KeyCode.UP,KeyCode.DOWN,KeyCode.LEFT,KeyCode.RIGHT));

    private ArrayList<User> leaderboard;

    // LIMITS OF THE MAP
    private final int xMax;
    private final int yMax;

    private int numberOfRandomBlocks = 20;
    private int numberOfEnemies = 3;
    private int numberOfCoins = 4;

    // The coordinates of the ground
    private final ArrayList<Coordinate> coordinateGround = new ArrayList<>();

    // The coordinates of the fixed blocks (the blocks that can't be destroyed)
    private final ArrayList<Coordinate> coordinatesFixedBlocks = new ArrayList<>();

    // The coordinates of the random blocks (the blocks that can be destroyed)
    private final ArrayList<Coordinate> coordinatesRandomBlocks = new ArrayList<>();

    // The coordinates of the coins
    private final ArrayList<Coordinate> coins = new ArrayList<>();

    // The coordinates of the enemies
    private final ArrayList<Coordinate> coordinateEnemies = new ArrayList<>();

    // The HP of the enemies
    private final ArrayList<Integer> enemiesHp = new ArrayList<>();

    // The free positions on the map
    private List<Coordinate>  freePositions;

    // The coordinates of the exit door
    private Coordinate exitDoor;

    // The coordinates of the high potential bomb
    private Coordinate bombPu;

    // The coordinates of the extra life
    private Coordinate lifePu;

    // The coordinates of the 10 seconds invincibility
    private Coordinate invinciblePu;

    // The coordinates of the tnt
    private Coordinate tntCoordinates;

    // The number of lives of the player
    private int playerHealthPoint;

    // The currents amount of points of the player
    private int points;

    // The actual bomb range
    private int bombRange = 1;

    // The actual level
    private int level = 1;

    // The invincibility boolean
    private boolean playerInvincible = false;


    private Coordinate playerPosition = new Coordinate(1,1);



    private final Random random = new Random();

    private boolean doorOpen = false;

//############################# CONSTRUCTOR AND INITIALIZATION ############################//

    public MainModel(int dx, int dy) {
        xMax = dx-2;
        yMax = dy-2-1;
        playerHealthPoint = 3;
        points = 0;
        load();
    }

    public void initialize(){
        generateBlocks();
        generateItemsAndExitDoor();
        generateEnemies();

        playerPosition = new Coordinate(1,1);

        bombRange = 1;
    }

    // This method resets all the part of the code
    public void reset() {
        coordinateGround.clear();
        coordinatesFixedBlocks.clear();
        coordinatesRandomBlocks.clear();
        coins.clear();
        invinciblePu = null;
        lifePu = null;
        bombPu = null;
        exitDoor = null;
        doorOpen = false;

        coordinateEnemies.clear();
        enemiesHp.clear();

        playerPosition = new Coordinate(1,1);
        bombRange = 1;
        playerInvincible = false;

        tntCoordinates = null;

        deleteObservers();
    }

    public void resetGame(){
        playerHealthPoint = 3;
        points = 0;
    }


    public void generateBlocks() {
        generateBackground();
        generateRandomBlocks();
    }


    private void generateBackground() {
        // this is the green ground (1 .. X_MAX, 1 .. Y_MAX)
        for (int x = 1; x <= xMax; x += 1) {
            for (int y = 1; y <= yMax; y += 1) {
                coordinateGround.add(new Coordinate(x, y));
            }
        }

        // at the beginning the free positions are all the green ground positions, except the ones of the first corner
        freePositions = coordinateGround.stream().filter(p -> p.x() + p.y() > 3).collect(Collectors.toList());

        // this generates the fixed checkerboard blocks, removing the corresponding positions from the free positions list
        for (int x = 1 + 1; x <= xMax; x += 2) {
            for (int y = 1 + 1; y <= yMax; y += 2) {
                var fixedBlock = new Coordinate(x, y);
                coordinatesFixedBlocks.add(fixedBlock);
                freePositions.remove(fixedBlock);
            }
        }

        // this generates the fixed blocks on the edges
        for (int x = 0; x <= xMax + 1; x += 1) {
            for (int y = 0; y <= yMax + 1; y += 1) {
                //verifica se la coordinata è ai bordi
                if (x == 0 || x == xMax +1 || y == 0 || y == yMax +1) {
                    coordinatesFixedBlocks.add(new Coordinate(x, y));
                }
            }
        }
    }

    /**
     * Generate NUM_RND_BLOCKS random blocks in the range 1 .. XMAX-1, 1 .. YMAX-1;
     * the corresponding coordinates are taken from the free positions list and put in the coordinatesRandomBlocks list.
     */
    private void generateRandomBlocks() {
        for (int i = 0; i< numberOfRandomBlocks; i++) {
            Coordinate rndBlock = freePositions.remove(random.nextInt(freePositions.size()-1));
            coordinatesRandomBlocks.add(rndBlock);
        }
    }

    private void generateItemsAndExitDoor() {
        // The items are put behind the random blocks; That's why we can use this array to place all
        // the items correctly and also to prevent the stack of the items over the same position
        ArrayList<Coordinate> availableCoordinates = new ArrayList<>(coordinatesRandomBlocks);

        // Generate the exit door
        int randomExit = random.nextInt(coordinatesRandomBlocks.size());
        exitDoor = coordinatesRandomBlocks.get(randomExit);
        availableCoordinates.remove(exitDoor);

        //Generate the bigger bomb power up
        int randomFire = random.nextInt(availableCoordinates.size());
        bombPu = availableCoordinates.get(randomFire);
        availableCoordinates.remove(bombPu);

        int randomLife = random.nextInt(availableCoordinates.size());
        lifePu = availableCoordinates.get(randomLife);
        availableCoordinates.remove(lifePu);

        int randomInvincible = random.nextInt(availableCoordinates.size());
        invinciblePu = availableCoordinates.get(randomInvincible);
        availableCoordinates.remove(invinciblePu);

        // Generate the coordinates of the coins
        for (int i = 0; i < numberOfCoins; i++) {
            int randomCoin = random.nextInt(availableCoordinates.size());
            Coordinate coin = availableCoordinates.get(randomCoin);
            availableCoordinates.remove(coin);
            coins.add(coin);
        }
    }

    public void generateEnemies() {
        for (int i = 0; i< numberOfEnemies; i++) {
            Coordinate enemy = freePositions.remove(random.nextInt(freePositions.size()-1));
            coordinateEnemies.add(enemy);
            // the number of lives of an enemy depends upon the level
            enemiesHp.add(level);
        }
    }



//######################################  TNT  ######################################//

    boolean isBombExploding = false;

    public boolean releaseBomb() {
        if (tntCoordinates != null || Objects.equals(playerPosition, new Coordinate(1, 1))){
            return false;
        }

        tntCoordinates = playerPosition;

        notifyBombReleased(tntCoordinates);
        return true;
    }

    public void explodeBomb() {
        Set<Coordinate> blocksToRemove = new HashSet<>();
        Set<Coordinate> enemiesToRemove = new HashSet<>();
        Set<Coordinate> enemiesHpToRemove = new HashSet<>();

        //with getCoordinates() I get every coordinates that needs to be checked
        ArrayList<Triad> adjacentCoordinates = getCoordinates();
        adjacentCoordinates.add(new Triad(tntCoordinates, Direction.CENTER, true));

        for (Triad terna : adjacentCoordinates) {
            Coordinate coord = terna.getCoordinate();

            if (playerPosition.equals(coord)) {
                lessLife();
            }

            if (coordinatesRandomBlocks.contains(coord)) {
                blocksToRemove.add(coord);
            }

            if (coordinateEnemies.contains(coord)) {
                int enemyIndex = coordinateEnemies.indexOf(coord);

                    enemiesHp.set(enemyIndex, enemiesHp.get(enemyIndex)-1);
                    if (enemiesHp.get(enemyIndex) == 0) {
                        enemiesToRemove.add(coord);
                        enemiesHp.remove(enemyIndex);
                    } else
                        enemiesHpToRemove.add(coord);
            }
        }

        blocksToRemove.forEach(coordinate -> {
            int index = coordinatesRandomBlocks.indexOf(coordinate);
            coordinatesRandomBlocks.remove(coordinate);
            notifyBlockRemoved(index);
        });

        enemiesToRemove.forEach(coordinate -> {
            int index = coordinateEnemies.indexOf(coordinate);
            coordinateEnemies.remove(coordinate);
            notifyDeadEnemy(index);
            points += POINTS_FOR_AN_ENEMY;
            notifyPoints(POINTS_FOR_AN_ENEMY, coordinate);
        });

        enemiesHpToRemove.forEach(coordinate -> {
            int index = coordinateEnemies.indexOf(coordinate);
            notifyLessLifeEnemy(index);
        });

        notifyExplosion(adjacentCoordinates);
        tntCoordinates = null;
        isBombExploding = false;

        openTheDoor();
    }

    private void openTheDoor() {
        if (!doorOpen && coordinateEnemies.isEmpty()){
            notifyOpenedDoor();
            doorOpen = true;
        }
    }


    private ArrayList<Triad> getCoordinates() {
        ArrayList<Triad> adjacentCoordinate = new ArrayList<>();

        // Flags to stop the propagation of the bomb in that direction
        // I'm using these flags because you need to know if the propagation in that direction has encountered an
        // obstacle the cycle before
        boolean stopUp = false;
        boolean stopDown = false;
        boolean stopLeft = false;
        boolean stopRight = false;

        // Checking every direction, then, if the bomb range is >1, check the external blocks
        for (int distance = 1; distance <= bombRange; distance++) {
            Coordinate coordUp = new Coordinate(tntCoordinates.x(), tntCoordinates.y() - distance);
            Coordinate coordDown = new Coordinate(tntCoordinates.x(), tntCoordinates.y() + distance);
            Coordinate coordLeft = new Coordinate(tntCoordinates.x() - distance, tntCoordinates.y());
            Coordinate coordRight = new Coordinate(tntCoordinates.x() + distance, tntCoordinates.y());

            if (!stopUp && !coordinatesFixedBlocks.contains(coordUp)) {
                adjacentCoordinate.add(new Triad(coordUp, Direction.UP, distance == bombRange));
            } else {
                stopUp = true;
            }

            if (!stopDown && !coordinatesFixedBlocks.contains(coordDown)) {
                adjacentCoordinate.add(new Triad(coordDown, Direction.DOWN, distance == bombRange));
            } else {
                stopDown = true;
            }

            if (!stopLeft && !coordinatesFixedBlocks.contains(coordLeft)) {
                adjacentCoordinate.add(new Triad(coordLeft, Direction.LEFT, distance == bombRange));
            } else {
                stopLeft = true;
            }

            if (!stopRight && !coordinatesFixedBlocks.contains(coordRight)) {
                adjacentCoordinate.add(new Triad(coordRight, Direction.RIGHT, distance == bombRange));
            } else {
                stopRight = true;
            }
        }
        return adjacentCoordinate;
    }



//####################################  POWER UPS AND LIFE  ####################################//

    private void lessLife() {
        if (!playerInvincible && playerHealthPoint > 0) {
            playerHealthPoint -= 1;
            if (playerHealthPoint <= 0) {
                notifyDefeat();
            } else {
                playerPosition = new Coordinate(1, 1);
                notifyLessLife();
            }
        }
    }



//####################################  PLAYER MOVEMENT  ####################################//
    /**
     * Given the key code of the key pressed by the player, changes accordingly its position,
     * checking if it found the exit or if got a power up or a coin
     * @param keyCode is the code of the key pressed by the player
     */
    public void movePlayer(KeyCode keyCode) {
        if (KEY_CODES.contains(keyCode)) {
        Coordinate oldPosition = playerPosition;
            Coordinate newPosition = calculateNewPosition(keyCode, playerPosition);

            // If the player can change position, then let him change position
            if (!newPosition.equals(oldPosition) && !collision(newPosition)) {
                playerPosition = newPosition;
                notifyPlayerPosition(newPosition, oldPosition);

                // Check if he found the exit and if he can finish the level
                // Else check if he got a power up or took a coin
                if (newPosition.equals(exitDoor) && coordinateEnemies.isEmpty()) {
                    notifyVictory();
                } else if (newPosition.equals(bombPu)) {
                    notifyPUExplosion();
                } else if (newPosition.equals(lifePu)) {
                    notifyPULife();
                } else if (newPosition.equals(invinciblePu)) {
                    notifyPUInvincible();
                } else {
                    ArrayList<Coordinate> coinsToRemove = new ArrayList<>(coins);
                    coinsToRemove.forEach(coordinate -> {
                        if (newPosition.equals(coordinate)) {
                            // Notify the removal of the coin
                            notifyCoin(coins.indexOf(coordinate));
                            coins.remove(coordinate);
                            // Notify the addition of the points
                            points += POINTS_FOR_A_COIN;
                            notifyPoints(POINTS_FOR_A_COIN, coordinate);
                        }
                    });
                }
            }
        }
    }



//####################################   UTILITIES   ####################################//
    /**
     * Starting from the current position, for the given keyCode calculates the new position on the game board
     * (without taking into account any collision).
     * @param keyCode is the code of the key pressed by the player
     * @return returns the new position of the player
     */
    private Coordinate calculateNewPosition(KeyCode keyCode, Coordinate currentPosition) {
        int deltaX = 0;
        int deltaY = 0;

        switch (keyCode) {
            case UP -> deltaY = -MOVEMENT;
            case DOWN -> deltaY = MOVEMENT;
            case LEFT -> deltaX = -MOVEMENT;
            case RIGHT -> deltaX = MOVEMENT;
            default -> {
            }
        }

        int newX = clamp(currentPosition.x() + deltaX, 1, xMax);
        int newY = clamp(currentPosition.y() + deltaY, 1, yMax);

        return new Coordinate(newX, newY);
    }

    /**
     * if value<0 it returns 0;
     * if value>max it returns max;
     * otherwise returns value.
     */
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    /**
     * Checks if the given coordinate collides with an existing fixed block or random block.
     * @param coordinate the coordinate to check
     * @return true if the given coordinate collides with an existing fixed block or random block.
     */
    private boolean collision(Coordinate coordinate) {
        return (coordinatesFixedBlocks.contains(coordinate) || coordinatesRandomBlocks.contains(coordinate) || coordinate.equals(tntCoordinates));
    }
    private void controlPosition() {
        if(coordinateEnemies.contains(playerPosition)){
            lessLife();
        }
    }


//####################################  NOTIFICATIONS  ####################################//
    public void notifyModelReady() {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_POINTS).setPoints(points).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_LIFE).setHealthPoint(playerHealthPoint).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LEVEL).setLevel(level).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_MAP).setSubBlock(SubMap.GROUND_BLOCKS).setBlocks(coordinateGround).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_MAP).setSubBlock(SubMap.STATIC_BLOCKS).setBlocks(coordinatesFixedBlocks).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_POWER_UP_BOMB).setCoordinate(bombPu).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_POWER_UP_LIFE).setCoordinate(lifePu).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_POWER_UP_INVINCIBLE).setCoordinate(invinciblePu).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_EXIT).setCoordinate(exitDoor).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_COINS).setEntities(coins).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_MAP).setSubBlock(SubMap.RANDOM_BLOCKS).setBlocks(coordinatesRandomBlocks).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_PLAYER).setCoordinate(playerPosition).build());
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.LOAD_ENEMIES).setEntities(coordinateEnemies).build());
    }

    /**
     * Notify that the bomb has been released
     * @param tnt The coordinates where the tnt needs to be drawn
     */
    private void notifyBombReleased(Coordinate tnt){
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_BOMB_RELEASED).setCoordinate(tnt).build());
    }

    /**
     * Notify that a block has been removed
     * @param blockToRemove The index of the block to be removed
     */
    private void notifyBlockRemoved(int blockToRemove) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_BLOCK_DESTROYED).setIndex(blockToRemove).build());
    }


    /**
     * Notify that a coin has been taken
     * @param coinToRemove The index of the coin to be removed
     */
    private void notifyCoin(int coinToRemove) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_COINS).setIndex(coinToRemove).build());
    }

    /**
     * Notify that points have been earned
     * @param earnedPoints The amount of points that have been added
     * @param pointsCoordinates The coordinates of where the points have been earned
     */
    private void notifyPoints(int earnedPoints, Coordinate pointsCoordinates) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_POINTS).setCoordinate(pointsCoordinates).setEarnedPoints(earnedPoints).setPoints(points).build());
    }

    /**
     * Notify that an enemy has lost a life
     * @param enemyWithLessLife The index of the enemy that has lost a life
     */
    private void notifyLessLifeEnemy(int enemyWithLessLife) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_ENEMY_LIFE).setIndex(enemyWithLessLife).build());
    }

    /**
     * Notify that the door is now open
     */
    private void notifyOpenedDoor() {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_DOOR).build());
    }

    /**
     * Notify that an enemy has been killed
     * @param deadEnemy the index of the enemy that is now dead
     */
    private void notifyDeadEnemy(int deadEnemy) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_ENEMY_DEAD).setIndex(deadEnemy).build());
    }

    /**
     * Notify that the player has changed position
     * @param newPosition The new player position
     * @param oldPosition The old player position
     */
    public void notifyPlayerPosition(Coordinate newPosition, Coordinate oldPosition) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_POSITION).setNewPosition(newPosition).setOldPosition(oldPosition).setIndex(-1).build());
        controlPosition();
    }

    private void notifyLessLife() {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_RESPAWN).setHealthPoint(playerHealthPoint).build());
    }

    private void notifyExplosion(ArrayList<Triad> triadArrayList) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_EXPLOSION).setTriadList(triadArrayList).build());
    }

    public void notifyPUExplosion(){
        bombRange += 1;
        bombPu = null;
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_PU_BOMB).build());
    }

    public void notifyPULife(){
        playerHealthPoint += 1;
        lifePu = null;
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_PU_LIFE).setHealthPoint(playerHealthPoint).build());
    }

    public void notifyPUInvincible(){
        invinciblePu = null;
        playerInvincible = true;
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_PU_INVINCIBLE).setPlayerInvincible(true).build());
        PauseTransition pauseInvincible = new PauseTransition(Duration.seconds(10));
        pauseInvincible.setOnFinished(actionEvent -> {
            playerInvincible = false;
            setChanged();
            notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_PU_INVINCIBLE).setPlayerInvincible(false).build());
        });
        pauseInvincible.play();
    }

    private void notifyVictory() {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_GAME_WIN).build());
    }

    private void notifyDefeat() {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_GAME_OVER).build());
    }

    private void notifyEnemyMovement(Coordinate oldPosition, Coordinate newPosition, int enemyId, KeyCode keyCode) {
        setChanged();
        notifyObservers(new UpdateInfo.Builder(UpdateType.UPDATE_POSITION).setOldPosition(oldPosition).setNewPosition(newPosition).setIndex(enemyId).setKeyCode(keyCode).setEnemyLastLife(enemiesHp.get(enemyId) == 1).build());
        controlPosition();
    }




//########################################  ENEMIES  ########################################//
    public void moveEnemies() {
        for (int i = 0; i< coordinateEnemies.size(); i++) {
            calculateNewEnemyPosition(i);
        }
    }

    public void calculateNewEnemyPosition(int enemyId) {
        Coordinate oldEnemyPosition = coordinateEnemies.get(enemyId);
        int randomInt = random.nextInt(KEY_CODES.size());
        Coordinate newEnemyPosition;
        int i = 0;
        KeyCode key;
        do{
            key = KEY_CODES.get((randomInt + i)%4);
            newEnemyPosition = calculateNewPosition(key, oldEnemyPosition);
            i++;

        } while((collision(newEnemyPosition) || coordinateEnemies.contains(newEnemyPosition)|| !isSafeZone(newEnemyPosition)) && i < 4);

        if (i != 4){
            coordinateEnemies.set(enemyId, newEnemyPosition);
            notifyEnemyMovement(oldEnemyPosition, newEnemyPosition, enemyId, key);
        }
    }

    private boolean isSafeZone(Coordinate newEnemyPosition) {
        return (newEnemyPosition.x()+newEnemyPosition.y() >3);
    }


    //##################### TEST ####################//
    //TODO remove after test
    public void removeRandom() {
        ArrayList<Coordinate> array = new ArrayList<>(coordinatesRandomBlocks);
        array.forEach(coordinate -> {
                    int index = coordinatesRandomBlocks.indexOf(coordinate);
                    coordinatesRandomBlocks.remove(coordinate);
                    notifyBlockRemoved(index);
                }
        );
    }

    public List<User> getLeaderboard(){
        return leaderboard;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public void setPlayer(String name) {
        //using the returning Optional value of the stream to check if the player is already present in the leaderboard
        Optional<User> existingUser = leaderboard.stream()
                .filter(user -> user.name().equals(name))
                .findAny();

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            //checking if
            if (points > user.score() || level > user.level()) {
                user = new User(name, points, level);
                leaderboard.remove(existingUser.get());
                leaderboard.add(user);
            }
        } else {
            User user = new User(name, points, level);
            leaderboard.add(user);
        }
    }

    private void load(){
        String filePath = getFilePath();

        try {
            FileReader fileReader = new FileReader(filePath);

            Gson gson = new Gson();

            //using type class to match the type of what is written in the gson file (an ArrayList of User)
            Type type = new TypeToken<ArrayList<User>>(){}.getType();
            leaderboard = gson.fromJson(fileReader, type);

            fileReader.close();

        } catch (IOException e) {
            System.out.println("Si è verificato un errore durante la lettura del file: " + e.getMessage());
            e.printStackTrace();
            leaderboard = new ArrayList<>();
        }
    }

    public void save(){
        Gson gson = new Gson();
        String jsonString = gson.toJson(leaderboard);

        System.out.println(jsonString);

        String filePath = getFilePath();

        try {
            FileWriter fileWriter = new FileWriter(filePath);

            fileWriter.write(jsonString);

            fileWriter.close();

            System.out.println("Stringa JSON salvata con successo su " + filePath);
        } catch (IOException e) {
            System.out.println("Si è verificato un errore durante il salvataggio del file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getFilePath() {
        String homeDir = System.getProperty("user.home");
        String separator = File.separator;
        return homeDir + separator+ "leaderboard.json";
    }
    //###############################################//
}