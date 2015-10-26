package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Aaron Weaver on 7/4/2015.
 */

public class Pizza
{
    private int x;
    private int y;
    private int type;
    private int yInc;
    private boolean bActive;
    private Sprite sprite;
    private boolean bTouched;
    private DebugSender dbgsender;


    public static float yLimit;

    public Pizza()
    {
        dbgsender=new DebugSender();
       dbgsender.findDbgIP();
        dbgsender.setDebugMode(true);
    }

    public void setPizza(int sX, int sY,int sType,int sInc)
    {
        x=sX;
        y=sY;
        type=sType;
        yInc=sInc;
        bTouched=false;
    }

    public void update()
    {
        moveDown();
    }

    private void setTouched()
    {
        bTouched=true;
    }

    public boolean getTouched()
    {
        return(bTouched);
    }

    public int getType()
    {
        return(type);
    }

    private void moveDown()
    {
        if(bTouched)//if touched,just stand in place
            return;

        y=y-yInc;

       if(y-GameDimensions.yGridsize <= GameDimensions.GroundStarty)
       {
           bActive=false;
       }
    }

    public void setActive(boolean sActive)
    {
        bActive= sActive;
    }

    public boolean getActive()
    {
        return(bActive);
    }

    public void draw(SpriteBatch sBatch)
    {
        //sprite.draw(batch);

        sprite.setPosition(x,y);
        sprite.draw(sBatch);
    }

    public void setSprite(Sprite sSprite)
    {
        sprite=  sSprite;
    }

    public Pizza checkForTouch(int Tx, int Ty)
    {
        if (Math.abs(Tx-x) <GameDimensions.xGridsize && Math.abs(Ty-y) <GameDimensions.yGridsize )
        {
            setTouched();
            bActive=false;
          // dbgsender.sendMessage("Hit Mon x " +x +" Money y: "+y +" Tx "+ Tx+" Ty "+ Ty );
            return(this);
        }
        else
        {
           // dbgsender.sendMessage("Miss Mon x " +x +" Money y: "+y +" Tx "+ Tx+" Ty "+ Ty );
            return(null);
        }

    }
}
