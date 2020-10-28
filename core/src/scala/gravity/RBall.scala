package gravity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.{MathUtils, Vector2}
import com.badlogic.gdx.physics.box2d._
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.TimeUtils

object RBall {
  private val texture = new Texture("android/assets/red.png")
}

class RBall(var aWorld: World, val pos_x: Float, val pos_y: Float) extends Image(RBall.texture) {
  var delete:Boolean = _
  var body:Body = _
  var world:World = _
  var time:Long = _
  var G:Float = _
  this.setSize(0.15f, 0.15f)
  this.setPosition(pos_x, pos_y)
  world = aWorld
  var bd: BodyDef = new BodyDef()
  bd.position.set(this.getX(),this.getY())
  bd.`type` = BodyDef.BodyType.DynamicBody
  body = world.createBody(bd)
  val circle = new CircleShape
  circle.setRadius(this.getWidth / 2)
  // 2. Create a FixtureDef, as usual.
  val fd = new FixtureDef
  fd.density = 10
  fd.friction = 0.5f
  fd.restitution = 0.3f
  fd.shape = circle
  val fixture: Fixture = body.createFixture(fd)
  time = TimeUtils.millis()

  body.setUserData(this)
  this.setOrigin(this.getWidth / 2, this.getHeight / 2)
  circle.dispose()



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
    var MAX_VELOCITY = 0.1f
    val vel: Vector2 = body.getLinearVelocity
    val pos: Vector2 = body.getPosition
    // apply left impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.UP) && vel.x > -MAX_VELOCITY) body.applyForce(new Vector2(0,20),body.getWorldCenter,true)
    if (Gdx.input.isKeyPressed(Keys.RIGHT) && vel.x > -MAX_VELOCITY) body.applyForce(new Vector2(20,0),body.getWorldCenter,true)
    if (Gdx.input.isKeyPressed(Keys.LEFT) && vel.x > -MAX_VELOCITY) body.applyForce(new Vector2(0,0),body.getWorldCenter,true)
    if (Gdx.input.isKeyPressed(Keys.DOWN) && vel.x > -MAX_VELOCITY) body.applyTorque(20,true)

    if (Gdx.input.isKeyPressed(Keys.A) && vel.x > -MAX_VELOCITY) body.applyAngularImpulse(20,true)
    if (Gdx.input.isKeyPressed(Keys.D) && vel.x > MAX_VELOCITY) body.applyTorque( 20, true)
    // apply right impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.W) && vel.y > MAX_VELOCITY) body.applyForce(0, 0.8f, pos.x, pos.y/2, true)
    // apply right impulse, but only if max velocity is not reached yet
    if (Gdx.input.isKeyPressed(Keys.S) && vel.y > MAX_VELOCITY) body.applyForce(0, 0.8f, pos.x/2, pos.y, true)
    var center:Vector2 = new Vector2(0, 0)

    G = 5
    if (TimeUtils.millis() - time > 5000) G = 10
    if (TimeUtils.millis() - time > 10000) G = 10
    if (TimeUtils.millis() - time > 12000) G = 10
    if (TimeUtils.millis() - time > 13000) G = 15
    if (TimeUtils.millis() - time > 15000) G = 15
    if (TimeUtils.millis() - time > 16000) G = 15
    if (TimeUtils.millis() - time > 17000) G = 15
    if (TimeUtils.millis() - time > 19000) G = 15
    if (TimeUtils.millis() - time > 20000) G = 15
    if (TimeUtils.millis() - time > 21000) G = 15
    if (TimeUtils.millis() - time > 22000) G = 13
    if (TimeUtils.millis() - time > 23000) G = 10
    if (TimeUtils.millis() - time > 24000) G = 7
    if (TimeUtils.millis() - time > 25000) G = 3

    var distance:Float = body.getPosition.dst(center)
    var forceValue:Float = G/(distance * distance)
    var direction:Vector2 = center.sub(body.getPosition)
    body.applyForce(direction.scl(forceValue), body.getWorldCenter,true)
  }


  def eliminate(): Unit = {
    delete = true
  }
}
