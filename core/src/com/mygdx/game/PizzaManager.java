package com.mygdx.game;

//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Aaron Weaver on 7/6/2015.
 */

//////////////////////////////////////////////
// Manages pizzas

public class PizzaManager
{
    private Array<Pizza> PizzaList;// list of all pizzas active and inactive
    private int xPos,yPos;
    private int xGrid;
    private int MAX_PIZZA_LINE=5; //how many in a line across the screen
    private int MAX_PIZZA=12*4; //total amount of pizzas 12 across 4 deep
    private int xLimit;
    private int xStart;
    private int yStart;
    private SpriteList spriteList;

    public PizzaManager()
    {
        PizzaList = new Array<Pizza>();
    }

    public void setSpritelist(SpriteList sspriteList)
    {
        spriteList=sspriteList;
    }

    public void fillist()
    {
        int a;
        Pizza pizza;

        // MathUtils.random(0, 800 - 64)

        for(a=0;a<MAX_PIZZA;a++)
        {
            pizza= new Pizza();

            pizza.setPizza(0, 0,
                    0,
                    0);

            pizza.setActive(false);
            PizzaList.add(pizza);
        }

    }

    /*

     */
    public void addPizza()
    {
        int a;
        int cnt=0;
        Pizza pizza;
        int sX;
        int spritetype;

        // MathUtils.random(0, 800 - 64)
        // after 30 game ticks, add a new line of pizzas

        sX=(int)GameDimensions.GameAreaStartx;

        for(a=0;a<MAX_PIZZA_LINE;a++)//MAX_CASH_LINE
        {

            pizza=getPizza();

            if(pizza!=null)
            {
                spritetype=MathUtils.random(SpriteList.PLAIN_SPRITE_ID, SpriteList.VEGGIE_SPRITE_ID);

                pizza.setPizza(sX, (int) GameDimensions.GameAreaStarty,
                        spritetype,
                        MathUtils.random(2, 5));

                pizza.setActive(true);
                pizza.setSprite(spriteList.getSprite(spritetype));

                sX=sX+GameDimensions.xGridsize;
            }
        }
    }

    private Pizza getPizza()
    {
        int a;

        for (a=0;a<MAX_PIZZA;a++)
        {
            if(PizzaList.get(a).getActive()==false)
                return(PizzaList.get(a));
        }

        return null;
    }

    public void draw(SpriteBatch batch)
    {
        int a;
        Pizza tMoney;

        for (a=0;a<MAX_PIZZA;a++)
        {
            if(PizzaList.get(a).getActive()==true)
            {
                tMoney=PizzaList.get(a);
                tMoney.draw(batch);
            }
        }
    }

    public void update()
    {
        int a;
        Pizza tpizza;

        for (a=0;a<MAX_PIZZA;a++)
        {
            if(PizzaList.get(a).getActive()==true)
            {
                PizzaList.get(a).update();
                //tpizza.update();
            }
        }
    }

    public Pizza checkForTouchedPizza(int Touchx, int Touchy)
    {
        int a;
        Pizza tpizza;

        for (a=0;a<MAX_PIZZA;a++)
        {
            tpizza=PizzaList.get(a);
            if(tpizza.getActive())
            {
                if(!tpizza.getTouched())//if touched, ignore
                {

                    if(tpizza.checkForTouch(Touchx, Touchy)!=null)
                        return(tpizza);

                    if(tpizza.getTouched())//stop searching for matches
                        return(null);
                }

            }
        }

        return(null);
    }


    int getPizzaVal(int type)
    {
        int val;

        switch(type)
        {
            case 1:
                val=30;
            break;

            case 2:
                val=40;
                break;
            case 3:
                val=50;
                break;
            case 4:
                val=60;
                break;

            case 5:
                val=70;
                break;

            default:
                val=0;
                break;
        }
        return(val);
    }
}//end of MoneyManager
