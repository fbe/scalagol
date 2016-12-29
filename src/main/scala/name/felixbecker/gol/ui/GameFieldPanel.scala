package name.felixbecker.gol.ui

import java.awt.{Color, Dimension, Graphics}
import javax.swing.JPanel

import name.felixbecker.gol.{CellState, Configuration, GameField}


class GameFieldPanel(cfg: Configuration, gameField: GameField) extends JPanel {

  setVisible(true)
  setSize(new Dimension(800,800))

  override def paint(g: Graphics): Unit = {
    super.paint(g)


    // draw black background
    g.setColor(new Color(38, 38, 38))
    g.fillRect(0, 0, 800, 800)

    g.setColor(new Color(55, 55, 55))

    // draw x axis lines
    // # | #
    // # | #
    // # | #
    (0 to cfg.cellCountX).foreach { tileNr =>
      val xPos = tileNr * cfg.pixelPerTileX
      g.drawLine(xPos, 0, xPos, cfg.gameFieldSizePxY)
    }

    // draw y axis lines
    // # --------- #

    (0 to cfg.cellCountY).foreach { tileNr =>
      val yPos = tileNr * cfg.pixelPerTileY
      g.drawLine(0, yPos, cfg.gameFieldSizePxX, yPos)

      // draw y axis lines
    }

    // draw cells

    g.setColor(new Color(200, 200, 200))


    gameField.cells.flatten.filter(_.cellState == CellState.Alive).foreach { cell =>
      val x = cell.x
      val y = cell.y
      g.fillRect(x*cfg.pixelPerTileX+1, y*cfg.pixelPerTileY+1, cfg.pixelPerTileX-1, cfg.pixelPerTileY-1)
    }

  }
}