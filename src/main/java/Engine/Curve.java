package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;


public class Curve extends Object{
    int i;
    public Curve(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, int i) {
        super(shaderModuleDataList, vertices, color);
        this.i = i;
    }

    @Override
    public void draw(Camera camera, Projection projection) {
        drawSetup(camera, projection);
        glLineWidth(i);
        glPointSize(0);
        glDrawArrays(GL_LINE_STRIP, 0, vertices.size());
    }

}
