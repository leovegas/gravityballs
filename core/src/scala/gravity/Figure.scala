package gravity

import java.util.Random

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont}
import com.badlogic.gdx.math.{Vector2, Vector3}
import com.badlogic.gdx.physics.box2d.{Body, BodyDef, FixtureDef, World}
import com.badlogic.gdx.scenes.scene2d.ui.{Image, Skin}
import gravity.libb.BodyEditorLoader2

class Figure(var world: World, val pos_x: Float, val pos_y: Float, val aWidth: Float, val aHeight: Float, typ:String, livesRandom:Int) extends Image(new Texture("android/assets/gravity/"+typ+".png")) {
  var delete: Boolean = _
  var impuls: Boolean = _
  var stUp: Boolean = false
  var renderText: Boolean = false

  import com.badlogic.gdx.graphics.g2d.SpriteBatch

  var batch2:SpriteBatch = new SpriteBatch

  private[this] var _update: Boolean = _

  def update: Boolean = _update

  def update_=(value: Boolean): Unit = {
    _update = value
  }

  private var body: Body = _
  private var angle: Int = _
  private var isAlive: Boolean = true
  var font = new BitmapFont
  var rand = new Random()
  angle = rand.nextInt(360)
  var lives: Int = livesRandom
  //lives = rand.nextInt(livesRandom)
  //lives =1 + (Math.random() * ((5 - 1) + 1)).asInstanceOf[Int]

  //class member variable
  var clickedPoint: Vector2 = _

  //in class constructor
  clickedPoint = new Vector2(0, 20); //initial starting point

  this.setSize(aWidth, aHeight)
  this.setPosition(pos_x, pos_y)
  val loader = new BodyEditorLoader2(Gdx.files.internal("android/assets/gravity/" + typ + ".json"))
  val bd = new BodyDef
  bd.position.set(Gdx.graphics.getWidth / 2, Gdx.graphics.getHeight / 2)
  bd.`type` = BodyDef.BodyType.KinematicBody
  bd.position.x = this.getX
  bd.position.y = this.getY
  body = world.createBody(bd)
  body.setAwake(true)
  // 2. Create a FixtureDef, as usual.
  val fd = new FixtureDef
  fd.density = 1
  fd.friction = 1f
  fd.restitution = 1f
  // 3. Create a Body, as usual.
  val scale: Float = this.getWidth * 0.009f
  loader.attachFixture(body, "Name", fd, scale)
  this.setOrigin(this.getWidth / 2, this.getHeight / 2)
  body.setTransform(bd.position.x,bd.position.y, Math.toRadians(angle).asInstanceOf[Float])
  body.setUserData(this)
 // font = new BitmapFont(Gdx.files.internal("android/assets/gravity/myfont.fnt"), false)
 // font.getData.setScale(0.03f)
  var label2: CustomLabelLives = _
  var skin: Skin = _
  renderText=false
  var i:Int = 0

  import com.badlogic.gdx.graphics.OrthographicCamera

  var cam:OrthographicCamera = new OrthographicCamera
  cam.setToOrtho(false, 1300, 900)
  skin = new Skin(Gdx.files.internal("android/assets/gravity/skin"))
  label2 = new CustomLabelLives(lives.toString, skin)
  label2.setScale(1f)


  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    if (i<10) renderText=true
    super.draw(batch, parentAlpha)
    batch.end()
    cam.update()
    batch2.begin()
    batch2.setProjectionMatrix(cam.combined)
    label2.draw(batch2, parentAlpha)
    batch2.end()
    batch.begin()
    i+=1
  }

  override def act(delta: Float): Unit = {
    super.act(delta)
    cam.update()
    label2.act(delta)

    if (delete) {
      world.destroyBody(body)
      isAlive = false
      this.remove
    }

    if (impuls) {
      body.applyTorque(0.6f, true)
      impuls = false
    }
    if (stUp) {
      body.setTransform(body.getPosition.x, body.getPosition.y+1, Math.toRadians(angle).asInstanceOf[Float])
      renderTex()
      stUp = false
    }

    this.setRotation(angle)
    this.setPosition(body.getPosition.x - this.getWidth / 2, body.getPosition.y - this.getHeight / 2)

    if (renderText) {
      label2 = new CustomLabelLives(lives.toString, skin)
      label2.setScale(1f)
      if (isAlive) {
      val sp3 = getStage.getCamera.project(new Vector3(body.getPosition.x, body.getPosition.y, 0))
      val tp = new Vector2(sp3.x-7, sp3.y-12)
      label2.setPosition(tp.x, tp.y)
    }
      renderText = false
    }

  }

  def eliminate(): Unit = {
    delete = true
    GravityGame.figurescount-=1
  }

  def renderTex(): Unit = {
    renderText=true
  }

  def reduceLives(): Unit = {
    lives-=1
  }
  def spin(): Unit = {
    impuls = true
  }
  def stepUp(): Unit = {
    stUp = true
  }
}
