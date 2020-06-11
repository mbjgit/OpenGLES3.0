package com.meng.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.meng.opengl.shape.BaseShape;
import com.meng.opengl.shape.ShapeRenderer;
import com.meng.opengl.shape.Triangle30ES;
import com.meng.opengl.filter.ImageBaseFilter;
import com.meng.opengl.filter.ImageRenderer;

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
    private BaseRenderer renderer;

    private void init(){
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3);
        //initShapeRender();
        initImageRender();
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }

    private void initShapeRender(){
        List<BaseShape> baseShapes=new ArrayList<>();
        // baseShapes.add(new Circle30ES());
        //baseShapes.add(new Square30ES());
        baseShapes.add(new Triangle30ES());
        renderer = new ShapeRenderer(baseShapes);
    }
    private void initImageRender(){
        ImageBaseFilter imageBaseFilter=new ImageBaseFilter(getContext(),R.mipmap.ic_launcher);
        renderer = new ImageRenderer(imageBaseFilter);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        renderer.destroyed();
    }
}
