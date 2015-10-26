package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Aaron Weaver on 7/20/2015.
 *
 * This manages the rats at the bottom of the screen.
 * At higher difficulty levels these will get longer so there is less space for the pizzas to fall.
 * The game gets faster and harder that way.
 */
public class RatManager
{
    private Array<Integer> handList;
    private int xPos,yPos;
    private int MAX_RATS=5;//12 for landscape
    private SpriteList spriteList;
    private Sprite sprite;


    public RatManager()
    {
        handList = new Array<Integer>();
    }

    public void setSpriteList(SpriteList sspritelist)
    {
        spriteList=  sspritelist;
    }

    public void draw(SpriteBatch sBatch)
    {
        int a,x,y;

        x=(int)GameDimensions.Leftmargin;
        y=(int)GameDimensions.GroundStarty;



        //sprite.draw(batch);
        for (a=0;a<MAX_RATS;a++)
        {

            sprite=spriteList.getSprite(SpriteList.RATHEAD_SPRITE_ID);
            sprite.setPosition(x,y);
            sprite.draw(sBatch);
            x=x+GameDimensions.xGridsize;
        }

    }

    public void update()
    {
        int a;

       //for now do nothing
        //later rats will grow longer

        /*
        for (a=0;a<MAX_HANDS;a++)
        {

        }
        */
    }
}
