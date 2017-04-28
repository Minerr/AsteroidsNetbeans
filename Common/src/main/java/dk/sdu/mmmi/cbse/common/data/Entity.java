package dk.sdu.mmmi.cbse.common.data;

import java.io.Serializable;
import java.util.UUID;

public abstract class Entity implements Serializable {

    private final UUID ID = UUID.randomUUID();

    private float width;
    private float height;
    private float x;
    private float y;
    private float dx;
    private float dy;
    private float radians;
    private float radius;
    private float maxSpeed;
    private float acceleration;
    private float deacceleration;

    private float[] shapeX;
    private float[] shapeY;
    private float[] shapeDistances;
    private int shapeNumPoints;

    private int rotationSpeed;
    private int life;
    private int score;
    private boolean isHit = false;
    private boolean isDestroyed = false;
    private float expiration;

    public float[] getShapeDistances() {
        return shapeDistances;
    }

    public void setShapeDistances(float[] shapeDistances) {
        this.shapeDistances = shapeDistances;
    }

    public int getShapeNumPoints() {
        return shapeNumPoints;
    }

    public void setShapeNumPoints(int shapeNumPoints) {
        this.shapeNumPoints = shapeNumPoints;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void reduceExpiration(float delta) {
        this.expiration -= delta;
    }

    public float getExpiration() {
        return expiration;
    }

    public void setExpiration(float value) {
        this.expiration = value;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setIsDestroyed(boolean isDestroyed) {
        this.isDestroyed = isDestroyed;
    }

    public boolean getIsHit() {
        return isHit;
    }

    public void setIsHit(boolean hit) {
        this.isHit = hit;
    }

    public void setRadius(float r) {
        this.radius = r;
    }

    public float getRadius() {
        return radius;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void increaseLife(int amount) {
        this.life += amount;
    }

    public void decreaseLife(int amount) {
        this.life -= amount;
    }

    public String getID() {
        return ID.toString();
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float w) {
        this.width = w;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float h) {
        this.height = h;
    }

    public void setDimension(float w, float h) {
        this.width = w;
        this.height = h;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setDirection(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public float getRadians() {
        return radians;
    }

    public void setRadians(float radians) {
        this.radians = radians;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getDeacceleration() {
        return deacceleration;
    }

    public void setDeacceleration(float deacceleration) {
        this.deacceleration = deacceleration;
    }

    public float[] getShapeX() {
        return shapeX;
    }

    public void setShapeX(float[] shapeX) {
        this.shapeX = shapeX;
    }

    public float[] getShapeY() {
        return shapeY;
    }

    public void setShapeY(float[] shapeY) {
        this.shapeY = shapeY;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void wrap(GameData gameData) {
        if (x < 0) {
            x = gameData.getDisplayWidth();
        }
        if (x > gameData.getDisplayWidth()) {
            x = 0;
        }
        if (y < 0) {
            y = gameData.getDisplayHeight();
        }
        if (y > gameData.getDisplayHeight()) {
            y = 0;
        }
    }
    
    public abstract void collision(Entity collider);
}
