package name.felixbecker.gol

import javax.swing.{JFrame, SwingUtilities}

import name.felixbecker.gol.ui.GameFieldPanel

import scala.util.Random

/**
  * Created by becker on 28.12.16.
  */

object CellState extends Enumeration {
  val Dead, Alive = Value
}


class GameField(cfg: Configuration) {

  @volatile var cells = new Array[Array[Cell]](cfg.cellCountX)

  (0 until cfg.cellCountX).foreach { x =>
      cells(x) = (0 until cfg.cellCountY).map { y =>

        if(Random.nextInt(30) % 3 == 0) {
          Cell(x, y, CellState.Dead)
        } else {
          Cell(x, y, CellState.Alive)
        }

      }.toArray
  }



  cells(4)(4) = Cell(4,4, CellState.Alive)
  cells(5)(4) = Cell(5,4, CellState.Alive)
  cells(5)(5) = Cell(5,5, CellState.Alive)
  cells(4)(5) = Cell(4,5, CellState.Alive)

  cells(8)(8) = Cell(8,8, CellState.Alive)
  cells(8)(9) = Cell(8,9, CellState.Alive)
  cells(8)(10) = Cell(8,10, CellState.Alive)



  cells(0)(0) = Cell(0,0, CellState.Alive)

  def neighboursOf(cell: Cell): Seq[Cell] = {

    // if border crossed, continue at the opposite side (field is a sphere)


    def arrayLimit(arrayLimit: Int, unsafeIndex: Int): Int = {
      if(unsafeIndex < 0) {
        arrayLimit + 1 + unsafeIndex
      } else if(unsafeIndex > arrayLimit) {
        unsafeIndex - 1 - arrayLimit
      } else {
        unsafeIndex
      }
    }

    def safeY(y: Int) = arrayLimit(cfg.cellCountY-1, y)
    def safeX(x: Int) = arrayLimit(cfg.cellCountX-1, x)


    val x = cell.x
    val y = cell.y

    Seq(
      // row above
      cells(safeX(x-1))(safeY(y-1)),
      cells(safeX(x))(safeY(y-1)),
      cells(safeX(x+1))(safeY(y-1)),

      // same row
      cells(safeX(x-1))(safeY(y)),
      cells(safeX(x+1))(safeY(y)),

      // row under
      cells(safeX(x-1))(safeY(y+1)),
      cells(safeX(x))(safeY(y+1)),
      cells(safeX(x+1))(safeY(y+1))

    )
  }
}

object GOLMain extends App {
  val gol = new GOL
}

class GOL {
  val configuration: Configuration = new ConfigurationImpl

  val frame = new JFrame("GOL")
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(850, 850)
  val gameField =  new GameField(configuration)
  val gameFieldPanel = new GameFieldPanel(configuration, gameField)
  frame.add(gameFieldPanel)
  frame.setResizable(false)
  frame.setVisible(true)

  while(true) {

    Thread.sleep(100)
    var newGameFieldCells = gameField.cells.transpose.transpose


    gameField.cells.flatten.foreach { cell =>


      val neighbours = gameField.neighboursOf(cell)
      val aliveNeighboursCount = neighbours.count(_.cellState == CellState.Alive)


      // dead: exactly 3 alive cells around: alive
      // alive: < 2 alive neighbours: die (dead)
      // alive: >= 2 <= 3 stay alive
      // alive: > 3 alive neighbours: dead


      gameField.synchronized {


        cell.cellState match {

          case CellState.Dead if aliveNeighboursCount == 3 =>
            newGameFieldCells(cell.x)(cell.y) = Cell(cell.x, cell.y, CellState.Alive)

          case CellState.Alive if aliveNeighboursCount < 2 || aliveNeighboursCount > 3 =>
            newGameFieldCells(cell.x)(cell.y) = Cell(cell.x, cell.y, CellState.Dead)

          case CellState.Alive if aliveNeighboursCount >= 2 || aliveNeighboursCount <= 3 =>
            newGameFieldCells(cell.x)(cell.y) = Cell(cell.x, cell.y, CellState.Alive)

          case other => // TODO debug output

        }
      }
    }

    gameField.cells = newGameFieldCells

    // TODO fixme
    SwingUtilities.invokeAndWait(new Runnable {
      override def run() = {
        frame.revalidate()
        frame.repaint()
        gameFieldPanel.revalidate()
        gameFieldPanel.repaint()
        gameFieldPanel.updateUI()

      }
    })


  }

}

case class Cell(x: Int, y: Int, cellState: CellState.Value)


