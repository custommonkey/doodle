/*
 * Copyright 2015 noelwelsh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package doodle
package animate
package examples

import doodle.algebra.Picture
import doodle.core._
import doodle.language.Basic
import doodle.syntax._

// To animate
// import doodle.java2d.effect._
// val canvas = Java2dRenderer.frame(Frame.size(500, 500))
// canvas.map(c => Orbit.frames.animate(c)).unsafeRunSync()
//
// To write to a file
// doodle.animate.java2d.Java2dWriter.writeIterable(new java.io.File("orbit.gif"), Frame.size(600, 600), Orbit.frames).unsafeRunSync()
object Orbit {
  def picture[F[_]](angle: Angle): Picture[Basic, F, Unit] =
    Basic.picture[F, Unit] { implicit algebra: Basic[F] =>
      import algebra._

      circle(10).at(angle.cos * 200, angle.sin * 200).fillColor(Color.cornSilk)
    }

  def frames[F[_]]: List[Picture[Basic, F, Unit]] =
    List
      .range(0, 3600)
      .map { angle =>
        angle.degrees
      }
      .map { angle =>
        picture[F](angle)
      }
}