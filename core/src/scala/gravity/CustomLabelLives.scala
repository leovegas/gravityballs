package gravity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.{Label, Skin}

class CustomLabelLives(val text: CharSequence, val style: Skin) extends Label(text, style) {

  private var text2:String = _
  text2=text.toString

  override def setText(newText: CharSequence): Unit = super.setText(newText)

  override def act(delta: Float): Unit = {
    this.setText(text2)
    super.act(delta)
  }

  def updateText(text: String): Unit = {
    text2 = text
  }

  override def draw(batch: Batch, parentAlpha: Float): Unit = {
    updateText(text.toString)
    super.draw(batch, parentAlpha)

  }
}