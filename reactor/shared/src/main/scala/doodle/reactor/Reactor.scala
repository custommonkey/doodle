package doodle
package reactor

import cats.effect.unsafe.IORuntime
import doodle.core.Point
import doodle.effect._
import doodle.image.Image
import doodle.language.Basic

import scala.concurrent.duration._

/** A [[Reactor]] that has reasonable defaults and a simple builder style for
  * creating more complicated behaviour.
  */
final case class Reactor[A](
    initial: A,
    onMouseClickHandler: (Point, A) => A = (_: Point, a: A) => a,
    onMouseMoveHandler: (Point, A) => A = (_: Point, a: A) => a,
    onTickHandler: A => A = (a: A) => a,
    tickRate: FiniteDuration = FiniteDuration(100, MILLISECONDS),
    renderHandler: A => Image = (_: A) => Image.empty,
    stopHandler: A => Boolean = (_: A) => false
) extends BaseReactor[A] {
  // Reactor methods -------------------------------------------------

  def onMouseClick(location: Point, state: A): A =
    onMouseClickHandler(location, state)

  def onMouseMove(location: Point, state: A): A =
    onMouseMoveHandler(location, state)

  def onTick(state: A): A =
    onTickHandler(state)

  def render(value: A): Image =
    renderHandler(value)

  def stop(value: A): Boolean =
    stopHandler(value)

  // Builder methods -------------------------------------------------

  def withOnMouseClick(f: (Point, A) => A): Reactor[A] =
    this.copy(onMouseClickHandler = f)

  def withOnMouseMove(f: (Point, A) => A): Reactor[A] =
    this.copy(onMouseMoveHandler = f)

  def withOnTick(f: A => A): Reactor[A] =
    this.copy(onTickHandler = f)

  def withTickRate(duration: FiniteDuration): Reactor[A] =
    this.copy(tickRate = duration)

  def withRender(f: A => Image): Reactor[A] =
    this.copy(renderHandler = f)

  def withStop(f: A => Boolean): Reactor[A] =
    this.copy(stopHandler = f)

  // Make stuff happen -----------------------------------------------

  def image: Image =
    render(initial)

  def step: Reactor[A] =
    this.copy(initial = this.onTick(this.initial))

  def draw[Alg[x[_]] <: Basic[x], F[_], Frame, Canvas](
      frame: Frame
  )(implicit
      renderer: Renderer[Alg, F, Frame, Canvas],
      runtime: IORuntime
  ): Unit = {
    import doodle.image.syntax.all._
    this.image.draw(frame)(renderer, runtime)
  }

  def draw[Alg[x[_]] <: Basic[x], F[_], Frame, Canvas]()(implicit
      renderer: DefaultRenderer[Alg, F, Frame, Canvas],
      runtime: IORuntime
  ): Unit = {
    import doodle.image.syntax.all._
    this.image.draw()(renderer, runtime)
  }
}
object Reactor {

  /** Create a [[Reactor]] with the given initial value.
    */
  def init[A](value: A): Reactor[A] =
    Reactor(initial = value)

  /** Create a [[Reactor]] where the value starts at `start` and goes to `stop`
    * in `step` increments.
    */
  def linearRamp(
      start: Double = 0.0,
      stop: Double = 1.0,
      step: Double = 0.01
  ): Reactor[Double] =
    Reactor
      .init[Double](start)
      .withOnTick((x: Double) => x + step)
      .withStop((x: Double) => x >= stop)
}
