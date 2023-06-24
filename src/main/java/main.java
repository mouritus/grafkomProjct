//c14210182 Mouritus Huangtara M
import Engine.*;
import Engine.Object;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class main {
    Camera camera = new Camera();
    private Window window = new Window(800, 800, "Hello World");
    Projection projection = new Projection(window.getWidth(), window.getHeight());

    private ArrayList<Object> objects = new ArrayList<>();
    private ArrayList<Object> objectsRectangle = new ArrayList<>();
    private ArrayList<Object> objectPointControl = new ArrayList<>();
    private ArrayList<Object> objectPointCurve = new ArrayList<>();

    private boolean dragDrop = false;
    private float posX = 0.0f;
    private float posY = 0.0f;
    private float posZ = 2.0f;
    private boolean state = false;
    private float rotate = 0f;

    private Object lowerBody;
    private Object upperBody;
    private Object head;
    private Object jointHead;
    private Object eyeRight; //from char perspective
    private Object eyeLeft;
    private Object jointUpperBody;
    private Object jointArmLeft;
    private Object jointArmRight;
    private Object jointHandLeft;
    private Object jointHandRight;
    private Object jointLegLeft;
    private Object jointLegRight;
    private Object jointFootLeft;
    private Object jointFootRight;

    private ArrayList<Double> valueArray = new ArrayList<>();
    private ArrayList<Boolean> stateArray = new ArrayList<>();



    public static void main(String[] args) {
        new main().run();
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
        camera.setRotation((float) Math.toRadians(0.0f), (float) Math.toRadians(0.0f));

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
                new Vector4f(1f, 1f, 1f, 0f), "resources/Blender/backrooms.obj"
        ));
        objects.get(0).translateObject(0f,-1f,0f);
//        objects.get(0).rotateObject((float)Math.toRadians(180),0f,1f,0f);
//        objects.add(new Sphere(
//                Arrays.asList(
//                        new ShaderProgram.ShaderModuleData(
//                                "resources/shaders/scene.vert"
//                                , GL_VERTEX_SHADER),
//                        new ShaderProgram.ShaderModuleData(
//                                "resources/shaders/scene.frag"
//                                , GL_FRAGMENT_SHADER)
//                ),
//                new ArrayList<>(
//                        List.of(
//                                new Vector3f(-0.5f,0.5f,0.0f),
//                                new Vector3f(-0.5f,-0.5f,0.0f),
//                                new Vector3f(0.5f,-0.5f,0.0f),
//                                new Vector3f(0.5f,0.5f,0.0f)
//                        )
//                ),
//                new Vector4f(0.0f,1.0f,1.0f,1.0f),
//                Arrays.asList(0.0f,0.0f,0.0f),
//                0.125f,
//                0.125f,
//                0.125f
//        ));

    }

    public void input(){
        float move = 0.1f;
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            objects.get(0).rotateObject((float) Math.toRadians(0.2f), 0.0f, 0.0f, 1.f);

            for (Object child : objects.get(0).getChildObject()) {
                Vector3f tempCenterPoint = child.updateCenterPointObject();
                child.translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
                child.rotateObject((float) Math.toRadians(0.5f),0.0f,0.0f,1.0f);
                child.translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);

            }

            for (Object child : objects.get(0).getChildObject().get(1).getChildObject()) {
                Vector3f tempCenterPoint =  objects.get(0).getChildObject().get(1).updateCenterPointObject();
                child.translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
                child.rotateObject((float) Math.toRadians(0.5f),0.0f,0.0f,1.0f);
                child.translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);

            }
        }

        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        }

        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        }

        if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }

        if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }

        if (window.isKeyPressed(GLFW_KEY_I)) {
            objects.get(0).translateObject(0.0f, 0.0f,-0.1f);
        }

        if (window.isKeyPressed(GLFW_KEY_J)) {
            objects.get(0).translateObject(-0.1f, 0.0f,0.0f);
        }

        if (window.isKeyPressed(GLFW_KEY_K)) {
            objects.get(0).translateObject(0.0f, 0.0f,0.1f);
        }

        if (window.isKeyPressed(GLFW_KEY_L)) {
            objects.get(0).translateObject(0.1f, 0.0f,0.0f);
        }

        if (window.getMousInput().isLeftButtonPressed()) {
            Vector2f displayVector = window.getMousInput().getDisplVec();
            camera.addRotation((float) Math.toRadians(displayVector.x * 0.1), (float) Math.toRadians(displayVector.y * 0.1));
        }

        if (window.getMousInput().getScroll().y != 0) {
            projection.setFOV(projection.getFOV() - window.getMousInput().getScroll().y * 0.1f);
            window.getMousInput().setScroll(new Vector2f(0f,0f));
        }

        if (window.isKeyPressed(GLFW_KEY_UP)) {
            objects.get(0).translateObject(0.0f, 0.1f,0.0f);
        }

        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            objects.get(0).translateObject(0.0f, -0.1f,0.0f);
        }

        if (window.isKeyPressed(GLFW_KEY_E)) {
            state = true;
        }

        if (state) {
            camera.addRotation(0f, (float) Math.toRadians(5f));
            rotate += 5f;
            if (rotate >= 360) {
                rotate = 0;
                state = false;
            }
        }
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
