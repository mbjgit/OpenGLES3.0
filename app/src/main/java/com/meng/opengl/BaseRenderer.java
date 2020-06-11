package com.meng.opengl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public interface BaseRenderer extends GLSurfaceView.Renderer {
    void destroyed();
}
