package gravity

import com.badlogic.gdx.physics.box2d.{Body, BodyDef, PolygonShape, World}

class WindowFrame(var world: World, val width: Float, val heigth: Float) {
  private var body:Body = _
  val bd = new BodyDef
  bd.position.set(-20, -5)
  bd.`type` = BodyDef.BodyType.StaticBody
  body = world.createBody(bd)
  val groundBox = new PolygonShape
  groundBox.setAsBox(40, 1)
  body.createFixture(groundBox, 0.0f)
  body.setUserData(this)
}
