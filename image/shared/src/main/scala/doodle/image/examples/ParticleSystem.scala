package doodle
package image
package examples

// import doodle.core._
//import doodle.syntax.all._
// import doodle.random._
import cats.Monad
import cats.Monoid
import cats.data.Kleisli
import cats.data.WriterT
import cats.implicits._
import doodle.image.Image

object ParticleSystem {

  def walk[F[_]: Monad, A](
      steps: Int,
      step: Kleisli[F, A, A]
  ): Kleisli[F, A, A] = {
    Kleisli(a => {
      def loop(count: Int, fa: F[A]): F[A] =
        count match {
          case 0 => fa
          case n => loop(n - 1, fa flatMap step.run)
        }

      loop(steps - 1, step(a))
    })
  }

  def trace[F[_]: Monad, A, B: Monoid](steps: Int, step: Kleisli[F, A, A])(
      f: A => B
  ): Kleisli[F, A, B] = {
    type Result[T] = WriterT[F, B, T]

    walk[Result, A](
      steps,
      Kleisli(a => WriterT.putT(step(a))(f(a)))
    ).mapF(fa => fa.written)
  }

  def particles[F[_]: Monad, A, B: Monoid](
      count: Int,
      initial: F[A],
      walk: Kleisli[F, A, B]
  ) = {
    def loop(count: Int, fb: F[B]): F[B] = {
      count match {
        case 0 => fb
        case n => loop(n - 1, ((initial flatMap walk.run), fb).mapN(_ |+| _))
      }
    }

    loop(count, Monad[F].pure(Monoid[B].empty))
  }

  implicit object imageOnMonoid extends Monoid[Image] {
    val empty: Image = Image.empty
    def combine(x: Image, y: Image): Image = x on y
  }
}
