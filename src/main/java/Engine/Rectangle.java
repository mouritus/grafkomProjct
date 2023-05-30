package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL15.*;


public class Rectangle extends Object {

    List<Integer> index;
    float posX1; //x kiri
    float posX2; //x kanan
    float posY1; //y bawah
    float posY2; //y atas
    int ibo; //index buffer object / element buffer object
    //rule draw rectangle kiri bawah, kiri atas, kanan bawah, kanan atas


    public Rectangle(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, List<Integer> index) {
        super(shaderModuleDataList, vertices, color);
        this.index = index;
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.listoInt(index), GL_STATIC_DRAW);
        posX1 = vertices.get(0).x;
        posX2 = vertices.get(2).x;
        posY1 = vertices.get(0).y;
        posY2 = vertices.get(1).y;
    }

    public void draw() {
//        drawSetup(camera, projection);
        //Bind IBO & draw
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, index.size(), GL_UNSIGNED_INT, 0);
    }

    public float getPosX1() {
        return posX1;
    }

    public float getPosX2() {
        return posX2;
    }

    public float getPosY1() {
        return posY1;
    }

    public float getPosY2() {
        return posY2;
    }

    public void changePosition(List<Vector3f> vertices) {
        super.changePosition(vertices);
        posX1 = vertices.get(0).x;
        posX2 = vertices.get(2).x;
        posY1 = vertices.get(0).y;
        posY2 = vertices.get(1).y;
    }
}
