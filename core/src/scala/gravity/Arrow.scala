package gravity

import java.util.Random

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.{MathUtils, Vector2, Vector3}
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.scenes.scene2d.ui.Image


class Arrow(var aWorld: World, val pos_x: Float, val pos_y: Float) extends Image(new Texture("android/assets/gravity/arrow2.png")) {
  var delete:Boolean = _
  var body:Body = _
  var world:World = _
  var freez:Boolean = _
  var defr:Boolean = _
  var defrostTime:Long = _
  var rand:Random = new Random()
  private var isAlive:Boolean = true
  var clickedPoint:Vector2 = _
  clickedPoint = new Vector2(0,20);//initial starting point
  this.setSize(4f, 4f)
  this.setPosition(pos_x, pos_y)
  world = aWorld
  var bd: BodyDef = new BodyDef()
  bd.position.set(this.getX(),this.getY())
  bd.`type` = BodyDef.BodyType.KinematicBody
  body = world.createBody(bd)
  body.setActive(false)
 // body.setTransform(body.getPosition.x,body.getPosition.y,-1.5708f)
  val circle = new CircleShape
  circle.setRadius(this.getWidth / 2)
  // 2. Create a FixtureDef, as usual.
  val fd = new FixtureDef
  fd.density = 10
  fd.friction = 1f
  fd.restitution = 1f
  fd.shape = circle
  fd.filter.groupIndex= -1
  val fixture: Fixture = body.createFixture(fd)
  body.setUserData(this)
  this.setOrigin(this.getWidth / 2, this.getHeight / 2)
  circle.dispose()
  freez=true
  defr=false

  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    super.draw(batch, parentAlpha)
  }

  override def act(delta: Float): Unit = {
    super.act(delta)
    if (delete) {
      world.destroyBody(body)
      this.remove
    }

    this.setRotation(body.getAngle * MathUtils.radiansToDegrees)
    this.setPosition(body.getPosition.x - this.getWidth / 2, body.getPosition.y - this.getHeight / 2)
  }

  def defrost(screenX:Float,screenY:Float) = {
    defr=true
    clickedPoint = new Vector2(Gdx.input.getX, Gdx.input.getY)
    freez=false
  }

  def followMouse(screenX:Float,screenY:Float) = {
    if (isAlive) {
      val sp3 = getStage.getCamera.unproject(new Vector3(screenX, screenY, 0))
      val sp2 = new Vector2(sp3.x, sp3.y)
      // Take the vector that goes from body origin to mouse in camera space
      val a = body.getPosition
      val d = sp2.sub(a)
      // Now you can set the angle;
      body.setTransform(body.getPosition, d.angleRad+1.5708f)
      clickedPoint = new Vector2(Gdx.input.getX, Gdx.input.getY)

    }
  }

  def eliminate(): Unit = {
    delete = true
  }

}
