package com.meng.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;

public class OpenGLUtil {
    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
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

    public static int loadTexture(Context context, int resourceId) {
        int[] textureIds = new int[1];
        //创建一个纹理对象
        GLES30.glGenTextures(1, textureIds, 0);
        if (textureIds[0] == 0) {
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        //这里需要加载原图未经缩放的数据
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            GLES30.glDeleteTextures(1, textureIds, 0);
            return 0;
        }
        // 绑定纹理到OpenGL
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0]);
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
        //根据以上指定的参数，生成一个2D纹理
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        // 生成MIP贴图
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        // 数据如果已经被加载进OpenGL,则可以回收该bitmap
        bitmap.recycle();
        // 取消绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return textureIds[0];
    }
}
