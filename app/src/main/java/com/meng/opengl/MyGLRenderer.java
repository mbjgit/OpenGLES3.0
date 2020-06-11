package com.meng.opengl;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.meng.opengl.shape.BaseShape;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final List<BaseShape> list;

    public MyGLRenderer(List<BaseShape> list) {
        this.list = list;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // Set the background frame color
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //square=new Square30ES();
        for(BaseShape baseShape:list){
            baseShape.init();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        // Red raw background color
        GLES30.glViewport(0, 0, i, i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        for(BaseShape baseShape:list){
            baseShape.draw();
        }
    }

    public void destroyed() {
        for(BaseShape baseShape:list){
            baseShape.destroy();
        }
    }
}
