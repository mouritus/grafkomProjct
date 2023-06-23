package Engine;
//c14210182 Mouritus Huangtara M

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.openvr.Texture;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Object extends ShaderProgram {
    public Matrix4f model;
    List<Vector3f> vertices;
    List<Vector2f> textures = new ArrayList<>();
    List<Vector3f> normal = new ArrayList<>();
    Vector4f color;
    UniformsMap uniformsMap;
    int vao;
    int vbo;
    List<Vector3f> verticesColor;
    int vbocolor;
    int texID;
    List<Object> childObject;
    Vector3f centerPosition = new Vector3f();


    //pos before animate
    Vector3f pos = new Vector3f();
    Vector3f rotation = new Vector3f();
    Vector3f rotationWithoutParent = new Vector3f();

    int hasTexture;

    public List<Vector3f> getVertices() {
        return vertices;
    }
    public Object(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color) {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.color = color;
        uniformsMap = new UniformsMap(getProgramId());
//        uniformsMap.createUniform("uni_color");
//        uniformsMap.createUniform("model");
//        uniformsMap.createUniform("view");
//        uniformsMap.createUniform("projection");

        model = new Matrix4f();
        childObject = new ArrayList<>();
        hasTexture = 0;
//        setupVAOVBO();
    }



    public Object(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, List<Vector3f> verticesColor) {
        super(shaderModuleDataList);
        this.vertices = vertices;
        this.verticesColor = verticesColor;
        setupVAOVBOWithVerticesColor();
    }


    public void setupVAOVBO() {
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        //kirim data ke shader
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(vertices), GL_STATIC_DRAW);

        //set texture

//        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        
//        glBufferData(GL_ARRAY_BUFFER, Utils.listoTexture(textures), GL_STATIC_DRAW);
    }

    public void setupVAOVBOWithVerticesColor() {
        //set vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        //set vbo
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        //kirim data ke shader
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(vertices), GL_STATIC_DRAW);

        //set vboColor
        vbocolor = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbocolor);
        //kirim data ke shader
        glBufferData(GL_ARRAY_BUFFER, Utils.listoFloat(verticesColor), GL_STATIC_DRAW);
    }

    //Rendering
    public void drawSetup(Camera camera, Projection projection) {
        bind();
        uniformsMap.setUniform("uni_color", color);
        uniformsMap.setUniform("model", model);
        uniformsMap.setUniform("view", camera.getViewMatrix());
        uniformsMap.setUniform("projection", projection.getProjMatrix());
//        uniformsMap.setUniform("hasTexture", 0);
//        uniformsMap.setUniform("texture_sampler", 0);

        //bind vbo
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

//        glBindTexture(GL_TEXTURE_2D, texID);
//        glEnableVertexAttribArray(1);
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texID);
//        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
    }

    public void drawSetupWIthVerticesColor() {
        bind();
        //bind vbo
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        //bind vbocolor
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, vbocolor);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
    }

    public void draw(Camera camera, Projection projection) {
        drawSetup(camera, projection);
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_TRIANGLES, 0, vertices.size());
        for (Object child : childObject) {
            child.draw(camera, projection);
        }
    }

    public void drawWithVerticesColor() {
        drawSetupWIthVerticesColor();
        glLineWidth(10);
        glPointSize(10);
        //GL_TRIANGLES
        //GL_LINE_LOOP
        //GL_LINE_STRIP
        //GL_LINES
        //GL_POINTS
        //GL_TRIANGLE_FAN
        glDrawArrays(GL_TRIANGLES, 0, vertices.size());
    }

    public void drawLine(Camera camera, Projection projection) {
        drawSetup(camera, projection);
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_LINE_STRIP, 0, vertices.size());
    }

    public void addVertices(Vector3f newVector) {
        vertices.add(newVector);
        setupVAOVBO();
    }

    public void clearVertices() {
        vertices = new ArrayList<>();
    }

    public void changePosition(List<Vector3f> vertices) {
        this.vertices = vertices;
        setupVAOVBO();
    }

    public void changePosition(Vector3f s, int temp) {
        vertices.get(temp).set(s);
        setupVAOVBO();
    }

    public int factorial(int x) {
        if (x == 0) {
            return 1;
        }

        if (x == 1) {
            return 1;
        }

        if (x == 2) {
            return 2;
        }

        if (x == 3) {
            return 6;
        }

        if (x == 4) {
            return 24;
        }

        if (x == 5) {
            return 120;
        }

        int t = 1;
        for (int i = 1; i <= x; i++) {
            t *= i;
        }
        return t;
    }

    public int combination(Integer n, int i) {
        int temp = factorial((n - i));
        n = factorial(n);
        i = factorial(i);


        return n / (i * temp);
    }

    public void bezierCurve(Object object) {
        int size = vertices.size() - 1;
        for (float i = 0; i <= 1; i += 0.01f) {
            float x = 0;
            float y = 0;
            float z = 0;
            for (int j = 0; j < vertices.size(); j++) {
                double pow = Math.pow((1 - i), size - j);
                x += (float) (combination(size, j) * pow * Math.pow(i, j) * vertices.get(j).x);
                y += (float) (combination(size, j) * pow * Math.pow(i, j) * vertices.get(j).y);
                z += (float) (combination(size, j) * pow * Math.pow(i, j) * vertices.get(j).z);
            }
            object.addVertices(new Vector3f(x, y, z));
        }
    }


    public void translateObject(Float offsetx, Float offsety, Float offsetz) {
        model = new Matrix4f().translate(offsetx, offsety, offsetz).mul(new Matrix4f(model));
        for (Object child : childObject) {
            child.translateObject(offsetx, offsety, offsetz);
        }
    }

    public void translateObjectAnimate(Float offsetx, Float offsety, Float offsetz) {
        rotateObject(-rotation.z, 0f, 0f, 1f);
        rotateObject(-rotation.y, 0f, 1f, 0f);
        rotateObject(-rotation.x, 1f, 0f, 0f);

        model = new Matrix4f().translate(offsetx, offsety, offsetz).mul(new Matrix4f(model));

        for (Object child : childObject) {
            child.translateObject(offsetx, offsety, offsetz);
        }

        rotateObject(rotation.x, 1f, 0f, 0f);
        rotateObject(rotation.y, 0f, 1f, 0f);
        rotateObject(rotation.z, 0f, 0f, 1f);

        pos.x += offsetx;
        pos.y += offsety;
        pos.z += offsetz;


    }

    public void rotateObject(Float degree, Float offsetx, Float offsety, Float offsetz) {
        model = new Matrix4f().rotate(degree, offsetx, offsety, offsetz).mul(new Matrix4f(model));
        for (Object child : childObject) {
            child.rotateObject(degree, offsetx, offsety, offsetz);
        }
    }

    public void rotateObjectAnimate(Float degree, Float offsetx, Float offsety, Float offsetz) {
        if (offsetx == 1f) {
            rotateObject(-rotation.z, 0f, 0f, 1f);
            rotateObject(-rotation.y, 0f, 1f, 0f);
            rotateObject(-rotation.x, 1f, 0f, 0f);

            model = new Matrix4f().rotate(degree, offsetx, offsety, offsetz).mul(new Matrix4f(model));
            for (Object child : childObject) {
                child.rotateObjectAnimateUtil(degree, offsetx, offsety, offsetz);
            }
            rotateObject(rotation.x, 1f, 0f, 0f);
            rotateObject(rotation.y, 0f, 1f, 0f);
            rotateObject(rotation.z, 0f, 0f, 1f);
            rotation.x += degree;
            rotationWithoutParent.x += degree;
        }

        if (offsety == 1f) {
            rotateObject(-rotation.z, 0f, 0f, 1f);
            rotateObject(-rotation.x, 1f, 0f, 0f);
            rotateObject(-rotation.y, 0f, 1f, 0f);
            model = new Matrix4f().rotate(degree, offsetx, offsety, offsetz).mul(new Matrix4f(model));
            for (Object child : childObject) {
                child.rotateObjectAnimateUtil(degree, offsetx, offsety, offsetz);
            }
            rotateObject(rotation.y, 0f, 1f, 0f);
            rotateObject(rotation.x, 1f, 0f, 0f);
            rotateObject(rotation.z, 0f, 0f, 1f);
            rotation.y += degree;
            rotationWithoutParent.y += degree;
        }

        if (offsetz == 1f) {

            rotateObject(-rotation.y, 0f, 1f, 0f);
            rotateObject(-rotation.x, 1f, 0f, 0f);
            rotateObject(-rotation.z, 0f, 0f, 1f);

            model = new Matrix4f().rotate(degree, offsetx, offsety, offsetz).mul(new Matrix4f(model));
            for (Object child : childObject) {
                child.rotateObjectAnimateUtil(degree, offsetx, offsety, offsetz);
            }
            rotateObject(rotation.z, 0f, 0f, 1f);
            rotateObject(rotation.x, 1f, 0f, 0f);
            rotateObject(rotation.y, 0f, 1f, 0f);

            rotation.z += degree;
            rotationWithoutParent.z += degree;
        }


    }

    private void rotateObjectAnimateUtil(Float degree, Float offsetx, Float offsety, Float offsetz) {
        model = new Matrix4f().rotate(degree, offsetx, offsety, offsetz).mul(new Matrix4f(model));

        if (offsetx == 1f) {
            rotation.x += degree;
        }

        if (offsety == 1f) {
            rotation.y += degree;
        }

        if (offsetz == 1f) {
            rotation.z += degree;
        }
        for (Object child : childObject) {
            child.rotateObjectAnimateUtil(degree, offsetx, offsety, offsetz);
        }
    }

    public void resetRotateUtilParent(Vector3f rotateParent){
        rotation.x += -rotateParent.x;
        rotation.y += -rotateParent.y;
        rotation.z += -rotateParent.z;
        for (Object children : getChildObject()) {
            children.resetRotateUtilParent(rotateParent);
        }
    }

    public void resetRotate() {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
        rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
        rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);
        resetRotateUtilParent(rotationWithoutParent);

        rotationWithoutParent = new Vector3f();
    }

    public void resetRotate(int i) {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        rotation.x += -rotationWithoutParent.x;
        rotation.y += -rotationWithoutParent.y;
        rotation.z += -rotationWithoutParent.z;
        resetRotateUtilParent(rotationWithoutParent);
        switch (i) {
            case 1:
                rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
                rotationWithoutParent.x = 0;
                rotation.x = 0;
                break;
            case 2:
                rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
                rotationWithoutParent.y = 0;
                rotation.y = 0;
                break;
            case 3:
                rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
                rotationWithoutParent.z = 0;
                rotation.z = 0;
                break;
        }
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);

    }

    public void resetRotateChildren() {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
        rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
        rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);
        resetRotateUtilParent(rotationWithoutParent);
        rotation = new Vector3f();
        rotationWithoutParent = new Vector3f();

        for (Object child : childObject) {
            child.resetAllChildren();
        }
    }

    public void resetRotateChildren(int i) {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        rotation.x += -rotationWithoutParent.x;
        rotation.y += -rotationWithoutParent.y;
        rotation.z += -rotationWithoutParent.z;
        resetRotateUtilParent(rotationWithoutParent);
        switch (i) {
            case 1:
                rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
                rotationWithoutParent.x = 0;
                rotation.x = 0;
                break;
            case 2:
                rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
                rotationWithoutParent.y = 0;
                rotation.y = 0;
                break;
            case 3:
                rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
                rotationWithoutParent.z = 0;
                rotation.z = 0;
                break;
        }
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);

        for (Object child : childObject) {
            child.resetAllChildren(i);
        }
    }

    public void resetALl() {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
        rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
        rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);
        resetRotateUtilParent(rotationWithoutParent);
        rotation.x += -rotationWithoutParent.x;
        rotation.y += -rotationWithoutParent.y;
        rotation.z += -rotationWithoutParent.z;
        rotationWithoutParent = new Vector3f();
        if (pos.x != 0 || pos.y != 0 || pos.z != 0) {
            translateObject(-pos.x, -pos.y, -pos.z);
            pos = new Vector3f();
        }

    }

    public void resetALl(int i) {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        rotation.x += -rotationWithoutParent.x;
        rotation.y += -rotationWithoutParent.y;
        rotation.z += -rotationWithoutParent.z;
        resetRotateUtilParent(rotationWithoutParent);
        switch (i) {
            case 1:
                rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
                rotationWithoutParent.x = 0;
                rotation.x = 0;
                break;
            case 2:
                rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
                rotationWithoutParent.y = 0;
                rotation.y = 0;
                break;
            case 3:
                rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
                rotationWithoutParent.z = 0;
                rotation.z = 0;
                break;
        }
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);

        if (pos.x != 0 || pos.y != 0 || pos.z != 0) {
            translateObject(-pos.x, -pos.y, -pos.z);
            pos = new Vector3f();
        }

    }

    public void resetOnlyChildren() {
        for (Object child : childObject) {
            child.resetAllChildren();
        }
    }

    public void resetAllChildren() {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
        rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
        rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);
        rotation = new Vector3f();
        rotationWithoutParent = new Vector3f();
        if (pos.x != 0 && pos.y != 0 && pos.z != 0) {
            translateObject(-pos.x, -pos.y, -pos.z);
        }
        pos = new Vector3f();

        for (Object child : childObject) {
            child.resetAllChildren();
        }
    }

    public void resetAllChildren(int i) {
        Vector3f tempCenterPoint = updateCenterPointObject();
        translateObject(tempCenterPoint.x * -1, tempCenterPoint.y * -1, tempCenterPoint.z * -1);
        switch (i) {
            case 1:
                rotateObject(-rotationWithoutParent.x, 1f, 0f, 0f);
                rotationWithoutParent.x = 0;
                rotation.x = 0;
                break;
            case 2:
                rotateObject(-rotationWithoutParent.y, 0f, 1f, 0f);
                rotationWithoutParent.y = 0;
                rotation.y = 0;
                break;
            case 3:
                rotateObject(-rotationWithoutParent.z, 0f, 0f, 1f);
                rotationWithoutParent.z = 0;
                rotation.z = 0;
                break;
        }
        translateObject(tempCenterPoint.x * 1, tempCenterPoint.y * 1, tempCenterPoint.z * 1);

        if (pos.x != 0 && pos.y != 0 && pos.z != 0) {
            translateObject(-pos.x, -pos.y, -pos.z);
        }
        pos = new Vector3f();

        for (Object child : childObject) {
            child.resetAllChildren(i);
        }
    }


    public void scaleObject(Float x, Float y, Float z) {
        model = new Matrix4f().scale(x, y, z).mul(new Matrix4f(model));
        for (Object child : childObject) {
            child.scaleObject(x, y, z);
        }
    }

    public Vector3f updateCenterPointObject() {
        Vector3f centerTemp = new Vector3f();
        model.transformPosition(0.0f, 0.0f, 0.0f, centerTemp);
        return centerTemp;
    }

    public List<Object> getChildObject() {
        return childObject;
    }

    public void readTexture(String texName){
        ByteBuffer buffer;
        int width, height;

        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(texName, w, h, c, 4);
            if (buffer == null){
                System.out.println(STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        hasTexture = 1;
    }

}