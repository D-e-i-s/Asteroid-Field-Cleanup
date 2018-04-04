package spollina.com.Asteroid_Field_Cleanup_spollina;

import android.widget.ImageView;

public class LaserBlast
{
    float posX, posY;
    double directionX, directionY;
    float speed;
    ImageView view;
    boolean active;

    public LaserBlast()
    {}

    public LaserBlast(ImageView view, float posX, float posY, double directionX, double directionY, float speed)
    {
        this.posX = posX;
        this.posY = posY;
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;
        this.view = view;
    }

    public void setView(ImageView view)
    {
        this.view = view;
    }


}
