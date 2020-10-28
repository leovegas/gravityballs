package gravity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont}
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.{Body, BodyDef, FixtureDef, World}
import com.badlogic.gdx.scenes.scene2d.ui.Image
import gravity.libb.BodyEditorLoader2

object FigureRedBall {
  var quantity:Int = 10
}

class FigureRedBall(var world: World, val pos_x: Float, val pos_y: Float, val aWidth: Float, val aHeight: Float) extends Image(new Texture("android/assets/gravity/redball.png")) {
  var delete:Boolean = _
  var stUp: Boolean = false
 private var body:Body = _
  private var angle:Float = _
  this.setSize(aWidth, aHeight)
  this.setPosition(pos_x, pos_y)
  val loader = new BodyEditorLoader2(Gdx.files.internal("android/assets/gravity/redball.json"))
  val bd = new BodyDef
  bd.position.set(Gdx.graphics.getWidth / 2, Gdx.graphics.getHeight / 2)
  bd.`type` = BodyDef.BodyType.StaticBody
  bd.position.x = this.getX
  bd.position.y = this.getY
  body = world.createBody(bd)
  // 2. Create a FixtureDef, as usual.
  val fd = new FixtureDef
  fd.density = 1
  fd.friction = 0.5f
  fd.restitution = 0.9f
  // 3. Create a Body, as usual.
  val scale: Float = this.getWidth*0.02f
  loader.attachFixture(body, "Name", fd, scale)
  this.setOrigin(this.getWidth / 2, this.getHeight / 2)
  body.setUserData(this)

  def buildFont:BitmapFont = {
    var font12: BitmapFont = new BitmapFont()
    val generator = new FreeTypeFontGenerator(Gdx.files.internal("android/assets/gravity/gamefont.ttf"))
    val parameter = new FreeTypeFontGenerator.FreeTypeFontParameter
    parameter.size = 6
    parameter.kerning = true
    font12 = generator.generateFont(parameter) // font size 12 pixels
    font12.getData.scale(1f)
    font12.getData.markupEnabled = true
    generator.dispose() // don't forget to dispose to avoid memory leaks!
    font12
  }

  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    super.draw(batch, parentAlpha)
  }

  override def act(delta: Float): Unit = {
    super.act(delta)

    if (delete) {
      world.destroyBody(body)
      this.remove
    }
    if (stUp) {
      body.setTransform(body.getPosition.x, body.getPosition.y+1,0)
      stUp = false
    }
    this.setRotation(body.getAngle * MathUtils.radiansToDegrees)
    this.setPosition(body.getPosition.x - this.getWidth / 2, body.getPosition.y - this.getHeight / 2)
  }

  def eliminate(): Unit = {
    delete = true
  }

  def stepUp(): Unit = {
    stUp = true
  }

}
