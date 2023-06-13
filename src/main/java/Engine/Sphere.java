package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;


public class Sphere extends Circle {
    float rz, centerpointY;

    List<Integer> index = new ArrayList<>();
    int ibo;
    List<Vector3f> normal;
    int nbo;

    public Sphere(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, List<Float> centerPoint, Float rx, Float ry, Float rz) {
        super(shaderModuleDataList, vertices, color, centerPoint.get(0), centerPoint.get(1), rx, ry);
        this.rz = rz;
        createBox();
        setupVAOVBO();
    }

    public Sphere(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, float centerpointX, float centerpointY, float rx, float ry, float rz) {
        super(shaderModuleDataList, vertices, color, centerpointX, centerpointY, rx, ry);
        this.rz = rz;
        this.centerpointY = centerpointY;
//        createSphere();
        createBox();
        setupVAOVBO();
    }

    public Sphere(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, float centerpointX, float centerpointY, float rx, float ry, float rz, int i) {
        super(shaderModuleDataList, vertices, color, centerpointX, centerpointY, rx, ry);
        this.rz = rz;
        this.centerpointY = centerpointY;
        int cases = i;
        createQuadratic(cases);
        setupVAOVBO();
    }

    public void createQuadratic(int cases) {

//        ArrayList<Vector3f> temp = new ArrayList<>();
//        for (double v = -Math.PI / 2; v <= Math.PI / 2; v += Math.PI / 60) {
//            for (double u = -Math.PI; u <= Math.PI; u += Math.PI / 60) {
//                float x = rx * (float) (Math.cos(v) * Math.cos(u));
//                float y = ry * (float) (Math.cos(v) * Math.sin(u));
//                float z = rz * (float) (Math.sin(v));
//                temp.add(new Vector3f(x, y, z));
//            }
//        }

//        ArrayList<Vector3f> temp = new ArrayList<>();
//        for (double v = -Math.PI / 2; v <= Math.PI / 2; v += Math.PI / 120) {
//            for (double u = -Math.PI; u <= Math.PI; u += Math.PI / 120) {
//                float x = rx * (float) (1/Math.cos(v) * Math.cos(u));
//                float y = ry * (float) (1/Math.cos(v) * Math.sin(u));
//                float z = rz * (float) (Math.tan(v));
//                temp.add(new Vector3f(x, z, y));
//            }
//        }

//        ArrayList<Vector3f> temp = new ArrayList<>();
//        for (double v = -Math.PI / 2; v <= Math.PI / 2; v += Math.PI / 120) {
//            for (double u = -Math.PI / 2; u <= Math.PI / 2; u += Math.PI / 180) {
//                float x = 0.7f * (float) (Math.tan(v) * Math.cos(u));
//                float y = 0.7f * (float) (Math.tan(v) * Math.sin(u));
//                float z = 0.7f * (float) (1 / Math.cos(v));
//                temp.add(new Vector3f(x, z, y));
//            }
//
//            for (double u = Math.PI/2; u <= 3 * Math.PI / 2; u += Math.PI / 180) {
//                float x = 0.7f * (float) (Math.tan(v) * Math.cos(u));
//                float y = 0.7f * (float) (Math.tan(v) * Math.sin(u));
//                float z = 0.7f * (float) (1 / Math.cos(v));
//                temp.add(new Vector3f(x, z, y));
//            }
//        }
        ArrayList<Vector3f> temp = new ArrayList<>();
        switch (cases) {
            //lowerbody upperbody Elliptic Paraboloid
            case 0:
                for (double v = Math.PI / 2 + centerpointY; v >= 0 + centerpointY; v -= Math.PI / 400) {
                    for (double u = -Math.PI + centerpointY; u <= Math.PI + centerpointY; u += Math.PI / 400) {
                        float x = rx * (float) (v * Math.cos(u));
                        float y = ry * (float) (v * Math.sin(u));
                        float z = rz * (float) (Math.pow(v, 2));
                        temp.add(new Vector3f(x, z, y));
                    }
                }

                break;

            //eye glare nose Ellipsoid
            case 1:
                for (double v = -Math.PI / 2; v <= Math.PI / 2; v += Math.PI / 200) {
                    for (double u = -Math.PI; u <= Math.PI; u += Math.PI / 200) {
                        float x = rx * (float) (Math.cos(v) * Math.cos(u));
                        float y = ry * (float) (Math.cos(v) * Math.sin(u));
                        float z = rz * (float) (Math.sin(v));
                        temp.add(new Vector3f(x, y, z));
                    }
                }
                break;

            //segitiga but 3d
            case 2:
                for (double v = 0; v <= 2 * Math.PI; v += Math.PI / 200) {
                    for (double u = -Math.PI; u <= Math.PI; u += Math.PI / 200) {
                        float x = (float) (rx * v * Math.cos(u));
                        float y = (float) (ry * v * Math.sin(u));
                        float z = (float) (rz * v);
                        temp.add(new Vector3f(x, y, z));
                    }
                }
                break;

            //hiperboloid 1 sisi
            case 3:
                for (double v = -Math.PI / 2; v <= Math.PI / 2; v += Math.PI / 60) {
                    for (double u = -Math.PI; u <= Math.PI; u += Math.PI / 60) {
                        float x = this.rx * (float) ((Math.cosh(v)) * Math.cos(u));
                        float y = this.ry * (float) ((Math.cosh(v)) * Math.sin(u));
                        float z = this.rz * (float) (Math.sinh(v));
                        temp.add(new Vector3f(x, z, y));
                    }
                }
                break;
        }
        vertices = temp;

//        draw();

    }

