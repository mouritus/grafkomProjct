//c14210182 Mouritus Huangtara M

import Engine.Object;
import Engine.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class mainProject {
    Camera camera = new Camera();
    private Window window = new Window(800, 800, "Hello World");
    Projection projection = new Projection(window.getWidth(), window.getHeight());

    private ArrayList<Object> objects = new ArrayList<>();

    private ArrayList<Object> Environment = new ArrayList<>();

    private boolean dragDrop = false;
    private float posX = 0.0f;
    private float posY = 0.025f;
    private float posZ = 3f;
    private boolean state = false;
    private float rotate = 0f;


    private ArrayList<Double> valueArray = new ArrayList<>();
    private ArrayList<Boolean> stateArray = new ArrayList<>();



    public static void main(String[] args) {
        new mainProject().run();
    }

    public Vector2f convertRange(Vector2f pos, float height, float width) {
        float OldMax = 255, OldMin = 0, NewMax = 1, NewMin = 0, OldValue = 208, NewRange, NewValue, OldRange;
        OldRange = (OldMax - OldMin);
        NewRange = (NewMax - NewMin);
        NewValue = (((OldValue - OldMin) * NewRange) / OldRange) + NewMin;
        System.out.println(NewValue);
        return new Vector2f((((pos.x - 0) * (1 - (-1))) / (width - 0) + (-1)), (((pos.y - 0) * (1 - (-1))) / (height - 0) + (-1)) * -1);
    }

    public void init() {
        window.init();
        GL.createCapabilities();

        camera.setPosition(posX, posY, posZ);
//        camera.setRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(0.0f));

        //Wall
        Environment.add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 1f, 1f, 0f), "resources/Blender/Project/BackWall.obj"
        ));
        //Floor
        Environment.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 1f, 1f, 0f), "resources/Blender/Project/FLoor.obj"
        ));

        //Extra Room
        Environment.get(0).getChildObject().add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(1f, 1f, 1f, 0f), "resources/Blender/Project/trap.obj"
        ));

        Environment.get(0).translateObject(0f,-1f,0f);


        objects.add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.9f, 1f, 0.3f, 0f), "resources/Blender/Project/player.obj"
        ));
        objects.get(0).translateObject(0f,-3f,-7f);
        objects.get(0).rotateObject((float)Math.toRadians(180),0f,1f,0f);
        objects.get(0).scaleObject(0.3f,0.3f,0.3f);

        objects.add(new Model(
                Arrays.asList(
                        //shaderFile lokasi menyesuaikan objectnya
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.vert"
                                        , GL_VERTEX_SHADER),
                        new ShaderProgram.ShaderModuleData
                                ("resources/shaders/scene.frag"
                                        , GL_FRAGMENT_SHADER)
                ),
                new ArrayList<>(),
                new Vector4f(0.9f, 1f, 0.3f, 0f), "resources/Blender/Project/SWAT.obj"
        ));

//        System.out.println(Environment.get(0).getVertices());
    }

    public boolean checkCollide(float x, float y, float z){
        if (getDist(x, y, z, Environment.get(0).updateCenterPointObject().get(0), Environment.get(0).updateCenterPointObject().get(1), Environment.get(0).updateCenterPointObject().get(2)) >= 10){
            return true;
        }
        return false;
    }


    public void move(){
        float move = 0.025f;
        float x = objects.get(0).updateCenterPointObject().get(0);
        float y = objects.get(0).updateCenterPointObject().get(1);
        float z = objects.get(0).updateCenterPointObject().get(2);
        //Maju
        if (window.isKeyPressed(GLFW_KEY_W)) {
            if (!checkCollide(x, y, z - 0.1f)) {
                camera.moveForward(move);
                objects.get(0).translateObject(0f, 0f, -move);
            }
        }
        //Kiri
        if (window.isKeyPressed(GLFW_KEY_A)) {
            if (!checkCollide(x-0.1f, y, z)) {
                camera.moveLeft(move);
                objects.get(0).translateObject(-move, 0f, 0f);
            }
        }
        //Mundur
        if (window.isKeyPressed(GLFW_KEY_S)) {
            if (!checkCollide(x, y, z + 0.1f)) {
                camera.moveBackwards(move);
                objects.get(0).translateObject(0f, 0f, move);
            }
        }
        //Kanan
        if (window.isKeyPressed(GLFW_KEY_D)) {
            if (!checkCollide(x+0.1f, y, z)) {
                camera.moveRight(move);
                objects.get(0).translateObject(move, 0f, 0f);
            }
        }


        if (window.getMousInput().isLeftButtonPressed()) {
            Vector2f displayVector = window.getMousInput().getDisplVec();
            System.out.println(displayVector);
//            camera.addRotation((float) Math.toRadians(displayVector.x * 0.1), (float) Math.toRadians(displayVector.y * 0.1));
            camera.addRotation(0, (float) Math.toRadians(displayVector.y * 0.1));
//            objects.get(0).rotateObjectAnimate();
        }
        System.out.println(getDist(x, y, z, Environment.get(0).updateCenterPointObject().get(0), Environment.get(0).updateCenterPointObject().get(1), Environment.get(0).updateCenterPointObject().get(2)));

    }
    public void input(){
        move();

        if (window.getMousInput().getScroll().y != 0) {
            projection.setFOV(projection.getFOV() - window.getMousInput().getScroll().y * 0.1f);
            window.getMousInput().setScroll(new Vector2f(0f,0f));
        }
    }

    public float getDist(float x1, float y1, float z1, float x2, float y2, float z2) {
        return (float) Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y2 - y1), 2) + Math.pow(Math.abs(z2 - z1), 2));
    }

    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(1f, 0.3f, 0.0f, 0.0f);
            GL.createCapabilities();
            glEnable(GL_DEPTH_TEST);

//            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
//            glDepthMask(true);
//            glDepthFunc(GL_LEQUAL);
//            glDepthRange(0.0f, 1.0f);
            glDisableVertexAttribArray(0);
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for (Object object : Environment) {
                object.draw(camera, projection);
            }
            for (Object object : objects) {
                object.draw(camera, projection);
            }

//            for (Object object : objectsRectangle) {
//                object.draw(camera, projection);
//            }
//
//            for (Object object : objectPointControl) {
//                object.drawLine(camera, projection);
//            }
//
//            for (Object object : objectPointCurve) {
//                object.drawLine(camera, projection);
//            }

            input();


            // Restore state
            glDisableVertexAttribArray(0);

            // Poll for window events.
            // The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public void run() {
        init();
        loop();

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
