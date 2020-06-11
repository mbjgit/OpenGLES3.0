package com.meng.opengl.shape;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class BaseShape {
    public final static String VERTEX_SHADER_CODE=
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = vPosition;" +
            "}";

    public final static String FRAGMENT_SHADER_CODE=
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";
    public final static String VERTEX_SHADER_CODE_30_ES=
            "#version 300 es                          \n"+
                    //变换矩阵 统一变量
                    "uniform mat4 uMVPMatrix;\n"+
                    "layout(location = 0) in vec4 vPosition;  \n"+
                    "layout (location = 1) in vec4 vColor;    \n"+
                    "out vec4 color;\n" +
                    "void main()                              \n"+
                    "{                                        \n"+
                    "   gl_Position =uMVPMatrix * vPosition;              \n"+
                    "    color = vColor;                   \n"+
                    "}                                        \n";

    public final static String FRAGMENT_SHADER_CODE_30_ES=
            "#version 300 es                              \n"+
                    "precision mediump float;                     \n"+
                    "in vec4 color;                               \n" +
                    "out vec4 fragColor;                          \n"+
                    "void main()                                  \n"+
                    "{                                            \n"+
                    "   fragColor = color;                        \n"+
                    "}                                            \n";
    public static final int COORDS_PER_VERTEX = 3;
    public static final int COORDS_PER_FRAGMENT = 4;
    public int vertexCount;
    public String vertexShaderCode;
    public String fragmentShaderCode;
    public FloatBuffer vertexBuffer;
    public FloatBuffer fragmentBuffer;
    public int mProgram;
    public int uMatrixLocation;
    public int positionHandle;
    public int colorHandle;

    protected abstract FloatBuffer getVertexBuffer();

    protected abstract FloatBuffer getFragmentBuffer();

    protected abstract String getVertexShaderCode();

    protected abstract String getFragmentShaderCode();

    public abstract void init();
    /**
     * int GL_POINTS       //将传入的顶点坐标作为单独的点绘制
     * int GL_LINES        //将传入的坐标作为单独线条绘制，ABCDEFG六个顶点，绘制AB、CD、EF三条线
     * int GL_LINE_STRIP   //将传入的顶点作为折线绘制，ABCD四个顶点，绘制AB、BC、CD三条线
     * int GL_LINE_LOOP    //将传入的顶点作为闭合折线绘制，ABCD四个顶点，绘制AB、BC、CD、DA四条线。
     * int GL_TRIANGLES    //将传入的顶点作为单独的三角形绘制，ABCDEF绘制ABC,DEF两个三角形
     * int GL_TRIANGLE_FAN    //将传入的顶点作为扇面绘制，ABCDEF绘制ABC、ACD、ADE、AEF四个三角形
     * int GL_TRIANGLE_STRIP   //将传入的顶点作为三角条带绘制，ABCDEF绘制ABC,BCD,CDE,DEF四个三角形
     * @return
     */
    public abstract void draw();

    public abstract void destroy();

    public BaseShape() {
        onInit();
    }

    public final float[] mMatrix = new float[16];

    public void onInit() {
        vertexShaderCode = getVertexShaderCode();
        fragmentShaderCode = getFragmentShaderCode();
        vertexBuffer=getVertexBuffer();
        fragmentBuffer=getFragmentBuffer();

    }

    public FloatBuffer setFloatBuffer(float[] data){
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = bb.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
    }

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
    }
}
