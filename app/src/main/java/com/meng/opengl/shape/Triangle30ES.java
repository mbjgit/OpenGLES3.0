package com.meng.opengl.shape;

import android.opengl.GLES30;
import android.util.Log;

import com.meng.opengl.OpenGLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle30ES extends BaseShape{

    public Triangle30ES() {
        super();
    }


    @Override
    protected FloatBuffer getVertexBuffer() {
        // number of coordinates per vertex in this array
        float triangleCoords[] = {   // in counterclockwise order:
                0.0f,  0.522008459f, 0.0f, // top
                -0.6f, -0.511004243f, 0.0f, // bottom left
                0.6f, -0.511004243f, 0.0f  // bottom right
        };
        vertexCount=triangleCoords.length / COORDS_PER_VERTEX;
        return setFloatBuffer(triangleCoords);
    }

    @Override
    protected FloatBuffer getFragmentBuffer() {
        // Set color with red, green, blue and alpha (opacity) values
        float[]  color=new float[]{ 0.43671875f, 0.73671875f, 0.73671875f, 0.3f,
                0.43671875f, 0.73671875f, 0.73671875f, 0.3f ,
                0.43671875f, 0.73671875f, 0.73671875f, 0.3f ,
                0.43671875f, 0.73671875f, 0.73671875f, 0.3f };
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
        GLES30.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(positionHandle);

        GLES30.glVertexAttribPointer(colorHandle, COORDS_PER_FRAGMENT, GLES30.GL_FLOAT, false, 0, fragmentBuffer);
        GLES30.glEnableVertexAttribArray(colorHandle);
        // Draw the triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount);
        // Disable vertex array
        GLES30.glDisableVertexAttribArray(positionHandle);
    }

    @Override
    public void destroy() {
        GLES30.glDeleteProgram(mProgram);
    }
}

