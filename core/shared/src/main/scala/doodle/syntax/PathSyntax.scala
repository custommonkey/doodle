/*
 * Copyright 2015-2020 Noel Welsh
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
package syntax

import doodle.algebra.Path
import doodle.algebra.Picture
import doodle.core.ClosedPath
import doodle.core.OpenPath
import doodle.core.Point

trait PathSyntax {
  implicit class ClosedPathOps(closedPath: ClosedPath) {
    def path[Alg[x[_]] <: Path[x], F[_]]: Picture[Alg, F, Unit] =
      Picture { implicit algebra: Alg[F] =>
        algebra.path(closedPath)
      }
  }

  implicit class OpenPathOps(openPath: OpenPath) {
    def path[Alg[x[_]] <: Path[x], F[_]]: Picture[Alg, F, Unit] =
      Picture { implicit algebra: Alg[F] =>
        algebra.path(openPath)
      }
  }

  def regularPolygon[Alg[x[_]] <: Path[x], F[_]](
      sides: Int,
      radius: Double
  ): Picture[Alg, F, Unit] =
    Picture { implicit algebra: Alg[F] =>
      algebra.regularPolygon(sides, radius)
    }

  def star[Alg[x[_]] <: Path[x], F[_]](
      points: Int,
      outerRadius: Double,
      innerRadius: Double
  ): Picture[Alg, F, Unit] =
    Picture { implicit algebra: Alg[F] =>
      algebra.star(points, outerRadius, innerRadius)
    }

  def roundedRectangle[Alg[x[_]] <: Path[x], F[_]](
      width: Double,
      height: Double,
      radius: Double
  ): Picture[Alg, F, Unit] =
    Picture { implicit algebra: Alg[F] =>
      algebra.roundedRectangle(width, height, radius)
    }

  def equilateralTriangle[Alg[x[_]] <: Path[x], F[_]](
      width: Double
  ): Picture[Alg, F, Unit] =
    Picture { implicit algebra: Alg[F] =>
      algebra.equilateralTriangle(width)
    }

  def interpolatingSpline[Alg[x[_]] <: Path[x], F[_]](
      points: Seq[Point]
  ): Picture[Alg, F, Unit] =
    Picture { implicit algebra: Alg[F] =>
      algebra.interpolatingSpline(points)
    }

  def catmulRom[Alg[x[_]] <: Path[x], F[_]](
      points: Seq[Point],
      tension: Double = 0.5
  ): Picture[Alg, F, Unit] =
    Picture { implicit algebra: Alg[F] =>
      algebra.catmulRom(points, tension)
    }
}
