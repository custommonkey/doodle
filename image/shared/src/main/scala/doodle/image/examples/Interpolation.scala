package doodle
package image
package examples

import cats.instances.all._
import doodle.core._
import doodle.image.syntax.all._
import doodle.syntax.all._

object Interpolation {
  val pts =
    for (x <- 1 to 400 by 20)
      yield Point.cartesian(x.toDouble, (x / 100.0).turns.sin * 100)

  val dot =
    Image.circle(5).fillColor(Color.red)

  val dots =
    (pts
      .map { pt =>
        dot.at(pt.toVec)
      })
      .toList
      .allOn

  val default =
    Image
      .interpolatingSpline(pts)
      .strokeColor(Color.cornflowerBlue)
      .strokeWidth(3.0)

  val tight =
    Image.catmulRom(pts, 1.0).strokeColor(Color.cornflowerBlue).strokeWidth(3.0)

  val loose =
    Image.catmulRom(pts, 0.0).strokeColor(Color.cornflowerBlue).strokeWidth(3.0)

  val image = (default on dots) above (tight on dots) above (loose on dots)
}
