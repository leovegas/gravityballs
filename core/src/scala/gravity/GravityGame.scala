package gravity

import java.util.Random

import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.graphics.{Color, GL20, Texture}
import com.badlogic.gdx.physics.box2d.{Box2DDebugRenderer, World}
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Skin}
import com.badlogic.gdx.scenes.scene2d.{Actor, Stage}
import com.badlogic.gdx.utils.viewport.{ScreenViewport, StretchViewport}
import com.badlogic.gdx._
import gravity.{FigureRedBall, GameContactListener}
import scala.collection.mutable.ListBuffer

object GravityGame {
  var ballNbr:Int = 0
  var ballsOut:Int = 0
  var ballsCatch:Int = 0
  var Fig1Nbr:Int = 0
  var FigNbr:Int = 0
  var points:Int = 0
  var lineN:Int = 1
  var ballOut:Boolean = false
  var nextR:Boolean = false
  var gameover:Boolean = false
  var figurescount:Int = 0

}

class GravityGame extends ApplicationAdapter{
  var batch: SpriteBatch = _
  private var world:World = _
  private var stage:Stage = _
  private var stage2:Stage = _
  private var debugRenderer:Box2DDebugRenderer = _
  private var count:Int = _
  private var gamePause:Boolean = false
  var i = 0
  var r = 0
  var livesRandom = 3
  var rand:Random = new Random()
  var bc:Int = 0
  var f:Int = 0
  var label: CustomLabel = _
  var label2: CustomLabel = _
  var label3: Label = _
  var Balls: ListBuffer[MainBall] = _
  var defrostTime:Long = _
  var skin: Skin = _
  var line:ListBuffer[Figure] = _
  var journal:Array[Boolean] = _
  lazy val menuScreen = new MainMenuScreen(batch,stage2)
  var backgroundTexture: Texture = _
  var backgroundSprite: Sprite = _
  var timeSeconds:Float = 0f
  var period:Float = 0.1f
  var startShoot:Boolean = false
  var scrX:Float = _
  var scrY:Float = _
  var iterator:Int = -1
  var tempArray: Array[Actor] = _

  import com.badlogic.gdx.math.Vector2

  def miniFunc(num: Int): Unit = {
    journal = Array.fill(num)(false)
    for (i <- 0 until num)
      journal(i) = rand.nextBoolean()
  }
  miniFunc(11)

    override def create(): Unit = {
    Gdx.app.setLogLevel(Application.LOG_DEBUG)
    count = 0
    world = new World(new Vector2(0, -6f), true)
    world.setContactListener(new GameContactListener)
    batch = new SpriteBatch
      Gdx.input.setInputProcessor(stage)
    val ratio = Gdx.graphics.getWidth.toFloat / Gdx.graphics.getHeight.toFloat
    stage = new Stage(new ScreenViewport)
    stage.getCamera.position.set(0, 0, 11)
    stage.getCamera.lookAt(0, 0, 0)
    stage.getCamera.viewportWidth = 11
    stage.getCamera.viewportHeight = 11 / ratio

    stage2 = new Stage(new StretchViewport(1300, 900))

      debugRenderer = new Box2DDebugRenderer

      Balls = new ListBuffer[MainBall]

      rand = new Random()

      skin = new Skin(Gdx.files.internal("android/assets/gravity/skin"))
      label = new CustomLabel(GravityGame.points.toString, skin,true)
      label.setColor(Color.GREEN)
      label.setPosition(50,Gdx.graphics.getHeight-50)
      label.setScale(2f)
      label.toFront()
      label.setName("label1")
      stage2.addActor(label)
      label2 = new CustomLabel(GravityGame.ballNbr.toString, skin,false)
      label2.setColor(Color.GREEN)
      label2.setPosition(10,Gdx.graphics.getHeight-50)
      label2.setScale(2f)
      label2.toFront()
      label2.setName("label2")
      stage2.addActor(label2)

      backgroundTexture = new Texture("android/assets/gravity/background.png")
      backgroundSprite = new Sprite(backgroundTexture)

      val podium = new Limits(world, 0, 0, 11.2f, 8f)

      stage.addActor(podium)

      new WindowFrame(world, stage.getCamera.viewportWidth, stage.getCamera.viewportHeight)

      generateBall()
      tempArray = stage.getActors.items.array.filter(_ != null).filter(_.getName == "ball1")

      GravityGame.ballNbr+=1
      generateLine(livesRandom)

      r = rand.nextInt(3)
    }

