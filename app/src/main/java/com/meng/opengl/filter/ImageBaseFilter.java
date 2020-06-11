package com.meng.opengl.filter;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.meng.opengl.BaseInterface;
import com.meng.opengl.OpenGLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ImageBaseFilter implements BaseInterface {
    public final static String VERTEX_SHADER_CODE_30_ES=
            "#version 300 es                          \n"+
            //变换矩阵 统一变量
            "uniform mat4 uMVPMatrix;\n"+
            "layout(location = 0) in vec4 vPosition;  \n"+
            "layout(location = 1) in vec2 vCoordinate;    \n"+
            "out vec2 aCoordinate;\n" +
            "void main()                              \n"+
            "{                                        \n"+
            "   gl_Position =uMVPMatrix * vPosition;              \n"+
            "   aCoordinate = vCoordinate;                   \n"+
            "}                                        \n";

    public final static String FRAGMENT_SHADER_CODE_30_ES=
            "#version 300 es                              \n"+
            "precision mediump float;                     \n"+
            "uniform sampler2D vTexture;                 \n" +
            "in vec2 aCoordinate;                         \n" +
            "out vec4 fragColor;                          \n"+
            "void main()                                  \n"+
            "{                                            \n"+
            "   fragColor = texture(vTexture, aCoordinate);\n"+
            "}                                            \n";
    public String vertexShaderCode;
    public String fragmentShaderCode;
    public FloatBuffer vertexBuffer;
    public FloatBuffer fragmentBuffer;
    public int mProgram;
    public int uMatrixLocation;
    public int vTextureLocation;
    public int vTextureId;
    public int positionHandle;
    public int coordinateHandle;
    public final float[] mMatrix = new float[16];
    public static final int COORDS_SIZE= 2;
    private int inputWidth;
    private int inputHeight;
    private final Context context;
    private final int rId;
    private static final float vertexCoord[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };
    private static final float fragmentCoord[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };


    public ImageBaseFilter(Context context, int rId) {
        this.context = context;
        this.rId = rId;
    }

    public void onInit(){
        vertexShaderCode=VERTEX_SHADER_CODE_30_ES;
        fragmentShaderCode=FRAGMENT_SHADER_CODE_30_ES;
        vertexBuffer=setFloatBuffer(vertexCoord);
        fragmentBuffer=setFloatBuffer(fragmentCoord);
    }

    @Override
    public void init() {
        onInit();
        mProgram= OpenGLUtil.compile(vertexShaderCode,fragmentShaderCode);
        // get handle to vertex shader's vPosition member
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        // get handle to fragment shader's vColor member
        coordinateHandle = GLES30.glGetAttribLocation(mProgram, "vCoordinate");
        uMatrixLocation= GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        vTextureLocation= GLES30.glGetUniformLocation(mProgram, "vTexture");
        vTextureId=OpenGLUtil.loadTexture(context,rId);
    }

    @Override
    public void initWindow(int width, int height){
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            //横屏
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            //竖屏
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        this.inputWidth=width;
        this.inputHeight=height;
    }

    @Override
    public void draw() {
        GLES20.glViewport(0, 0, inputWidth, inputHeight);
        GLES30.glUseProgram(mProgram);
        /**引用矩阵 */
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
        /**引用顶点坐标点 */
        GLES30.glVertexAttribPointer(positionHandle, COORDS_SIZE, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(positionHandle);
        /**引用片元坐标点 */
        GLES30.glVertexAttribPointer(coordinateHandle, COORDS_SIZE, GLES30.GL_FLOAT, false, 0, fragmentBuffer);
        GLES30.glEnableVertexAttribArray(coordinateHandle);
        /**引用贴图 */
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, vTextureId);
        GLES30.glUniform1i(vTextureLocation, 0);
        /**绘制 */
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        /**解绑 */
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(coordinateHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void destroy() {
        GLES30.glDeleteProgram(mProgram);
    }

    public FloatBuffer setFloatBuffer(float[] data){
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = bb.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }
}
