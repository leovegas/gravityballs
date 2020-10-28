package gravity

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, TextureRegion}
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.StretchViewport

class MainMenuScreen(val batch2:SpriteBatch, val stage2:Stage) extends Screen{

  import com.badlogic.gdx.graphics.g2d.SpriteBatch

  private var batch: SpriteBatch = batch2
  protected var stage: Stage = stage2

  stage = new Stage(new StretchViewport(1300, 900),batch)
  stage.getViewport.apply()

  def render(delta: Float): Unit = {
    import com.badlogic.gdx.Gdx
    import com.badlogic.gdx.graphics.GL20
    Gdx.gl.glClearColor(.1f, .12f, .16f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    stage.act()
    stage.draw()
//
//
//    if (Gdx.input.isTouched) {
//      dispose()
//    }
  }

  // остальное опущено для краткости...
  override def show(): Unit = {
    import com.badlogic.gdx.Gdx
    import com.badlogic.gdx.scenes.scene2d.ui.Table
    //Stage should controll input:
    Gdx.input.setInputProcessor(stage)

    //Create Table
    val mainTable = new Table
    //Set table to fill stage
    //mainTable.setPosition(100,100)
    mainTable.setFillParent(true)
    //Set alignment of contents in the table.
    mainTable.top

    //Create buttons

    var myTexture = new Texture(Gdx.files.internal("android/assets/gravity/button1.png"))
    var myTextureRegion = new TextureRegion(myTexture)
    var myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion)
    var playButton = new ImageButton(myTexRegionDrawable) //Set the button up
   // playButton.setPosition(100,100)
    //Add listeners to buttons
    //Add buttons to table
    mainTable.add(playButton)
    mainTable.row
    mainTable.add(playButton)
    mainTable.row
    mainTable.add(playButton)

    //Add table to stage
    stage.addActor(mainTable)
  }

  override def resize(width: Int, height: Int): Unit = {
    stage.getViewport.update(width, height)
    stage.getCamera.position.set(stage.getCamera.viewportWidth / 2, stage.getCamera.viewportHeight / 2, 0)

  }

  override def pause(): Unit = {}

  override def resume(): Unit = {}

  override def hide(): Unit = {}

  override def dispose(): Unit = {}
}