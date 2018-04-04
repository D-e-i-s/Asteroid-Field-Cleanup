package spollina.com.Asteroid_Field_Cleanup_spollina;

import android.widget.ImageView;

public class Asteroid
{

    float posX, posY;
    double directionX, directionY;
    float speed;
    ImageView view;

    // Default constructor
    public Asteroid()
    {}

    public Asteroid(ImageView view, float posX, float posY, double directionX, double directionY, float speed)
    {
        this.posX = posX;
        this.posY = posY;
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;
        this.view = view;
    }
}
