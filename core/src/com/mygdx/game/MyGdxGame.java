package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;


public class MyGdxGame extends ApplicationAdapter implements InputProcessor
{

	private SpriteBatch batch;
	private Texture img;
	private BitmapFont font;
	private int width,height;
	private Sprite sprite;
	private float posX, posY;
	private DebugSender dbgsender;
	private SpriteList spriteList;
	private PizzaManager pizzaManager;
	private int newCashLine=0;
	private RatManager ratlist;
	private Sound wavSound;
	private Pizza currPizza;
	private int matchCnt=0;
	private String scoreString;
	private int score=0;

    public GameDimensions gameDimensions;


/*
       For now the screen is set up x 0 is to the right
       y 0 is at the bottom

       Opengl works from the lower left, 0,0 is at the lower left corner

       next
       https://github.com/libgdx/libgdx/wiki/Extending%20the%20Simple%20Game
    */

/*
Adding sounds

Sound wavSound = Gdx.audio.newSound(Gdx.files.internal("data/wav.wav"));

wavSound.play();
wavSound.play(0.5f);


 */

	@Override
	public void create ()
	{
        Gdx.input.setInputProcessor(this);

        dbgsender=new DebugSender();
       dbgsender.findDbgIP();//fix later
        dbgsender.setDebugMode(true);

        gameInit();
	}

    private void gameInit()
    {
        calcBounds();
        createObjects();
        scoreString="Score: ";

    }

    private void calcBounds()
    {
        //y starts at the bottom at 0, moves up to screen limit

        GameDimensions.ScreenWidth=Gdx.graphics.getWidth();
        GameDimensions.ScreenHeight = Gdx.graphics.getHeight();

        GameDimensions.TextStarty=GameDimensions.ScreenHeight;
        GameDimensions.TextStartx=5.0f;
        GameDimensions.Topmargin=78;//size of slice, origin is on the bottom

        GameDimensions.GroundStarty= GameDimensions.Bottommargin;

        GameDimensions.GameAreaEndy= GameDimensions.GroundStarty;
        GameDimensions.GameAreaStartx=GameDimensions.Leftmargin;
        GameDimensions.GameAreaStarty=GameDimensions.ScreenHeight- GameDimensions.Topmargin;
        GameDimensions.Rightmargin=GameDimensions.ScreenWidth-GameDimensions.Rightmargin;

        GameDimensions.xGridsize=88;//62
        GameDimensions.yGridsize=78;//62

        //calc how many units fit on the screen horz
        GameDimensions.xUnits=(int) (GameDimensions.ScreenWidth-(GameDimensions.Leftmargin+GameDimensions.Rightmargin))/GameDimensions.xGridsize;
    }

    private void createObjects()
    {
        batch = new SpriteBatch();
        spriteList= new SpriteList();

        pizzaManager= new PizzaManager();
        pizzaManager.setSpritelist(spriteList);
        pizzaManager.fillist();

        pizzaManager.addPizza();//add a line of money

        ratlist= new RatManager();
        ratlist.setSpriteList(spriteList);
        wavSound = Gdx.audio.newSound(Gdx.files.internal("MoneyPickup.wav"));

        font = new BitmapFont();

        //30 times a second we update the status
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                update();
            }
        }
                , 0, 1 / 30.0f);

    }

	@Override
	public void render ()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		font.draw(batch, scoreString + score, GameDimensions.TextStartx, GameDimensions.TextStarty);

		//sprite.draw(batch);
        pizzaManager.draw(batch);
        ratlist.draw(batch);

		batch.end();
	}

    public void update()
    {
        doUpdate();
    }

    private void doUpdate()
    {

        pizzaManager.update();
        ratlist.update();

        newCashLine++;
        if(newCashLine>=30)
        {
            pizzaManager.addPizza();
            newCashLine=0;
        }
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        font.dispose();
        wavSound.dispose();
    }

    @Override
    public void resize(int swidth, int sheight)
    {
        width=swidth;
        height=sheight;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    void playfirstPickup()
    {
        wavSound.play(0.10f);
    }

    void playSecondPickup()
    {
        wavSound.play(0.50f);
    }

    void playlastPickup()
    {
        wavSound.play();
    }


	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        boolean bst=false;
        Pizza tpizza;

        if(button == Input.Buttons.LEFT)
        {
            // posX = screenX - sprite.getWidth()/2;
            // posY = Gdx.graphics.getHeight() - screenY - sprite.getHeight()/2;
            //dbgsender.sendMessage("left ScreenX " +screenX +" Screen Y: "+screenY +" Leftbutton ptr: "+pointer);

            tpizza= pizzaManager.checkForTouchedPizza(screenX,
                    ((int)GameDimensions.ScreenHeight-screenY));//touch origin is different than game origin

            if(tpizza!=null)
            {


                if(matchCnt==0 )
                {
                    matchCnt++;
                    playfirstPickup();
                    currPizza=tpizza;
                }
                else
                {
                    if(currPizza.getType()== tpizza.getType())
                    {
                        matchCnt++;

                        if(matchCnt==2)
                        {
                            playSecondPickup();
                        }


                        if(matchCnt==3)
                        {
                            playlastPickup();
                            score=score + pizzaManager.getPizzaVal(currPizza.getType());
                            matchCnt=0;
                            Gdx.input.vibrate(1000);

                            //pattern vibrate
                            //Gdx.input.vibrate(new long[] { 0, 200, 200, 200}, -1);  -1 no repeat
                        }

                    }
                    else
                    {
                        matchCnt=1;
                        currPizza=tpizza;
                        playfirstPickup();
                    }

                }
            }



            if(bst)
                wavSound.play();
        }

        if(button == Input.Buttons.RIGHT)
        {
            //posX = Gdx.graphics.getWidth()/2 - sprite.getWidth()/2;
            //posY = Gdx.graphics.getHeight()/2 - sprite.getHeight()/2;
           // dbgsender.sendMessage("Right button ptr: "+pointer);

            //dbgsender.sendMessage("right ScreenX " +screenX +"Screen Y: "+screenY +"right button ptr: "+pointer);
        }

        return false;

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}//end of MyGdxGame
