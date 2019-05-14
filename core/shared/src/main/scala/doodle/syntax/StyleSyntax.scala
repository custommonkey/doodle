/*
 * Copyright 2015 Creative Scala
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

import doodle.algebra.{Image, Style}
import doodle.core.Color

trait StyleSyntax {
  implicit class StyleOps[F[_], A](image: F[A]) {
    def fillColor(fillColor: Color)(implicit s: Style[F]): F[A] =
      s.fillColor(image, fillColor)

    def strokeColor(strokeColor: Color)(implicit s: Style[F]): F[A] =
      s.strokeColor(image, strokeColor)

    def strokeWidth(strokeWidth: Double)(implicit s: Style[F]): F[A] =
      s.strokeWidth(image, strokeWidth)

    def noFill(implicit s: Style[F]): F[A] =
      s.noFill(image)

    def noStroke(implicit s: Style[F]): F[A] =
      s.noStroke(image)
  }

  implicit class StyleImageOps[Alg[x[_]] <: Style[x], F[_], A](
      image: Image[Alg, F, A]) {
    def fillColor(fillColor: Color): Image[Alg, F, A] =
      Image { implicit algebra: Alg[F] =>
        image(algebra).fillColor(fillColor)
      }

    def strokeColor(strokeColor: Color): Image[Alg, F, A] =
      Image { implicit algebra: Alg[F] =>
        image(algebra).strokeColor(strokeColor)
      }

    def strokeWidth(strokeWidth: Double): Image[Alg, F, A] =
      Image { implicit algebra: Alg[F] =>
        image(algebra).strokeWidth(strokeWidth)
      }

    def noFill: Image[Alg, F, A] =
      Image { implicit algebra: Alg[F] =>
        image(algebra).noFill
      }

    def noStroke: Image[Alg, F, A] =
      Image { implicit algebra: Alg[F] =>
        image(algebra).noStroke
      }
  }
}
