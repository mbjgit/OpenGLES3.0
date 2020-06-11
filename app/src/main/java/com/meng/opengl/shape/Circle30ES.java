package com.meng.opengl.shape;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.meng.opengl.OpenGLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Circle30ES extends BaseShape{

    public Circle30ES() {
        super();
    }

    @Override
    protected FloatBuffer getVertexBuffer() {
        List<Float> data=new ArrayList<>();
        //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        data.add(0.0f);
        //分成100条边，绘制出来应该很像圆了
        float radius = 0.5f; //半径1
        float angDegSpan=360f/360; //依次递加的角度
        for(float i=0;i<=360;i+=angDegSpan){
            data.add((float)(radius*Math.cos(i*Math.PI/180f))); //x
            data.add((float) (radius*Math.sin(i*Math.PI/180f))); //y
            data.add(0.0f); //z
        }
        float[] circleCoods=new float[data.size()];
        for (int i=0;i<circleCoods.length;i++){
            circleCoods[i]=data.get(i);
        }
        vertexCount = circleCoods.length / COORDS_PER_VERTEX;
        return setFloatBuffer(circleCoods);
    }

    @Override
    protected FloatBuffer getFragmentBuffer() {
        float[]  color=new float[]{ 1f, 1f,0f, 0f};
        return setFloatBuffer(color);
    }

    @Override
    protected String getVertexShaderCode() {
        return  VERTEX_SHADER_CODE_30_ES;
    }

    @Override
    protected String getFragmentShaderCode() {
        return  FRAGMENT_SHADER_CODE_30_ES;
    }

    @Override
    public void init() {
        mProgram= OpenGLUtil.compile(vertexShaderCode,fragmentShaderCode);
        // get handle to vertex shader's vPosition member
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        // get handle to fragment shader's vColor member
        colorHandle = GLES30.glGetAttribLocation(mProgram, "vColor");
        uMatrixLocation= GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    @Override
    public void draw() {
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(colorHandle);
        GLES30.glVertexAttribPointer(colorHandle, COORDS_PER_FRAGMENT, GLES30.GL_FLOAT, false, 0, fragmentBuffer);
        // Draw the triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN , 0, vertexCount);
        // Disable vertex array
        GLES30.glDisableVertexAttribArray(positionHandle);
    }

    @Override
    public void destroy() {
        GLES30.glDeleteProgram(mProgram);
    }
}
