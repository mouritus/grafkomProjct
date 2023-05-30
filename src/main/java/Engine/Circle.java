package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;


public class Circle extends Object {
    float x, y, centerpointX, centerpointY, rx, ry;
    Vector3f centerPoint = new Vector3f();

    public Circle(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, float centerpointX, float centerpointY, float rx) {
        super(shaderModuleDataList, vertices, color);
        centerPoint.x = centerpointX;
        centerPoint.y = centerpointY;
        this.rx = rx;
        createCircle();
//        setupVAOVBO();
    }

    public Circle(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, float centerpointX, float centerpointY, float rx, float ry) {
        super(shaderModuleDataList, vertices, color);
        centerPoint.x = centerpointX;
        centerPoint.y = centerpointY;
        this.rx = rx;
        this.ry = ry;
        creareElipse();
//        setupVAOVBO();

    }

    public void createCircle() {
        //vertices -> clear
        vertices.clear();

        for (float i = 0; i < 360; i += 0.001f) {
            x = (float) (centerPoint.x + rx * Math.cos(Math.toRadians(i)));
            y = (float) (centerPoint.y + rx * Math.sin(Math.toRadians(i)));
            vertices.add(new Vector3f((float) x, (float) y, 0.0f));
        }
    }

    public void creareElipse() {
        vertices.clear();

        for (float i = 0; i < 360; i += 0.001f) {
            x = (float) (centerPoint.x + rx * Math.cos(Math.toRadians(i)));
            y = (float) (centerPoint.y + ry * Math.sin(Math.toRadians(i)));
            vertices.add(new Vector3f((float) x, (float) y, 0.0f));
        }
    }

//    public void draw() {
//        drawSetup();
//        //Bind IBO & draw
//        glLineWidth(1);
//        glPointSize(0);
//        glDrawArrays(GL_POLYGON, 0, vertices.size());
//    }

}

