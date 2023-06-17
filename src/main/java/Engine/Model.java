package Engine;

import org.joml.Vector2f;
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
    ArrayList<ModelObj> modelObjs = new ArrayList<>();

    // list kumpulan
    ArrayList<Vector3f> tempVertices = new ArrayList<>();
    ArrayList<Vector2f> tempVerticeTexture = new ArrayList<>();
    ArrayList<Vector3f> tempNormal = new ArrayList<>();


    List<Vector3f> normal = new ArrayList<>();
    ArrayList<ArrayList<Vector3f>> tempFaces = new ArrayList<>(Arrays.asList(
            //v, vt, vn
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
    ));

    ArrayList<ArrayList<Vector3f>> tempFacesMtl = new ArrayList<>(Arrays.asList(
            //v, vt, vn
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
    ));


    int nbo;
    String filePath;


    public Model(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, String filePath) {
        super(shaderModuleDataList, vertices, color);
        this.filePath = filePath;
        createModel();
        setupVAOVBO();
    }

    public void createModel() {
        readFile();
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
            int ifObjorMtl = 0;
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("o ") || ifObjorMtl == 1) {
                        while (true) {


                            //if line start with o
                            //read till line not start with v vn vt
                            //
                            //if line start with usemtl
                            //read till not start with f s

                            //                            if (line.startsWith("v ")) {
//                                float x = Float.parseFloat(line.split(" ")[1]);
//                                float y = Float.parseFloat(line.split(" ")[2]);
//                                float z = Float.parseFloat(line.split(" ")[3]);
//                                tempVertices.add(new Vector3f(x, y, z));
//                            } else if (line.startsWith("vn ")) {
//                                float x = Float.parseFloat(line.split(" ")[1]);
//                                float y = Float.parseFloat(line.split(" ")[2]);
//                                float z = Float.parseFloat(line.split(" ")[3]);
//                                tempNormal.add(new Vector3f(x, y, z));
//                            } else if (line.startsWith("vt ")) {
//                                float x = Float.parseFloat(line.split(" ")[1]);
//                                float y = Float.parseFloat(line.split(" ")[2]);
//                                tempVerticeTexture.add(new Vector2f(x, y));
//                            }
//                        if (line.startsWith("f ")) {
//                            Vector3f v = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[0]),
//                                    Float.parseFloat(line.split(" ")[2].split("/")[0]),
//                                    Float.parseFloat(line.split(" ")[3].split("/")[0]));
//                            Vector3f vt = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[1]),
//                                    Float.parseFloat(line.split(" ")[2].split("/")[1]),
//                                    Float.parseFloat(line.split(" ")[3].split("/")[1]));
//                            Vector3f vn = new Vector3f(Float.parseFloat(line.split(" ")[1].split("/")[2]),
//                                    Float.parseFloat(line.split(" ")[2].split("/")[2]),
//                                    Float.parseFloat(line.split(" ")[3].split("/")[2]));
//                            tempFaces.get(0).add(v);
//                            tempFaces.get(1).add(vt);
//                            tempFaces.get(2).add(vn);
//                        }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.printf("");
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
        for (int i = 0; i < tempFaces.get(0).size(); i++) {
            float vA = tempFaces.get(0).get(i).x - 1;
            float vB = tempFaces.get(0).get(i).y - 1;
            float vC = tempFaces.get(0).get(i).z - 1;

            vertices.add(tempVertices.get((int) vA));
            vertices.add(tempVertices.get((int) vB));
            vertices.add(tempVertices.get((int) vC));

            float vnA = tempFaces.get(2).get(i).x - 1;
            float vnB = tempFaces.get(2).get(i).y - 1;
            float vnC = tempFaces.get(2).get(i).z - 1;

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

    public void drawSetup(Camera camera, Projection projection) {
        super.drawSetup(camera, projection);

        // Bind NBO
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1,
                3, GL_FLOAT,
                false,
                0, 0);

        //directional Light
        uniformsMap.setUniform("dirLight.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(0.05f, 0.05f, 0.05f));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));

        //posisi pointLight
        Vector3f[] _pointLightPositions = {
                new Vector3f(0.7f, 0.2f, 2.0f),
                new Vector3f(2.3f, -3.3f, -4.0f),
                new Vector3f(-4.0f, 2.0f, -12.0f),
                new Vector3f(0.0f, 0.0f, -3.0f)
        };
        for (int i = 0; i < _pointLightPositions.length; i++) {
            uniformsMap.setUniform("pointLights[" + i + "].position", _pointLightPositions[i]);
            uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(1.0f, 1.0f, 1.0f));
            uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
            uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
        }

        //spotlight
        uniformsMap.setUniform("spotLight.position", camera.getPosition());
        uniformsMap.setUniform("spotLight.direction", camera.getDirection());
        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.specular", new Vector3f(1.0f, 1.0f, 1.0f));
        uniformsMap.setUniform("spotLight.constant", 1.0f);
        uniformsMap.setUniform("spotLight.linear", 0.09f);
        uniformsMap.setUniform("spotLight.quadratic", 0.032f);
        uniformsMap.setUniform("spotLight.cutOff", (float) Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("viewPos", camera.getPosition());
    }
    @Override
    public void draw(Camera camera, Projection projection) {
        for (ModelObj modelObj : modelObjs) {
            super.draw(camera, projection);
        }
    }


}