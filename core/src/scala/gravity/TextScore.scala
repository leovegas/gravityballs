package gravity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont}
import com.badlogic.gdx.scenes.scene2d.Actor

class TextScore extends Actor{

  var font12: BitmapFont = _
  var myScore: Int = _ //I assumed you have some object
  var ms: Int = GravityGame.points //I assumed you have some object
  private var textCamera:OrthographicCamera = _
  var cameraViewPortWidth:Float = 1024; // Set the size of the viewport for the text to something big
  val ratio = Gdx.graphics.getWidth.toFloat / Gdx.graphics.getHeight.toFloat
  var cameraViewPortHeight:Float = cameraViewPortWidth * ratio;
  textCamera = new OrthographicCamera(cameraViewPortWidth, cameraViewPortHeight)

  //that you use to access score.
  //Remember to pass this in!
  def this(myScore: Int) {
    this()
    val generator = new FreeTypeFontGenerator(Gdx.files.internal("android/assets/gravity/gamefont.ttf"))
    val parameter = new FreeTypeFontGenerator.FreeTypeFontParameter
    parameter.size = (12*Gdx.graphics.getDensity).toInt
    parameter.kerning = true
    font12 = generator.generateFont(parameter) // font size 12 pixels
    //font12.getData.scale(1f)
    font12.getData.markupEnabled = true
    generator.dispose() // don't forget to dispose to avoid memory leaks!
  }


  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    textCamera.update()
    batch.setProjectionMatrix(textCamera.combined)
    batch.setProjectionMatrix(getStage.getCamera.combined)
    font12.draw(batch, GravityGame.points.toString, -4, 3)  }
}
