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

import doodle.algebra.{Picture, Text}
import doodle.core.font.Font

trait TextSyntax {
  implicit class TextPictureOps[Alg[x[_]] <: Text[x], F[_], A](
      picture: Picture[Alg, F, A]) {
    def font(font: Font): Picture[Alg, F, A] =
      Picture{ implicit algebra: Alg[F] =>
        algebra.font(picture(algebra), font)
      }
  }

  def text[Alg[x[_]] <: Text[x], F[_]](text: String): Picture[Alg, F, Unit] =
    Picture{ implicit algebra: Alg[F] =>
      algebra.text(text)
    }
}
