package name.felixbecker.gol

/**
  * Created by becker on 29.12.16.
  */
trait Configuration {

  val cellCountX: Int
  val cellCountY: Int

  // GFX
  val gameFieldSizePxX: Int
  val gameFieldSizePxY: Int

  final def pixelPerTileX: Int = gameFieldSizePxX / cellCountX
  final def pixelPerTileY: Int = gameFieldSizePxY / cellCountY
}

class ConfigurationImpl extends Configuration {

  val cellCountX = 80
  val cellCountY = 80

  // GFX

  val gameFieldSizePxX = 800
  val gameFieldSizePxY = 800

}
