package com.meng.opengl.shape;

import android.opengl.GLES30;

import com.meng.opengl.OpenGLUtil;
import com.meng.opengl.shape.BaseShape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square30ES extends BaseShape {
    public Square30ES() {
        super();
    }

    // number of coordinates per vertex in this array
    private static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f,  0.5f, 0.0f , // top right
            0.5f, -0.5f, 0.0f   // bottom right

    };
    // Set color with red, green, blue and alpha (opacity) values
    private int positionHandle;
    private int colorHandle;

    @Override
    protected FloatBuffer getVertexBuffer() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
        vertexCount=squareCoords.length / COORDS_PER_VERTEX;
        return vertexBuffer;
    }

    @Override
    protected FloatBuffer getFragmentBuffer() {
        float[]  fragmentColor=new float[]{ 0.83671875f, 0.26953125f, 0.52265625f, 0.3f,
                0.83671875f, 0.26953125f, 0.52265625f, 0.3f ,
                0.83671875f, 0.26953125f, 0.52265625f, 0.3f ,
                0.83671875f, 0.26953125f, 0.52265625f, 0.3f };
        return setFloatBuffer(fragmentColor);
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
    }

    @Override
    public void draw() {
        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram);
        // Enable a handle to the triangle vertices
        GLES30.glEnableVertexAttribArray(positionHandle);
        // Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                0, vertexBuffer);
        // Enable a handle to the triangle vertices
        GLES30.glEnableVertexAttribArray(colorHandle);
        // Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer(colorHandle, COORDS_PER_FRAGMENT,
                GLES30.GL_FLOAT, false,
                0, fragmentBuffer);
        // Draw the triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP , 0, vertexCount);
        // Disable vertex array
        GLES30.glDisableVertexAttribArray(positionHandle);
    }
    @Override
    public void destroy() {
        GLES30.glDeleteProgram(mProgram);
    }
}