  override def render(): Unit = {
    if (gamePause) {
    }
    else {
      Gdx.gl.glClearColor(0, 0.1f, 0.1f, 1)
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
      stage2.getViewport.apply()
      stage.act()
      stage2.act()
      stage2.getBatch.begin()
      stage2.getBatch.draw(backgroundSprite, -3, -3, 1300,900)
      stage2.getBatch.end()
      stage.draw()
      stage2.draw()
    }

   //debugRenderer.render(world, stage.getCamera.combined);
   // world.step(Gdx.graphics.getDeltaTime, 4, 4)
    world.step(1f/60f, 6, 2)  //android

    if (GravityGame.nextR) {
      nextRound()
    }

    if (GravityGame.ballOut) {
     // timeSeconds=0
      iterator = -1
      startShoot=false

      val arrow = new Arrow(world, 0, 3.5f)
      arrow.setName("arrow")
      stage.addActor(arrow)

      for (i <- 1 to GravityGame.ballNbr) {
        generateBall()
        tempArray = stage.getActors.items.array.filter(_ != null).filter(_.getName == "ball1")
      }
      GravityGame.ballOut=false
    }

    timeSeconds += Gdx.graphics.getRawDeltaTime
    if (timeSeconds > period) {
      timeSeconds-=period
      if (startShoot) {
        iterator += 1
        if (tempArray.length > iterator) {
        tempArray(iterator).asInstanceOf[MainBall].defrost(scrX, scrY)
        println("impulseBall+")
      }
      }
    }

    for (label <- stage2.getActors.items.array.filter(_ != null).filter(_.getName == "label1")) {
      label.asInstanceOf[CustomLabel].updateText(GravityGame.points.toString)
    }
    for (label <- stage2.getActors.items.array.filter(_ != null).filter(_.getName == "label2")) {
      label.asInstanceOf[CustomLabel].updateText(GravityGame.ballNbr.toString)
    }
    for (ball <- stage.getActors.items.array.filter(_ != null).filter(_.getName == "ball1")) {
      if (ball.asInstanceOf[MainBall].body.getLinearVelocity.x==0.0&&ball.asInstanceOf[MainBall].body.getLinearVelocity.y==0.0) {
        ball.asInstanceOf[MainBall].body.applyLinearImpulse(new Vector2(5.0f,0f), ball.asInstanceOf[MainBall].body.getWorldCenter, true)
      }
    }

      Gdx.input.setInputProcessor(new InputProcessor {
      override def keyDown(keycode: Int): Boolean = false

      override def keyUp(keycode: Int): Boolean = false

      override def keyTyped(character: Char): Boolean = false

        override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
          if (button == Input.Buttons.LEFT) {
            if (!startShoot) {
              scrX = screenX
              scrY = screenY
            }
            startShoot=true
            for (arrow <- stage.getActors.items.array.filter(_ != null).filter(_.getName == "arrow")) {
              arrow.asInstanceOf[Arrow].eliminate()
            }
          }
          false
        }

      override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

      override def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

      override def mouseMoved(screenX: Int, screenY: Int): Boolean = {
          for (ball <- stage.getActors.items.array.filter(_ != null).filter(_.getName == "ball1")) {
            ball.asInstanceOf[MainBall].followMouse(screenX, screenY)
          }

        for (arrow <- stage.getActors.items.array.filter(_ != null).filter(_.getName == "arrow")) {
          arrow.asInstanceOf[Arrow].followMouse(screenX,screenY)
        }
        false
      }

      override def scrolled(amount: Int): Boolean = {
        for (ball <- stage.getActors.items.array.filter(_ != null).filter(_.getName == "ball1")) {ball.asInstanceOf[MainBall].eliminate()
        }
        timeSeconds = 0
        iterator = -1

        for (ball <- stage.getActors.items.array.filter(_ != null).filter(_.getName == "ball1")) {
          println(ball.asInstanceOf[MainBall].body.getLinearVelocity)
        }
        false
      }
    })
    }

  def menu(): Unit ={
   // setScreen(menuScreen)
    GravityGame.gameover=false
  }

  private def nextRound(): Unit = {
    livesRandom+=1
    miniFunc(11)
    moveUp()
    generateLine(livesRandom)
    GravityGame.nextR=false
  }

  override def dispose(): Unit = {
    batch.dispose()
    stage.dispose()
    stage2.dispose()
    skin.dispose()
  }

  private def generateBall(): Unit = {
    var i = 0
      val ball = new MainBall(world, 0, 3.5f, new Texture("android/assets/gravity/ball.png"))
      ball.setName("ball1")
      stage.addActor(ball)
    i+=1
    println("Generated "+i+" Ball")
  }


  private def myRand(t: Int): Float = {
    val rand = new Random()
    if (t == 0) {
      var i: Float = rand.nextFloat()*(5.0f+5.0f)-5.0f
     i
    }
    else {
    var i: Float = rand.nextFloat()*(3.25f+3.25f)-3.25f
   i
  }
  }

  private def myRand(): Int = {
    val rand = new Random()
      var i: Int = -5 + (Math.random() * ((5 + 5) + 1)).asInstanceOf[Int]
    i
  }

  def moveUp(): Unit = {
  for (f <- stage.getActors.items.array.filter(_!=null).filter(_.getName=="figure")) {
    f.asInstanceOf[Figure].stepUp()
    if (f.getY>2.5f&&f.getY()<2.0f) {
      GravityGame.gameover=true
      gamePause=true
    }
  }
    for (f <- stage.getActors.items.array.filter(_!=null).filter(_.getName=="redball")) {
      f.asInstanceOf[FigureRedBall].stepUp()
    }
  }

  private def generateLine(livesRandom:Int): Unit = {
    val rand = new Random()
    var posX = myRand()
    journal(posX+5)=false
    if (rand.nextInt(1)==0) {
      var figure: FigureRedBall = new FigureRedBall(world, posX, -3, 0.8f, 0.8f)
      figure.setName("redball")
      stage.addActor(figure)
    }

    for (j <- journal.filter(!_==false)) {
      var x: Int = rand.nextInt(5)
      var str: String =
        x match {
          case 0 => "fig1"
          case 1 => "fig2"
          case 2 => "fig3"
          case 3 => "fig4"
          case 4 => "fig5"
          case 5 => "fig6"
        }
        posX=journal.indexOf(j)-5
        journal(posX + 5) = false
        val figure1 = new Figure(world, posX, -3, 1f, 1f, str,1+rand.nextInt(livesRandom))
        figure1.setName("figure")
        stage.addActor(figure1)
     // println(GravityGame.figurescount)
      GravityGame.figurescount+=1
    }

  }

  private def generateFigure(q:Int): Unit = {
    for (i <- 0 to 5) {
      var figure:FigureRedBall = new FigureRedBall(world, myRand(0), myRand(1), 0.4f, 0.4f)
      figure.setName("redball")
      stage.addActor(figure)
    }

    val rand = new Random()
    for (i <- 0 to q) {
      var x:Int = rand.nextInt(4)
        var str:String =
        x match {
          case 0 => "figure1"
          case 1 => "figure2"
          case 2 => "figure3"
          case 3 => "figure4"
          case 4 => "figure5"
        }
      val figure1 = new Figure(world, myRand(0), myRand(1), 0.6f, 0.6f,str,10)
      figure1.setName("figure")
      stage.addActor(figure1)
    }
  }

  override def resize(width: Int, height: Int): Unit = {
   // stage.getViewport.update(width, height, true)
    stage2.getViewport.update(width, height, true)

  }

  override def pause(): Unit = {}

  override def resume(): Unit = {}
}
