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
package image
package syntax

import cats.effect.unsafe.IORuntime
import doodle.algebra.Picture
import doodle.core.{Base64 => B64}
import doodle.effect.Base64
import doodle.image.Image
import doodle.language.Basic

trait JvmImageSyntax extends ImageSyntax {
  implicit class Base64ImageOps(image: Image) {
    def base64[Format] = new Base64Ops[Format](image)
  }

  /** This strange construction allows the user to write
    * `anImage.base64[AFormat]` without having to specify other, mostly
    * irrelevant to the user, type parameters.
    */
  final class Base64Ops[Format](image: Image) {
    def apply[Alg[x[_]] <: Basic[x], F[_], Frame](implicit
        w: Base64[Alg, F, Frame, Format],
        runtime: IORuntime
    ): B64[Format] = {
      val picture = Picture((algebra: Alg[F]) => image.compile(algebra))
      val (_, base64) = w.base64(picture).unsafeRunSync()
      base64
    }
  }

}