    public void createBox() {
        vertices.clear();
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();
        //Titik 1 kiri atas belakang
        temp.x = centerPoint.get(0) - rx / 2;
        temp.y = centerPoint.get(1) + ry / 2;
        temp.z = centerPoint.get(2) - rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 2 kiri bawah belakang
        temp.x = centerPoint.get(0) - rx / 2;
        temp.y = centerPoint.get(1) - ry / 2;
        temp.z = centerPoint.get(2) - rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 3 kanan bawah belakang
        temp.x = centerPoint.get(0) + rx / 2;
        temp.y = centerPoint.get(1) - ry / 2;
        temp.z = centerPoint.get(2) - rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 4 kanan atas belakang
        temp.x = centerPoint.get(0) + rx / 2;
        temp.y = centerPoint.get(1) + ry / 2;
        temp.z = centerPoint.get(2) - rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 5 kiri atas depan
        temp.x = centerPoint.get(0) - rx / 2;
        temp.y = centerPoint.get(1) + ry / 2;
        temp.z = centerPoint.get(2) + rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 6 kiri bawah depan
        temp.x = centerPoint.get(0) - rx / 2;
        temp.y = centerPoint.get(1) - ry / 2;
        temp.z = centerPoint.get(2) + rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 7 kanan bawah depan
        temp.x = centerPoint.get(0) + rx / 2;
        temp.y = centerPoint.get(1) - ry / 2;
        temp.z = centerPoint.get(2) + rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();
        //Titik 8 kanan atas depan
        temp.x = centerPoint.get(0) + rx / 2;
        temp.y = centerPoint.get(1) + ry / 2;
        temp.z = centerPoint.get(2) + rz / 2;
        tempVertices.add(temp);
        temp = new Vector3f();

        //kotak belakang
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(2));

        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(0));
        //kotak depan
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(4));
        //kotak samping kiri
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(4));

        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(4));
        //kotak samping kanan
        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(2));

        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(7));
        //kotak bawah
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(1));
        //kotak atas
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(7));

        vertices.add(tempVertices.get(7));
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(3));

        normal = new ArrayList<>(Arrays.asList(
                //belakang
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                //depan
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                //kiri
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                //kanan
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                //bawah
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                //atas
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f)
        ));
    }

    public void createSphere() {
        vertices.clear();
        ArrayList<Vector3f> temp = new ArrayList<>();
        int stackCount = 270, sectorCount = 540;
        float x, y, z, xy, nx, ny, nz;
        float sectorStep = (float) (2 * Math.PI) / sectorCount; //sector count
        float stackStep = (float) Math.PI / stackCount; // stack count
        float sectorAngle, stackAngle;

        //titik persegi
        for (int i = 0; i <= stackCount; i++) {
            stackAngle = (float) Math.PI / 2 - i * stackStep;
            xy = (float) (0.5f * Math.cos(stackAngle));
            z = (float) (0.5f * Math.sin(stackAngle));
            for (int j = 0; j < sectorCount; j++) {
                sectorAngle = j * sectorStep;
                x = (float) (xy * Math.cos(sectorAngle));
                y = (float) (xy * Math.sin(sectorAngle));
                temp.add(new Vector3f(x, y, z));
            }
        }
        vertices = temp;

        int k1, k2;
        ArrayList<Integer> temp_indices = new ArrayList<>();
        for (int i = 0; i < stackCount; i++) {
            k1 = i * (sectorCount + 1);
            k2 = k1 + sectorCount + 1;
            k2 = k1 + sectorCount + 1;

            for (int j = 0; j < sectorCount; ++j, ++k1, ++k2) {
                if (i != 0) {
                    temp_indices.add(k1);
                    temp_indices.add(k2);
                    temp_indices.add(k1 + 1);
                }
                if (i != (18 - 1)) {
                    temp_indices.add(k1 + 1);
                    temp_indices.add(k2);
                    temp_indices.add(k2 + 1);
                }
            }
        }

        this.index = temp_indices;
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,
                ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,
                Utils.listoInt(index), GL_STATIC_DRAW);

    }

    public void setupVAOVBO() {
        super.setupVAOVBO();

        //nbo
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
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

    //    public void draw() {
//        drawSetup();
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
//        glDrawElements(GL_LINE_STRIP,
//                index.size(),
//                GL_UNSIGNED_INT, 0);
//    }

//    public void draw() {
//        drawSetup();
//        glLineWidth(1);
//        glPointSize(0);
//        glDrawArrays(GL_LINE_STRIP, 0, vertices.size());
//    }
}