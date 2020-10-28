package gravity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.physics.box2d.{Contact, ContactImpulse, ContactListener, Manifold}


class GameContactListener extends ContactListener {
  var dropSound: Sound = _
  dropSound = Gdx.audio.newSound(Gdx.files.internal("android/assets/gravity/drop.wav"))

  override def beginContact(contact: Contact): Unit = {
    val classA = contact.getFixtureA.getBody.getUserData.getClass.getName
    val classB = contact.getFixtureB.getBody.getUserData.getClass.getName

   // Gdx.app.debug("begin Contact","between: "+classA+" and "+ classB);
    if (classA.equalsIgnoreCase("gravity.WindowFrame") && classB.equalsIgnoreCase("gravity.MainBall")) {
      val b = contact.getFixtureB.getBody.getUserData.asInstanceOf[MainBall]
      b.eliminate()
      GravityGame.ballsOut+=1
      if (GravityGame.ballsOut==GravityGame.ballNbr) {
        GravityGame.ballOut=true
        GravityGame.ballNbr+=GravityGame.ballsCatch
        GravityGame.ballsCatch=0
        GravityGame.ballsOut=0
        GravityGame.nextR=true
      }
      println("ballNbr-")
      dropSound.play()
    }

     if (classA.equalsIgnoreCase("gravity.Figure") && classB.equalsIgnoreCase("gravity.MainBall")) {
      val b = contact.getFixtureA.getBody.getUserData.asInstanceOf[Figure]
       b.renderTex()
       b.reduceLives()
       if (b.lives<=0) {
       b.eliminate()
       }
       GravityGame.FigNbr -= 1
       GravityGame.points += 1
       dropSound.play()
    }

    if (classA.equalsIgnoreCase("gravity.FigureRedBall") && classB.equalsIgnoreCase("gravity.MainBall")) {
      val b = contact.getFixtureA.getBody.getUserData.asInstanceOf[FigureRedBall]
      b.eliminate()
      GravityGame.FigNbr -= 1
      GravityGame.ballsCatch += 1
      println("ballscatch+")
      dropSound.play()
    }
  }

  override def endContact(contact: Contact): Unit = {
  }

  override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
  }

  override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
  }
}
