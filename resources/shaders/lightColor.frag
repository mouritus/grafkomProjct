#version 330
uniform vec4 uni_color;
out vec4 frag_color;

uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 viewPos;

in vec3 Normal;
in vec3 FragPos;
void main()
{
    //vec4(red,green,blue,alpha)
    //rgba -> red 100/255

    //Ambient
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;

    //Diffuse
    vec3 lightDirection = normalize(lightPos - FragPos);
    float diff = max(dot(Normal, lightDirection),0.0);
    vec3 diffuse = diff * lightColor;

    //specular
    float specularStrength = 0.5f;
    vec3 viewDir = normalize(viewPos - FragPos);

    //blinn phong
    vec3 halfwayDir = normalize(lightDirection + viewDir);
    float spec = pow(max(dot(Normal, halfwayDir), 0.0),64.0*3.0);

    //original phong
    //    vec3 reflectDir = reflect(-lightDirection, Normal);
    //    float spec = pow(max(dot(viewDir, reflectDir), 0.0),64.0);

    vec3 specular = specularStrength * spec * lightColor;

    vec3 result = (ambient+diffuse+specular) * vec3(uni_color);
    //frag_color = uni_color;
    frag_color = vec4(result,1.0);
    //    frag_color = vec4(1.0,0.0,0.0,1.0);

}
