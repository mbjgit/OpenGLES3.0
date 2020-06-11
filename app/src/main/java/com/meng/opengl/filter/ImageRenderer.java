package com.meng.opengl.filter;

import com.meng.opengl.BaseRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ImageRenderer implements BaseRenderer {
    private ImageBaseFilter imageBaseFilter;

    public ImageRenderer(ImageBaseFilter imageBaseFilter) {
        this.imageBaseFilter = imageBaseFilter;
    }

    @Override
    public void destroyed() {
        imageBaseFilter.destroy();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        imageBaseFilter.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        imageBaseFilter.initWindow(i,i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        imageBaseFilter.draw();
    }
}
