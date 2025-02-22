package doodle
package image
package examples

import doodle.core._
import doodle.image._
import doodle.syntax.all._

object Tree {
  import PathElement._

  def leaf(angle: Angle, length: Double): Image =
    Image
      .openPath(
        Seq(
          moveTo(Point.zero),
          lineTo(Point.polar(length, angle))
        )
      )
      .strokeColor(Color.hsl(angle, .5, .5))

  def branch(depth: Int, angle: Angle, length: Double): Image = {
    if (depth == 0) {
      leaf(angle, length)
    } else {
      val l = branch(depth - 1, angle + 20.degrees, length * 0.8)
      val r = branch(depth - 1, angle - 20.degrees, length * 0.8)
      val b = leaf(angle, length)
      b on ((l on r) at Vec.polar(length, angle))
    }
  }

  def image = branch(10, 90.degrees, 50)
}
