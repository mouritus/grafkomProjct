package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Star extends Object {

    double x, y, centerpointX, centerpointY, r;
    int ibo;

    List<Integer> index;

    public Star(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color,List<Integer> index, double centerpointX, double centerpointY, double r) {
        super(shaderModuleDataList, vertices, color);
        this.centerpointX = centerpointX;
        this.centerpointY = centerpointY;
        this.r = r;
    }

    public void createStar()
    {
        //clear vertices
        vertices.clear();

        for (double i = 36; i < 396; i+=72)
        {
            x = centerpointX + r * (float)Math.cos(Math.toRadians(i));
            y = centerpointY + r * (float)Math.sin(Math.toRadians(i));

            vertices.add(new Vector3f((float)x, (float)y, 0.0f));
        }
        setupVAOVBO();
    }

    public void draw()
    {
//        drawSetup(camera, projection);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_LINES, index.size(), GL_UNSIGNED_INT, 0);
    }
}
