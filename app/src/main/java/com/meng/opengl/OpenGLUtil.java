package com.meng.opengl;

import android.opengl.GLES30;
import android.util.Log;

public class OpenGLUtil {
    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);
        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
        Log.d(OpenGLUtil.class.getName(),"compiled[0]:"+compiled[0]+" shaderCode :"+
                shaderCode);
        //容错判断
        if (compiled[0] == 0) {
            return 0;
        }
        return shader;
    }

    public static int compile(String vertexShaderCode,String fragmentShaderCode){
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        // create empty OpenGL ES Program
       int mProgram = GLES30.glCreateProgram();
        // add the vertex shader to program
        GLES30.glAttachShader(mProgram, vertexShader);
        // add the fragment shader to program
        GLES30.glAttachShader(mProgram, fragmentShader);
        // creates OpenGL ES program executables
        GLES30.glLinkProgram(mProgram);
        //删除已链接后的着色器
        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);
        return mProgram;
    }
}
