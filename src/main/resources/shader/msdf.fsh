#version 120

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform vec2 unitRange;

float median(float r, float g, float b) {
    return max(min(r, g), min(max(r, g), b));
}

float screenPxRange() {
    vec2 screenTexSize = vec2(1.0) / fwidth(gl_TexCoord[0].st);
    return max(0.5 * dot(unitRange, screenTexSize), 1.0);
}

void main() {
    vec4 msd = texture2D(Sampler0, gl_TexCoord[0].st);
    float sd = median(msd.r, msd.g, msd.b);
    float screenPxDistance = screenPxRange() * (sd - 0.5);
    float opacity = clamp(screenPxDistance + 0.5, 0.0, 1.0);

    gl_FragColor = vec4(1.0, 1.0, 1.0, opacity) * ColorModulator;
}
