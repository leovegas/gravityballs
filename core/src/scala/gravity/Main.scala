package gravity

import com.badlogic.gdx.backends.lwjgl.{LwjglApplication, LwjglApplicationConfiguration}
import gravity.GravityGame

object Main extends App {

  val config = new LwjglApplicationConfiguration
  config.title = "Engine Test 1"
  config.width = 1300
  config.height = 900
  config.forceExit = true
//  config.fullscreen = true
//  config.vSyncEnabled = true
  new LwjglApplication(new GravityGame, config)
}
