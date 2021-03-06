package org.yourorghere;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * * GLRenderer.java <BR> * author: Brian Paul (converted to Java by Ron Cemer
 * and Sven Goethel)
 * <P>
 *  * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class GLRenderer implements GLEventListener {

    float rotation = 150f;
    float direction = 20;
    float putar = 0;
    float Cx = 0, Cy = 0, Cz = 0;
    float Lx = 0f, Ly = 0f, Lz = 0f;
    boolean bergerak = false;
    float angle_kanan = 150f;
    float angle_kiri = 150f;

    //class vector untuk memudah vektor-isasi   
    private class vector {

        float x;
        float y;
        float z;

        public vector(float startX, float startY, float startZ) {
            x = startX;
            y = startY;
            z = startZ;
        }
    }
    vector lineal = new vector(0f, 0f, 1f);//deklarasi awal vektor untuk maju
    vector lineal1 = new vector(0f, 0f, -1f);
    vector lateral = new vector(1f, 0f, 0f);//deklarasi awal vektor untuk gerakan ke kanan 
    vector lateral1 = new vector(-1f, 0f, 0f);
    vector vertical = new vector(0f, 1f, 0f);//deklarasi awal vetor untuk gerakan naik          /*        ini adalah metod untuk melakukan pergerakan.        magnitude adalah besarnya gerakan sedangkan direction digunakan untuk menentukan arah.        gunakan -1 untuk arah berlawanan dengan vektor awal     */       
    vector vertical1 = new vector(0f, -1f, 0f);

    private void vectorMovement(vector toMove, float magnitude, float direction) {
        float speedX = toMove.x * magnitude * direction;
        float speedY = toMove.y * magnitude * direction;
        float speedZ = toMove.z * magnitude * direction;
        Cx += speedX;
        Cy += speedY;
        Cz += speedZ;
        Lx += speedX;
        Ly += speedY;
        Lz += speedZ;
    }

    public void init(GLAutoDrawable drawable) {
// Use debug pipeline  
        // drawable.setGL(new DebugGL(drawable.getGL())); 

        Cx = 0;
        Cy = 0;
        Cz = 5;
        Lx = 0;
        Ly = 0;
        Lz = -20;
        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync       
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode 
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.     
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glEnable(GL.GL_NORMALIZE);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL gl = drawable.getGL();
        GLU glu = new GLU();
        if (height <= 0) { // avoid a divide by zero error!    
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    public void display(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();
        GLU glu = new GLU();
        // Clear the drawing area     
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"   
        gl.glLoadIdentity();

        // Move the "drawing cursor" around   
        glu.gluLookAt(Cx, Cy, Cz,
                Lx, Ly, Lz,
                0, 1, 0);

        gl.glTranslatef(0f, -2f, -10f);

        //BOX
        gl.glPushMatrix();
        Objek.Box(gl);
        gl.glPopMatrix();
        //Orang
        gl.glPushMatrix();
        Objek.orang(gl);
        gl.glPopMatrix();

           //ROTASI TANGAN
        if (bergerak) {
            rotation += direction;
            if (rotation >= 220 || rotation <= 140) {
                direction = -direction;
            }
        }
            //TANGAN KIRI
        gl.glPushMatrix();
        gl.glTranslatef(2.8f, 5.5f, -1f);
        gl.glRotatef(rotation, 0, 0, -1);
        gl.glRotatef(90, 0, 1, 0);
        Objek.tangan(gl);
        gl.glPopMatrix();

        //TANGAN KANAN
        
        gl.glPushMatrix();
        gl.glTranslatef(3.2f, 5.5f, -1f);

        gl.glRotatef(rotation, 0, 0, 1);
        gl.glRotatef(90, 0, -1, 0);
        Objek.tangan(gl);
        gl.glPopMatrix();

        //gear besar
        gl.glPushMatrix();
        putar += 15f;
        gl.glTranslatef(-3, 3.5f, 0f);
        gl.glRotatef(putar, 0, -1, 0);
        Objek.Gear1(gl);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(0, 3.5f, 0f);
        gl.glRotatef(putar, 0, 1, 0);
        Objek.Gear2(gl);
        gl.glPopMatrix();

        // Flush all drawing operations to the graphics card
        gl.glFlush();

    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void Key_Pressed(int keyCode) {

        //huruf w    
        if (keyCode == 87) {
            vectorMovement(lineal, 2f, -1f);
        } //huruf a    
        else if (keyCode == 65) {
            vectorMovement(lateral1, 2f, -1f);
        } //huruf s 
        else if (keyCode == 83) {
            vectorMovement(lineal1, 2f, -1f);
        } //huruf d 
        else if (keyCode == 68) {
            vectorMovement(lateral, 2f, -1f);
        }//panah atas      
        else if (keyCode == 38) {
            vectorMovement(vertical, 2f, -1f);
        } // panah bawah
        else if (keyCode == 40) {
            vectorMovement(vertical1, 2f, -1f);
        }//panah kanan
        else if (keyCode == 39) {
            vectorMovement(lateral, 2f, -1f);
        } //panah kiri 
        else if (keyCode == 37) {
            vectorMovement(lateral1, 2f, -1f);
        }//rotasi berputar
        else if (keyCode == 82) {
            if (bergerak) {
                bergerak = false;
            } else {
                bergerak = true;
            }
        } // huruf b 
        else if (keyCode == 66) {

            Cx = 0;
            Cy = 15;
            Cz = 0;
            Lx = 0;
            Ly = -20;
            Lz = -20;

            //huruf h
        } else if (keyCode == 72) {
            Cx = 0;
            Cy = 0;
            Cz = 5;
            Lx = 0;
            Ly = 0;
            Lz = -20;
        } else {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.     
        }
    }

}
