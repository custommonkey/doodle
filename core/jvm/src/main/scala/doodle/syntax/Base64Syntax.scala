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

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import doodle.algebra.Algebra
import doodle.algebra.Picture
import doodle.core.{Base64 => B64}
import doodle.effect.Base64

trait Base64Syntax {
  implicit class Base64Ops[Alg[x[_]] <: Algebra[x], F[_], A](
      picture: Picture[Alg, F, A]
  ) {
    // This class exists solely so the user doesn't have to provide the `Frame`
    // type parameter when calling syntax methods.
    class Base64OpsHelper[Format](picture: Picture[Alg, F, A]) {
      def apply[Frame]()(implicit
          w: Base64[Alg, F, Frame, Format],
          r: IORuntime
      ): (A, B64[Format]) =
        w.base64(picture).unsafeRunSync()

      def apply[Frame](frame: Frame)(implicit
          w: Base64[Alg, F, Frame, Format],
          r: IORuntime
      ): (A, B64[Format]) =
        w.base64(frame, picture).unsafeRunSync()
    }

    class Base64IOOpsHelper[Format](picture: Picture[Alg, F, A]) {
      def apply[Frame]()(implicit
          w: Base64[Alg, F, Frame, Format]
      ): IO[(A, B64[Format])] =
        w.base64(picture)

      def apply[Frame](frame: Frame)(implicit
          w: Base64[Alg, F, Frame, Format]
      ): IO[(A, B64[Format])] =
        w.base64(frame, picture)
    }

    def base64[Format] =
      new Base64OpsHelper[Format](picture)

    def base64ToIO[Format] =
      new Base64IOOpsHelper[Format](picture)
  }
}
