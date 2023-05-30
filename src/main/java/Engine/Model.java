package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Model extends Object {

    int ibo;
    List<Vector3f> normal = new ArrayList<>();
    ArrayList<ArrayList<Vector3f>> faces = new ArrayList<>(Arrays.asList(
            //v, vt, vn
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
    ));
    ArrayList<Vector3f> tempVertices = new ArrayList<>();
    ArrayList<Vector3f> tempNormal = new ArrayList<>();
    int nbo;
    String filePath;


    public Model(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, String filePath) {
        super(shaderModuleDataList, vertices, color);
        this.filePath = filePath;
        createModel();
        setupVAOVBO();
    }

    public Model(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, String filePath, String condition) {
        super(shaderModuleDataList, vertices, color);
        this.filePath = filePath;
        createModel(condition);
        setupVAOVBO();
    }

    public void createModel() {
        readFile();
        makeModel();
    }

    public void createModel(String condition) {
        readFile(condition);
        makeModel();
    }

    public void readFile() {
        File myObj = new File(filePath);
        if (myObj.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("v ")) {
                        float x = Float.parseFloat(line.split(" ")[1]);
                        float y = Float.parseFloat(line.split(" ")[2]);
                        float z = Float.parseFloat(line.split(" ")[3]);
                        tempVertices.add(new Vector3f(x, y, z));
                    } else if (line.startsWith("vn ")) {
                        float x = Float.parseFloat(line.split(" ")[1]);
                        float y = Float.parseFloat(line.split(" ")[2]);
                        float z = Float.parseFloat(line.split(" ")[3]);
                        tempNormal.add(new Vector3f(x, y, z));
                    } else if (line.startsWith("f ")) {
                        Vector3f v = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[0]),
                                Float.parseFloat(line.split(" ")[2].split("/")[0]),
                                Float.parseFloat(line.split(" ")[3].split("/")[0]));
                        Vector3f vt = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[1]),
                                Float.parseFloat(line.split(" ")[2].split("/")[1]),
                                Float.parseFloat(line.split(" ")[3].split("/")[1]));
                        Vector3f vn = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[2]),
                                Float.parseFloat(line.split(" ")[2].split("/")[2]),
                                Float.parseFloat(line.split(" ")[3].split("/")[2]));
                        faces.get(0).add(v);
                        faces.get(1).add(vt);
                        faces.get(2).add(vn);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFile(String condition) {
        File myObj = new File(filePath);
        if (myObj.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line;
            try {
                boolean state = false;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("o")) {
                        if (line.startsWith(condition)) {
                            state = true;
                        } else
                            state = false;
                    }

                    if (line.startsWith("v ")) {
                        float x = Float.parseFloat(line.split(" ")[1]);
                        float y = Float.parseFloat(line.split(" ")[2]);
                        float z = Float.parseFloat(line.split(" ")[3]);
                        tempVertices.add(new Vector3f(x, y, z));
                    } else if (line.startsWith("vn ")) {
                        float x = Float.parseFloat(line.split(" ")[1]);
                        float y = Float.parseFloat(line.split(" ")[2]);
                        float z = Float.parseFloat(line.split(" ")[3]);
                        tempNormal.add(new Vector3f(x, y, z));
                    } else if (line.startsWith("f ") && state) {
                        Vector3f v = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[0]),
                                Float.parseFloat(line.split(" ")[2].split("/")[0]),
                                Float.parseFloat(line.split(" ")[3].split("/")[0]));
                        Vector3f vt = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[1]),
                                Float.parseFloat(line.split(" ")[2].split("/")[1]),
                                Float.parseFloat(line.split(" ")[3].split("/")[1]));
                        Vector3f vn = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[2]),
                                Float.parseFloat(line.split(" ")[2].split("/")[2]),
                                Float.parseFloat(line.split(" ")[3].split("/")[2]));
                        faces.get(0).add(v);
                        faces.get(1).add(vt);
                        faces.get(2).add(vn);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void makeModel() {
        for (int i = 0; i < faces.get(0).size(); i++) {
            float vA = faces.get(0).get(i).x - 1;
            float vB = faces.get(0).get(i).y - 1;
            float vC = faces.get(0).get(i).z - 1;

            vertices.add(tempVertices.get((int) vA));
            vertices.add(tempVertices.get((int) vB));
            vertices.add(tempVertices.get((int) vC));

            float vnA = faces.get(2).get(i).x - 1;
            float vnB = faces.get(2).get(i).y - 1;
            float vnC = faces.get(2).get(i).z - 1;

            normal.add(tempNormal.get((int) vnA));
            normal.add(tempNormal.get((int) vnB));
            normal.add(tempNormal.get((int) vnC));
        }
    }

    public void setupVAOVBO() {
        super.setupVAOVBO();
        //nbo
        //set nbo
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        //mengirim vertices ke shader
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(vertices),
                GL_STATIC_DRAW);
//        uniformsMap.createUniform("lightColor");
//        uniformsMap.createUniform("lightPos");
    }

    @Override
    public void drawSetup(Camera camera, Projection projection) {
        super.drawSetup(camera, projection);

        //bind nbo
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        uniformsMap.setUniform("lightColor", new Vector3f(1.0f, 1.0f, 0.0f));
        uniformsMap.setUniform("lightPos", new Vector3f(1.0f, 1.0f, 0.0f));
        uniformsMap.setUniform("viewPos",camera.getPosition());
    }
}
