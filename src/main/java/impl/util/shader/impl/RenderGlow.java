package impl.util.shader.impl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public final class RenderGlow {

//    private final float radius;
//    private final Framebuffer[] blurFramebuffers;
//
//    public RenderGlow(final float radius, final int iterations) {
//        this.radius = radius / iterations;
//        this.blurFramebuffers = new Framebuffer[iterations];
//
//        for (int i = 0; i < blurFramebuffers.length; i++) {
//            blurFramebuffers[i] = new Framebuffer(1, 1,false);
//        }
//    }
//
//    private void refreshFramebuffers() {
//        for (int i = 0; i < blurFramebuffers.length; i++) {
//            blurFramebuffers[i] = FramebufferUtil.createFrameBuffer(blurFramebuffers[i],true);
//        }
//    }
//
//    public void glow(final Framebuffer framebufferGlow) {
//        refreshFramebuffers();
//        performGlowPass(0, framebufferGlow.framebufferTexture);
//
//        for (int i = 1; i < blurFramebuffers.length; i++) {
//            performGlowPass(i, blurFramebuffers[i - 1].framebufferTexture);
//        }
//
//        mc.getFramebuffer().bindFramebuffer(true);
//
//        // First bind texture units, then bind textures
//        glActiveTexture(GL_TEXTURE5);
//        glBindTexture(GL_TEXTURE_2D, blurFramebuffers[blurFramebuffers.length - 1].framebufferTexture);
//
//        glActiveTexture(GL_TEXTURE6);
//        glBindTexture(GL_TEXTURE_2D, framebufferGlow.framebufferTexture);
//
//        maskInShader.bind();
//        GlStateManager.enableBlend();
//        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
//
//        maskInShader.setUniform("iChannel0", 5);
//        maskInShader.setUniform("textureGlow", 6);
//
//        maskInShader.drawQuads();
//        maskInShader.unbind();
//
//        GlStateManager.setActiveTexture(GL_TEXTURE1);
//        GlStateManager.bindTexture(0);
//        GlStateManager.setActiveTexture(GL_TEXTURE0);
//        GlStateManager.bindTexture(0);
//        GlStateManager.disableBlend();
//    }
//
//    private void performGlowPass(final int index, final int sourceTexture) {
//        blurFramebuffers[index].framebufferClear();
//        blurFramebuffers[index].bindFramebuffer(true);
//        glowShader.bind();
//        glBindTexture(GL_TEXTURE_2D, sourceTexture);
//        setupGlowUniforms(radius + index);
//        glowShader.drawQuads();
//        glowShader.unbind();
//        blurFramebuffers[index].unbindFramebuffer();
//    }
//
//    private void setupGlowUniforms(final float radius) {
//        glowShader.setUniform("iChannel0", 0);
//        glowShader.setUniform("iResolution", (float) mc.displayWidth, (float) mc.displayHeight);
//        glowShader.setUniform("radius", radius);
//    }

}
