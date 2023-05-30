package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;


public class Circle_triangle extends Object {
    double x, y, centerpointX, centerpointY, r, ry;

    public Circle_triangle(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, double centerpointX, double centerpointY, double r) {
        super(shaderModuleDataList, vertices, color);
        this.centerpointX = centerpointX;
        this.centerpointY = centerpointY;
        this.r = r;
        createTriangle();
        setupVAOVBO();
    }

    public void createTriangle()
    {
        //clear vertices
        vertices.clear();

        for (double i = 90; i < 360; i+=120)
        {
            x = centerpointX + r * (float)Math.cos(Math.toRadians(i));
            y = centerpointY + r * (float)Math.sin(Math.toRadians(i));

            x = (double)Math.round(x*100)/100;
            y = (double)Math.round(y*100)/100;
            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    public void draw() {
//        drawSetup(camera, projection);
        //Bind IBO & draw
        glLineWidth(1);
        glPointSize(0);
        glDrawArrays(GL_POLYGON, 0, vertices.size());
    }

}

