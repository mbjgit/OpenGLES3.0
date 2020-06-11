package com.meng.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.meng.opengl.shape.BaseShape;
import com.meng.opengl.shape.Circle30ES;
import com.meng.opengl.shape.Square30ES;
import com.meng.opengl.shape.Triangle30ES;

import java.util.ArrayList;
import java.util.List;

public class MyGLSurfaceView extends GLSurfaceView {
    public MyGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
   private MyGLRenderer renderer;
    private void init(){
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3);
        List<BaseShape> baseShapes=new ArrayList<>();
        baseShapes.add(new Circle30ES());
        // baseShapes.add(new Square30ES());
        //baseShapes.add(new Triangle30ES());
        renderer = new MyGLRenderer(baseShapes);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        renderer.destroyed();
    }
}
