package name.felixbecker.gol

import org.scalatest.{FlatSpec, FunSpec}

/**
  * Created by becker on 29.12.16.
  */
class GameFieldTest extends FlatSpec {

  behavior of "a GameField"

  it should "identify the right neighbour cells" in {

    val testConfiguration = new Configuration {
      override val cellCountY: Int = 100
      override val gameFieldSizePxX: Int = 1000
      override val cellCountX: Int = 100
      override val gameFieldSizePxY: Int = 1000
    }

    val gameField = new GameField(testConfiguration)

    // expected 1 and 99..

    val cell = Cell(0, 0, CellState.Alive)
    gameField.cells(0)(0) = cell

    val neighbours = gameField.neighboursOf(cell)
    neighbours.size == 8
    println(neighbours)



  }

}
