package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Aaron Weaver on 7/4/2015.
 */
public class SpriteList
{
    static final public int PLAIN_SPRITE_ID=0;
    static final public int CHEESE_SPRITE_ID=1;
    static final public int PEPPERONI_SPRITE_ID=2;
    static final public int MUSHROOM_SPRITE_ID=3;
    static final public int VEGGIE_SPRITE_ID=4;
    static final public int RATHEAD_SPRITE_ID=5;



    private Array<Sprite> SourceSpriteList;

    public SpriteList()
    {
        SourceSpriteList = new Array<Sprite>();
        fillList();
    }

    private void fillList()
    {
        Sprite tSprite;
        Texture img;

        //add dollar
        img = new Texture("Plain.png");
        tSprite = new Sprite(img);
        SourceSpriteList.add(tSprite);

        //add Euro
        img = new Texture("Cheese.png");
        tSprite = new Sprite(img);
        SourceSpriteList.add(tSprite);

        //add pound
        img = new Texture("Pepperoni.png");
        tSprite = new Sprite(img);
        SourceSpriteList.add(tSprite);

        //add yen
        img = new Texture("Mushroom.png");
        tSprite = new Sprite(img);
        SourceSpriteList.add(tSprite);

        //add pink hand
        img = new Texture("Veggie.png");
        tSprite = new Sprite(img);
        SourceSpriteList.add(tSprite);

        //add pink hand
        img = new Texture("EnemyRat.png");
        tSprite = new Sprite(img);
        SourceSpriteList.add(tSprite);
    }

    public Sprite getSprite(int sSprite)
    {
        return(SourceSpriteList.get(sSprite));
    }
}
