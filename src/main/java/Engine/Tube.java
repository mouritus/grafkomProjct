package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Tube extends Circle{

    public Tube(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, float centerpointX, float centerpointY, float rx, float ry) {
        super(shaderModuleDataList, vertices, color, centerpointX, centerpointY, rx, ry);
        createTube();
        setupVAOVBO();
    }

    public void createTube () {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();

        for (double v = 0; v <= 360; v += 0.01f) {
            float x = rx * (float) (Math.cos(Math.toRadians(v)));
            float y = rx * (float) (Math.sin(Math.toRadians(v)));
            temp.add(new Vector3f(x,y,-(ry)/2.0f));
            temp.add(new Vector3f(x,y,(ry)/2.0f));
        }
        vertices = temp;
    }
}
